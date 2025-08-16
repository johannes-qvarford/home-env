use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct InstallBunTask;

impl task::Task for InstallBunTask {
    fn name(&self) -> String {
        format!("install_bun")
    }

    fn execute(&self) -> Result<()> {
        process::execute(
            "powershell",
            &["-c", r#"irm https://bun.com/install.ps1 | iex"#],
            &[],
        )
        .wrap_err_with(|| "Installing Bun via PowerShell")
    }
}

pub(crate) fn install_bun_task() -> Box<dyn task::Task> {
    Box::new(InstallBunTask)
}
