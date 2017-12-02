package com.github.slamdev.micro.playground.libs.spring.conventions;

import ch.qos.logback.core.pattern.DynamicConverter;
import net.rakugakibox.spring.boot.logback.access.undertow.UndertowLogbackAccessEvent;

public class RequestUriLoggingConverter extends DynamicConverter<UndertowLogbackAccessEvent> {

    @Override
    public String convert(UndertowLogbackAccessEvent event) {
        Object originalRequestUri = event.getRequest().getAttribute("javax.servlet.error.request_uri");
        String uri = originalRequestUri == null ? event.getRequestURI() : originalRequestUri.toString();
        return uri + event.getQueryString();
    }
}
