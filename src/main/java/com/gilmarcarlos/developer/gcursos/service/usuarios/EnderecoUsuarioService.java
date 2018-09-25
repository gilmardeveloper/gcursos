package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.EnderecoUsuario;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.EnderecoUsuarioRepository;

/**
 * Classe com serviços de persistência para entidade (EnderecoUsuario)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class EnderecoUsuarioService {

	@Autowired
	private EnderecoUsuarioRepository repository;
	
	
	/**
	 * Método que salva um endereço na base 
	 * 
	 * @param enderecoUsuario entidade que representa um endereço 
	 * @return EnderecoUsuario retorna um endereço
	 * 
	 */
	public EnderecoUsuario salvar(EnderecoUsuario enderecoUsuario) {
		return repository.save(enderecoUsuario);
	}
	
	
	/**
	 * Método para deletar um endereço na base por id
	 * 
	 * @param id id de um endereço 
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que retorna uma lista com todos os endereços 
	 * 
	 * @return List retorna uma lista 
	 * 
	 */
	public List<EnderecoUsuario> listarTodos(){
		return repository.listAll();
	}
	
	
}
