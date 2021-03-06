package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.PermissoesEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.PermissoesEventoPresencialRepository;

/**
 * Classe com serviços de persistência para entidade (PermissoesEventoPresencial) 
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class PermissoesEventoPresencialService {

	@Autowired
	private PermissoesEventoPresencialRepository repository;
	
	public PermissoesEventoPresencial salvar(PermissoesEventoPresencial permissoes) {
		return repository.save(permissoes);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<PermissoesEventoPresencial> listarTodos(){
		return repository.listAll();
	}

	public PermissoesEventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método de validação por unidade 
	 * 
	 * @param unidade representa uma unidade de trabalho
	 * @return boolean
	 * 
	 */
	public boolean temUnidade(UnidadeTrabalho unidade) {
		if(unidade == null) return false;
		
		return listarTodos().stream().anyMatch( p -> p.getUnidades().contains(unidade.getNome()));
	}
	
	/**
	 * Método de validação por cargo 
	 * 
	 * @param cargo representa um cargo
	 * @return boolean
	 * 
	 */
	public boolean temCargo(Cargo cargo) {
		if(cargo == null) return false;
		
		return listarTodos().stream().anyMatch( p -> p.getCargos().contains(cargo.getNome()));
	}
	
	/**
	 * Método de validação por departamento
	 * 
	 * @param departamento representa um cargo
	 * @return boolean
	 * 
	 */
	public boolean temDepartamento(Departamento departamento) {
		if(departamento == null) return false;
		
		return departamento.getUnidades().stream().anyMatch( u -> temUnidade(u));
	}

	
}
