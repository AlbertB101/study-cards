package edu.albert.studycards.authserver.config;

import edu.albert.studycards.authserver.security.filter.JwtAuthenticationFilter;
import edu.albert.studycards.authserver.security.filter.JwtAuthenticationValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private JwtAuthenticationValidationFilter jwtAuthenticationValidationFilter;
	
	@Override
	public void configure(HttpSecurity httpSecurity) {
		httpSecurity.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
		httpSecurity.addFilterBefore(jwtAuthenticationValidationFilter, JwtAuthenticationFilter.class);
	}
	
}
