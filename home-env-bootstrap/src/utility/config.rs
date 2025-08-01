use std::collections::HashMap;
use std::fs;
use std::path::PathBuf;

use color_eyre::{eyre::Context, Result};
use dirs::config_local_dir;
use serde::{Deserialize, Serialize};

use super::task_status::TaskStatus;

#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
#[serde(rename_all = "snake_case")]
pub enum ConfigTaskStatus {
    NotExecuted,
    InProgress,
    Completed,
    Failed,
}

impl From<TaskStatus> for ConfigTaskStatus {
    fn from(status: TaskStatus) -> Self {
        match status {
            TaskStatus::NotExecuted => ConfigTaskStatus::NotExecuted,
            TaskStatus::InProgress => ConfigTaskStatus::InProgress,
            TaskStatus::Completed => ConfigTaskStatus::Completed,
            TaskStatus::Failed => ConfigTaskStatus::Failed,
        }
    }
}

impl From<ConfigTaskStatus> for TaskStatus {
    fn from(status: ConfigTaskStatus) -> Self {
        match status {
            ConfigTaskStatus::NotExecuted => TaskStatus::NotExecuted,
            ConfigTaskStatus::InProgress => TaskStatus::InProgress,
            ConfigTaskStatus::Completed => TaskStatus::Completed,
            ConfigTaskStatus::Failed => TaskStatus::Failed,
        }
    }
}

#[derive(Debug, Serialize, Deserialize)]
pub struct TaskConfig {
    pub status: ConfigTaskStatus,
}

impl Default for TaskConfig {
    fn default() -> Self {
        Self {
            status: ConfigTaskStatus::NotExecuted,
        }
    }
}

#[derive(Debug, Default, Serialize, Deserialize)]
pub struct BootstrapConfig {
    pub tasks: HashMap<String, TaskConfig>,
}

impl BootstrapConfig {
    pub fn load() -> Result<Self> {
        let config_path = Self::get_config_path()?;

        if config_path.exists() {
            let content = fs::read_to_string(&config_path)
                .wrap_err_with(|| format!("Reading config file: {config_path:?}"))?;

            let config: BootstrapConfig = toml::from_str(&content)
                .wrap_err_with(|| format!("Parsing TOML config: {config_path:?}"))?;

            Ok(config)
        } else {
            Ok(Self::default())
        }
    }

    pub fn save(&self) -> Result<()> {
        let config_path = Self::get_config_path()?;

        // Ensure the parent directory exists
        if let Some(parent) = config_path.parent() {
            fs::create_dir_all(parent)
                .wrap_err_with(|| format!("Creating config directory: {parent:?}"))?;
        }

        let toml_content = toml::to_string_pretty(self).wrap_err("Serializing config to TOML")?;

        fs::write(&config_path, toml_content)
            .wrap_err_with(|| format!("Writing config file: {config_path:?}"))?;

        Ok(())
    }

    pub fn get_task_status(&self, task_name: &str) -> TaskStatus {
        self.tasks
            .get(task_name)
            .map(|config| config.status.clone().into())
            .unwrap_or(TaskStatus::NotExecuted)
    }

    pub fn set_task_status(&mut self, task_name: &str, status: TaskStatus) {
        let config = TaskConfig {
            status: status.into(),
        };
        self.tasks.insert(task_name.to_string(), config);
    }

    fn get_config_path() -> Result<PathBuf> {
        let mut dir = config_local_dir()
            .ok_or_else(|| color_eyre::eyre::eyre!("Config local directory was not found"))?;
        dir.push("home-env-bootstrap");
        dir.push("config.toml");
        Ok(dir)
    }

    pub fn get_config_directory() -> Result<PathBuf> {
        let mut dir = config_local_dir()
            .ok_or_else(|| color_eyre::eyre::eyre!("Config local directory was not found"))?;
        dir.push("home-env-bootstrap");
        Ok(dir)
    }
}
