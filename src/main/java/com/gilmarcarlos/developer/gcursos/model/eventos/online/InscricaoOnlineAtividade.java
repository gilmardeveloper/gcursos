package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class InscricaoOnlineAtividade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private AtividadeOnline atividadeOnline;
	
	@OneToOne
	private InscricaoOnline inscricaoOnline;

	public InscricaoOnlineAtividade(InscricaoOnline inscricao, AtividadeOnline atividade) {
		this.inscricaoOnline = inscricao;
		this.atividadeOnline = atividade;
	}
	
	public InscricaoOnlineAtividade() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AtividadeOnline getAtividadeOnline() {
		return atividadeOnline;
	}

	public void setAtividadeOnline(AtividadeOnline atividadeOnline) {
		this.atividadeOnline = atividadeOnline;
	}

	public InscricaoOnline getInscricaoOnline() {
		return inscricaoOnline;
	}

	public void setInscricaoOnline(InscricaoOnline inscricaoOnline) {
		this.inscricaoOnline = inscricaoOnline;
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
		InscricaoOnlineAtividade other = (InscricaoOnlineAtividade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
