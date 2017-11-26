package com.github.slamdev.micro.playground.libs.authentication.client;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.time.Instant;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Configuration
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final String INVALID_ID_TOKEN_ERROR_CODE = "invalid_id_token";

    private final String jwtSecret;

    private final SimpleAuthorityMapper authoritiesMapper;

    public JwtAuthenticationProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
        authoritiesMapper = new SimpleAuthorityMapper();
        authoritiesMapper.setConvertToUpperCase(true);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        JWT parsedJwt = (JWT) authentication.getPrincipal();
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.parse(JwsAlgorithms.HS256);
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(jwtSecret.getBytes(UTF_8));
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgorithm, jwkSource);
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        JWTClaimsSet jwtClaimsSet;
        try {
            jwtClaimsSet = jwtProcessor.process(parsedJwt, null);
        } catch (BadJOSEException | JOSEException e) {
            throw new InsufficientAuthenticationException("Unable to validate JWT token", e);
        }
        Instant expiresAt = jwtClaimsSet.getExpirationTime().toInstant();
        Instant issuedAt = null;
        if (jwtClaimsSet.getIssueTime() != null) {
            issuedAt = jwtClaimsSet.getIssueTime().toInstant();
        } else {
            throwInvalidIdTokenException();
        }
        Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
        Map<String, Object> claims = fixDateClaims(jwtClaimsSet.getClaims());
        Jwt jwt = new Jwt(parsedJwt.getParsedString(), requireNonNull(issuedAt), expiresAt, headers, claims);
        OidcIdToken idToken = new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());
        validateIdToken(idToken);
        OidcUser oidcUser = new DefaultOidcUser(getAuthoritiesFromClaims(idToken.getClaimAsStringList("roles")), idToken, new OidcUserInfo(idToken.getClaims()));
        Collection<? extends GrantedAuthority> mappedAuthorities = authoritiesMapper.mapAuthorities(oidcUser.getAuthorities());
        JwtAuthenticationToken authenticationResult = new JwtAuthenticationToken(oidcUser, mappedAuthorities, parsedJwt.getParsedString());
        authenticationResult.setDetails(authentication.getDetails());
        authenticationResult.setAuthenticated(true);
        return authenticationResult;
    }

    private Map<String, Object> fixDateClaims(Map<String, Object> claims) {
        return claims.entrySet().stream()
                .map(this::fixDateClaim)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, Object> fixDateClaim(Map.Entry<String, Object> e) {
        if (e.getValue() instanceof Date) {
            long epochSecond = ((Date) e.getValue()).toInstant().getEpochSecond();
            return new AbstractMap.SimpleEntry<>(e.getKey(), epochSecond);
        }
        return e;
    }

    private Set<GrantedAuthority> getAuthoritiesFromClaims(List<String> claims) {
        return claims.stream().map(SimpleGrantedAuthority::new).collect(toSet());
    }

    private void validateIdToken(OidcIdToken idToken) {
        // 3.1.3.7  ID Token Validation
        // http://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation

        // Validate REQUIRED Claims
        URL issuer = idToken.getIssuer();
        if (issuer == null) {
            this.throwInvalidIdTokenException();
        }
        String subject = idToken.getSubject();
        if (subject == null) {
            this.throwInvalidIdTokenException();
        }
        List<String> audience = idToken.getAudience();
        if (CollectionUtils.isEmpty(audience)) {
            this.throwInvalidIdTokenException();
        }
        Instant expiresAt = idToken.getExpiresAt();
        if (expiresAt == null) {
            this.throwInvalidIdTokenException();
        }
        Instant issuedAt = idToken.getIssuedAt();
        if (issuedAt == null) {
            this.throwInvalidIdTokenException();
        }

        // 4. If the ID Token contains multiple audiences,
        // the Client SHOULD verify that an azp Claim is present.
        String authorizedParty = idToken.getAuthorizedParty();
        if (audience.size() > 1 && authorizedParty == null) {
            this.throwInvalidIdTokenException();
        }

        // 9. The current time MUST be before the time represented by the exp Claim.
        Instant now = Instant.now();
        if (!now.isBefore(requireNonNull(expiresAt))) {
            this.throwInvalidIdTokenException();
        }

        // 10. The iat Claim can be used to reject tokens that were issued too far away from the current time,
        // limiting the amount of time that nonces need to be stored to prevent attacks.
        // The acceptable range is Client specific.
        Instant maxIssuedAt = now.plusSeconds(30);
        if (requireNonNull(issuedAt).isAfter(maxIssuedAt)) {
            this.throwInvalidIdTokenException();
        }
    }

    private void throwInvalidIdTokenException() {
        OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE);
        throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreAuthenticatedToken.class.isAssignableFrom(authentication);
    }
}
