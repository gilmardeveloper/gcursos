package com.gilmarcarlos.developer.gcursos.model.eventos;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class AtividadePresencial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;
	private String sala;
	private Integer vagas;
	private String horaInicio;
	private String horaFim;
	private String nomeResponsavel;

	@OneToOne
	private DiaEvento diaEvento;
	
	@OneToMany(mappedBy = "atividadePresencial")
	private List<InscricaoPresencial> inscricoes;

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

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public Integer getVagas() {
		return vagas;
	}

	public void setVagas(Integer vagas) {
		this.vagas = vagas;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public DiaEvento getDiaEvento() {
		return diaEvento;
	}

	public void setDiaEvento(DiaEvento diaEvento) {
		this.diaEvento = diaEvento;
	}
	
	public List<InscricaoPresencial> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(List<InscricaoPresencial> inscricoes) {
		this.inscricoes = inscricoes;
	}
	
	@Transient
	public Boolean inscrito(Usuario usuario) {
		return inscricoes.stream().anyMatch( i -> i.getUsuario().equals(usuario));
	}
	
	@Transient
	public Long getInscricao(Usuario usuario) {
		return inscricoes.stream().filter( i -> i.getUsuario().equals(usuario)).findFirst().get().getId();
	} 
	
	@Transient
	public LocalTime getTimeInicio() {
		return LocalTime.parse(this.horaInicio, DateTimeFormatter.ofPattern("HH:mm"));
	}

	@Transient
	public LocalTime getTimeFim() {
		return LocalTime.parse(this.horaFim, DateTimeFormatter.ofPattern("HH:mm"));
	}

	@Transient
	public boolean podeSeInscrever(Usuario usuario) {
		
		if (temPermissoes(usuario) && usuarioNaoEhResponsavelDoEvento(usuario)) {
			return verificaSeAtividadeNaoTemMesmoHorario(usuario);
		} else {
			return false;
		}
	}
	
	@Transient
	public boolean usuarioNaoEhResponsavelDoEvento(Usuario usuario) {
		return !getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getResponsavel().equals(usuario);
	}

	@Transient
	private boolean verificaSeAtividadeNaoTemMesmoHorario(Usuario usuario) {
		
		for (InscricaoPresencial i : usuario.getInscricoes()) {

			AtividadePresencial temp = i.getAtividadePresencial();

			if (atividadesSaoDiferentes(temp)) { 
				
				if (horaInicialCoincide(temp) && mesmoDia(temp)) {
					return false;
				}

				if (horaFinalCoincide(temp) && mesmoDia(temp)) {
					return false;
				}

			} else {
				return false;
			}
		}

		return true;
	}
	
	@Transient
	private boolean mesmoDia(AtividadePresencial temp) {
		return temp.getDiaEvento().equals(this.getDiaEvento());
	}
	
	@Transient
	private boolean atividadesSaoDiferentes(AtividadePresencial temp) {
		return !temp.equals(this);
	}

	@Transient
	private boolean horaFinalCoincide(AtividadePresencial temp) {
		return this.getTimeFim().isAfter(temp.getTimeInicio().minusMinutes(1))
				&& this.getTimeFim().isBefore(temp.getTimeFim().plusMinutes(1));
	}

	@Transient
	private boolean horaInicialCoincide(AtividadePresencial temp) {
		return this.getTimeInicio().isAfter(temp.getTimeInicio().minusMinutes(1))
				&& this.getTimeInicio().isBefore(temp.getTimeFim().plusMinutes(1));
	}

	@Transient
	private Boolean temPermissoes(Usuario usuario) {
		return getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getPermissoes().valida(usuario);
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
		AtividadePresencial other = (AtividadePresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
