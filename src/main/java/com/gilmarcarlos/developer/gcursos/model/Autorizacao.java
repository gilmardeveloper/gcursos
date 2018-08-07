package com.gilmarcarlos.developer.gcursos.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Autorizacao {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String nome;
    
    @ManyToMany(mappedBy = "autorizacoes")
    private Collection<Usuario> usuarios;
 
    @ManyToMany
    @JoinTable(
        name = "autorizacoes_privilegios", 
        joinColumns = @JoinColumn(
        name = "privilegio_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
        name = "autorizacao_id", referencedColumnName = "id"))
    private Collection<Privilegio> privilegios;

    public Autorizacao() {
	}
    
    public Autorizacao(String nome) {
    	this.nome = nome;
    }
    
	public void setPrivilegios(Collection<Privilegio> privilegios) {
		this.privilegios = privilegios;
		
	}

	public Collection<? extends Privilegio> getPrivilegios() {
		return privilegios;
	}   
}
