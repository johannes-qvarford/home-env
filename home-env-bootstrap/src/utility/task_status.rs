use std::fs::{create_dir_all, File};
use std::path::PathBuf;

use color_eyre::{eyre::Context, Result};
use dirs::config_local_dir;

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum TaskStatus {
    NotExecuted,
    InProgress,
    Completed,
    Failed,
}

pub struct TaskStatusManager {
    mark_directory: PathBuf,
}

impl TaskStatusManager {
    pub fn new() -> Result<Self> {
        let mark_directory = Self::get_mark_directory()?;
        create_dir_all(&mark_directory).wrap_err("Creating mark directory")?;

        Ok(Self { mark_directory })
    }

    pub fn get_status(&self, task_name: &str) -> Result<TaskStatus> {
        let mark_path = self.mark_path(task_name);
        let failed_path = self.failed_path(task_name);
        let in_progress_path = self.in_progress_path(task_name);

        if failed_path.exists() {
            Ok(TaskStatus::Failed)
        } else if in_progress_path.exists() {
            Ok(TaskStatus::InProgress)
        } else if mark_path.exists() {
            Ok(TaskStatus::Completed)
        } else {
            Ok(TaskStatus::NotExecuted)
        }
    }

    #[allow(dead_code)]
    pub fn mark_completed(&self, task_name: &str) -> Result<()> {
        self.clear_status_files(task_name)?;
        File::create(self.mark_path(task_name))
            .wrap_err_with(|| format!("Creating completion mark file for task '{task_name}'"))?;
        Ok(())
    }

    #[allow(dead_code)]
    pub fn mark_failed(&self, task_name: &str) -> Result<()> {
        self.clear_status_files(task_name)?;
        File::create(self.failed_path(task_name))
            .wrap_err_with(|| format!("Creating failure mark file for task '{task_name}'"))?;
        Ok(())
    }

    pub fn mark_in_progress(&self, task_name: &str) -> Result<()> {
        File::create(self.in_progress_path(task_name))
            .wrap_err_with(|| format!("Creating in-progress mark file for task '{task_name}'"))?;
        Ok(())
    }

    #[allow(dead_code)]
    pub fn clear_status(&self, task_name: &str) -> Result<()> {
        self.clear_status_files(task_name)
    }

    #[allow(dead_code)]
    fn clear_status_files(&self, task_name: &str) -> Result<()> {
        let paths = [
            self.mark_path(task_name),
            self.failed_path(task_name),
            self.in_progress_path(task_name),
        ];

        for path in &paths {
            if path.exists() {
                std::fs::remove_file(path)
                    .wrap_err_with(|| format!("Removing status file: {path:?}"))?;
            }
        }

        Ok(())
    }

    fn mark_path(&self, task_name: &str) -> PathBuf {
        self.mark_directory.join(task_name)
    }

    fn failed_path(&self, task_name: &str) -> PathBuf {
        self.mark_directory.join(format!("{task_name}.failed"))
    }

    fn in_progress_path(&self, task_name: &str) -> PathBuf {
        self.mark_directory.join(format!("{task_name}.in_progress"))
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
