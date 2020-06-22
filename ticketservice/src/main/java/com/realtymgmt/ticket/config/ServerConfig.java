package com.realtymgmt.ticket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
        http
	        .anonymous().disable()
	        .authorizeRequests()
	        .antMatchers("/home").hasAnyRole("TENANT", "OWNER", "CONTACT")
	 	    .antMatchers("/tenant/**").hasAnyRole("TENANT")
	 	    .antMatchers("/contact/**").hasAnyRole("CONTACT")
	 	    .antMatchers("/owner/**").hasAnyRole("OWNER")
	 	    .antMatchers("/task/**").hasAnyRole("OWNER", "CONTACT");
	}

}