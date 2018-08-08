package com.gilmarcarlos.developer.gcursos.security.brute.force;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AutenticacaoErroCredencial implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>{

	@Autowired
	private AutenticacaoBlockForcaBruta blockForcaBruta;
	
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent evento) {
		
		WebAuthenticationDetails auth = (WebAuthenticationDetails) evento.getAuthentication().getDetails();
   		blockForcaBruta.loginFailed(auth.getRemoteAddress());
	}

}
