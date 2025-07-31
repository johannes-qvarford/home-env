use color_eyre::Result;
use eframe::{App, Frame};
use egui::{CentralPanel, Context, SidePanel};
use std::sync::{Arc, Mutex};

use super::{
    log_panel::LogPanel,
    task_panel::{TaskPanel, TaskPanelResponse},
};
use crate::utility::{logging::LogManager, task::Task, task_status::TaskStatusManager};

pub struct BootstrapApp {
    task_panel: TaskPanel,
    log_panel: LogPanel,
    status_manager: TaskStatusManager,
    executor: Arc<Mutex<Option<TaskExecutor>>>,
}

impl BootstrapApp {
    pub fn new(tasks: Vec<Box<dyn Task>>) -> Result<Self> {
        let log_manager = LogManager::new()?;
        let status_manager = TaskStatusManager::new()?;

        log_manager.log("Bootstrap GUI started")?;

        Ok(Self {
            task_panel: TaskPanel::new(tasks),
            log_panel: LogPanel::new(log_manager),
            status_manager,
            executor: Arc::new(Mutex::new(None)),
        })
    }
}

impl App for BootstrapApp {
    fn update(&mut self, ctx: &Context, _frame: &mut Frame) {
        SidePanel::left("task_panel")
            .resizable(true)
            .default_width(300.0)
            .show(ctx, |ui| {
                let response = self.task_panel.show(ui);
                self.handle_task_response(response);
            });

        CentralPanel::default().show(ctx, |ui| {
            self.log_panel.show(ui);
        });

        if self.is_executor_running() {
            ctx.request_repaint_after(std::time::Duration::from_millis(100));
        }
    }
}

impl BootstrapApp {
    fn handle_task_response(&mut self, response: TaskPanelResponse) {
        match response {
            TaskPanelResponse::RunTask(index) => {
                let task_name = if let Some(task) = self.task_panel.get_task(index) {
                    task.name()
                } else {
                    return;
                };
                self.execute_task_by_name(&task_name);
            }
            TaskPanelResponse::RunAllRemaining => {
                let task_names: Vec<String> = self
                    .task_panel
                    .get_remaining_tasks()
                    .into_iter()
                    .map(|(_, task)| task.name())
                    .collect();
                self.execute_tasks_by_names(task_names);
            }
            TaskPanelResponse::None => {}
        }
    }

    fn execute_task_by_name(&mut self, task_name: &str) {
        let log_manager = self.log_panel.log_manager().clone();

        if let Err(e) = log_manager.log(&format!("Starting task: {task_name}")) {
            eprintln!("Failed to log: {e:?}");
        }

        if let Err(e) = self.status_manager.mark_in_progress(task_name) {
            eprintln!("Failed to mark task in progress: {e:?}");
        }
    }

    fn execute_tasks_by_names(&mut self, task_names: Vec<String>) {
        let log_manager = self.log_panel.log_manager().clone();

        if let Err(e) = log_manager.log("Starting execution of all remaining tasks") {
            eprintln!("Failed to log: {e:?}");
        }

        for task_name in task_names {
            self.execute_task_by_name(&task_name);
        }
    }

    fn is_executor_running(&self) -> bool {
        self.executor.lock().unwrap().is_some()
    }
}

struct TaskExecutor {}

impl TaskExecutor {
    #[allow(dead_code)]
    fn new() -> Self {
        Self {}
    }
}
