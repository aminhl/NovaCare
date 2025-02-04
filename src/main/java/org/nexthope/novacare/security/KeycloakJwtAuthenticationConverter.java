package org.nexthope.novacare.security;

import lombok.NonNull;
import org.apache.commons.collections4.MapUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ACCOUNT = "account";
    private static final String ROLES = "roles";

    @NonNull
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(
                jwt,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(jwt).stream(),
                        extractResourceRoles(jwt).stream()
                ).collect(Collectors.toSet())
        );
    }

    /**
     * Extract resource roles from the jwt and return them into a set of {@link SimpleGrantedAuthority} objects.
     * @param jwt the {@link Jwt} object containing the token claims
     * @return a set of {@link SimpleGrantedAuthority} objects representing the user roles
     */
    private Set<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = MapUtils.emptyIfNull(jwt.getClaim(RESOURCE_ACCESS));
        Map<String, List<String>> eternal = (Map<String, List<String>>) resourceAccess.get(ACCOUNT);
        List<String> roles = eternal.get(ROLES);

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }

}
