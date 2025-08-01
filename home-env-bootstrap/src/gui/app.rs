use color_eyre::Result;
use eframe::{App, Frame};
use egui::{CentralPanel, Context, SidePanel, TopBottomPanel};
use std::collections::HashMap;
use tokio::sync::mpsc;

use super::{
    config_panel::ConfigPanel,
    log_panel::LogPanel,
    task_panel::{TaskPanel, TaskPanelResponse},
};
use crate::utility::{logging::LogManager, task::Task, task_status::TaskStatusManager};

pub struct BootstrapApp {
    task_panel: TaskPanel,
    log_panel: LogPanel,
    config_panel: ConfigPanel,
    status_manager: TaskStatusManager,
    executor: TaskExecutor,
    message_receiver: mpsc::UnboundedReceiver<TaskMessage>,
    running_tasks: HashMap<String, bool>,
}

#[derive(Debug)]
enum TaskMessage {
    Output { output: String },
    Completed { task_name: String, success: bool },
}

impl BootstrapApp {
    pub fn new(tasks: Vec<Box<dyn Task>>) -> Result<Self> {
        let log_manager = LogManager::new()?;
        let status_manager = TaskStatusManager::new()?;
        let (sender, receiver) = mpsc::unbounded_channel();

        log_manager.log("Bootstrap GUI started")?;

        Ok(Self {
            task_panel: TaskPanel::new(tasks),
            log_panel: LogPanel::new(log_manager),
            config_panel: ConfigPanel::new()?,
            status_manager,
            executor: TaskExecutor::new(sender),
            message_receiver: receiver,
            running_tasks: HashMap::new(),
        })
    }
}

impl App for BootstrapApp {
    fn update(&mut self, ctx: &Context, _frame: &mut Frame) {
        // Process messages from async tasks
        self.process_task_messages();

        // Configuration panel at the top
        TopBottomPanel::top("config_panel").show(ctx, |ui| {
            self.config_panel.show(ui);
        });

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

        // Keep GUI responsive during task execution
        if !self.running_tasks.is_empty() {
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
        if self.running_tasks.contains_key(task_name) {
            return; // Task already running
        }

        let log_manager = self.log_panel.log_manager().clone();
        if let Err(e) = log_manager.log(&format!("Starting task: {task_name}")) {
            eprintln!("Failed to log: {e:?}");
        }

        if let Err(e) = self.status_manager.mark_in_progress(task_name) {
            eprintln!("Failed to mark task in progress: {e:?}");
        }

        // Find and execute the task
        if let Some(task) = self.task_panel.get_task_by_name(task_name) {
            self.executor.execute_task(task);
            self.running_tasks.insert(task_name.to_string(), true);
        } else {
            let log_msg = format!("Error: Task '{task_name}' not found");
            if let Err(e) = self.log_panel.log_manager().log(&log_msg) {
                eprintln!("Failed to log error: {e:?}");
            }
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

    fn process_task_messages(&mut self) {
        while let Ok(message) = self.message_receiver.try_recv() {
            match message {
                TaskMessage::Output { output } => {
                    if let Err(e) = self.log_panel.log_manager().log(&output) {
                        eprintln!("Failed to log task output: {e:?}");
                    }
                }
                TaskMessage::Completed { task_name, success } => {
                    self.running_tasks.remove(&task_name);

                    let status_result = if success {
                        self.status_manager.mark_completed(&task_name)
                    } else {
                        self.status_manager.mark_failed(&task_name)
                    };

                    if let Err(e) = status_result {
                        eprintln!("Failed to update task status: {e:?}");
                    }

                    // Refresh the task panel status
                    self.task_panel.refresh_status();

                    let status_msg = if success {
                        "completed successfully"
                    } else {
                        "failed"
                    };
                    let log_msg = format!("Task '{task_name}' {status_msg}");
                    if let Err(e) = self.log_panel.log_manager().log(&log_msg) {
                        eprintln!("Failed to log task completion: {e:?}");
                    }
                }
            }
        }
    }
}

pub struct TaskExecutor {
    sender: mpsc::UnboundedSender<TaskMessage>,
}

impl TaskExecutor {
    fn new(sender: mpsc::UnboundedSender<TaskMessage>) -> Self {
        Self { sender }
    }

    fn execute_task(&self, task: &dyn Task) {
        let sender = self.sender.clone();
        let task_name = task.name();

        // Send initial output message
        let _ = sender.send(TaskMessage::Output {
            output: format!("Executing task: {task_name}"),
        });

        // Execute the task synchronously and send result
        let result = task.execute();
        let success = result.is_ok();

        if let Err(e) = result {
            let _ = sender.send(TaskMessage::Output {
                output: format!("Task error: {e:?}"),
            });
        }

        let _ = sender.send(TaskMessage::Completed { task_name, success });
    }
}
