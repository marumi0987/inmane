cd %~dp0..
echo %CD%

docker-compose down

rd /q /s ".\inmane-db\data"
mkdir ".\inmane-db\data"

rd /q /s .\sonarqube\sonarqube_data
mkdir .\sonarqube\sonarqube_data

pause
