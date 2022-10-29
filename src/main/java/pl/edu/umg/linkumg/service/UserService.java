package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.controllers.UserController;
import pl.edu.umg.linkumg.dto.JwtRequest;
import pl.edu.umg.linkumg.models.User;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserService {

    private static Logger LOGGER = LogManager.getLogger(UserService.class);

    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;


    public String generateToken(JwtRequest jwtRequest) {
        User user =
                userRepository
                        .findByEmail(jwtRequest.email())
                        .orElseThrow(() -> new UsernameNotFoundException(String
                                .format("User with email %s not found.", jwtRequest.email())));
        LOGGER.info("User found in database {}", user);
        if (!passwordEncoder.matches(jwtRequest.password(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        Instant now = Instant.now();
        String scope = user
                .getClass()
                .getSimpleName()
                .toUpperCase(Locale.ROOT);
        LOGGER.info("User roles is {}", scope);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .subject(jwtRequest.email())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // TODO: not used
    public Page<User> findUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // TODO: not used
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
