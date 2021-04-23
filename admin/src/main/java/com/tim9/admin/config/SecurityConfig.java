package com.tim9.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import com.tim9.admin.security.AudienceValidator;
import com.tim9.admin.security.PermissionClaimConverter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);
        jwtDecoder.setClaimSetConverter(new PermissionClaimConverter());

        return jwtDecoder;
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable().authorizeRequests()
        
    	.antMatchers(HttpMethod.GET, "/api/csr/**").hasAuthority("SCOPE_read:csr")
        .antMatchers(HttpMethod.POST, "/api/csr/**").hasAuthority("SCOPE_write:csr")
        .antMatchers(HttpMethod.DELETE, "/api/csr/**").hasAuthority("SCOPE_delete:csr")
        .antMatchers("/api/csr/by-page").hasAuthority("SCOPE_read:csr")
        .antMatchers("/api/csr/verify-csr").permitAll()
        
        .antMatchers(HttpMethod.GET, "/api/certificates/**").hasAuthority("SCOPE_read:certificates")
        .antMatchers(HttpMethod.POST, "/api/certificates/**").hasAuthority("SCOPE_write:certificates")
        .antMatchers(HttpMethod.DELETE, "/api/certificates/**").hasAuthority("SCOPE_delete:certificates")
        .antMatchers("/api/certificates/revoke").hasAuthority("SCOPE_delete:certificates")
        
        .antMatchers("**").authenticated()
        .and().oauth2ResourceServer().jwt();
    }
    
}