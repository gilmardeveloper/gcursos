package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.io.Serializable;

/**
 * Classe auxiliar para ordenação de modulos e atividades
 *  
 * @author Gilmar Carlos
 *
 */
public class OrdenarHelper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
