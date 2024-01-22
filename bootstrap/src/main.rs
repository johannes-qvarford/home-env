use color_eyre::{Result, eyre::Context};

// TODO: Separate utility from tasks
mod utility;
mod tasks;

use utility::task::Execution;
use tasks::winget::winget;
use tasks::choco::choco;
use tasks::scheduled_task::scheduled_task;
use tasks::nerd_font::nerd_font;
use tasks::install_wsl::install_wsl;
use colored::Colorize;
use tasks::connect_windows_terminal::connect_windows_terminal;
use tasks::move_sensitive_information::move_sensitive_information;

fn main() -> Result<()> {
    color_eyre::install()?;

    let tasks = vec![
        // Before restart
        install_wsl(),

        // Development
        winget("Microsoft.WindowsTerminal"),
        winget("Microsoft.VisualStudioCode"),
        winget("gerardog.gsudo"),
        winget("9PGCV4V3BK4W"), // DevToys
        winget("DBBrowserForSQLite.DBBrowserForSQLite"),
        winget("Microsoft.PowerToys"),
        // winget("Canonical.Ubuntu.2204"), The latest Ubuntu should be installed by default.

        // Media
        winget("Mozilla.Firefox"),
        winget("Brave.Brave"),
        winget("Valve.Steam"),
        winget("XBMCFoundation.Kodi"),
        winget("VideoLAN.VLC"),
        winget("Chocolatey.Chocolatey"),
        choco("tartube"),

        // Privacy
        winget("Proton.ProtonDrive"),
        winget("ProtonTechnologies.ProtonVPN"),
        winget("Bitwarden.Bitwarden"),

        // Utility
        winget("Facebook.Messenger"),
        winget("WinDirStat.WinDirStat"),
        winget("BleachBit.BleachBit"),
        winget("7zip.7zip"),


        // WSL

        // Clone repository
        bash!("github"),
        bash!("clone-home-env"),

        // Home-env usage.
        bash!("dotfiles"),
        bash!("fish"),

        // win-server
        bash!("wslu"),
        bash!("systemd"),
        bash!("docker"),

        // privacy
        bash!("ansible"),
        
        // languages
        bash!("nushell"),
        bash!("powershell"),
        bash!("rust"),
        bash!("java"),
        bash!("zig"),

        // Fun
        bash!("colors"),
        bash!("fzf"),
        bash!("extra"),

        bash!("backup"),
        scheduled_task("backup-media", "11:00 am"),
        scheduled_task("upgrade-tools", "12:00 pm"),
        move_sensitive_information(),

        // Cool fonts
        nerd_font(),
        connect_windows_terminal(),

        // Not really used
        // winget("WiresharkFoundation.Wireshark"),
        // winget("Oracle.VirtualBox"),
        // winget("Lexikos.AutoHotkey"),
        // winget("HandBrake.HandBrake"),
        // winget("Telerik.Fiddler.Classic"),
    ];

    // let tasks: Vec<Box<dyn Task>> = vec![
    //     // Z: Drive
    // ];

    for task in tasks {
        let name = task.name();
        let execution = task.execute_if_needed().wrap_err_with(|| format!("Executing task '{name}' if needed"))?;
        if execution == Execution::Performed && task.requires_restart() {
            println!("{}", "Please restart now before running again.".yellow());
            return Ok(())
        }
    }

    Ok(())
}
