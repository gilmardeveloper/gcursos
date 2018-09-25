package com.gilmarcarlos.developer.gcursos.model.locais;

import java.beans.Transient;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.NomeColaborador;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe de entidade que representa um um codigo funcional ou representação de dados profissionais
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class CodigoFuncional implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String codigo;
	
	@OneToOne
	private Cargo cargo;
	
	@OneToOne
	private UnidadeTrabalho unidadeTrabalho;
	
	@OneToOne(mappedBy = "codigoFuncional")
	private NomeColaborador nomeColaborador;
	
	@OneToOne
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public UnidadeTrabalho getUnidadeTrabalho() {
		return unidadeTrabalho;
	}

	public void setUnidadeTrabalho(UnidadeTrabalho unidadeTrabalho) {
		this.unidadeTrabalho = unidadeTrabalho;
	}

	@Transient
	public String getTipo() {
		return (codigo.equals("nf") ? "outros" : "funcionarios");
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
		CodigoFuncional other = (CodigoFuncional) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
