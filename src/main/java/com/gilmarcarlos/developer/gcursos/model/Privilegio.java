package com.gilmarcarlos.developer.gcursos.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Privilegio {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String nome;
 
    @ManyToMany(mappedBy = "privilegios")
    private Collection<Autorizacao> autorizacoes;
    
    public Privilegio() {
    	
    }
    
    public Privilegio(String nome) {
    	this.nome = nome;
    }

	public String getNome() {
		return nome;
	}
}
