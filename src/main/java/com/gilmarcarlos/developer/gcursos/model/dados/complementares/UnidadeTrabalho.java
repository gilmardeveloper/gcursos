package com.gilmarcarlos.developer.gcursos.model.dados.complementares;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class UnidadeTrabalho implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long qtdFuncionarios;
	private String nome;
	private String email;
	private String gerente;
	
	@OneToMany(mappedBy = "unidadeTrabalho")
	private List<TelefoneUnidade> telefones;
	
	@OneToMany(mappedBy = "unidadeTrabalho")
	private List<CodigoFuncional> codigosFuncionais;
	
	@OneToOne
	private Departamento departamento;
	
	@OneToOne(mappedBy = "unidadeTrabalho")
	private EnderecoUnidade endereco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGerente() {
		return gerente.toUpperCase();
	}

	public void setGerente(String gerente) {
		this.gerente = gerente;
	}

	public Long getQtdFuncionarios() {
		return qtdFuncionarios;
	}

	public void setQtdFuncionarios(Long qtdFuncionarios) {
		this.qtdFuncionarios = qtdFuncionarios;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<TelefoneUnidade> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneUnidade> telefones) {
		this.telefones = telefones;
	}

	public List<CodigoFuncional> getCodigosFuncionais() {
		return codigosFuncionais;
	}

	public void setCodigosFuncionais(List<CodigoFuncional> codigosFuncionais) {
		this.codigosFuncionais = codigosFuncionais;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EnderecoUnidade getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoUnidade endereco) {
		this.endereco = endereco;
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
		UnidadeTrabalho other = (UnidadeTrabalho) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
