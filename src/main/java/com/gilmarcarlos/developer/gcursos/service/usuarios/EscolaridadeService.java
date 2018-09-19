package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.EscolaridadeExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.EscolaridadeRepository;

@Service
public class EscolaridadeService {

	@Autowired
	private EscolaridadeRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public Escolaridade salvar(Escolaridade escolaridade) throws EscolaridadeExisteException {
		
		if(usuarioService.escolaridadeExiste(escolaridade.getNome())) {
			throw new EscolaridadeExisteException("para alterar essa opção é necessário antes alterar os usuários selecionados com essa opção");
		}
		
		return repository.save(escolaridade);
	}
		
	public void deletar(Long id) throws EscolaridadeExisteException {
		
		Escolaridade escolaridade = buscarPor(id);
		
		if(escolaridade == null) {
			throw new EscolaridadeExisteException("opção não foi encontrada");
		}
		
		if(usuarioService.escolaridadeExiste(escolaridade.getNome())){
			throw new EscolaridadeExisteException("para deletar essa opção é necessário antes alterar os usuários selecionados com essa opção");
		}
		
		repository.deleteById(id);
	}
			
	public List<Escolaridade> listarTodos(){
		return repository.listAll();
	}

	public Escolaridade buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
