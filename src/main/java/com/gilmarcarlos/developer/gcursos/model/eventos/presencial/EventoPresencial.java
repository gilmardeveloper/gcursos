package com.gilmarcarlos.developer.gcursos.model.eventos.presencial;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialTop;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.type.EventoStatus;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class EventoPresencial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String descricao;
	private String localEvento;
	private String enderecoLocalEvento;
	private Boolean certificado;
	private String cargaHoraria;
	private Long vagas;
	private String tipoEvento;

	@OneToOne(mappedBy = "eventoPresencial")
	private Sobre sobre;
	
	@OneToOne(mappedBy = "eventoPresencial")
	private EstiloPresencial estilo;
	
	@OneToOne(mappedBy = "eventoPresencial")
	private PermissoesEventoPresencial permissoes;

	@OneToOne(mappedBy = "eventoPresencial")
	private ImagensEventoPresencialDestaque imagemDestaque;

	@OneToOne(mappedBy = "eventoPresencial")
	private ImagensEventoPresencialTop imagemTopDetalhes;
	
	@OneToOne(mappedBy = "eventoPresencial")
	private ImagensLogoListaPresenca imagemLogo;

	@OneToOne(mappedBy = "eventoPresencial")
	private ProgramacaoPresencial programacao;

	@OneToOne
	private Usuario responsavel;

	@OneToMany(mappedBy = "eventoPresencial")
	private List<EventoPresencialLog> logs;
	
	@OneToMany(mappedBy = "eventoPresencial")
	private List<InscricaoPresencial> inscricoes;

	private LocalDate dataInicio;
	private LocalDate dataTermino;

	private String horaAbertura;
	private String horaTermino;

	private LocalDate dataCriacao;
	private LocalDate dataAtualizacao;

	@OneToOne
	private CategoriaEvento categoria;

	private Boolean publicado;

	private Boolean ativo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo.toUpperCase();
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Sobre getSobre() {
		return sobre;
	}

	public void setSobre(Sobre sobre) {
		this.sobre = sobre;
	}

	public String getLocalEvento() {
		return localEvento;
	}

	public void setLocalEvento(String localEvento) {
		this.localEvento = localEvento;
	}

	public String getEnderecoLocalEvento() {
		return enderecoLocalEvento;
	}

	public void setEnderecoLocalEvento(String enderecoLocalEvento) {
		this.enderecoLocalEvento = enderecoLocalEvento;
	}

	public Boolean isCertificado() {
		return certificado;
	}

	public void setCertificado(Boolean certificado) {
		this.certificado = certificado;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Long getVagas() {
		return vagas;
	}

	public void setVagas(Long vagas) {
		this.vagas = vagas;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public ImagensEventoPresencialDestaque getImagemDestaque() {
		return imagemDestaque;
	}

	public void setImagemDestaque(ImagensEventoPresencialDestaque imagemDestaque) {
		this.imagemDestaque = imagemDestaque;
	}

	public ImagensEventoPresencialTop getImagemTopDetalhes() {
		return imagemTopDetalhes;
	}

	public void setImagemTopDetalhes(ImagensEventoPresencialTop imagemTopDetalhes) {
		this.imagemTopDetalhes = imagemTopDetalhes;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(LocalDate dataTermino) {
		this.dataTermino = dataTermino;
	}

	public String getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(String horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public String getHoraTermino() {
		return horaTermino;
	}

	public void setHoraTermino(String horaTermino) {
		this.horaTermino = horaTermino;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDate dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public CategoriaEvento getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEvento categoria) {
		this.categoria = categoria;
	}

	public ProgramacaoPresencial getProgramacao() {
		return programacao;
	}

	public void setProgramacao(ProgramacaoPresencial programacao) {
		this.programacao = programacao;
	}

	public List<EventoPresencialLog> getLogs() {
		return logs;
	}

	public void setLogs(List<EventoPresencialLog> logs) {
		this.logs = logs;
	}

	public Boolean isPublicado() {
		return publicado;
	}

	public PermissoesEventoPresencial getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(PermissoesEventoPresencial permissoes) {
		this.permissoes = permissoes;
	}
	
	public EstiloPresencial getEstilo() {
		return estilo;
	}

	public void setEstilo(EstiloPresencial estilo) {
		this.estilo = estilo;
	}
	
	public List<InscricaoPresencial> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(List<InscricaoPresencial> inscricoes) {
		this.inscricoes = inscricoes;
	}
	
	public ImagensLogoListaPresenca getImagemLogo() {
		return imagemLogo;
	}

	public void setImagemLogo(ImagensLogoListaPresenca imagemLogo) {
		this.imagemLogo = imagemLogo;
	}

	@Transient
	public LocalTime getTimeAbertura() {
		return LocalTime.parse(this.horaAbertura, DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	@Transient
	public LocalTime getTimeTermino() {
		return LocalTime.parse(this.horaTermino, DateTimeFormatter.ofPattern("HH:mm"));
	}

	@Transient
	public EventoStatus getStatus() {
		if (isAtivo()) {
			return (isFechado() ? EventoStatus.FECHADO : EventoStatus.ABERTO);
		} else {
			return EventoStatus.CANCELADO;
		}
	}

	@Transient
	public void ativarEvento() {
		this.ativo = true;
	}

	@Transient
	public void cancelarEvento() {
		this.publicado = false;
		this.ativo = false;
	}

	@Transient
	public void ativarPublicacao() throws EventoCanceladoException {
		if(!isAtivo()) {
			throw new EventoCanceladoException();
		}
		this.publicado = true;
	}

	@Transient
	public void desativarPublicacao() {
		this.publicado = false;
	}

	@Transient
	public String getDisplayCertificado() {
		return (isCertificado() ? "SIM" : "NÃO");
	}

	@Transient
	public String getDisplayPublicado() {
		return (isPublicado() ? "SIM" : "NÃO");
	}

	@Transient
	public Boolean isFechado() {
		return getDataTermino().isBefore(LocalDate.now()); 
	}

	@Transient
	public Boolean isAtivo() {
		return ativo;
	}
	
	@Transient
	public Long assiduidade(Usuario usuario) {
		
		Double numerador = 0.0;
		Double denominador = 0.0;
		Double presenca = 0.0;
		
			for(InscricaoPresencial i : getInscricoes()) {
				if(i.getUsuario().equals(usuario)) {
					numerador += (i.isPresente() ? 100.0 : 0.0);
					denominador++;
				}
			}
			
		presenca = numerador / denominador;
		
		
		return Math.round(presenca);
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
		EventoPresencial other = (EventoPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
