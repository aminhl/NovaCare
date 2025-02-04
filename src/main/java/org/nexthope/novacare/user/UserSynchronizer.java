package org.nexthope.novacare.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    /**
     * Synchronizes the user information with the Identity Provider (IdP) using JWT claims.
     * @param jwt the {@link Jwt} object containing the token claims
     */
    public void synchronizeWithIdp(Jwt jwt) {
        log.debug("synchronizeWithIdp - START");
        getUserEmailFromToken(jwt).ifPresent(userEmail -> {
            log.info("Synchronizing user with email : {}", userEmail);
            Optional<User> userByEmail = userRepository.findByEmail(userEmail);
            if (userByEmail.isEmpty()) {
                User user = userMapper.fromTokenClaims(jwt.getClaims());
                userRepository.save(user);
            }
            log.info("Synchronized user with email : {}", userEmail);
        });
        log.debug("synchronizeWithIdp - END");
    }

    /**
     * Extracts the user email attribute from the {@link Jwt}.
     * @param jwt the {@link Jwt} object containing the token claims
     * @return an {@link Optional<String>} containing the user email if exists otherwise an empty Optional
     */
    private Optional<String> getUserEmailFromToken(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        return Optional.ofNullable(claims.get("email")).map(Object::toString);
    }

}
