package com.gilmarcarlos.developer.gcursos.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gilmarcarlos.developer.gcursos.security.crypt.PasswordCrypt;

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
		.antMatchers("/dashboard","/dashboard/**").hasAnyRole("Administrador","Usuario")
		.antMatchers("/","/**").permitAll()
		.anyRequest().authenticated()
		.and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .defaultSuccessUrl("/dashboard/", true)
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .and()
        .headers().defaultsDisabled().cacheControl();
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
