package org.inneo.services.security;

import lombok.RequiredArgsConstructor;

import org.inneo.services.repository.login.LoginRep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
	private final LoginRep loginRep;

	@Bean
	public UserDetailsService userDetailsService() {
	   return username ->loginRep.findByUsername(username)
	       .orElseThrow(() -> new UsernameNotFoundException("Username não encontrado"));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
	   DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	   authProvider.setUserDetailsService(userDetailsService());
	   authProvider.setPasswordEncoder(passwordEncoder());
	   return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	   return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}