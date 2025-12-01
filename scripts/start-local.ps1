# script to start the app locally (Windows PowerShell)
# - ensures the DB exists (requires mysql client or Docker)
# - builds the project
# - runs the jar with local profile

Param(
    [switch]$UseDocker
)

function Ensure-Database {
    param(
        [string]$DbName = 'matchpoint',
        [string]$RootUser = 'root',
        [string]$RootPass = '123456789'
    )

    if ($UseDocker) {
        Write-Host "Starting MySQL container for local dev..."
        docker run --name matchpoint-mysql -e MYSQL_ROOT_PASSWORD=$RootPass -e MYSQL_DATABASE=$DbName -p 3306:3306 -d mysql:8.1
        Start-Sleep -Seconds 8
        return
    }

    # Try mysql client
    try {
        mysql -u $RootUser -p$RootPass -e "CREATE DATABASE IF NOT EXISTS $DbName CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        Write-Host "Database ensured: $DbName"
    } catch {
        Write-Warning "Could not ensure DB via mysql client. Install MySQL client or run script with -UseDocker to start a local MySQL container."
    }
}

# Build
Write-Host "Building project..."
./mvnw -DskipTests package

# Ensure DB
Ensure-Database -UseDocker:$UseDocker

# Run
Write-Host "Starting application with profile 'local' (port 8080)..."
java -Dspring.profiles.active=local -Dserver.port=8080 -jar target/matchpoint-0.0.1-SNAPSHOT.jar

