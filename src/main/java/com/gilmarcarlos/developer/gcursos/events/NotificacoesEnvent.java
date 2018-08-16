package com.gilmarcarlos.developer.gcursos.events;

import org.springframework.context.ApplicationEvent;

public class NotificacoesEnvent extends ApplicationEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Mensagens mensagens;
	
	public NotificacoesEnvent(Mensagens mensagens) {
		super(mensagens);
		this.mensagens = mensagens;
	}
	
	public String getMensagens() {
		return this.mensagens.getMensagem();
	}
}
