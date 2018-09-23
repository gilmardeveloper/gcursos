package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;
import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.PermissoesEventoOnlineRepository;

@Service
public class PermissoesEventoOnlineService {

	@Autowired
	private PermissoesEventoOnlineRepository repository;
	
	public PermissoesEventoOnline salvar(PermissoesEventoOnline permissoes) {
		return repository.save(permissoes);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<PermissoesEventoOnline> listarTodos(){
		return repository.listAll();
	}

	public PermissoesEventoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public boolean temUnidade(UnidadeTrabalho unidade) {
		if (unidade == null) return false;
		
		return listarTodos().stream().anyMatch( p -> p.getUnidades().contains(unidade.getNome()));
	}
	
	public boolean temCargo(Cargo cargo) {
		if(cargo ==  null) return false;
		
		return listarTodos().stream().anyMatch( p -> p.getCargos().contains(cargo.getNome()));
	}

	public boolean temDepartamento(Departamento departamento) {
		if(departamento == null) return false;
		
		return departamento.getUnidades().stream().anyMatch( u -> temUnidade(u));
	}
}
