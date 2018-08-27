package com.gilmarcarlos.developer.gcursos.model.eventos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class EstiloPresencial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String corFundoBox;
	private String corFundoTitulo;
	private String corFundoDescricao;
	
	@OneToOne
	private EventoPresencial eventoPresencial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorFundoBox() {
		return corFundoBox;
	}

	public void setCorFundoBox(String corFundoBox) {
		this.corFundoBox = corFundoBox;
	}

	public String getCorFundoTitulo() {
		return corFundoTitulo;
	}

	public void setCorFundoTitulo(String corFundoTitulo) {
		this.corFundoTitulo = corFundoTitulo;
	}

	public String getCorFundoDescricao() {
		return corFundoDescricao;
	}

	public void setCorFundoDescricao(String corFundoDescricao) {
		this.corFundoDescricao = corFundoDescricao;
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
		EstiloPresencial other = (EstiloPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
