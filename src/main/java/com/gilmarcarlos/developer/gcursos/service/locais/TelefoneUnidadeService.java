package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.TelefoneUnidade;
import com.gilmarcarlos.developer.gcursos.repository.locais.TelefoneUnidadeRepository;

/**
 * Classe com serviços de persistência para entidade (TelefoneUnidade)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class TelefoneUnidadeService {

	@Autowired
	private TelefoneUnidadeRepository repository;
	
	/**
	 * Método que salva um telefone na base 
	 * 
	 * @param telefoneUnidade telefone da unidade
	 * @return TelefoneUnidade 
	 * 
	 */
	public TelefoneUnidade salvar(TelefoneUnidade telefoneUnidade) {
		return repository.save(telefoneUnidade);
	}
	
	/**
	 * Método que deleta um telefone na base por id 
	 * 
	 * @param id id do telefone
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que deleta telefones na base por unidade
	 * 
	 * @param id id da unidade
	 * 
	 */
	public void deletarByUnidade(Long id) {
		repository.deleteByUnidade(id);
	}
	
	/**
	 * Método que lista todos os telefones na base
	 * 
	 * @return List
	 * 
	 */
	public List<TelefoneUnidade> listarTodos(){
		return repository.listAll();
	}
	
	
}
