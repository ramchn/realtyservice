package com.realtymgmt.oauth2.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ServerConfig extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	DataSource datasource;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth
		   .jdbcAuthentication()
		   .dataSource(datasource)
		   .usersByUsernameQuery("select EmailAddress,UserPassword,Enabled "
			        + "from User "
			        + "where EmailAddress = ?")
		   .authoritiesByUsernameQuery("select User_EmailAddress,Authority "
			        + "from Access "
			        + "where User_EmailAddress = ?");
	}
}
