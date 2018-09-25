package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.ProgramacaoPresencialRepository;

/**
 * Classe com serviços de persistência para entidade (ProgramacaoPresencial) crud básico
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class ProgramacaoPresencialService {

	@Autowired
	private ProgramacaoPresencialRepository repository;
	
	public ProgramacaoPresencial salvar(ProgramacaoPresencial programacao) {
		return repository.save(programacao);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<ProgramacaoPresencial> listarTodos(){
		return repository.listAll();
	}

	public ProgramacaoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
