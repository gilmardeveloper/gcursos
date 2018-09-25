package com.gilmarcarlos.developer.gcursos.service.notificacoes;

import java.io.Serializable;

@SuppressWarnings("serial")

/**
 * Classe auxiliar da entidade (Mensagens) 
 * 
 * @author Gilmar Carlos
 *
 */
public class MensagensHelper implements Serializable {

	private String titulo;
	private String mensagem;
	private Long destinatario;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Long getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Long destinatario) {
		this.destinatario = destinatario;
	}
	
}
