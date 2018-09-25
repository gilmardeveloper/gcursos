package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.OrdenarHelper;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.exceptions.PosicaoExisteException;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.ModuloRepository;

/**
 * Classe com serviços de persistência para entidade (Modulo)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class ModuloService {

	@Autowired
	private ModuloRepository repository;
	
	/**
	 * Método que salva um modulo na base 
	 * 
	 * @param modulo representa um modulo
	 * @return Modulo
	 * @throws PosicaoExisteException se a posição já existir
	 * 
	 */
	public Modulo salvar(Modulo modulo) throws PosicaoExisteException {
		
		for(Modulo m : modulo.getEventoOnline().getModulos()) {
			if((m.getPosicao() == modulo.getPosicao()) && !m.equals(modulo)) {
				throw new PosicaoExisteException("módulos do mesmo evento não podem ter a mesma posição");
			}
		}
		
		return repository.save(modulo);
	}

	public void deletar(Long id){
		repository.deleteById(id);
	}

	public List<Modulo> listarTodos() {
		return repository.listAll();
	}

	public Modulo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que altera a ordem uma lista de modulos 
	 * 
	 * @param lista lista de modulos
	 * 
	 */
	public void ordenar(List<OrdenarHelper> lista) {
		Integer posicao = 1;
		for (OrdenarHelper helper : lista) {
			Modulo modulo = buscarPor(helper.getId());
			modulo.setPosicao(posicao);
			repository.save(modulo);
			posicao++;
		}

	}

}
