package com.tim9.bolnica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.tim9.bolnica.security.AudienceValidator;
import com.tim9.bolnica.security.PermissionClaimConverter;

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
        
    	.antMatchers(HttpMethod.GET, "/api/alarms/by-page").hasAuthority("SCOPE_read:alarms")
    	.antMatchers(HttpMethod.GET, "/api/alarms/by-page-doctor").hasAuthority("SCOPE_read:alarmsDoctor")
        .antMatchers(HttpMethod.POST, "/api/csr").hasAuthority("SCOPE_write:csr")
        .antMatchers(HttpMethod.POST, "/api/logs/by-page").hasAuthority("SCOPE_read:logs")
        .antMatchers(HttpMethod.POST, "/api/messages/by-page").hasAuthority("SCOPE_read:messages")
        .antMatchers(HttpMethod.GET, "/api/patients").hasAuthority("SCOPE_read:patients")
        .antMatchers(HttpMethod.POST, "/api/reports").hasAuthority("SCOPE_read:reports")
        .antMatchers(HttpMethod.POST, "/api/rules/create-str").hasAuthority("SCOPE_write:rules")
        .antMatchers(HttpMethod.POST, "/api/rules/create-mtr").hasAuthority("SCOPE_write:rules")
        .antMatchers(HttpMethod.POST, "/api/rules/create-lowt").hasAuthority("SCOPE_write:rulesDoctor")
        .antMatchers(HttpMethod.POST, "/api/rules/create-hight").hasAuthority("SCOPE_write:rulesDoctor")
        .antMatchers(HttpMethod.POST, "/api/rules/create-orl").hasAuthority("SCOPE_write:rulesDoctor")
        .antMatchers(HttpMethod.POST, "/api/rules/create-prule").hasAuthority("SCOPE_write:rulesDoctor")
        
        .antMatchers("**").authenticated()
        .and().oauth2ResourceServer().jwt();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/messages");
    }
}