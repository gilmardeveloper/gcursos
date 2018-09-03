package com.gilmarcarlos.developer.gcursos.model.eventos;

import java.beans.Transient;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class InscricaoPresencial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private EventoPresencial eventoPresencial;

	@OneToOne
	private AtividadePresencial atividadePresencial;
	
	private Boolean presenca;	
	
	@OneToOne
	private Usuario usuario;
	
	public InscricaoPresencial() {
		
	}

	public InscricaoPresencial(Usuario usuarioLogado, AtividadePresencial atividade) {
		this.usuario = usuarioLogado;
		this.atividadePresencial = atividade;
		this.eventoPresencial = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventoPresencial getEventoPresencial() {
		return eventoPresencial;
	}

	public void setEventoPresencial(EventoPresencial eventoPresencial) {
		this.eventoPresencial = eventoPresencial;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public AtividadePresencial getAtividadePresencial() {
		return atividadePresencial;
	}

	public void setAtividadePresencial(AtividadePresencial atividadePresencial) {
		this.atividadePresencial = atividadePresencial;
	}
	
	public Boolean getPresenca() {
		return presenca;
	}

	public void setPresenca(Boolean presenca) {
		this.presenca = presenca;
	}
	
	@Transient
	public Boolean inscrito(Usuario usuario) {
		return getUsuario().equals(usuario);
	}
	
	@Transient
	public Boolean isPresente() {
		return (this.presenca != null ? this.presenca : false);
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
		InscricaoPresencial other = (InscricaoPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
