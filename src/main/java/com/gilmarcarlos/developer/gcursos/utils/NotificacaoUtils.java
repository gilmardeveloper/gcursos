package com.gilmarcarlos.developer.gcursos.utils;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

public class NotificacaoUtils {

	public static void sucesso(NotificacaoService notificacao, Usuario usuario, String titulo,
			String mensagem) {
		notificacao
				.salvar(new Notificacao(usuario, titulo, IconeTypeUtils.INFORMACAO, StatusTypeUtils.SUCESSO, mensagem));
	}

}
