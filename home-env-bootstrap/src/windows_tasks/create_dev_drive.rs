use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct CreateDevDriveTask;

impl task::Task for CreateDevDriveTask {
    fn name(&self) -> String {
        format!("create_dev_drive")
    }

    fn execute(&self) -> Result<()> {
        let powershell_string = powershell_string(self);
        let acceptable_status_codes = [0, 1]; // Allow exit code 1 in case drive already exists
        process::execute(
            "powershell.exe",
            &["-ExecutionPolicy", "Bypass", "-Command", &powershell_string],
            &acceptable_status_codes,
        )
        .wrap_err_with(|| format!("Creating DEV drive with PowerShell"))
    }
}

fn powershell_string(_task: &CreateDevDriveTask) -> String {
    format!(
        r#"
        # Check if Z: drive already exists
        if (Test-Path "Z:\") {{
            Write-Host "Z: drive already exists"
            exit 0
        }}

        # Create a 100GB DEV drive VHD on Z: using diskpart (no restart required)
        try {{
            Write-Host "Creating VHD using diskpart..."
            $vhdPath = "$env:USERPROFILE\DevDrive.vhdx"
            
            # Create diskpart script file
            $diskpartScript = @"
create vdisk file="$vhdPath" maximum=102400 type=expandable
select vdisk file="$vhdPath"
attach vdisk
create partition primary
active
assign letter=Z
exit
"@
            
            $scriptFile = "$env:TEMP\create_dev_drive.txt"
            $diskpartScript | Out-File -FilePath $scriptFile -Encoding ASCII
            
            # Run diskpart to create and mount VHD
            Write-Host "Running diskpart to create VHD..."
            $result = Start-Process -FilePath "diskpart.exe" -ArgumentList "/s `"$scriptFile`"" -Wait -PassThru -WindowStyle Hidden
            
            if ($result.ExitCode -ne 0) {{
                throw "Diskpart failed with exit code $($result.ExitCode)"
            }}
            
            # Clean up script file
            Remove-Item -Path $scriptFile -Force -ErrorAction SilentlyContinue
            
            # Wait a moment for the drive to be ready
            Start-Sleep -Seconds 3
            
            # Format as Dev Drive with ReFS (Windows 11 feature)
            Write-Host "Formatting as Dev Drive with ReFS optimizations..."
            try {{
                Format-Volume -DriveLetter Z -FileSystem ReFS -NewFileSystemLabel "DEV" -Force -DevDrive
            }} catch {{
                Write-Host "Dev Drive formatting failed, using NTFS instead..."
                Format-Volume -DriveLetter Z -FileSystem NTFS -NewFileSystemLabel "DEV" -Force
            }}
            
            # Create common development directories
            New-Item -Path "Z:\Projects" -ItemType Directory -Force
            New-Item -Path "Z:\Tools" -ItemType Directory -Force
            New-Item -Path "Z:\Cache" -ItemType Directory -Force
            
            Write-Host "DEV drive created successfully at Z:"
        }} catch {{
            Write-Error "Failed to create DEV drive: $_"
            exit 1
        }}
        "#
    )
}

pub(crate) fn create_dev_drive_task() -> Box<dyn task::Task> {
    Box::new(CreateDevDriveTask)
}