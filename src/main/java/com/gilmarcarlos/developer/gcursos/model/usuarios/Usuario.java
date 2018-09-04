package com.gilmarcarlos.developer.gcursos.model.usuarios;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;

@Entity
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String email;
	private String senha;
	private boolean habilitado;
	private boolean tokenExpired;

	@ManyToMany
	@JoinTable(name = "usuarios_autorizacoes", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "autorizacao_id", referencedColumnName = "id"))
	private List<Autorizacao> autorizacoes;

	@OneToOne(mappedBy = "usuario")
	private DadosPessoais dadosPessoais;

	@OneToOne(mappedBy = "usuario")
	private CodigoFuncional codigoFuncional;

	@OneToMany(mappedBy = "usuario")
	private List<Notificacao> notificacoes;

	@OneToOne(mappedBy = "usuario")
	private Imagens imagens;

	@OneToMany(mappedBy = "responsavel")
	private List<EventoPresencial> eventoPresencial;

	@OneToMany(mappedBy = "responsavel")
	private List<EventoOnline> eventoOnline;
	
	@OneToMany(mappedBy = "usuario")
	private List<InscricaoPresencial> inscricoes;

	public Usuario() {
		this.habilitado = false;
	}

	public CodigoFuncional getCodigoFuncional() {
		return codigoFuncional;
	}

	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
	}

	public void setCodigoFuncional(CodigoFuncional codigoFuncional) {
		this.codigoFuncional = codigoFuncional;
	}

	public DadosPessoais getDadosPessoais() {
		return dadosPessoais;
	}

	public void setDadosPessoais(DadosPessoais dadosPessoais) {
		this.dadosPessoais = dadosPessoais;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome.toUpperCase();
	}

	public void setSenha(String senha) {
		this.senha = senha;

	}

	public String getSenha() {
		return senha;
	}

	public void setEmail(String email) {
		this.email = email;

	}

	public String getEmail() {
		return email;
	}

	public void setAutorizacoes(List<Autorizacao> autorizacoes) {
		this.autorizacoes = autorizacoes;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public List<Autorizacao> getAutorizacoes() {
		return autorizacoes;
	}

	public Imagens getImagens() {
		return imagens;
	}

	public void setImagens(Imagens imagens) {
		this.imagens = imagens;
	}

	public List<EventoPresencial> getEventoPresencial() {
		return eventoPresencial;
	}

	public void setEventoPresencial(List<EventoPresencial> eventoPresencial) {
		this.eventoPresencial = eventoPresencial;
	}
	
	public List<InscricaoPresencial> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(List<InscricaoPresencial> inscricoes) {
		this.inscricoes = inscricoes;
	}
	
	public List<EventoOnline> getEventoOnline() {
		return eventoOnline;
	}

	public void setEventoOnline(List<EventoOnline> eventoOnline) {
		this.eventoOnline = eventoOnline;
	}

	@Transient
	public String getStatus() {
		return autorizacoes.get(0).getNome().split("_")[1];
	}

	@Transient
	public Boolean isPerfilCompleto() {
		return (this.codigoFuncional != null && this.dadosPessoais != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Notificacao> getNotificaoesNaoLidas() {
		return this.notificacoes.stream().filter(n -> !n.getFoiLido()).collect(Collectors.toList());
	}
	
	public List<Notificacao> getNotificaoesLidas() {
		return this.notificacoes.stream().filter(n -> n.getFoiLido()).collect(Collectors.toList());
	}

}
