package com.gilmarcarlos.developer.gcursos.model.eventos.presencial;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class CertificadoPresencial implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "image.notnull")
	private Blob imagemFundo;
	
	@Lob
	private String conteudo; 
		
	@OneToOne
	private EventoPresencial eventoPresencial;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Blob getImagemFundo() {
		return imagemFundo;
	}

	public void setImagemFundo(Blob imagemFundo) {
		this.imagemFundo = imagemFundo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public EventoPresencial getEventoPresencial() {
		return eventoPresencial;
	}

	public void setEventoPresencial(EventoPresencial eventoPresencial) {
		this.eventoPresencial = eventoPresencial;
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
		CertificadoPresencial other = (CertificadoPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
