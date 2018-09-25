package com.gilmarcarlos.developer.gcursos.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;

/**
 * Classe com serviços para envio de emails
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	/**
	 * Método que envia uma confirmação de registro por email
	 * 
	 * @param usuario  
	 * @param token token de confirmação 
	 * 
	 */
	public void enviarConfirmacaoDeCadastro(Usuario usuario, String token) {
		
		String recipientAddress = usuario.getEmail();
        String subject = ConfUtils.EMAIL_EVIAR_TITULO_FINALIZAR_REGISTRO;
        String url = "/confirmar-registro/" + token;
                
        SimpleMailMessage email = new SimpleMailMessage();
                
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(ConfUtils.EMAIL_EVIAR_MSG_FINALIZAR_REGISTRO + ConfUtils.BASE_DOMINIO + url);
        mailSender.send(email);
	}
	
	/**
	 * Método que envia uma solicitação para redefinir a senha
	 * 
	 * @param usuario  
	 * @param token token de confirmação 
	 * 
	 */
	public void enviarSolicitacaoDeNovaSenha(Usuario usuario, String token) {
		
		String recipientAddress = usuario.getEmail();
        String subject = ConfUtils.EMAIL_EVIAR_TITULO_REDEFINIR_SENHA;
        String url = "/redefinir-senha/" + token;
                
        SimpleMailMessage email = new SimpleMailMessage();
                
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(ConfUtils.EMAIL_EVIAR_MSG_REDEFINIR_SENHA + ConfUtils.BASE_DOMINIO + url);
        mailSender.send(email);
	}
	
	/**
	 * Método que envia um aviso que um novo evento foi publicado com o perfil do usuário
	 * 
	 * @param emails[] array de emails 
	 * @param url uri do evento  
	 * 
	 */
	public void enviarNovoEvento(String emails[], String url) {
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(emails);
		email.setSubject(ConfUtils.EMAIL_EVIAR_TITULO_NOVO_EVENTO_PUBLICADO);
		email.setText(ConfUtils.EMAIL_EVIAR_MSG_NOVO_EVENTO_PUBLICADO + ConfUtils.BASE_DOMINIO + url);
		mailSender.send(email);
		
	}

}
