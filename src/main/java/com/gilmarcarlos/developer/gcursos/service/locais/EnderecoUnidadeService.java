package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.repository.locais.EnderecoUnidadeRepository;

/**
 * Classe com serviços de persistência para entidade (EnderecoUnidade)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class EnderecoUnidadeService {

	@Autowired
	private EnderecoUnidadeRepository repository;
	
	/**
	 * Método que salva um endereço na base 
	 * 
	 * @param enderecoUnidade endereço da unidade
	 * @return EnderecoUnidade 
	 * 
	 */
	public EnderecoUnidade salvar(EnderecoUnidade enderecoUnidade) {
		return repository.save(enderecoUnidade);
	}
	
	/**
	 * Método que deleta um endereço na base por id
	 * 
	 * @param id id do endereço
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que deleta um endereço na base por unidade
	 * 
	 * @param id id da unidade
	 * 
	 */
	public void deletarByUnidade(Long id) {
		repository.deleteByUnidade(id);
	}
	
	/**
	 * Método que lista todos os endereços 
	 * 
	 * @return List
	 * 
	 */
	public List<EnderecoUnidade> listarTodos(){
		return repository.listAll();
	}
	
	
}
