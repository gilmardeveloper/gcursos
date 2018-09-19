package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Sexo;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.SexoExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.SexoRepository;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;

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
			
	public List<Sexo> listarTodos(){
		return repository.listAll();
	}

	public Sexo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
