mod bash;

use crate::bash_task;
pub(crate) use bash::*;

use color_eyre::Result;

use crate::task_registry::{TaskProvider, TaskRegistry};

/// Linux-specific task provider
pub struct LinuxTaskProvider;

impl LinuxTaskProvider {
    pub fn new() -> Self {
        Self
    }
}

impl TaskProvider for LinuxTaskProvider {
    fn provide_tasks(&self, registry: &mut TaskRegistry) -> Result<()> {
        // Register all Linux tasks
        registry.register_task(bash_task!("test"));
        registry.register_task(bash_task!("start"));
        registry.register_task(bash_task!("github"));
        registry.register_task(bash_task!("clone-home-env"));
        registry.register_task(bash_task!("secrets"));
        registry.register_task(bash_task!("dotfiles-service"));
        registry.register_task(bash_task!("fish"));
        registry.register_task(bash_task!("docker"));
        registry.register_task(bash_task!("rust"));
        registry.register_task(bash_task!("node"));
        registry.register_task(bash_task!("colors"));
        registry.register_task(bash_task!("extra"));
        registry.register_task(bash_task!("backup"));
        registry.register_task(bash_task!("claude"));
        registry.register_task(bash_task!("android"));
        registry.register_task(bash_task!("install-git-hooks"));
        registry.register_task(bash_task!("metamcp"));
        registry.register_task(bash_task!("bluetooth"));
        registry.register_task(bash_task!("vscode"));
        registry.register_task(bash_task!("vlc"));
        registry.register_task(bash_task!("intellij"));
        registry.register_task(bash_task!("steam"));
        registry.register_task(bash_task!("gnome"));

        Ok(())
    }
}
