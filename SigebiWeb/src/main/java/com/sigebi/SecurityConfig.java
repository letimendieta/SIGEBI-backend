package com.sigebi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sigebi.service.UsuariosService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuariosService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
	}
	
	@Override
	protected void configure(HttpSecurity http)	throws Exception{
		/*http
			.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();*/
		http.csrf().disable().authorizeRequests()
		.antMatchers("/login").permitAll()
		//.anyRequest().authenticated()
		.and()
		.addFilterBefore(new LoginFilter("/login", authenticationManager()),
			UsernamePasswordAuthenticationFilter.class)
		
		.addFilterBefore(new JwtFilter(),
				UsernamePasswordAuthenticationFilter.class);
	}
	
}
