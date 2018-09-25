package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineModulo;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.InscricaoOnlineModuloRepository;

/**
 * Classe com serviços de persistência para entidade (InscricaoOnlineModulo) crud básico
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class InscricaoOnlineModuloService {

	@Autowired
	private InscricaoOnlineModuloRepository repository;
	
	public InscricaoOnlineModulo salvar(InscricaoOnlineModulo inscricao){
		return repository.save(inscricao);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<InscricaoOnlineModulo> listarTodos() {
		return repository.listAll();
	}

	public InscricaoOnlineModulo buscarPor(Long id) {
		return repository.buscarPor(id);
	}

}
