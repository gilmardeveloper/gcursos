package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.CargoExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.CargoNotFoundException;
import com.gilmarcarlos.developer.gcursos.repository.locais.CargoRepository;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.PermissoesEventoPresencialService;

/**
 * Classe com serviços de persistência para entidade (Cargo)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class CargoService {

	@Autowired
	private CargoRepository repository;
	
	@Autowired
	private PermissoesEventoPresencialService permissoesEventoPresencialService;

	@Autowired
	private PermissoesEventoOnlineService permissoesEventoOnlineService;
	
	@Autowired
	private CodigoFuncionalService codigoService;
	
	/**
	 * Método que salva um cargo na base 
	 * 
	 * @param cargo representa um cargo
	 * @return Cargo
	 * @throws CargoExisteException se o cargo estiver selecionado nas permissões dos eventos 
	 * 
	 */
	public Cargo salvar(Cargo cargo) throws CargoExisteException {
		
		if (permissoesEventoPresencialService.temCargo(buscarPor(cargo.getId()))) {
			throw new CargoExisteException("Existem eventos presenciais com este cargo cadastrado em suas permissçoes");
		}

		if (permissoesEventoOnlineService.temCargo(buscarPor(cargo.getId()))) {
			throw new CargoExisteException("Existem eventos online com este cargo cadastrado em suas permissçoes");
		}
		
		return repository.save(cargo);
	}
	
	/**
	 * Método que deleta um cargo na base por id
	 * 
	 * @param id id de um cargo
	 * @throws CargoNotFoundExceptio se o cargo não existir
	 * @throws CargoExisteException se o cargo estiver selecionado nas permissões dos eventos 
	 * 
	 */
	public void deletar(Long id) throws CargoNotFoundException, CargoExisteException {
		
		Cargo cargo = buscarPor(id);
		
		if(cargo == null) {
			throw new CargoNotFoundException();
		}
		
		if(codigoService.temCargo(cargo)) {
			throw new CargoExisteException("Existem usuários cadastrados com esse cargo");
		}
		
		if (permissoesEventoPresencialService.temCargo(cargo)) {
			throw new CargoExisteException("Existem eventos presenciais com este cargo cadastrado em suas permissçoes");
		}

		if (permissoesEventoOnlineService.temCargo(cargo)) {
			throw new CargoExisteException("Existem eventos online com este cargo cadastrado em suas permissçoes");
		}
		
		repository.deleteById(id);
	}
	
	/**
	 * Método que lista todos os cargos na base
	 * 
	 * @return List
	 * 
	 */
	public List<Cargo> listarTodos(){
		return repository.listAll();
	}
	
	/**
	 * Método que busca um cargo na base por id
	 * 
	 * @param id id de um cargo
	 * @return Cargo
	 * 
	 */
	public Cargo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
