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
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        //todo constants
                        .antMatchers(HttpMethod.POST, "/announcements").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.GET, "/announcements/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/announcements").authenticated()
                        .antMatchers(HttpMethod.PATCH, "/announcements/{stringUUID}").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.DELETE, "/announcements/{stringUUID}").hasRole("COLLEGE")

                        .antMatchers(HttpMethod.POST, "/jobOffers").hasRole("COMPANY")
                        .antMatchers(HttpMethod.GET, "/jobOffers/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/jobOffers").authenticated()
                        .antMatchers(HttpMethod.PATCH, "/jobOffers/{stringUUID}").hasRole("COMPANY")
                        .antMatchers(HttpMethod.DELETE, "/jobOffers/{stringUUID}").hasRole("COMPANY")

                        .antMatchers(HttpMethod.POST, "/privateLessons").hasRole("STUDENT")
                        .antMatchers(HttpMethod.GET, "/privateLessons/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/privateLessons").authenticated()
                        .antMatchers(HttpMethod.PATCH, "/privateLessons/{stringUUID}").hasRole("STUDENT")
                        .antMatchers(HttpMethod.DELETE, "/privateLessons/{stringUUID}").hasRole("STUDENT")

                        .anyRequest()
                        .permitAll()
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
