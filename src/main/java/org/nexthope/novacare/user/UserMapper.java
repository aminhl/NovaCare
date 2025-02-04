package org.nexthope.novacare.user;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserMapper {

    /**
     *
     * @param claims a Map
     * @return a {@link User} object filled with used infos
     */
    public User fromTokenClaims(Map<String, Object> claims) {
        User user = new User();
        user.setId(claims.getOrDefault("sub", null).toString());
        user.setFirstname(claims.getOrDefault("given_name", claims.getOrDefault("nick_name", null)).toString());
        user.setLastname(claims.getOrDefault("family_name", null).toString());
        user.setEmail(claims.getOrDefault("email", null).toString());
        return user;
    }
}
