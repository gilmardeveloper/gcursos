package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.repository.locais.CodigoFuncionalRepository;

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

	public boolean temUnidade(UnidadeTrabalho unidade) {
		return listarTodos().stream().anyMatch( c -> c.getUnidadeTrabalho().equals(unidade));
	}

	public boolean temCargo(Cargo cargo) {
		return listarTodos().stream().anyMatch( c -> c.getCargo().equals(cargo));
	}

	
}
