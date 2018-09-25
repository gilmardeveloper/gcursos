package com.gilmarcarlos.developer.gcursos.utils;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

/**
 * Classe para o envio de notificações de usuários da plataforma
 * 
 * @author Gilmar Carlos
 *
 */
public class NotificacaoUtils {
	
	/**
	 * Método que envia notificações de sucesso 
	 * 
	 * @param notificacao serviço de persistências  
	 * @param usuario entidade que representa o usuário 
	 * @param titulo titulo da mensagem
	 * @param mensagem mensagem de notificação
	 * 
	 */
	public static void sucesso(NotificacaoService notificacao, Usuario usuario, String titulo,
			String mensagem) {
		notificacao
				.salvar(new Notificacao(usuario, titulo, IconeTypeUtils.INFORMACAO, StatusTypeUtils.SUCESSO, mensagem));
	}
	
	/**
	 * Método que envia notificações de erro
	 * 
	 * @param notificacao serviço de persistências  
	 * @param usuario entidade que representa o usuário 
	 * @param titulo titulo da mensagem
	 * @param mensagem mensagem de notificação
	 * 
	 */
	public static void error(NotificacaoService notificacao, Usuario usuario, String titulo,
			String mensagem) {
		notificacao
				.salvar(new Notificacao(usuario, titulo, IconeTypeUtils.INFORMACAO, StatusTypeUtils.ERRO, mensagem));
	}
}
