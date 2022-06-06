#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER keycloak WITH ENCRYPTED PASSWORD '${APP_DB_KEYCLOAK_PASSWORD:-keycloak}';;
	CREATE DATABASE keycloak;
	GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL