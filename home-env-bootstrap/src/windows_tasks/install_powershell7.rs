use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct InstallPowerShell7Task;

impl task::Task for InstallPowerShell7Task {
    fn name(&self) -> String {
        format!("install_powershell7")
    }

    fn execute(&self) -> Result<()> {
        process::execute("winget", &["install", "Microsoft.PowerShell"], &[])
            .wrap_err_with(|| "Installing PowerShell 7 via winget")
    }
}

pub(crate) fn install_powershell7_task() -> Box<dyn task::Task> {
    Box::new(InstallPowerShell7Task)
}
