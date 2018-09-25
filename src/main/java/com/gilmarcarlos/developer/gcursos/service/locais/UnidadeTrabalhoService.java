package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.locais.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.UnidadeExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.UnidadeNotFoundException;
import com.gilmarcarlos.developer.gcursos.repository.locais.UnidadeTrabalhoRepository;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.PermissoesEventoPresencialService;


/**
 * Classe com serviços de persistência para entidade (UnidadeTrabalho)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class UnidadeTrabalhoService {

	@Autowired
	private UnidadeTrabalhoRepository repository;
	
	@Autowired
	private CodigoFuncionalService codigoService;

	@Autowired
	private EnderecoUnidadeService enderecoService;
	
	@Autowired
	private TelefoneUnidadeService telefoneService;

	@Autowired
	private PermissoesEventoPresencialService permissoesEventoPresencialService;

	@Autowired
	private PermissoesEventoOnlineService permissoesEventoOnlineService;
	
	/**
	 * Método que salva uma unidade na base 
	 * 
	 * @param unidade entidade que representa uma unidade 
	 * @return UnidadeTrabalho retorna um usuario
	 * @throws UnidadeExisteException se existem eventos selecionados com essa unidade
	 * 
	 */
	public UnidadeTrabalho salvar(UnidadeTrabalho unidade) throws UnidadeExisteException {
		
		if (permissoesEventoPresencialService.temUnidade(buscarPor(unidade.getId()))) {
			throw new UnidadeExisteException("Existem eventos presenciais com essa opção nas permissões, é necessário remover essa opção do evento antes de altera-lo");
		}

		if (permissoesEventoOnlineService.temUnidade(buscarPor(unidade.getId()))) {
			throw new UnidadeExisteException("Existem eventos online com essa opção nas permissões, é necessário remover essa opção do evento antes de altera-lo");
		}
		
		EnderecoUnidade endereco = unidade.getEndereco();
		
		if (endereco != null) {
			endereco = enderecoService.salvar(endereco);
		}

		UnidadeTrabalho novaUnidade = repository.save(unidade);
		
		if (endereco != null) {
			endereco.setUnidadeTrabalho(novaUnidade);
			enderecoService.salvar(endereco);
		}

		return novaUnidade;
	}
	
	/**
	 * Método que deleta uma unidade na base por id
	 * 
	 * @param id id de uma unidade 
	 * @throws UnidadeNotFoundException se a unidade não existir
	 * @throws UnidadeExisteException se existem eventos selecionados com essa unidade
	 * 
	 */
	public void deletar(Long id) throws UnidadeNotFoundException, UnidadeExisteException {
		
		UnidadeTrabalho unidade = buscarPor(id);
		
		if(unidade == null) {
			throw new UnidadeNotFoundException();
		}
		
		if(codigoService.temUnidade(unidade)) {
			throw new UnidadeExisteException("Existem usuários cadastrados com essa opção, é necessário alterar todos os cadastrados antes de deleta-lo");
		}
		
		if (permissoesEventoPresencialService.temUnidade(unidade)) {
			throw new UnidadeExisteException("Existem eventos presenciais com essa opção nas permissões, é necessário remover essa opção do evento antes de deleta-lo");
		}

		if (permissoesEventoOnlineService.temUnidade(unidade)) {
			throw new UnidadeExisteException("Existem eventos online com essa opção nas permissões, é necessário remover essa opção do evento antes de deleta-lo");
		}
		
		telefoneService.deletarByUnidade(id);
		enderecoService.deletarByUnidade(id);
		repository.deleteById(id);
	}
	
	/**
	 * Método que lista todas as unidades na base
	 * 
	 * @return List
	 * 
	 */
	public List<UnidadeTrabalho> listarTodos() {
		return repository.listAll();
	}
	
	
	/**
	 * Método que lista todas as unidades na base por departamento
	 * 
	 * @param departamento id de uma departamento
	 * @return List
	 * 
	 */
	public List<UnidadeTrabalho> listarTodos(Long departamento) {
		return repository.listAll(departamento);
	}
	
	/**
	 * Método que lista todas as unidades na base sem telefones
	 * 
	 * @return List
	 * 
	 */
	public List<UnidadeTrabalho> listarTodosSemFones() {
		return repository.listaTodos();
	}
	
	/**
	 * Método que busca uma unidade na base por id
	 * 
	 * @param id 
	 * @return UnidadeTrabalho 
	 * 
	 */
	public UnidadeTrabalho buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que valida uma unidade na base por departamento
	 * 
	 * @param deparatamento id de uma departamento 
	 * @return boolean 
	 * 
	 */
	public boolean temDepartamento(Departamento departamento) {
		return listarTodos().stream().anyMatch( u -> u.getDepartamento().equals(departamento));
	}

}
