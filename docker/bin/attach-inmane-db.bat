FOR /F %%i in ('docker ps -aqf "name=docker_inmane-db_1"') do set CID=%%i
docker exec -it %CID% /bin/bash
