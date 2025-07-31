use crate::utility::logging::{LogEntry, LogManager};
use egui::{ScrollArea, Ui};

pub struct LogPanel {
    log_manager: LogManager,
    auto_scroll: bool,
}

impl LogPanel {
    pub fn new(log_manager: LogManager) -> Self {
        Self {
            log_manager,
            auto_scroll: true,
        }
    }

    pub fn show(&mut self, ui: &mut Ui) {
        ui.heading("Log Output");

        ui.horizontal(|ui| {
            ui.checkbox(&mut self.auto_scroll, "Auto-scroll");
            if ui.button("Clear").clicked() {
                self.log_manager.clear_buffer();
            }
        });

        ui.separator();

        let entries = self.log_manager.get_entries();

        ScrollArea::vertical()
            .auto_shrink([false; 2])
            .stick_to_bottom(self.auto_scroll)
            .show(ui, |ui| {
                for entry in &entries {
                    self.show_log_entry(ui, entry);
                }
            });
    }

    fn show_log_entry(&self, ui: &mut Ui, entry: &LogEntry) {
        let timestamp_text = entry.timestamp.format("%Y-%m-%d %H:%M:%S").to_string();
        let combined_text = format!("{} {}", timestamp_text, entry.message);

        ui.horizontal(|ui| {
            // Make the entire log entry selectable as one piece
            let _ = ui.selectable_label(false, egui::RichText::new(&combined_text).monospace());
        });
    }

    pub fn log_manager(&self) -> &LogManager {
        &self.log_manager
    }
}
