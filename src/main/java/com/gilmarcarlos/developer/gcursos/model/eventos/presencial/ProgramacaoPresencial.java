package com.gilmarcarlos.developer.gcursos.model.eventos.presencial;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ProgramacaoPresencial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy = "programacaoPresencial")
	private List<DiaEvento> dias;
	
	@OneToOne
	private EventoPresencial eventoPresencial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DiaEvento> getDias() {
		dias.sort((d1, d2) -> d1.getData().compareTo(d2.getData()));
		return dias;
	}

	public void setDias(List<DiaEvento> dias) {
		this.dias = dias;
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
		ProgramacaoPresencial other = (ProgramacaoPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
