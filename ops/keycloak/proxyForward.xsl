<?xml version="1.0" encoding="UTF-8"?>
<!--suppress XmlUnusedNamespaceDeclaration -->
<xsl:stylesheet version="2.0"
                xmlns:dm="urn:jboss:domain:4.0"
                xmlns:ut="urn:jboss:domain:undertow:3.0"
                xmlns:kc="urn:jboss:domain:keycloak-server:1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Enable proxy address forwarding -->
    <xsl:template xmlns="urn:jboss:domain:undertow:3.0" xmlns:ut="urn:jboss:domain:undertow:3.0"
                  match="//ut:subsystem/ut:server/ut:http-listener">
        <http-listener proxy-address-forwarding="true">
            <xsl:apply-templates select="@*|node()"/>
        </http-listener>
    </xsl:template>

    <!-- Enable multicast on public interface -->
    <xsl:template match="//dm:server/dm:interfaces/dm:interface[@name='public']">
        <dm:interface name="public">
            <dm:inet-address value="${{jboss.bind.address:127.0.0.1}}"/>
            <dm:multicast/>
        </dm:interface>
    </xsl:template>

    <!-- Configure jgroups-tcp socket binding to use public interface -->
    <xsl:template match="//dm:server/dm:socket-binding-group/dm:socket-binding[@name='jgroups-udp']/@interface">
        <xsl:attribute name="interface">
            <xsl:text>public</xsl:text>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>
