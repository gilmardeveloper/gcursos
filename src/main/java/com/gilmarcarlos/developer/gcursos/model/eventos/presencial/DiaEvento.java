package com.gilmarcarlos.developer.gcursos.model.eventos.presencial;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Classe de entidade que representa um dia do evento presencial
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class DiaEvento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate data;
	
	@OneToMany(mappedBy = "diaEvento")
	private List<AtividadePresencial> atividades;
	
	@OneToOne
	private ProgramacaoPresencial programacaoPresencial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public List<AtividadePresencial> getAtividades() {
		atividades.sort((a1, a2) -> a1.getHoraInicio().compareTo(a2.getHoraInicio()));
		return atividades;
	}

	public void setAtividades(List<AtividadePresencial> atividades) {
		this.atividades = atividades;
	}

	public ProgramacaoPresencial getProgramacaoPresencial() {
		return programacaoPresencial;
	}

	public void setProgramacaoPresencial(ProgramacaoPresencial programacaoPresencial) {
		this.programacaoPresencial = programacaoPresencial;
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
		DiaEvento other = (DiaEvento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
