use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct CaddyStartupTask;

impl task::Task for CaddyStartupTask {
    fn name(&self) -> String {
        format!("caddy_startup_task")
    }

    fn execute(&self) -> Result<()> {
        let powershell_string = powershell_string(self);
        let acceptable_status_codes = [0, 1];
        process::execute(
            "powershell.exe",
            &["-command", &powershell_string],
            &acceptable_status_codes,
        )
        .wrap_err_with(|| format!("Executing caddy startup setup: '{powershell_string}'"))
    }
}

fn powershell_string(_task: &CaddyStartupTask) -> String {
    format!(
        r#"
        # Create Caddy configuration directory if it doesn't exist
        $caddyDir = "$env:USERPROFILE\.caddy"
        if (!(Test-Path $caddyDir)) {{
            New-Item -ItemType Directory -Path $caddyDir -Force
        }}

        # Create Caddyfile with reverse proxy configuration
        $caddyfile = @"
:64343 {{
    reverse_proxy 127.0.0.1:64342 {{
        header_up Host 127.0.0.1:64342
    }}
}}
"@
        $caddyfile | Out-File -FilePath "$caddyDir\Caddyfile" -Encoding UTF8

        # Remove existing scheduled task if it exists
        $taskName = "CaddyProxy"
        $exists = (Get-ScheduledTask | Where-Object {{ $_.TaskName -eq $taskName }}).Count -ne 0
        if ($exists) {{
            Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
        }}

        # Create startup scheduled task for Caddy
        $trigger = New-ScheduledTaskTrigger -AtStartup
        $action = New-ScheduledTaskAction -Execute "$env:LOCALAPPDATA\Microsoft\WinGet\Packages\CaddyServer.Caddy_Microsoft.Winget.Source_8wekyb3d8bbwe\caddy.exe" -Argument "run --config `"$caddyDir\Caddyfile`"" -WorkingDirectory $caddyDir
        $principal = New-ScheduledTaskPrincipal -UserId $env:USERNAME -LogonType Interactive
        
        Register-ScheduledTask -Action $action -Trigger $trigger -TaskName $taskName -Description "Caddy reverse proxy for MCP" -TaskPath "\Startup\" -Principal $principal
    "#
    )
}

pub(crate) fn caddy_startup_task() -> Box<dyn task::Task> {
    Box::new(CaddyStartupTask)
}
