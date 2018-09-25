package com.gilmarcarlos.developer.gcursos.model.images;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;

/**
 * Classe de entidade que representa um cabeçalho para uma lista de presença, contendo logo
 * e titulo
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class ImagensLogoListaPresenca implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "image.notnull")
	private Blob imagemDireita;
	
	@NotNull(message = "image.notnull")
	private Blob imagemEsquerda;
	
	@NotNull
	private String titulo;
		
	@OneToOne
	private EventoPresencial eventoPresencial;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Blob getImagemDireita() {
		return imagemDireita;
	}

	public void setImagemDireita(Blob imagemDireita) {
		this.imagemDireita = imagemDireita;
	}

	public Blob getImagemEsquerda() {
		return imagemEsquerda;
	}

	public void setImagemEsquerda(Blob imagemEsquerda) {
		this.imagemEsquerda = imagemEsquerda;
	}
	
	public EventoPresencial getEventoPresencial() {
		return eventoPresencial;
	}

	public void setEventoPresencial(EventoPresencial eventoPresencial) {
		this.eventoPresencial = eventoPresencial;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
		ImagensLogoListaPresenca other = (ImagensLogoListaPresenca) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
