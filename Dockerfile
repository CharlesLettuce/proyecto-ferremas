# ───── MySQL para Ferremas ─────
FROM mysql/mysql-server:8.0

ENV MYSQL_DATABASE=ferremas_db
ENV MYSQL_USER=carlos
ENV MYSQL_PASSWORD=1234
ENV MYSQL_ROOT_PASSWORD=root_secret_password

COPY ./create_tables.sql /docker-entrypoint-initdb.d/
