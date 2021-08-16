package com.sigebi.security;

import com.sigebi.security.jwt.JwtEntryPoint;
import com.sigebi.security.jwt.JwtTokenFilter;
import com.sigebi.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;
    
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs", 
            "/swagger-resources/**", 
            "/configuration/ui",
            "/configuration/security", 
            "/swagger-ui.html",
            "/webjars/**",
            "/csrf"
    };

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
	        .authorizeRequests()
	        .antMatchers("/auth/login").permitAll().and()
	        .authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().and()	        
	        //.authorizeRequests().antMatchers("/archivos/filesName/**").permitAll().and()
	        .authorizeRequests().antMatchers("/auth/archivos/**").permitAll().and()
				//.authorizeRequests().antMatchers(HttpMethod.GET, "/auth/archivos/**")
				//.hasAuthority("ROL_ADMIN")//, "ROL_CONSULTORIO", "ROL_ABM_PACIENTE", "ROL_AYUDA")
				//.and()
	        .authorizeRequests().antMatchers("/swagger").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
	        .and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
