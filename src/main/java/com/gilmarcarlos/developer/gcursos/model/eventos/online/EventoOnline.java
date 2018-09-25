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

import com.gilmarcarlos.developer.gcursos.interfaces.Eventos;
import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;
import com.gilmarcarlos.developer.gcursos.model.type.EventoStatus;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe de entidade que representa um evento online
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class EventoOnline implements Serializable, Eventos {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;
	private String descricao;
	private Boolean certificado;
	private String cargaHoraria;
	private String tipoEvento;

	@OneToOne(mappedBy = "eventoOnline")
	private EstiloOnline estilo;

	@OneToOne(mappedBy = "eventoOnline")
	private PermissoesEventoOnline permissoes;

	@OneToOne(mappedBy = "eventoOnline")
	private ImagensEventoOnlineDestaque imagemDestaque;

	@OneToOne(mappedBy = "eventoOnline")
	private ImagensEventoOnlineTop imagemTopDetalhes;

	@OneToMany(mappedBy = "eventoOnline")
	private List<Modulo> modulos;

	@OneToOne
	private Usuario responsavel;

	@OneToMany(mappedBy = "eventoOnline")
	private List<EventoOnlineLog> logs;

	private LocalDate dataCriacao;
	private LocalDate dataAtualizacao;

	@OneToOne
	private CategoriaEvento categoria;

	@OneToOne(mappedBy = "eventoOnline")
	private SobreOnline sobre;

	private Boolean publicado;

	private Boolean ativo;

	@OneToMany(mappedBy = "eventoOnline")
	private List<InscricaoOnline> inscricoes;

	@OneToOne(mappedBy = "eventoOnline")
	private CertificadoOnline certificadoOnline;

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

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public EstiloOnline getEstilo() {
		return estilo;
	}

	public void setEstilo(EstiloOnline estilo) {
		this.estilo = estilo;
	}

	public PermissoesEventoOnline getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(PermissoesEventoOnline permissoes) {
		this.permissoes = permissoes;
	}

	public ImagensEventoOnlineDestaque getImagemDestaque() {
		return imagemDestaque;
	}

	public void setImagemDestaque(ImagensEventoOnlineDestaque imagemDestaque) {
		this.imagemDestaque = imagemDestaque;
	}

	public ImagensEventoOnlineTop getImagemTopDetalhes() {
		return imagemTopDetalhes;
	}

	public void setImagemTopDetalhes(ImagensEventoOnlineTop imagemTopDetalhes) {
		this.imagemTopDetalhes = imagemTopDetalhes;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public List<EventoOnlineLog> getLogs() {
		return logs;
	}

	public void setLogs(List<EventoOnlineLog> logs) {
		this.logs = logs;
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

	public Boolean isPublicado() {
		return publicado;
	}

	public Boolean isCertificado() {
		return certificado;
	}

	public void setCertificado(Boolean certificado) {
		this.certificado = certificado;
	}

	public List<Modulo> getModulos() {
		modulos.sort((m1, m2) -> Integer.compare(m1.getPosicao(), m2.getPosicao()));
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public SobreOnline getSobre() {
		return sobre;
	}

	public void setSobre(SobreOnline sobre) {
		this.sobre = sobre;
	}

	public List<InscricaoOnline> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(List<InscricaoOnline> inscricoes) {
		this.inscricoes = inscricoes;
	}

	public CertificadoOnline getCertificadoOnline() {
		return certificadoOnline;
	}

	public void setCertificadoOnline(CertificadoOnline certificadoOnline) {
		this.certificadoOnline = certificadoOnline;
	}

	@Transient
	public Boolean isUltimo(AtividadeOnline atividade) {
		Modulo ultimoModulo = getModulos().get(getModulos().size() - 1);
		AtividadeOnline ultimaAtividade = ultimoModulo.getAtividades().get(ultimoModulo.getAtividades().size() - 1);
		return ultimaAtividade.equals(atividade);
	}

	@Transient
	public Boolean isInscrito(Usuario usuario) {
		return getInscricoes().stream().anyMatch((i -> i.getUsuario().equals(usuario)));
	}

	@Transient
	public InscricaoOnline getInscricao(Usuario usuario) {
		try {
			return getInscricoes().stream().filter((i -> i.getUsuario().equals(usuario))).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Transient
	public EventoStatus getStatus() {
		if (isAtivo()) {
			return EventoStatus.ATIVO;
		} else {
			return EventoStatus.CANCELADO;
		}
	}
	
	/**
	 * Método que ativa o evento
	 * 
	 * 
	 */
	@Transient
	public void ativarEvento() {
		this.ativo = true;
	}
	
	/**
	 * Método que cancela o evento
	 * 
	 * 
	 */
	@Transient
	public void cancelarEvento() {
		this.publicado = false;
		this.ativo = false;
	}
	
	/**
	 * Método que ativa uma publicação 
	 * 
	 * @throws EventoCanceladoException se o evento estiver cancelado
	 * 
	 */
	@Transient
	public void ativarPublicacao() throws EventoCanceladoException {
		if (!isAtivo()) {
			throw new EventoCanceladoException();
		}
		this.publicado = true;
	}
	
	/**
	 * Método que desativa uma publicação do evento
	 * 
	 * 
	 */
	@Transient
	public void desativarPublicacao() {
		this.publicado = false;
	}
	
	/**
	 * Método que retorna se o evento possui certificado
	 * 
	 * @return String
	 */
	@Transient
	public String getDisplayCertificado() {
		return (isCertificado() ? "SIM" : "NÃO");
	}
	
	/**
	 * Método que retorna se o evento foi publicado
	 * 
	 * @return String
	 */
	@Transient
	public String getDisplayPublicado() {
		return (isPublicado() ? "SIM" : "NÃO");
	}
	
	/**
	 * Método que retorna se o evento se encontra ativo
	 * 
	 * @return Boolean
	 */
	@Transient
	public Boolean isAtivo() {
		return ativo;
	}
	
	/**
	 * Método que retorna o progresso de um usuario inscrito no evento
	 * 
	 * @param usuario
	 * @return Long
	 */
	@Transient
	public Long progresso(Usuario usuario) {

		Double numerador = 0.0;
		Double denominador = 0.0;
		Double presenca = 0.0;

		InscricaoOnline inscricao = getInscricao(usuario);

		for (Modulo modulo : getModulos()) {

			for (AtividadeOnline atividade : modulo.getAtividades()) {
				if (inscricao.realizou(atividade)) {
					numerador += 100.00;
				}
				denominador++;
			}

		}
		
		if(denominador == 0.0) return 0l;
		
		presenca = numerador / denominador;

		return Math.round(presenca);
	}
	
	/**
	 * Método que retorna o progresso total de um evento
	 * 
	 * @return Long
	 */
	@Transient
	public Long progressoTotal() {
		if(qtdInscricoes() == 0) return 0l;
		return (qtdFinalizado() * 100) / qtdInscricoes();
	}
	
	/**
	 * Método que retorna a quantidade de módulos
	 * 
	 * @return Integer
	 */
	@Transient
	public Integer qtdModulos() {
		return getModulos().size();
	}
	
	/**
	 * Método que retorna a quantidade de atividades
	 * 
	 * @return Integer
	 */
	@Transient
	public Integer qtdAtividades() {
		return getModulos().stream().mapToInt(m -> m.getAtividades().size()).sum();
	}
	
	/**
	 * Método que retorna a quantidade de inscrições
	 * 
	 * @return Integer
	 */
	@Transient
	public Integer qtdInscricoes() {
		return getInscricoes().size();
	}
	
	/**
	 * Método que retorna a quantidade de inscrições em andamento
	 * 
	 * @return Long
	 */
	@Transient
	public Long qtdAndamento() {
		return getInscricoes().stream().filter(i -> !i.isFinalizado()).count();
	}
	
	/**
	 * Método que retorna a quantidade inscrições finalizadas
	 * 
	 * @return Long
	 */
	@Transient
	public Long qtdFinalizado() {
		return getInscricoes().stream().filter(i -> i.isFinalizado()).count();
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
		EventoOnline other = (EventoOnline) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
