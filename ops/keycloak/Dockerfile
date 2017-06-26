FROM jboss/keycloak-postgres:latest

ENV JAVA_OPTS        -Xms128M \
                     -Xmx2G \
                     -XX:MetaspaceSize=128M \
                     -XX:MaxMetaspaceSize=256m \
                     -Djava.net.preferIPv4Stack=true \
                     -Djboss.modules.system.pkgs=org.jboss.byteman \
                     -Djava.awt.headless=true \
                     # Added  [org.jgroups.protocols.TCP] (TransferQueueBundler...) to remove JGROUPS WARN message like ... no physical address for ..., dropping message
                     -Djava.net.preferIPv4Stack=true

ADD ["proxyForward.xsl", "/opt/jboss/keycloak/"]
RUN ["java", "-jar", "/usr/share/java/saxon.jar", \
     "-s:/opt/jboss/keycloak/standalone/configuration/standalone-ha.xml", \
     "-xsl:/opt/jboss/keycloak/proxyForward.xsl", \
     "-o:/opt/jboss/keycloak/standalone/configuration/standalone-ha.xml"]

ADD ["keycloak-realm.json", "/opt/jboss/keycloak/standalone/configuration/"]
ADD ["docker-entrypoint.sh", "/opt/jboss/"]

CMD ["--server-config", "standalone-ha.xml", \
     "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", \
     "-Dkeycloak.migration.file=/opt/jboss/keycloak/standalone/configuration/keycloak-realm.json", \
     "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING"]
