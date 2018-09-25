package com.gilmarcarlos.developer.gcursos.model.images;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;

/**
 * Classe de entidade que representa uma imagem para eventos online
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class ImagensEventoOnlineTop implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "image.notnull")
	private Blob imagem;
		
	@OneToOne
	private EventoOnline eventoOnline;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Blob getImagem() {
		return imagem;
	}

	public void setImagem(Blob imagem) {
		this.imagem = imagem;
	}

	public EventoOnline getEventoOnline() {
		return eventoOnline;
	}

	public void setEventoOnline(EventoOnline eventoOnline) {
		this.eventoOnline = eventoOnline;
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
		ImagensEventoOnlineTop other = (ImagensEventoOnlineTop) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
