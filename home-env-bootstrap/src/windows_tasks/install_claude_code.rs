use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct InstallClaudeCodeTask;

impl task::Task for InstallClaudeCodeTask {
    fn name(&self) -> String {
        format!("install_claude_code")
    }

    fn execute(&self) -> Result<()> {
        process::execute(
            "npm",
            &["install", "-g", "@anthropic-ai/claude-code"],
            &[],
        )
        .wrap_err_with(|| "Installing Claude Code via npm")
    }
}

pub(crate) fn install_claude_code_task() -> Box<dyn task::Task> {
    Box::new(InstallClaudeCodeTask)
}