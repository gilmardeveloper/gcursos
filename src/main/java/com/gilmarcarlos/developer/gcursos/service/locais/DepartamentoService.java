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

	public Departamento salvar(Departamento departamento) throws DepartamentoExisteException {

		if (permissoesService.temDepartamento(buscarPor(departamento.getId()))) {
			throw new DepartamentoExisteException(
					"Existem administradores com restrição por este departamento, é necessário remover essa opção das permissões antes de altera-lo");
		}

		return repository.save(departamento);
	}

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

	public List<Departamento> listarTodos() {
		return repository.listAll();
	}

	public Departamento buscarPor(Long id) {
		return repository.buscaPorId(id);
	}

}
