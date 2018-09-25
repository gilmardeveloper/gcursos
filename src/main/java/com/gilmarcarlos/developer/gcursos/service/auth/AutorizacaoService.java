package com.gilmarcarlos.developer.gcursos.service.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.repository.auth.AutorizacaoRepository;

/**
 * Classe com serviços de persistência para entidade (Autorizacao) crud básico
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class AutorizacaoService {

@Autowired
private AutorizacaoRepository repository;

public List<Autorizacao> listarTodos(){
	return repository.listAll();
}

public Autorizacao buscarPor(String nome) {
	return repository.findByNome(nome);
}

}
