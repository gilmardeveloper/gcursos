package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.DadosPessoaisRepository;

@Service
public class DadosPessoaisService {

	@Autowired
	private DadosPessoaisRepository repository;

	public DadosPessoais salvar(DadosPessoais dados) throws CpfExisteException {
		
		if(cpfExiste(dados)) {
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

	public Boolean cpfExiste(DadosPessoais dados) {
		if(dados.getId() != null) {
			return repository.existsByCpf(dados.getCpf(), dados.getUsuario().getId());
		}else {
			return repository.existsByCpf(dados.getCpf());
		}
	}
	
	public Boolean cpfExiste(String cpf, Usuario usuario) {
		return repository.existsByCpf(cpf, usuario.getId());
	}

}
