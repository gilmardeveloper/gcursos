package com.gilmarcarlos.developer.gcursos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
		
	@Autowired
	private UsuarioService detailsService;
	
	@Autowired
	private PasswordCrypt passwordCrypt;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/painel","/painel/**").hasAnyRole("ADMIN")
		.antMatchers("/","/**").permitAll()
		.anyRequest().authenticated()
		.and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .defaultSuccessUrl("/painel/", true)
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(detailsService);
	    authProvider.setPasswordEncoder(passwordCrypt);
	    return authProvider;
	}
	
	
}
