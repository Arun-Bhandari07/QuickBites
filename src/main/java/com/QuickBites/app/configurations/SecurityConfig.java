package com.QuickBites.app.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private  JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	 public  SecurityConfig() {}
	 
	 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf->csrf.disable())
				.cors(cors->cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth->auth
						.requestMatchers(HttpMethod.OPTIONS).permitAll()
						.requestMatchers("/public/**","/uploads/**").permitAll()
						.requestMatchers("/api/v1/stripe/**").permitAll()
						.requestMatchers("/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html").permitAll()
						.requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthEntryPoint).accessDeniedHandler(customAccessDeniedHandler))
				.formLogin(form->form.disable())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
	@Bean
	public  CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("https://528fd0e96e71.ngrok-free.app","https://3b1c40af6695.ngrok-free.app/","http://localhost:3000","http://127.0.0.1:5500","http://127.0.0.1:5501","https://5aeb0071168a.ngrok-free.app","https://9f73af7fb1c0.ngrok-free.app"));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",config);
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
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
