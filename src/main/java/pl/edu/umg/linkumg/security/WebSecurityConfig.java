package pl.edu.umg.linkumg.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final RsaKeyProperties rsaKeys;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        .antMatchers(HttpMethod.POST, "/companies").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/students").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/activate/{token}").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/login").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/renewToken").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/remindPassword/{email}").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/changePassword/{token}").not().authenticated()

                        .antMatchers(HttpMethod.GET, "/announcements/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/announcements").authenticated()
                        .antMatchers(HttpMethod.GET, "/jobOffers/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/jobOffers").authenticated()
                        .antMatchers(HttpMethod.GET, "/privateLessons/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/privateLessons").authenticated()
                        .antMatchers(HttpMethod.POST, "/users/logout").authenticated()
                        .antMatchers(HttpMethod.GET, "/users/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.POST, "/users/changePassword").authenticated()

                        .antMatchers(HttpMethod.PATCH, "/students").hasRole("STUDENT")
                        .antMatchers(HttpMethod.POST, "/privateLessons").hasRole("STUDENT")
                        .antMatchers(HttpMethod.PATCH, "/privateLessons/{stringUUID}").hasRole("STUDENT")
                        .antMatchers(HttpMethod.DELETE, "/privateLessons/{stringUUID}").hasRole("STUDENT")

                        .antMatchers(HttpMethod.PATCH, "/companies").hasRole("COMPANY")
                        .antMatchers(HttpMethod.POST, "/jobOffers").hasRole("COMPANY")
                        .antMatchers(HttpMethod.PATCH, "/jobOffers/{stringUUID}").hasRole("COMPANY")
                        .antMatchers(HttpMethod.DELETE, "/jobOffers/{stringUUID}").hasRole("COMPANY")

                        .antMatchers(HttpMethod.PATCH, "/colleges").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.POST, "/announcements").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.PATCH, "/announcements/{stringUUID}").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.DELETE, "/announcements/{stringUUID}").hasRole("COLLEGE")

                        .antMatchers(HttpMethod.POST, "/collages").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/users/ban/{stringUUID}").hasRole("ADMIN")

                        .anyRequest()
                        .hasRole("ADMIN")
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
