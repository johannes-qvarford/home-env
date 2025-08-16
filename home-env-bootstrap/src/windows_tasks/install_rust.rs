use color_eyre::{eyre::Context, Result};
use std::fs;

use crate::{
    utility::http,
    utility::process,
    utility::task::{self},
};

pub(crate) struct InstallRustTask;

impl task::Task for InstallRustTask {
    fn name(&self) -> String {
        format!("install_rust")
    }

    fn execute(&self) -> Result<()> {
        println!("Installing Rust programming language...");

        // Download rustup-init.exe
        let rustup_url = "https://win.rustup.rs/x86_64";
        let rustup_path = std::env::temp_dir().join("rustup-init.exe");

        println!("Downloading rustup installer...");
        let bytes = http::download(rustup_url).wrap_err("Failed to download rustup installer")?;

        fs::write(&rustup_path, bytes)
            .wrap_err("Failed to write rustup installer to temporary file")?;

        // Run rustup-init with default settings
        println!("Running rustup installer...");
        process::execute(
            rustup_path.to_str().unwrap(),
            &["-y", "--default-toolchain", "stable"],
            &[],
        )
        .wrap_err("Failed to run rustup installer")?;

        println!("Rust installed successfully!");
        println!("Note: You may need to restart your terminal or add ~/.cargo/bin to PATH to use Rust commands.");

        // Clean up the installer
        let _ = fs::remove_file(&rustup_path);

        Ok(())
    }
}

pub(crate) fn install_rust_task() -> Box<dyn task::Task> {
    Box::new(InstallRustTask)
}
