package com.gilmarcarlos.developer.gcursos.model.eventos;

import java.io.Serializable;

import com.gilmarcarlos.developer.gcursos.interfaces.Eventos;

/**
 * Classe auxiliar para eventos
 *  
 * @author Gilmar Carlos
 *
 */
public class EventoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String titulo;
	
	public EventoDTO(Eventos evento) {
		this.id = evento.getId();
		this.titulo = evento.getTitulo();
	}
	
	public EventoDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
}
