package com.gilmarcarlos.developer.gcursos.service.eventos.categorias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.exceptions.CategoriaException;
import com.gilmarcarlos.developer.gcursos.repository.eventos.categorias.CategoriaEventoRepository;

/**
 * Classe com serviços de persistência para entidade (CategoriaEvento)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class CategoriaEventoService {

	@Autowired
	private CategoriaEventoRepository repository;
	
	public CategoriaEvento salvar(CategoriaEvento categoria) {
		return repository.save(categoria);
	}
	
	/**
	 * Método que deleta uma categoria por id
	 * 
	 * @param id 
	 * @throws CategoriaException se existem eventos com essa categoria
	 * 
	 */
	public void deletar(Long id) throws CategoriaException {
		
		CategoriaEvento categoria = buscarPor(id);
		
		if(categoria == null) {
			throw new CategoriaException("categoria não existe");
		}
		
		if(!categoria.getEventos().isEmpty()) {
			throw new CategoriaException("existem eventos cadastrados com essa categoria");
		}	
		
		repository.deleteById(id);
	}
			
	public List<CategoriaEvento> listarTodos(){
		return repository.listAll();
	}

	public CategoriaEvento buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
