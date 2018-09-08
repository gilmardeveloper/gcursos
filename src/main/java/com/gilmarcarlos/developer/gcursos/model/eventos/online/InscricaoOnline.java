package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class InscricaoOnline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private EventoOnline eventoOnline;

	@OneToOne
	private Usuario usuario;

	@ElementCollection
	private List<Modulo> modulos;

	@ElementCollection
	private List<AtividadeOnline> atividades;
	
	private Boolean finalizado;

	public InscricaoOnline() {

	}

	public InscricaoOnline(Usuario usuario, EventoOnline evento) {
		this.usuario = usuario;
		this.eventoOnline = evento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventoOnline getEventoOnline() {
		return eventoOnline;
	}

	public void setEventoOnline(EventoOnline eventoOnline) {
		this.eventoOnline = eventoOnline;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Modulo> getModulos() {
		return (modulos.isEmpty() ? new ArrayList<>() : modulos);
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public List<AtividadeOnline> getAtividades() {
		return (atividades.isEmpty() ? new ArrayList<>() : atividades);
	}

	public void setAtividades(List<AtividadeOnline> atividades) {
		this.atividades = atividades;
	}
	
	public Boolean getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	@Transient
	public Boolean isFinalizado() {
		return (finalizado != null ? finalizado : false);
	}

	@Transient
	public Modulo ultimoModulo() {
		if (this.modulos.isEmpty()) {
			return this.eventoOnline.getModulos().get(0);
		} else {
			this.modulos.sort((m1, m2) -> Integer.compare(m1.getPosicao(), m2.getPosicao()));
			return this.modulos.get(this.modulos.size() - 1);
		}
	}

	@Transient
	public AtividadeOnline ultimaAtividade() {

		if (this.atividades.isEmpty()) {
			return this.eventoOnline.getModulos().get(0).getAtividades().get(0);
		} else {
			try {
				return ultimoModulo().getAtividades().stream().filter(a -> !realizou(a)).findFirst().get();
			}catch (NoSuchElementException e) {
				return ultimoModulo().getAtividades().get(ultimoModulo().getAtividades().size() - 1);
			}
		}
	}

	@Transient
	public Boolean realizou(Modulo modulo) {
		return getModulos().contains(modulo);
	}

	@Transient
	public Boolean realizou(AtividadeOnline atividade) {
		return getAtividades().contains(atividade);
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
		InscricaoOnline other = (InscricaoOnline) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
