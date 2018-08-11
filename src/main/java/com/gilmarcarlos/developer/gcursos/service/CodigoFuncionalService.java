package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.repository.CodigoFuncionalRepository;

@Service
public class CodigoFuncionalService {

	@Autowired
	private CodigoFuncionalRepository repository;
	
	public CodigoFuncional salvar(CodigoFuncional codigoFuncional) {
		return repository.save(codigoFuncional);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<CodigoFuncional> listarTodos(){
		return repository.listAll();
	}

	public CodigoFuncional buscarPor(String codigoFuncional) {
		return repository.buscarPor(codigoFuncional);
	}
	
	public CodigoFuncional buscarPor(DadosPessoais dadosPessoais) {
		return repository.buscarPor(dadosPessoais.getId());
	}
	
	
}
