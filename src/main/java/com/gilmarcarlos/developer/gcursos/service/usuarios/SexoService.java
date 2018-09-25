package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Sexo;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.SexoExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.SexoRepository;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;

/**
 * Classe com serviços de persistência para entidade (SexoService)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class SexoService {

	@Autowired
	private SexoRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EventoOnlineService eventoOnlineService;
	
	@Autowired
	private EventoPresencialService eventoPresencialService;
	
	/**
	 * Método que salva uma opção na base 
	 * 
	 * @param sexo entidade que representa uma opção 
	 * @return Sexo retorna um sexo
	 * @throws SexoExisteException se essa opção estiver presente nas permissões, no cadastro de usuários ou eventos
	 * 
	 */
	public Sexo salvar(Sexo sexo) throws SexoExisteException {
		
		if(usuarioService.sexoExiste(sexo.getNome())){
			throw new SexoExisteException("para alterar essa opção é necessário antes alterar os usuários selecionados com essa opção");
		}
		
		if(eventoOnlineService.sexoExiste(sexo.getNome())) {
			throw new SexoExisteException("para alterar essa opção é necessário antes excluir ou alterar as permissões dos eventos online");
		}
		
		if(eventoPresencialService.sexoExiste(sexo.getNome())) {
			throw new SexoExisteException("para alterar essa opção é necessário antes excluir ou alterar as permissões dos eventos presenciais");
		}
		
		return repository.save(sexo);
	}
	
	/**
	 * Método que deleta uma opção na base 
	 * 
	 * @param id id de uma opção 
	 * @throws SexoExisteException se essa opção estiver presente nas permissões, no cadastro de usuários ou eventos
	 * 
	 */
	public void deletar(Long id) throws SexoExisteException {
		
		Sexo sexo = buscarPor(id);
		
		if(sexo == null) {
			throw new SexoExisteException("opção sexual não foi encontrada");
		}
		
		if(usuarioService.sexoExiste(sexo.getNome())){
			throw new SexoExisteException("para deletar essa opção é necessário antes alterar os usuários selecionados com essa opção");
		}
		
		if(eventoOnlineService.sexoExiste(sexo.getNome())) {
			throw new SexoExisteException("para deletar essa opção é necessário antes excluir ou alterar as permissões dos eventos online");
		}
		
		if(eventoPresencialService.sexoExiste(sexo.getNome())) {
			throw new SexoExisteException("para deletar essa opção é necessário antes excluir ou alterar as permissões dos eventos presenciais");
		}
		
		repository.deleteById(id);
	}
	
	/**
	 * Método que retorna uma lista com todas as opções
	 * 
	 * @return List retorna uma lista
	 * 
	 */
	public List<Sexo> listarTodos(){
		return repository.listAll();
	}
	
	/**
	 * Método para buscar uma opção na base por id
	 * 
	 * @param id id de uma opção 
	 * @return Sexo retorna um sexo
	 * 
	 */
	public Sexo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
