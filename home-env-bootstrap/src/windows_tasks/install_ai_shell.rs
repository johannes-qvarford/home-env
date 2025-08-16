use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct InstallAiShellTask;

impl task::Task for InstallAiShellTask {
    fn name(&self) -> String {
        format!("install_ai_shell")
    }

    fn execute(&self) -> Result<()> {
        process::execute(
            "pwsh",
            &[
                "-Command",
                r#"Invoke-Expression "& { $(Invoke-RestMethod 'https://aka.ms/install-aishell.ps1') }""#
            ],
            &[]
        ).wrap_err_with(|| "Installing Microsoft AI-Shell via PowerShell 7")
    }
}

pub(crate) fn install_ai_shell_task() -> Box<dyn task::Task> {
    Box::new(InstallAiShellTask)
}
