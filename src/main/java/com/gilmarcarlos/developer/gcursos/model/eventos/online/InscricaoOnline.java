package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe de entidade que representa uma inscrição em um evento
 * presencial
 *  
 * @author Gilmar Carlos
 *
 */
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

	@OneToMany(mappedBy = "inscricaoOnline")	
	private List<InscricaoOnlineModulo> modulos;

	@OneToMany(mappedBy = "inscricaoOnline")		
	private List<InscricaoOnlineAtividade> atividades;
	
	private Boolean finalizado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataConclusao;

	public InscricaoOnline() {

	}

	public InscricaoOnline(Usuario usuario, EventoOnline evento) {
		this.usuario = usuario;
		this.eventoOnline = evento;
		this.dataInicio = LocalDate.now();
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

	public List<InscricaoOnlineModulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<InscricaoOnlineModulo> modulos) {
		this.modulos = modulos;
	}

	public List<InscricaoOnlineAtividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<InscricaoOnlineAtividade> atividades) {
		this.atividades = atividades;
	}

	public Boolean getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(LocalDate dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
	
	/**
	 * Método que valida se um evento em andamento foi finalizado
	 * 
	 * @return Boolean
	 * 
	 */
	@Transient
	public Boolean isFinalizado() {
		return (finalizado != null ? finalizado : false);
	}
	
	/**
	 * Método que retorna o ultimo módulo concluído pelo usuario
	 * 
	 * @return Modulo
	 * 
	 */
	@Transient
	public Modulo ultimoModulo() {
		if (this.modulos.isEmpty()) {
			return this.eventoOnline.getModulos().get(0);
		} else {
			this.modulos.sort((m1, m2) -> Integer.compare(m1.getModulo().getPosicao(), m2.getModulo().getPosicao()));
			return this.modulos.get(this.modulos.size() - 1).getModulo();
		}
	}
	
	/**
	 * Método que retorna a última atividade concluída pelo usuario
	 * 
	 * @return Modulo
	 * 
	 */
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
	
	/**
	 * Método que verifica se um modulo foi realizado
	 * 
	 * @param modulo
	 * @return Boolean
	 * 
	 */
	@Transient
	public Boolean realizou(Modulo modulo) {
		return getModulos().stream().anyMatch(m -> m.getModulo().equals(modulo));
	}
	
	/**
	 * Método que verifica se uma atividade foi realizada
	 * 
	 * @param atividade
	 * @return Boolean
	 * 
	 */
	@Transient
	public Boolean realizou(AtividadeOnline atividade) {
		return getAtividades().stream().anyMatch(a -> a.getAtividadeOnline().equals(atividade));
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
