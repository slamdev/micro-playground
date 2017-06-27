#!/bin/bash

if [ $KEYCLOAK_USER ] && [ $KEYCLOAK_PASSWORD ]; then
    keycloak/bin/add-user-keycloak.sh --user $KEYCLOAK_USER --password $KEYCLOAK_PASSWORD
fi

export CONTAINER_IP=$(hostname -i)
exec /opt/jboss/keycloak/bin/standalone.sh -b $CONTAINER_IP -bmanagement $CONTAINER_IP -Djboss.default.multicast.address=224.0.55.55 $@
exit $?
