## Credentials

Admin account: admin/admin

User account: test/test

## Export command

```bash
docker-compose up
```

```bash
docker network ls
```

```bash
docker run  -it --rm  -v $(pwd):/backup --net keycloak_default --link keycloak_postgres_1:postgres -e POSTGRES_PORT_5432_TCP_ADDR=postgres -e POSTGRES_DATABASE=keycloak -e POSTGRES_USER=keycloak -e POSTGRES_PASSWORD=password jboss/keycloak-postgres -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.realmName=micro-playground -Dkeycloak.migration.usersExportStrategy=REALM_FILE -Dkeycloak.migration.file=/backup/keycloak-realm-$(date +"%Y-%m-%d").json
```
