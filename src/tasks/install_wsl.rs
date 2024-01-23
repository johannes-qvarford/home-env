use color_eyre::{eyre::Context, Result};

use crate::{utility::process, utility::task};

pub(crate) struct InstallWsl;

impl task::Task for InstallWsl {
    fn name(&self) -> String {
        "install_wsl".to_owned()
    }

    fn execute(&self) -> Result<()> {
        process::execute("wsl.exe", &["--install"], &[]).wrap_err("Installing WSL".to_string())
    }

    fn requires_restart(&self) -> bool {
        true
    }
}

pub(crate) fn install_wsl() -> Box<dyn task::Task> {
    Box::new(InstallWsl)
}
