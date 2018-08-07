package com.gilmarcarlos.developer.gcursos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.Usuario;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	public void enviar(Usuario usuario, String token) {
		
		String recipientAddress = usuario.getEmail();
        String subject = "Registration Confirmation";
        String url = "/login/confirmar-registro.html?token=" + token;
                
        SimpleMailMessage email = new SimpleMailMessage();
                
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Você se cadastrou no CNH+, para finalizar o registro, favor clicar no link ao lado " + "http://blog.cnhmais.com" + url);
        mailSender.send(email);
	}

}
