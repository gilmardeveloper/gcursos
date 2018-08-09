package com.gilmarcarlos.developer.gcursos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.interfaces.Proprietario;
import com.gilmarcarlos.developer.gcursos.model.type.TipoTelefone;

@Entity
public class Telefone implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String numero;
	
	@Enumerated(EnumType.STRING)
	private TipoTelefone tipo;
	
	@OneToOne
	private Proprietario proprietario;

}
