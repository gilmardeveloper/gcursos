package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.repository.DadosPessoaisRepository;

@Service
public class DadosPessoaisService {

	@Autowired
	private DadosPessoaisRepository repository;

	public DadosPessoais salvar(DadosPessoais dados) throws CpfExisteException {
		
		if(dados.getId() == null && cpfExiste(dados.getCpf())) {
				throw new CpfExisteException("já existe um usuário com este CPF cadastrado");
		}
		
		if(dados.getId() != null && cpfExiste(dados.getCpf(), dados.getUsuario().getId())) {
				throw new CpfExisteException("já existe um usuário com este CPF cadastrado");
		}
		
		return repository.save(dados);
	}
	
	public DadosPessoais salvarD(DadosPessoais dados) {
		return repository.save(dados);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<DadosPessoais> listarTodos() {
		return repository.listAll();
	}

	public DadosPessoais buscarPor(String cpf) {
		return repository.buscarPor(cpf);
	}

	public Boolean cpfExiste(String cpf, Long id) {
		return repository.existsByCpf(cpf, id);
	}

	public Boolean cpfExiste(String cpf) {
		return repository.existsByCpf(cpf);
	}

}
