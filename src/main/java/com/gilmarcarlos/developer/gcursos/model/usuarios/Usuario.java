package com.gilmarcarlos.developer.gcursos.model.usuarios;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;

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
	
	public Usuario() {
		this.habilitado = false;
	}
	
	public CodigoFuncional getCodigoFuncional() {
		return codigoFuncional;
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
	
	@Transient
	public String getStatus() {
		return autorizacoes.get(0).getNome().split("_")[1];
	}
	
	@Transient
	public Boolean isPerfilCompleto() {
		return  (this.codigoFuncional != null && this.dadosPessoais != null) ;
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
