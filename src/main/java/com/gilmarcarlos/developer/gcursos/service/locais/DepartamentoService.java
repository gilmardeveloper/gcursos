package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.DepartamentoExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.DepartamentoNotFoundException;
import com.gilmarcarlos.developer.gcursos.repository.locais.DepartamentoRepository;
import com.gilmarcarlos.developer.gcursos.service.auth.PermissoesService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.PermissoesEventoPresencialService;

/**
 * Classe com serviços de persistência para entidade (Departamento)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class DepartamentoService {

	@Autowired
	private DepartamentoRepository repository;
	
	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private PermissoesService permissoesService;

	@Autowired
	private PermissoesEventoPresencialService permissoesEventoPresencialService;

	@Autowired
	private PermissoesEventoOnlineService permissoesEventoOnlineService;
	
	/**
	 * Método que salva um departamento na base 
	 * 
	 * @param departametno entidade que representa um departametno 
	 * @return Departamento retorna um departamento
	 * @throws DepartamentoExisteException se o departamento estiver selecionado nas permissões do administrador
	 * 
	 */
	public Departamento salvar(Departamento departamento) throws DepartamentoExisteException {

		if (permissoesService.temDepartamento(buscarPor(departamento.getId()))) {
			throw new DepartamentoExisteException(
					"Existem administradores com restrição por este departamento, é necessário remover essa opção das permissões antes de altera-lo");
		}

		return repository.save(departamento);
	}
	
	/**
	 * Método que deleta um departamento na base por id 
	 * 
	 * @param id id de um departamento
	 * @return Departamento retorna um departametno
	 * @throws DepartamentoNotFoundException se o departamento não existir
	 * @throws DepartamentoExisteException se o departamento estiver selecionado nas permissões do administrador
	 * 
	 */
	public void deletar(Long id) throws DepartamentoNotFoundException, DepartamentoExisteException {
		
		Departamento departamento = buscarPor(id);
		
		if(departamento == null) {
			throw new DepartamentoNotFoundException();
		}
		
		if(unidadeService.temDepartamento(departamento)) {
			throw new DepartamentoExisteException("Existem unidades de trabalho cadastrados com esse departamento");
		}
				
		if (permissoesEventoPresencialService.temDepartamento(departamento)) {
			throw new DepartamentoExisteException("Existem eventos presenciais com unidades de trabalho cadastradas com esse departamento");
		}

		if (permissoesEventoOnlineService.temDepartamento(departamento)) {
			throw new DepartamentoExisteException("Existem eventos online com unidades de trabalho cadastradas com esse departamento");
		}

		repository.deleteById(id);
	}
	
	/**
	 * Método que lista todos os departamentos  
	 * 
	 * @return List
	 * 
	 */
	public List<Departamento> listarTodos() {
		return repository.listAll();
	}
	
	/**
	 * Método que busca um departamento por id  
	 * 
	 * @param id id do departamento
	 * @return Departamento
	 * 
	 */
	public Departamento buscarPor(Long id) {
		return repository.buscaPorId(id);
	}

}
