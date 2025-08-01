use std::fs::{File, OpenOptions};
use std::io::{BufWriter, Write};
use std::path::PathBuf;
use std::sync::{Arc, Mutex};

use chrono::{DateTime, Local};
use color_eyre::{eyre::Context, Result};
use dirs::config_dir;

#[derive(Clone)]
pub struct LogManager {
    buffer: Arc<Mutex<Vec<LogEntry>>>,
    file_writer: Arc<Mutex<BufWriter<File>>>,
}

#[derive(Clone, Debug)]
pub struct LogEntry {
    pub timestamp: DateTime<Local>,
    pub message: String,
}

impl LogManager {
    pub fn new() -> Result<Self> {
        let log_file_path = Self::get_log_file_path()?;

        if let Some(parent) = log_file_path.parent() {
            std::fs::create_dir_all(parent).wrap_err("Creating log directory")?;
        }

        let file = OpenOptions::new()
            .create(true)
            .append(true)
            .open(&log_file_path)
            .wrap_err_with(|| format!("Opening log file: {log_file_path:?}"))?;

        let file_writer = BufWriter::new(file);

        Ok(Self {
            buffer: Arc::new(Mutex::new(Vec::new())),
            file_writer: Arc::new(Mutex::new(file_writer)),
        })
    }

    pub fn log(&self, message: &str) -> Result<()> {
        let timestamp = Local::now();
        let formatted_timestamp = timestamp.format("%Y-%m-%d %H:%M:%S").to_string();

        let clean_message = Self::strip_ansi_colors(message);

        let entry = LogEntry {
            timestamp,
            message: message.to_string(),
        };

        {
            let mut buffer = self.buffer.lock().unwrap();
            buffer.push(entry);
        }

        {
            let mut writer = self.file_writer.lock().unwrap();
            writeln!(writer, "{formatted_timestamp} {clean_message}")
                .wrap_err("Writing to log file")?;
            writer.flush().wrap_err("Flushing log file")?;
        }

        Ok(())
    }

    pub fn get_entries(&self) -> Vec<LogEntry> {
        self.buffer.lock().unwrap().clone()
    }

    pub fn clear_buffer(&self) {
        self.buffer.lock().unwrap().clear();
    }

    fn get_log_file_path() -> Result<PathBuf> {
        let mut path = config_dir()
            .ok_or_else(|| color_eyre::eyre::eyre!("Could not find config directory"))?;
        path.push("home-env-bootstrap");
        path.push("bootstrap.log");
        Ok(path)
    }

    fn strip_ansi_colors(text: &str) -> String {
        let mut result = String::new();
        let mut chars = text.chars().peekable();

        while let Some(ch) = chars.next() {
            if ch == '\x1b' {
                if chars.peek() == Some(&'[') {
                    chars.next();
                    for ch in chars.by_ref() {
                        if ch.is_ascii_alphabetic() {
                            break;
                        }
                    }
                } else {
                    result.push(ch);
                }
            } else {
                result.push(ch);
            }
        }

        result
    }
}

impl Default for LogManager {
    fn default() -> Self {
        Self::new().expect("Failed to create LogManager")
    }
}
