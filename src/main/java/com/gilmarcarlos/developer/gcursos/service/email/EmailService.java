package com.gilmarcarlos.developer.gcursos.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	private static final String URL_PRODUCE = "http://35.188.89.234";
	private static final String URL_LOCAL = "http://localhost:8080";
	
	public void enviarConfirmacaoDeCadastro(Usuario usuario, String token) {
		
		String recipientAddress = usuario.getEmail();
        String subject = "Confirmação de registro";
        String url = "/confirmar-registro/" + token;
                
        SimpleMailMessage email = new SimpleMailMessage();
                
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Você se cadastrou na plataforma de ensino, para finalizar o registro, favor clicar no link ao lado " + URL_PRODUCE + url);
        mailSender.send(email);
	}
	
	public void enviarSolicitacaoDeNovaSenha(Usuario usuario, String token) {
		String recipientAddress = usuario.getEmail();
        String subject = "Redefinir senha";
        String url = "/redefinir-senha/" + token;
                
        SimpleMailMessage email = new SimpleMailMessage();
                
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Uma solicitação para redefinir a senha foi realizada, caso tenha sido você, favor clicar no link " + URL_PRODUCE + url);
        mailSender.send(email);
	}

}
