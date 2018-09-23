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

	public List<UnidadeTrabalho> listarTodos() {
		return repository.listAll();
	}

	public List<UnidadeTrabalho> listarTodos(Long departamento) {
		return repository.listAll(departamento);
	}

	public List<UnidadeTrabalho> listarTodosSemFones() {
		return repository.listaTodos();
	}

	public UnidadeTrabalho buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public boolean temDepartamento(Departamento departamento) {
		return listarTodos().stream().anyMatch( u -> u.getDepartamento().equals(departamento));
	}

}
