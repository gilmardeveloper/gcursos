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
import com.gilmarcarlos.developer.gcursos.model.auth.Permissoes;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.notifications.Mensagens;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;

/**
 * Classe de entidade que representa um Usuario
 * 
 * @author Gilmar Carlos
 *
 */
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
	
	@OneToMany(mappedBy = "usuario")
	private List<Mensagens> mensagens;

	@OneToOne(mappedBy = "usuario")
	private Imagens imagens;

	@OneToMany(mappedBy = "responsavel")
	private List<EventoPresencial> eventoPresencial;

	@OneToMany(mappedBy = "responsavel")
	private List<EventoOnline> eventoOnline;
	
	@OneToMany(mappedBy = "usuario")
	private List<InscricaoPresencial> inscricoes;
	
	@OneToMany(mappedBy = "usuario")
	private List<InscricaoOnline> inscricoesOnline;
	
	@OneToOne(mappedBy = "usuario")
	private Permissoes permissoes;

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
	
	public List<Mensagens> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagens> mensagens) {
		this.mensagens = mensagens;
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

	public List<InscricaoOnline> getInscricoesOnline() {
		return inscricoesOnline;
	}

	public void setInscricoesOnline(List<InscricaoOnline> inscricoesOnline) {
		this.inscricoesOnline = inscricoesOnline;
	}
	
	public Permissoes getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Permissoes permissoes) {
		this.permissoes = permissoes;
	}
	
	public boolean isTokenExpired() {
		return tokenExpired;
	}

	public void setTokenExpired(boolean tokenExpired) {
		this.tokenExpired = tokenExpired;
	}
	
	/**
	 * Método que retorna o status da autorização 
	 * 
	 * @return String 
	 * 
	 */
	@Transient
	public String getStatus() {
		return autorizacoes.get(0).getNome().split("_")[1];
	}
	
	/**
	 * Método que retorna se o perfil esta completo
	 * 
	 * @return Boolean <code>true</code> se verdadeiro 
	 * 
	 */
	@Transient
	public Boolean isPerfilCompleto() {
		return (this.codigoFuncional != null && this.dadosPessoais != null);
	}
	
	/**
	 * Método que retorna uma lista com as notificações não lidas
	 * 
	 * @return List 
	 * 
	 */
	@Transient
	public List<Notificacao> getNotificaoesNaoLidas() {
		return this.notificacoes.stream().filter(n -> !n.getFoiLido()).collect(Collectors.toList());
	}
	
	/**
	 * Método que retorna uma lista com as notificações lidas
	 * 
	 * @return List 
	 * 
	 */
	@Transient
	public List<Notificacao> getNotificaoesLidas() {
		return this.notificacoes.stream().filter(n -> n.getFoiLido()).collect(Collectors.toList());
	}
	
	/**
	 * Método que retorna uma lista com as mensagens não lidas
	 * 
	 * @return List 
	 * 
	 */
	@Transient
	public List<Mensagens> getMensagensNaoLidas() {
		return this.mensagens.stream().filter(m -> !m.getFoiLido()).collect(Collectors.toList());
	}
	
	/**
	 * Método que retorna uma lista com as mensagens lidas
	 * 
	 * @return List 
	 * 
	 */
	@Transient
	public List<Mensagens> getMensagensLidas() {
		return this.mensagens.stream().filter(m -> m.getFoiLido()).collect(Collectors.toList());
	}
	
	/**
	 * Método que valida se o usuario é responsavel por um evento online
	 * 
	 * @param evento
	 * @return boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public boolean ehResponsavel(EventoOnline evento) {
		return getEventoOnline().stream().anyMatch( e-> e.equals(evento));
	}
	

	/**
	 * Método que valida se o usuario é responsavel por um evento presencial
	 * 
	 * @param evento
	 * @return boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public boolean ehResponsavel(EventoPresencial evento) {
		return getEventoPresencial().stream().anyMatch( e-> e.equals(evento));
	}
	

	/**
	 * Método que retorna a quantidade de inscrições presenciais
	 * 
	 * @return Integer
	 * 
	 */
	@Transient
	public Integer qtdInscricoesPresenciais() {
		return getInscricoes().size();
	}
	
	/**
	 * Método que retorna a quantidade de inscrições online
	 * 
	 * @return Integer
	 * 
	 */
	@Transient
	public Integer qtdInscricoesOnline() {
		return getInscricoesOnline().size();
	}
	
	/**
	 * Método que retorna a quantidade de inscrições total
	 * 
	 * @return Integer
	 * 
	 */
	@Transient
	public Integer qtdInscricoesTotal() {
		return qtdInscricoesOnline() + qtdInscricoesPresenciais();
	}
	
	/**
	 * Método que retorna a assiduidade total de todos os evento presenciais
	 * 
	 * @return Long
	 * 
	 */
	@Transient
	public Long assiduidadeTotal() {
		return getInscricoes().stream().filter(i -> i.isPresente()).count();
	}
	
	/**
	 * Método que retorna o absenteismo total de todos os evento presenciais
	 * 
	 * @return Long
	 * 
	 */
	@Transient
	public Long absenteismoTotal() {
		return getInscricoes().stream().filter(i -> !i.isPresente()).count();
	}
	
	/**
	 * Método que retorna o progresso total de todos os eventos online
	 * 
	 * @return Long
	 * 
	 */
	@Transient
	public Long progressoTotal() {
		return getInscricoesOnline().stream().filter(i -> i.isFinalizado()).count();
	}
	
	/**
	 * Método que retorna o andamento total de todos os eventos online
	 * 
	 * @return Long
	 * 
	 */
	@Transient
	public Long andamentoTotal() {
		return getInscricoesOnline().stream().filter(i -> !i.isFinalizado()).count();
	}
	
	/**
	 * Método que valida se o usuario é administrador
	 * 
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean isAdmin() {
		return getAutorizacoes().get(0).getNome().equals("ROLE_Administrador");
	}
	
	/**
	 * Método que valida se o usuario é administrador com permissões para criar
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean podeCriar(String opcao) {
		return (getPermissoes() == null ? false : (getPermissoes().getCriar().contains("tudo") ? true : getPermissoes().getCriar().contains(opcao)));
	}
	
	/**
	 * Método que valida se o usuario é administrador com permissões para visualizar
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean podeVisualizar(String opcao) {
		return (getPermissoes() == null ? false : (getPermissoes().getVisualizar().contains("tudo") ? true : getPermissoes().getVisualizar().contains(opcao)));
	}
	
	/**
	 * Método que valida se o usuario é administrador com permissões para alterar
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean podeAlterar(String opcao) {
		return (getPermissoes() == null ? false : (getPermissoes().getAlterar().contains("tudo") ? true : getPermissoes().getAlterar().contains(opcao)));
	}
	
	/**
	 * Método que valida se o usuario é administrador com permissões para deletar
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean podeDeletar(String opcao) {
		return (getPermissoes() == null ? false : (getPermissoes().getDeletar().contains("tudo") ? true : getPermissoes().getDeletar().contains(opcao)));
	}
	
	/**
	 * Método que valida se o usuario é administrador com restrições
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean temRestricao(String opcao) {
		return (getPermissoes() == null ? false : getPermissoes().getRestringir().contains(opcao));
	}
	
	/**
	 * Método que valida se o usuario é administrador especifico de um departamento
	 * 
	 * @param opcao
	 * @return Boolean <code>true</code> se for verdadeiro
	 * 
	 */
	@Transient
	public Boolean temMesmoDepartamento(Usuario usuario) {
		return getPermissoes().getDepartamento() == usuario.getCodigoFuncional().getUnidadeTrabalho().getDepartamento().getId();
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

}
