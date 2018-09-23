package com.gilmarcarlos.developer.gcursos.model.auth;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class UsuarioToken {
	
	 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     
    private String token;
   
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;
     
    private LocalDate dataExpiracao;
    
    public UsuarioToken() {
    	
    }
    
    public UsuarioToken(Usuario usuario, String token) {
    	this.usuario = usuario;
    	this.token = token;
    	this.dataExpiracao = LocalDate.now();
    }

	public boolean expirou() {
		  return Period.between(this.dataExpiracao, LocalDate.now()).getDays() > 1;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
