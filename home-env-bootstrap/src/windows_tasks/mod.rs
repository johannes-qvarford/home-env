pub(crate) mod bootstrap_linux;
pub(crate) mod choco;
pub(crate) mod connect_windows_terminal;
pub(crate) mod install_wsl;
pub(crate) mod scheduled_task;
pub(crate) mod set_path;
pub(crate) mod winget;

pub(crate) use bootstrap_linux::*;
pub(crate) use choco::*;
pub(crate) use connect_windows_terminal::*;
pub(crate) use install_wsl::*;
pub(crate) use scheduled_task::*;
pub(crate) use set_path::*;
pub(crate) use winget::*;

use color_eyre::Result;

use crate::task_registry::{TaskProvider, TaskRegistry};

/// Windows-specific task provider
pub struct WindowsTaskProvider;

impl WindowsTaskProvider {
    pub fn new() -> Self {
        Self
    }
}

impl TaskProvider for WindowsTaskProvider {
    fn provide_tasks(&self, registry: &mut TaskRegistry) -> Result<()> {
        // Register all Windows tasks
        registry.register_task(install_wsl_task());

        // Development tools
        registry.register_task(winget_task("Microsoft.WindowsTerminal"));
        registry.register_task(winget_task("Microsoft.VisualStudioCode"));
        registry.register_task(winget_task("gerardog.gsudo"));
        registry.register_task(winget_task("9PGCV4V3BK4W")); // DevToys
        registry.register_task(winget_task("DBBrowserForSQLite.DBBrowserForSQLite"));
        registry.register_task(winget_task("Microsoft.PowerToys"));

        // Media applications
        registry.register_task(winget_task("Mozilla.Firefox"));
        registry.register_task(winget_task("Brave.Brave"));
        registry.register_task(winget_task("Valve.Steam"));
        registry.register_task(winget_task("XBMCFoundation.Kodi"));
        registry.register_task(winget_task("VideoLAN.VLC"));

        // Package managers
        registry.register_task(winget_task("Chocolatey.Chocolatey"));
        registry.register_task(choco_task("tartube"));

        // Privacy and security
        registry.register_task(winget_task("Proton.ProtonDrive"));
        registry.register_task(winget_task("ProtonTechnologies.ProtonVPN"));
        registry.register_task(winget_task("Bitwarden.Bitwarden"));

        // Utilities
        registry.register_task(winget_task("WinDirStat.WinDirStat"));
        registry.register_task(winget_task("BleachBit.BleachBit"));
        registry.register_task(winget_task("7zip.7zip"));
        registry.register_task(winget_task("HandBrake.HandBrake"));
        registry.register_task(choco_task("messenger"));

        // System integration
        registry.register_task(scheduled_task_task("backup-media", "11:00 am"));
        registry.register_task(scheduled_task_task("upgrade-tools", "12:00 pm"));
        registry.register_task(set_path_task());
        registry.register_task(connect_windows_terminal_task());
        registry.register_task(download_bootstrap_linux_task());
        registry.register_task(run_bootstrap_linux_task());

        Ok(())
    }
}
