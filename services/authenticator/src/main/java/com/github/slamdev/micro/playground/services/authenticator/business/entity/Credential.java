package com.github.slamdev.micro.playground.services.authenticator.business.entity;

import lombok.Value;

@Value
public class Credential {
    String email;
    String password;
}
