package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioDeleteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.TelefoneUsuarioRepository;

/**
 * Classe com serviços de persistência para entidade (TelefoneUsuario)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class TelefoneUsuarioService {

	@Autowired
	private TelefoneUsuarioRepository repository;
	
	/**
	 * Método que salva um telefone relacionado a um usuario na base 
	 * 
	 * @param telefoneUsuario entidade que representa o TelefoneUsuario 
	 * @return TelefoneUsuario retorna um telefone de um usuario
	 * 
	 */
	public TelefoneUsuario salvar(TelefoneUsuario telefoneUsuario) {
		return repository.save(telefoneUsuario);
	}
	
	/**
	 * Método que deleta um telefone relacionado a um usuario na base 
	 * 
	 * @param id representa o id de um TelefoneUsuario 
	 * @throws UsuarioDeleteException se for responsavel por eventos e tiver apenas um telefone cadastrado
	 * 
	 */
	public void deletar(Long id) throws UsuarioDeleteException {
		
		TelefoneUsuario telefone = buscarPor(id);
		DadosPessoais dados = telefone.getDadosPessoais();
		
		if(dados.getTelefones().size() == 1 && (!dados.getUsuario().getEventoOnline().isEmpty() || !dados.getUsuario().getEventoPresencial().isEmpty())) {
			throw new UsuarioDeleteException("usuário é reponsável por eventos, e precisa ter pelo menos um telefone cadastrado!");			
		}
		
		repository.deleteById(id);
	}
	
	/**
	 * Método que retorna uma lista de telefones de um usuario 
	 * 
	 * @return List retorna uma lista
	 * 
	 */
	public List<TelefoneUsuario> listarTodos() {
		return repository.listAll();
	}
	
	/**
	 * Método que busca por um telefone por numero
	 * 
	 * @param numero representa um numero de telefone 
	 * @return TelefoneUsuario retorna um telefone de um usuario
	 * 
	 */
	public TelefoneUsuario buscarPor(String numero) {
		return repository.buscarPor(numero);
	}
	
	/**
	 * Método que busca por um telefone por id
	 * 
	 * @param id representa um id
	 * @return TelefoneUsuario retorna um telefone de um usuario
	 * 
	 */
	public TelefoneUsuario buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que deleta telefones por usuario
	 * 
	 * @param usuario representa um usuario
	 * 
	 */
	public void deletar(Usuario usuario) {
		if (!usuario.getDadosPessoais().getTelefones().isEmpty()) {
			usuario.getDadosPessoais().getTelefones().forEach(t -> {
				try {
					deletar(t.getId());
				} catch (UsuarioDeleteException e) {
					e.printStackTrace();
				}
			});
		}
	}

}
