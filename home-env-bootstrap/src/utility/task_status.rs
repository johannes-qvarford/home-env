use std::fs;
use std::path::PathBuf;

use color_eyre::{eyre::Context, Result};
use dirs::config_local_dir;

use super::config::BootstrapConfig;

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum TaskStatus {
    NotExecuted,
    InProgress,
    Completed,
    Failed,
}

pub struct TaskStatusManager {
    config: BootstrapConfig,
    mark_directory: PathBuf,
}

impl TaskStatusManager {
    pub fn new() -> Result<Self> {
        let mark_directory = Self::get_mark_directory()?;
        fs::create_dir_all(&mark_directory).wrap_err("Creating mark directory")?;

        let mut manager = Self {
            config: BootstrapConfig::default(),
            mark_directory,
        };

        // Attempt to load existing TOML config, or migrate from mark files
        manager.config = match BootstrapConfig::load() {
            Ok(config) => config,
            Err(_) => {
                // TOML config doesn't exist, try to migrate from mark files
                let migrated_config = manager.migrate_from_mark_files()?;
                migrated_config.save()?;
                migrated_config
            }
        };

        Ok(manager)
    }

    pub fn get_status(&self, task_name: &str) -> Result<TaskStatus> {
        Ok(self.config.get_task_status(task_name))
    }

    pub fn mark_completed(&mut self, task_name: &str) -> Result<()> {
        self.config
            .set_task_status(task_name, TaskStatus::Completed);
        self.config
            .save()
            .wrap_err_with(|| format!("Saving completion status for task '{task_name}'"))
    }

    pub fn mark_failed(&mut self, task_name: &str) -> Result<()> {
        self.config.set_task_status(task_name, TaskStatus::Failed);
        self.config
            .save()
            .wrap_err_with(|| format!("Saving failure status for task '{task_name}'"))
    }

    pub fn mark_in_progress(&mut self, task_name: &str) -> Result<()> {
        self.config
            .set_task_status(task_name, TaskStatus::InProgress);
        self.config
            .save()
            .wrap_err_with(|| format!("Saving in-progress status for task '{task_name}'"))
    }

    /// Migrate existing mark files to TOML configuration
    fn migrate_from_mark_files(&self) -> Result<BootstrapConfig> {
        let mut config = BootstrapConfig::default();

        // Read all files in the mark directory
        if self.mark_directory.exists() {
            let entries = fs::read_dir(&self.mark_directory)
                .wrap_err_with(|| format!("Reading mark directory: {:?}", self.mark_directory))?;

            for entry in entries {
                let entry = entry.wrap_err("Reading directory entry")?;
                let path = entry.path();

                if path.is_file() {
                    if let Some(file_name) = path.file_name().and_then(|n| n.to_str()) {
                        // Skip log files and other non-mark files
                        if file_name.ends_with(".log") {
                            continue;
                        }

                        if file_name.ends_with(".failed") {
                            let task_name = file_name.strip_suffix(".failed").unwrap();
                            config.set_task_status(task_name, TaskStatus::Failed);
                        } else if file_name.ends_with(".in_progress") {
                            let task_name = file_name.strip_suffix(".in_progress").unwrap();
                            config.set_task_status(task_name, TaskStatus::InProgress);
                        } else {
                            // Regular completion mark file
                            config.set_task_status(file_name, TaskStatus::Completed);
                        }
                    }
                }
            }
        }
        Ok(config)
    }

    fn get_mark_directory() -> Result<PathBuf> {
        let mut dir = config_local_dir()
            .ok_or_else(|| color_eyre::eyre::eyre!("Config local directory was not found"))?;
        dir.push("home-env-bootstrap");
        Ok(dir)
    }
}

impl Default for TaskStatusManager {
    fn default() -> Self {
        Self::new().expect("Failed to create TaskStatusManager")
    }
}
