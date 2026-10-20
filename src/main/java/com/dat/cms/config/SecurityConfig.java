package com.dat.cms.config;

import com.dat.cms.security.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(DatabaseUserDetailsService service, PasswordEncoder encoder) {

DaoAuthenticationProvider p =
        new DaoAuthenticationProvider(service);

		p.setPasswordEncoder(encoder);
		return p;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception {
		http.authenticationProvider(provider)
				.authorizeHttpRequests(
						a -> a.requestMatchers("/login", "/error").permitAll().anyRequest().authenticated())
				.formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
				.logout(l -> l.logoutSuccessUrl("/login?logout").permitAll());
		return http.build();
	}
}