use std::{
    fmt::Debug,
    fs::{create_dir_all, File},
    path::PathBuf,
};

use color_eyre::{
    eyre::{Context, Report},
    Result,
};

use dirs::config_local_dir;

#[derive(PartialEq, Eq, Debug)]
#[allow(dead_code)]
pub(crate) enum Execution {
    Performed,
    Skipped,
}

pub(crate) trait Task {
    fn name(&self) -> String;
    fn execute(&self) -> Result<()>;
}

impl dyn Task {
    #[allow(dead_code)]
    pub(crate) fn execute_or_pause(&self) -> Result<()> {
        self.execute()
    }

    #[allow(dead_code)]
    pub(crate) fn execute_if_needed(&self) -> Result<Execution> {
        let name = self.name();
        create_dir_all(self.mark_directory()?).wrap_err("Creating mark directory")?;
        let executed = self
            .has_been_executed()
            .wrap_err("Checking if task been executed")?;
        if !executed {
            self.execute_or_pause().wrap_err("Executing tasks")?;
            self.mark_executed().wrap_err("Marking task as executed")?;
            println!("Marked task '{name}' as executed.");
            Ok(Execution::Performed)
        } else {
            println!("Skipping task '{name}' since it has already been executed.");
            Ok(Execution::Skipped)
        }
    }

    #[allow(dead_code)]
    pub(crate) fn has_been_executed(&self) -> Result<bool> {
        let path = self.mark_path()?;
        path.try_exists()
            .wrap_err_with(|| format!("Verifying existance of mark path '{path:?}'"))
    }

    #[allow(dead_code)]
    pub(crate) fn mark_executed(&self) -> Result<()> {
        File::create(
            self.mark_path()
                .wrap_err("Calculating path of mark file to create")?,
        )
        .wrap_err("Creating mark file")?;
        Ok(())
    }

    #[allow(dead_code)]
    fn mark_path(&self) -> Result<PathBuf> {
        let mut path = self.mark_directory().wrap_err("Calculating mark path")?;
        path.push(self.name());
        Ok(path)
    }

    #[allow(dead_code)]
    fn mark_directory(&self) -> Result<PathBuf> {
        let mut dir =
            config_local_dir().ok_or(Report::msg("Config local directory was not found"))?;
        dir.push("home-env-bootstrap");
        Ok(dir)
    }
}
