package com.gilmarcarlos.developer.gcursos.model.auth;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe de entidade que representa permissões de usuarios
 *  
 * @author Gilmar Carlos
 *
 */
@Entity
public class Permissoes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ElementCollection
	private List<String> criar;
	
	@ElementCollection 
	private List<String> visualizar;
	
	@ElementCollection
	private List<String> alterar;
	
	@ElementCollection
	private List<String> deletar;
	
	@ElementCollection
	private List<String> restringir;
	
	private Long departamento;
	
	@OneToOne
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<String> getCriar() {
		return criar;
	}

	public void setCriar(List<String> criar) {
		this.criar = criar;
	}

	public List<String> getVisualizar() {
		return visualizar;
	}

	public void setVisualizar(List<String> visualizar) {
		this.visualizar = visualizar;
	}

	public List<String> getAlterar() {
		return alterar;
	}

	public void setAlterar(List<String> alterar) {
		this.alterar = alterar;
	}

	public List<String> getDeletar() {
		return deletar;
	}

	public void setDeletar(List<String> deletar) {
		this.deletar = deletar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Long getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Long departamento) {
		this.departamento = departamento;
	}

	public List<String> getRestringir() {
		return restringir;
	}

	public void setRestringir(List<String> restringir) {
		this.restringir = restringir;
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
		Permissoes other = (Permissoes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
