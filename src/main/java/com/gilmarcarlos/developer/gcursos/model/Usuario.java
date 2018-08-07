package com.gilmarcarlos.developer.gcursos.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Usuario {

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
	private Collection<Autorizacao> autorizacoes;
	
	public Usuario() {
		this.habilitado = false;
	}

	public Long getId() {
		return id;
	}
		
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
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

	
	public Collection<Autorizacao> getAutorizacoes() {
		return autorizacoes;
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
