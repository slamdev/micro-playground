package com.github.slamdev.micro.playground.libs.spring.convetions;

import org.springframework.core.io.ByteArrayResource;

import static java.util.Base64.getDecoder;

public class Base64DecodedResource extends ByteArrayResource {

    public Base64DecodedResource(byte[] bytes) {
        super(fromBase64(bytes));
    }

    private static byte[] fromBase64(byte[] bytes) {
        return getDecoder().decode(bytes);
    }
}
