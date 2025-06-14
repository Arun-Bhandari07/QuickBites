package com.QuickBites.app.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.QuickBites.app.services.CustomUserDetailsService;
import com.QuickBites.app.utilities.JwtAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	 private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private  JwtAuthenticationFilter jwtAuthenticationFilter;

	 
	 
	 public  SecurityConfig() {}
	 
	 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf->csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth->auth
				.requestMatchers(HttpMethod.OPTIONS).permitAll()
				.requestMatchers("/public/**").permitAll()
				.requestMatchers("/api/v1/auth/**").permitAll()
				.anyRequest().authenticated())
				.formLogin(form->form.disable())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
	@Bean
	public  CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("https://8b17-111-119-35-71.ngrok-free.app","http://localhost:3000"));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization","Content-Type"));
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",config);
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception{
		return authConfig.getAuthenticationManager();
	}
	
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
}
