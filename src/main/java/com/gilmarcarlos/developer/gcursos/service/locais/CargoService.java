package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.repository.locais.CargoRepository;

@Service
public class CargoService {

	@Autowired
	private CargoRepository repository;
	
	public Cargo salvar(Cargo cargo) {
		return repository.save(cargo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Cargo> listarTodos(){
		return repository.listAll();
	}

	public Cargo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
