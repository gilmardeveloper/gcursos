package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.InscricaoPresencialRepository;

/**
 * Classe com serviços de persistência para entidade (InscricaoPresencial)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class InscricaoPresencialService {

	@Autowired
	private InscricaoPresencialRepository repository;
	
	public InscricaoPresencial salvar(InscricaoPresencial inscricao) {
		return repository.save(inscricao);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<InscricaoPresencial> listarTodos(){
		return repository.listAll();
	}
	
	public Page<InscricaoPresencial> listarTodos(Long id, Pageable pageable) {
		return repository.listarTodos(id, pageable);
	}

	public InscricaoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que altera a presença de uma inscrição  
	 * 
	 * @param presenca true para presente
	 * @param id id da inscricao
	 * 
	 */
	public void confirmarPresenca(Long id, boolean presenca) {
		InscricaoPresencial inscrito = buscarPor(id);
		inscrito.setPresenca(presenca);
		salvar(inscrito);
	}
	
	/**
	 * Método que deleta todas as inscrições por usuario  
	 * 
	 * @param usuario representa um usuario
	 * 
	 */
	public void deletar(Usuario usuario) {
		if(!usuario.getInscricoes().isEmpty()) {
			usuario.getInscricoes().forEach( i -> deletar(i.getId()));
		}
	}
	
	
}
