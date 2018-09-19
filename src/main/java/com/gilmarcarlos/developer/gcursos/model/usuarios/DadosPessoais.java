package com.gilmarcarlos.developer.gcursos.model.usuarios;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class DadosPessoais implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cpf;
	private LocalDate nascimento;
	private String sexo;
	private String escolaridade;
	
	@OneToMany(mappedBy = "dadosPessoais")
	private List<TelefoneUsuario> telefones;
	
	@OneToOne(mappedBy = "dadosPessoais")
	private EnderecoUsuario endereco;

	@OneToOne(mappedBy = "dadosPessoais")
	private NomeColaborador nomeColaborador;
	
	@OneToOne
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public List<TelefoneUsuario> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneUsuario> telefones) {
		this.telefones = telefones;
	}

	public EnderecoUsuario getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoUsuario endereco) {
		this.endereco = endereco;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		DadosPessoais other = (DadosPessoais) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
