//! Task registry system for managing platform-specific tasks.
//!
//! This module provides a centralized registry for managing tasks across different platforms.
//! It automatically detects the current platform and provides the appropriate task set.

use color_eyre::Result;
use std::collections::HashMap;

use crate::utility::task::Task;

/// Task registry that manages all available tasks for the current platform
pub struct TaskRegistry {
    tasks: Vec<Box<dyn Task>>,
    task_map: HashMap<String, usize>,
}

impl TaskRegistry {
    pub fn new() -> Self {
        Self {
            tasks: Vec::new(),
            task_map: HashMap::new(),
        }
    }

    /// Register a new task with the registry
    pub fn register_task(&mut self, task: Box<dyn Task>) {
        let name = task.name();
        let index = self.tasks.len();
        self.tasks.push(task);
        self.task_map.insert(name, index);
    }

    /// Get all registered tasks
    pub fn get_all_tasks(self) -> Vec<Box<dyn Task>> {
        self.tasks
    }

    /// Get a task by name
    #[allow(dead_code)]
    pub fn get_task_by_name(&self, name: &str) -> Option<&dyn Task> {
        self.task_map
            .get(name)
            .map(|&index| self.tasks[index].as_ref())
    }

    /// Get the number of registered tasks
    #[allow(dead_code)]
    pub fn task_count(&self) -> usize {
        self.tasks.len()
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
