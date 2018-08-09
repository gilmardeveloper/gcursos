package com.gilmarcarlos.developer.gcursos.model;

import java.io.Serializable;

public class Endereco implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String endereco;
	private String bairro;
	private String cidade;
	private String uf;
	private String numero;
	private String cep;

}
