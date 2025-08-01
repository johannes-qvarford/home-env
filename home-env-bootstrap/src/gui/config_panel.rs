use crate::utility::config::BootstrapConfig;
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
        let config_dir = BootstrapConfig::get_config_directory()?
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

        let result = if cfg!(target_os = "windows") {
            // Native Windows - use path as-is
            Command::new("explorer.exe").arg(&self.config_dir).spawn()
        } else if self.is_wsl() {
            // WSL - convert path using wslpath and use explorer.exe
            match self.convert_wsl_path_for_explorer(&self.config_dir) {
                Ok(windows_path) => Command::new("explorer.exe").arg(&windows_path).spawn(),
                Err(e) => {
                    self.last_explorer_error = Some(format!("Failed to convert WSL path: {e:?}"));
                    return;
                }
            }
        } else {
            // Pure Linux - use xdg-open
            Command::new("xdg-open").arg(&self.config_dir).spawn()
        };

        if let Err(e) = result {
            self.last_explorer_error = Some(format!("{e:?}"));
        }
    }

    fn convert_wsl_path_for_explorer(&self, wsl_path: &str) -> Result<String, std::io::Error> {
        // Use wslpath to convert WSL path to Windows path
        let output = Command::new("wslpath").arg("-w").arg(wsl_path).output()?;

        if output.status.success() {
            let windows_path = String::from_utf8_lossy(&output.stdout).trim().to_string();
            Ok(windows_path)
        } else {
            let error = String::from_utf8_lossy(&output.stderr);
            Err(std::io::Error::other(format!("wslpath failed: {error}")))
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
