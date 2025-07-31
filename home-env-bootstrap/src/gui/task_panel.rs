use crate::utility::{
    task::Task,
    task_status::{TaskStatus, TaskStatusManager},
};
use egui::Ui;

pub struct TaskPanel {
    tasks: Vec<Box<dyn Task>>,
    status_manager: TaskStatusManager,
    selected_task: Option<usize>,
}

impl TaskPanel {
    pub fn new(tasks: Vec<Box<dyn Task>>) -> Self {
        let status_manager = TaskStatusManager::default();
        Self {
            tasks,
            status_manager,
            selected_task: None,
        }
    }

    pub fn show(&mut self, ui: &mut Ui) -> TaskPanelResponse {
        let mut response = TaskPanelResponse::None;

        ui.heading("Bootstrap Tasks");
        ui.separator();

        ui.horizontal(|ui| {
            if ui.button("Run All Remaining").clicked() {
                response = TaskPanelResponse::RunAllRemaining;
            }

            if ui.button("Run Selected Task").clicked() {
                if let Some(selected) = self.selected_task {
                    response = TaskPanelResponse::RunTask(selected);
                }
            }
        });

        ui.separator();

        egui::ScrollArea::vertical()
            .max_height(300.0)
            .show(ui, |ui| {
                for (index, task) in self.tasks.iter().enumerate() {
                    let task_name = task.name();
                    let status = self
                        .status_manager
                        .get_status(&task_name)
                        .unwrap_or(TaskStatus::NotExecuted);

                    ui.horizontal(|ui| {
                        let status_icon = match status {
                            TaskStatus::Completed => "✅",
                            TaskStatus::Failed => "❌",
                            TaskStatus::InProgress => "⏳",
                            TaskStatus::NotExecuted => "⭕",
                        };

                        let is_selected = self.selected_task == Some(index);
                        let text = format!("{status_icon} {task_name}");

                        if ui.selectable_label(is_selected, text).clicked() {
                            self.selected_task = Some(index);
                        }
                    });
                }
            });

        response
    }

    pub fn get_task(&self, index: usize) -> Option<&dyn Task> {
        self.tasks.get(index).map(|task| task.as_ref())
    }

    pub fn get_remaining_tasks(&self) -> Vec<(usize, &dyn Task)> {
        self.tasks
            .iter()
            .enumerate()
            .filter_map(|(index, task)| {
                let status = self
                    .status_manager
                    .get_status(&task.name())
                    .unwrap_or(TaskStatus::NotExecuted);
                if status == TaskStatus::NotExecuted || status == TaskStatus::Failed {
                    Some((index, task.as_ref()))
                } else {
                    None
                }
            })
            .collect()
    }

    #[allow(dead_code)]
    pub fn refresh_status(&mut self) {}
}

pub enum TaskPanelResponse {
    None,
    RunTask(usize),
    RunAllRemaining,
}
