package com.gilmarcarlos.developer.gcursos.model.eventos;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class EventoPresencial implements Serializable{

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
	private ImagensEventoPresencial imagemDestaque;
	
	@OneToOne(mappedBy = "eventoPresencial")
	private ImagensEventoPresencial imagemTopDetalhes;
	
	@OneToOne(mappedBy = "eventoPresencial")
	private ProgramacaoPresencial programacao;
	
	@OneToOne
	private Usuario responsavel;
	
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	
	private String horaAbertura;
	private String horaTermino;
	
	private LocalDate dataCriacao;
	private LocalDate dataAtualizacao;
	
	@OneToOne
	private CategoriaEvento categoria;

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

	public Boolean getCertificado() {
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

	public ImagensEventoPresencial getImagemDestaque() {
		return imagemDestaque;
	}

	public void setImagemDestaque(ImagensEventoPresencial imagemDestaque) {
		this.imagemDestaque = imagemDestaque;
	}

	public ImagensEventoPresencial getImagemTopDetalhes() {
		return imagemTopDetalhes;
	}

	public void setImagemTopDetalhes(ImagensEventoPresencial imagemTopDetalhes) {
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

	@Transient
	public String getDisplayCertificado() {
		return (getCertificado() ? "SIM" : "NÃO");
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
