package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Classe de entidade que representa que guarda o historico de modulos realizados pelo usuario
 * em um evento online
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class InscricaoOnlineModulo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private InscricaoOnline inscricaoOnline;
	
	@OneToOne
	private Modulo modulo;

	public InscricaoOnlineModulo(InscricaoOnline inscricao, Modulo modulo) {
		this.inscricaoOnline = inscricao;
		this.modulo = modulo;
	}
	
	public InscricaoOnlineModulo() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InscricaoOnline getInscricaoOnline() {
		return inscricaoOnline;
	}

	public void setInscricaoOnline(InscricaoOnline inscricaoOnline) {
		this.inscricaoOnline = inscricaoOnline;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InscricaoOnlineModulo other = (InscricaoOnlineModulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
