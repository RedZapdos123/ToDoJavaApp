# This PowerShell script launches the To-Do List Application.
# Created by Mridankan Mandal.

$Host.UI.RawUI.WindowTitle = "ToDo Java App: Made by Mridankan Mandal"

$jarFile = Join-Path $PSScriptRoot "ToDoApp.jar"

if (-not (Test-Path $jarFile)) {
    Write-Host ""
    Write-Host "Error: ToDoApp.jar not found in the current directory." -ForegroundColor Red
    Write-Host "Please ensure the JAR file exists at: $jarFile" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Launching To-Do List Application..." -ForegroundColor Green
Write-Host "Tasks will be stored in tasks.json in the current directory." -ForegroundColor Cyan

try {
    & java -jar $jarFile
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "Error: Failed to launch the application." -ForegroundColor Red
        Write-Host "Please ensure Java JDK 17 or later is installed and in your PATH." -ForegroundColor Yellow
        Write-Host ""
        Read-Host "Press Enter to exit"
        exit 1
    }
}
catch {
    Write-Host ""
    Write-Host "Error: An exception occurred while launching the application." -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Please ensure Java JDK 17 or later is installed." -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}
