package com.gilmarcarlos.developer.gcursos.model.eventos.online;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;
import com.gilmarcarlos.developer.gcursos.model.type.EventoStatus;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class EventoOnline implements Serializable {

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

	@Transient
	public EventoStatus getStatus() {
		if (isAtivo()) {
			return EventoStatus.ATIVO;
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
	public Boolean isAtivo() {
		return ativo;
	}
	
	@Transient
	public Long assiduidade(Usuario usuario) {
		
		Double numerador = 0.0;
		Double denominador = 0.0;
		Double presenca = 0.0;
			
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
		EventoOnline other = (EventoOnline) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
