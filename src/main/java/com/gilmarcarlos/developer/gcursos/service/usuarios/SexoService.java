package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Sexo;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.SexoRepository;

@Service
public class SexoService {

	@Autowired
	private SexoRepository repository;
	
	public Sexo salvar(Sexo sexo) {
		return repository.save(sexo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Sexo> listarTodos(){
		return repository.listAll();
	}

	public Sexo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
