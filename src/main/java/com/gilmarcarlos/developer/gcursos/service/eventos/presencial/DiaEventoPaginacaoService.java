package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.DiaEventoPaginacaoRepository;

/**
 * Classe com serviços de persistência para entidade (Cargo) crud básico
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class DiaEventoPaginacaoService {
	
	@Autowired
	private DiaEventoPaginacaoRepository repository;
	
	public Page<DiaEvento> listarTodos(Pageable pageable){
		return repository.listarTodos(pageable);
	}
	
	public Page<DiaEvento> listarDiasPorProgramacao(Long id, Pageable pageable){
		return repository.listarDiasPorProgramacao(id, pageable);
	}

	
}
