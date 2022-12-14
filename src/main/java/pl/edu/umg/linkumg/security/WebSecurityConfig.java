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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        .antMatchers(HttpMethod.POST, "/companies").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/students").not().authenticated()
                        .antMatchers(HttpMethod.PATCH, "/users/activate/{token}").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/login").not().authenticated()
                        // TODO: deployment
//                        .antMatchers(HttpMethod.POST, "/api/users/login").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/renewToken").not().authenticated()
                        .antMatchers(HttpMethod.POST, "/users/remindPassword/{email}").not().authenticated()
                        .antMatchers(HttpMethod.PATCH, "/users/changePassword/{token}").not().authenticated()

                        .antMatchers(HttpMethod.GET, "/announcements/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/announcements").authenticated()
                        .antMatchers(HttpMethod.GET, "/announcements/image/{fileName}").authenticated()
                        .antMatchers(HttpMethod.GET, "/jobOffers/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/jobOffers").authenticated()
                        .antMatchers(HttpMethod.GET, "/privateLessons/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.GET, "/privateLessons").authenticated()
                        .antMatchers(HttpMethod.POST, "/users/logout").authenticated()
                        .antMatchers(HttpMethod.GET, "/users/info").authenticated()
                        .antMatchers(HttpMethod.GET, "/users/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.POST, "/users/changePassword").authenticated()
                        .antMatchers(HttpMethod.GET, "/users/image/{fileName}").authenticated()
                        .antMatchers(HttpMethod.POST, "/users/image/{stringUUID}").authenticated()

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
                        .antMatchers(HttpMethod.GET, "/announcements/image/{stringUUID}").authenticated()
                        .antMatchers(HttpMethod.PATCH, "/announcements/{stringUUID}").hasRole("COLLEGE")
                        .antMatchers(HttpMethod.DELETE, "/announcements/{stringUUID}").hasRole("COLLEGE")

                        .antMatchers(HttpMethod.POST, "/colleges").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/users/ban/{stringUUID}").hasRole("ADMIN")

                        .anyRequest()
                        .hasRole("ADMIN"))
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

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    // CorsConfiguration corsConfiguration = new CorsConfiguration();
    // corsConfiguration.setAllowedOrigins(Arrays.asList("http://client-prod:4173",
    // "http://client-prod:80",
    // "http://localhost:80", "http://localhost:4173", "*"));
    // corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH",
    // "DELETE", "PUT"));
    // corsConfiguration.setAllowedHeaders(List.of("Authorization"));
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", corsConfiguration);
    // return source;
    // }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
