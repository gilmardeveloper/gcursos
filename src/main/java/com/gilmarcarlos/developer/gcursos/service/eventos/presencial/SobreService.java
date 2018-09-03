package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.Sobre;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.SobreRepository;

@Service
public class SobreService {

	@Autowired
	private SobreRepository repository;
	
	public Sobre salvar(Sobre sobre) {
		return repository.save(sobre);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Sobre> listarTodos(){
		return repository.listAll();
	}

	public Sobre buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
