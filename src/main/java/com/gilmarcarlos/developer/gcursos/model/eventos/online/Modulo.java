package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Classe de entidade que representa um módulo no evento online
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class Modulo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	
	@OneToMany(mappedBy = "modulo")
	private List<AtividadeOnline> atividades;
		
	private Integer posicao;
	
	@OneToOne
	private EventoOnline eventoOnline;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<AtividadeOnline> getAtividades() {
		atividades.sort((a1, a2) -> Integer.compare(a1.getPosicao(), a2.getPosicao()));
		return atividades;
	}

	public void setAtividades(List<AtividadeOnline> atividades) {
		this.atividades = atividades;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
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
		Modulo other = (Modulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
