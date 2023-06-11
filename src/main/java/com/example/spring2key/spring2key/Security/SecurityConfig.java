package com.example.spring2key.spring2key.Security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter { // Extends Keycloak's web security configuration adapter

    @Autowired
    //method configures the authentication manager to use Keycloak authentication provider and sets the granted authorities mapper
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configures the global authentication manager builder with Keycloak's authentication provider
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper()); // Sets the granted authorities mapper
        auth.authenticationProvider(keycloakAuthenticationProvider); // Sets the authentication provider
    }

    @Bean
    //method creates a KeycloakSpringBootConfigResolver bean to resolve the Keycloak configuration from the Spring Boot properties file
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Override
    //method configures the HTTP security. It specifies that the /secured/** URL pattern requires authentication and all other requests are permitted
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http); // Configures HTTP security with Keycloak's default settings
        http
                .cors()
                .and()
                .authorizeRequests() // Configures authorization for HTTP requests
                .antMatchers("/secured/**").authenticated() // Requires authentication for URLs that start with "/secured/"
                .antMatchers("/task/**").authenticated()
                .antMatchers("/").permitAll()
                .anyRequest().permitAll(); // Allows all other requests to be made without authentication


    }

    @Bean
    @Override
    //method creates a RegisterSessionAuthenticationStrategy to register authenticated sessions in the session registry.
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }


}

