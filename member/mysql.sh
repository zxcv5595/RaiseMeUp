#!
docker run -d \
--name raisemeup-mysql \
-e MYSQL_ROOT_PASSWORD="1234" \
-p 3306:3306 \
mysql:latest