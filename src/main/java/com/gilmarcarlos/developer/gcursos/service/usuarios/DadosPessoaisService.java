package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.DadosPessoaisRepository;

/**
 * Classe com serviços de persistência para entidade (DadosPessoais)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class DadosPessoaisService {

	@Autowired
	private DadosPessoaisRepository repository;
	
	/**
	 * Método que salva dados pessoais de um usuario na base 
	 * 
	 * @param dados entidade que representa dados pessoais de um usuário 
	 * @return DadosPessoais retorna um usuario
	 * @throws CpfExisteException se o usuario com mesmo cpf já existe
	 * 
	 */
	public DadosPessoais salvar(DadosPessoais dados) throws CpfExisteException {
		
		if(cpfExiste(dados)) {
				throw new CpfExisteException("já existe um usuário com este CPF cadastrado");
		}
		
		return repository.save(dados);
	}
	
	/**
	 * Método que salva dados pessoais de um usuario na base 
	 * 
	 * @param dados entidade que representa dados pessoais de um usuário 
	 * @return DadosPessoais retorna um usuario
	 * 
	 */
	public DadosPessoais salvarD(DadosPessoais dados) {
		return repository.save(dados);
	}
	
	/**
	 * Método que deleta dados de usuario na base por id 
	 * 
	 * @param id id de um dado 
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que retorna uma lista 
	 * 
	 * @return List
	 * 
	 */
	public List<DadosPessoais> listarTodos() {
		return repository.listAll();
	}
	
	/**
	 * Método para buscar dados pessoais de um usuario na base por cpf 
	 * 
	 * @param cpf representa um cpf de um usuário 
	 * @return DadosPessoais retorna um usuario
	 * 
	 */
	public DadosPessoais buscarPor(String cpf) {
		return repository.buscarPor(cpf);
	}
	
	/**
	 * Método para validar dados pessoais de um usuario na base por cpf 
	 * 
	 * @param dados representa uma entidade de dados pessoais de um usuário 
	 * @return Boolean <code>true</code> se verdade 
	 * 
	 */
	public Boolean cpfExiste(DadosPessoais dados) {
		if(dados.getId() != null) {
			return repository.existsByCpf(dados.getCpf(), dados.getUsuario().getId());
		}else {
			return repository.existsByCpf(dados.getCpf());
		}
	}
	
	/**
	 * Método para validar dados pessoais de um usuario na base por cpf 
	 * 
	 * @param cpf cpf de um usuário 
	 * @param usuario representa uma entidade de um usuário 
	 * @return Boolean <code>true</code> se verdade 
	 * 
	 */
	public Boolean cpfExiste(String cpf, Usuario usuario) {
		return repository.existsByCpf(cpf, usuario.getId());
	}

}
