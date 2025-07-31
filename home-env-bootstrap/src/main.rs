#![windows_subsystem = "console"]
//! Home Environment Bootstrap Tool
//!
//! A GUI-based cross-platform environment setup tool for automating the installation
//! and configuration of development tools, applications, and system settings.

use color_eyre::Result;

mod gui;
#[cfg(unix)]
#[macro_use]
mod linux_tasks;
mod task_registry;
mod utility;
#[cfg(windows)]
mod windows_tasks;

fn main() -> Result<()> {
    match private_main() {
        Ok(_) => std::process::exit(0),
        Err(e) => {
            eprintln!("{e:?}");
            std::process::exit(1)
        }
    }
}

fn private_main() -> Result<()> {
    let no_colors = cfg!(windows) && cfg!(not(debug_assertions));
    std::env::set_var("RUST_SPANTRACE", "1");
    if no_colors {
        std::env::set_var("NO_COLOR", "1");
    } else {
        color_eyre::install()?;
    }

    let registry = task_registry::create_task_registry()?;
    let tasks = registry.get_all_tasks();
    run_gui_mode(tasks)
}

fn run_gui_mode(tasks: Vec<Box<dyn utility::task::Task>>) -> Result<()> {
    let options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default().with_inner_size([1200.0, 800.0]),
        ..Default::default()
    };

    let app = gui::BootstrapApp::new(tasks)?;

    eframe::run_native(
        "Bootstrap Environment Setup",
        options,
        Box::new(|cc| {
            // Set larger font sizes for all text styles
            let mut style = (*cc.egui_ctx.style()).clone();
            style.text_styles.insert(
                egui::TextStyle::Body,
                egui::FontId::new(16.0, egui::FontFamily::Proportional),
            );
            style.text_styles.insert(
                egui::TextStyle::Button,
                egui::FontId::new(16.0, egui::FontFamily::Proportional),
            );
            style.text_styles.insert(
                egui::TextStyle::Heading,
                egui::FontId::new(20.0, egui::FontFamily::Proportional),
            );
            style.text_styles.insert(
                egui::TextStyle::Monospace,
                egui::FontId::new(14.0, egui::FontFamily::Monospace),
            );
            style.text_styles.insert(
                egui::TextStyle::Small,
                egui::FontId::new(12.0, egui::FontFamily::Proportional),
            );

            cc.egui_ctx.set_style(style);

            Box::new(app)
        }),
    )
    .map_err(|e| color_eyre::eyre::eyre!("GUI error: {}", e))?;

    Ok(())
}
