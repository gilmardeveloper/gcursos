package com.gilmarcarlos.developer.gcursos.model.usuarios;

import java.io.Serializable;

/**
 * Classe auxiliar da entidade usuario
 * 
 * @author Gilmar Carlos
 *
 */
public class UsuarioDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String cpf;
	private String email;
	private String codigo;
	
	public UsuarioDTO(Usuario u) {
		this.id = u.getId();
		this.nome =u.getNome();
		this.email = u.getEmail();
		this.cpf = u.getDadosPessoais().getCpf();
		this.codigo = u.getCodigoFuncional().getCodigo();
	}
	
	public UsuarioDTO() {
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
