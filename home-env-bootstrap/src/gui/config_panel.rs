use crate::utility::task_status::TaskStatusManager;
use arboard::Clipboard;
use color_eyre::Result;
use egui::Ui;
use std::process::Command;

pub struct ConfigPanel {
    config_dir: String,
    clipboard: Option<Clipboard>,
    last_clipboard_error: Option<String>,
    last_explorer_error: Option<String>,
}

impl ConfigPanel {
    pub fn new() -> Result<Self> {
        let config_dir = TaskStatusManager::get_config_directory()?
            .to_string_lossy()
            .to_string();

        // Initialize clipboard (may fail on some systems)
        let clipboard = Clipboard::new().ok();

        Ok(Self {
            config_dir,
            clipboard,
            last_clipboard_error: None,
            last_explorer_error: None,
        })
    }

    pub fn show(&mut self, ui: &mut Ui) {
        ui.heading("Configuration");
        ui.separator();

        ui.horizontal(|ui| {
            ui.label("Config Directory:");
            ui.monospace(&self.config_dir);
        });

        ui.horizontal(|ui| {
            if ui.button("📋 Copy Path").clicked() {
                self.copy_to_clipboard();
            }

            if ui.button("📁 Open in Explorer").clicked() {
                self.open_in_explorer();
            }
        });

        // Show error messages if any
        if let Some(error) = &self.last_clipboard_error {
            ui.colored_label(egui::Color32::RED, format!("Clipboard error: {error}"));
        }

        if let Some(error) = &self.last_explorer_error {
            ui.colored_label(egui::Color32::RED, format!("Explorer error: {error}"));
        }
    }

    fn copy_to_clipboard(&mut self) {
        self.last_clipboard_error = None;

        if let Some(clipboard) = &mut self.clipboard {
            if let Err(e) = clipboard.set_text(&self.config_dir) {
                self.last_clipboard_error = Some(format!("{e:?}"));
            }
        } else {
            self.last_clipboard_error = Some("Clipboard not available".to_string());
        }
    }

    fn open_in_explorer(&mut self) {
        self.last_explorer_error = None;

        let result = if cfg!(target_os = "windows") || self.is_wsl() {
            // Use explorer.exe for both Windows and WSL
            Command::new("explorer.exe").arg(&self.config_dir).spawn()
        } else {
            // Use xdg-open for Linux
            Command::new("xdg-open").arg(&self.config_dir).spawn()
        };

        if let Err(e) = result {
            self.last_explorer_error = Some(format!("{e:?}"));
        }
    }

    fn is_wsl(&self) -> bool {
        // Check if we're running in WSL by looking for WSL environment variables
        std::env::var("WSL_DISTRO_NAME").is_ok()
            || std::env::var("WSLENV").is_ok()
            || std::fs::read_to_string("/proc/version")
                .map(|content| content.to_lowercase().contains("microsoft"))
                .unwrap_or(false)
    }
}
