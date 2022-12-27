package com.dranoj.api.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig{
	
	@Autowired
	private JWTAuthFilterConfig authFilter;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.headers().addHeaderWriter(
				new StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
		
		.and()
			.cors().and()
			.csrf().disable()
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
				
		.and()
		.exceptionHandling()
		.authenticationEntryPoint( 
				(request, response,ex) -> { 
					response.sendError( HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()); 
					}
				)			 
		
		.and()
		.authorizeRequests()
			.antMatchers("/api/authenticate").permitAll()
			.antMatchers("/api/**").authenticated()
			
		.and()
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean 
	public AuthenticationProvider authenticationProvider() {	  
		 DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		 authProvider.setUserDetailsService(this.userDetailsService);
		 authProvider.setPasswordEncoder(passwordEncoder());
		 return authProvider;
	 }
	
	@Bean
	public AuthenticationManager athenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	 
}
