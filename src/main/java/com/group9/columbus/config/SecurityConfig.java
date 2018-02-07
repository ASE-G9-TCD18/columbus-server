package com.group9.columbus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.group9.columbus.security.RestAuthenticationEntryPoint;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/login";
	public static final String FORM_BASED_USER_SIGN_UP = "/signup";
	public static final String SWAGGER_UI = "/swagger-ui.";
	
	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 http
	        .csrf().disable() 
	        .exceptionHandling()
	        .authenticationEntryPoint(this.authenticationEntryPoint)
	        
	        .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

	        .and()
	        	.authorizeRequests()
	        	.antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
	        	.antMatchers(FORM_BASED_USER_SIGN_UP).permitAll()
	        	.antMatchers(SWAGGER_UI).permitAll()
	        	.antMatchers("/*").authenticated()
	        
	        	.and().httpBasic();
		 
		 
		 http.formLogin()
		 .usernameParameter("loginId")
		 .passwordParameter("password")
		 ;

	}
	
	@Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    authProvider.setHideUserNotFoundExceptions(false);
	    return authProvider;
	}
	 
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(11);
	}

}
