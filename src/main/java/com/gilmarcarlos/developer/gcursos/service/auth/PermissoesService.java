package com.gilmarcarlos.developer.gcursos.service.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Permissoes;
import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.auth.PermissoesRepository;

/**
 * Classe com serviços de persistência para entidade (Permissoes) crud básico
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class PermissoesService {

	@Autowired
	private PermissoesRepository repository;
	
	public Permissoes salvar(Permissoes permissoes) {
		return repository.save(permissoes);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Permissoes> listarTodos(){
		return repository.listarTodos();
	}

	public Permissoes buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void deletar(Usuario usuario) {
		if(usuario.getPermissoes() != null) {
			deletar(usuario.getPermissoes().getId());
		}
	}

	public boolean temDepartamento(Departamento departamento) {
		
		if(departamento == null) return false;
		
		return listarTodos().stream().anyMatch( p -> p.getDepartamento() == departamento.getId());
	}
	
}
