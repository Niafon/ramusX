param(
    [string]$Mode = $null
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Path $MyInvocation.MyCommand.Path -Parent
Push-Location $scriptDir
try {
    Copy-Item ..\LICENSE LICENSE -Force

    if ($Mode -eq '--docker') {
        Write-Host 'Building windows installer with docker'
        $pwdPath = (Get-Location).Path.Replace('\\', '/')
        & docker run -v "${pwdPath}:/wine/drive_c/src/" cdrx/nsis
        Write-Host 'Done'
    }
    else {
        Write-Host 'Building windows installer with NSIS'
        $candidateRoots = @()
        if ($env:NSIS_HOME) {
            $candidateRoots += $env:NSIS_HOME
        }

        $programFiles = [Environment]::GetEnvironmentVariable('ProgramFiles')
        if ($programFiles) {
            $candidateRoots += (Join-Path $programFiles 'NSIS')
            $candidateRoots += (Join-Path $programFiles 'NSIS\\Bin')
        }

        $programFilesX86 = [Environment]::GetEnvironmentVariable('ProgramFiles(x86)')
        if ($programFilesX86) {
            $candidateRoots += (Join-Path $programFilesX86 'NSIS')
            $candidateRoots += (Join-Path $programFilesX86 'NSIS\\Bin')
        }

        $nsisPath = $null
        foreach ($root in $candidateRoots | Where-Object { $_ }) {
            $candidate = Join-Path $root 'makensis.exe'
            if (Test-Path $candidate) {
                $nsisPath = $candidate
                break
            }
        }

        if (-not $nsisPath) {
            throw "NSIS not found. Checked: $($candidateRoots -join ', '). Set NSIS_HOME or install NSIS."
        }

        & $nsisPath 'ramus-setup.nsi'
        Write-Host "Done (using $nsisPath)"
    }

    Write-Host "Installer should be in the $((Get-Location).Path)\full"
}
finally {
    if (Test-Path LICENSE) {
        Remove-Item LICENSE -Force
    }
    Pop-Location
}
