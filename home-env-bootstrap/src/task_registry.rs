//! Task registry system for managing platform-specific tasks.
//!
//! This module provides a centralized registry for managing tasks across different platforms.
//! It automatically detects the current platform and provides the appropriate task set.

use color_eyre::Result;

use crate::utility::task::Task;

/// Task registry that manages all available tasks for the current platform
pub struct TaskRegistry {
    tasks: Vec<Box<dyn Task>>,
}

impl TaskRegistry {
    pub fn new() -> Self {
        Self { tasks: Vec::new() }
    }

    /// Register a new task with the registry
    pub fn register_task(&mut self, task: Box<dyn Task>) {
        self.tasks.push(task);
    }

    /// Get all registered tasks
    pub fn get_all_tasks(self) -> Vec<Box<dyn Task>> {
        self.tasks
    }
}

/// Trait for platform-specific task providers
pub trait TaskProvider {
    fn provide_tasks(&self, registry: &mut TaskRegistry) -> Result<()>;
}

/// Factory function to create a complete task registry for the current platform
pub fn create_task_registry() -> Result<TaskRegistry> {
    let mut registry = TaskRegistry::new();

    #[cfg(unix)]
    {
        let provider = crate::linux_tasks::LinuxTaskProvider::new();
        provider.provide_tasks(&mut registry)?;
    }

    #[cfg(windows)]
    {
        let provider = crate::windows_tasks::WindowsTaskProvider::new();
        provider.provide_tasks(&mut registry)?;
    }

    Ok(registry)
}
