package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.repository.locais.CodigoFuncionalRepository;

/**
 * Classe com serviços de persistência para entidade (CodigoFuncional)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class CodigoFuncionalService {

	@Autowired
	private CodigoFuncionalRepository repository;
	
	/**
	 * Método que salva um codigo na base 
	 * 
	 * @param codigoFuncional representa um codigo funcional
	 * @return CodigoFuncional 
	 * 
	 */
	public CodigoFuncional salvar(CodigoFuncional codigoFuncional) {
		return repository.save(codigoFuncional);
	}
	
	/**
	 * Método que deleta um codigo na base por id 
	 * 
	 * @param id id de um codigo funcional
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que lista todos os codigos 
	 * 
	 * @return List
	 * 
	 */
	public List<CodigoFuncional> listarTodos(){
		return repository.listAll();
	}

	/**
	 * Método que busca um codigo na base por codigo 
	 * 
	 * @param codigoFuncional representa um codigo funcional
	 * @return CodigoFuncional
	 */
	public CodigoFuncional buscarPor(String codigoFuncional) {
		return repository.buscarPor(codigoFuncional);
	}
	
	/**
	 * Método que busca um codigo na base por dados pessoais
	 * 
	 * @param dadosPessoais representa um dado pessoal
	 * @return CodigoFuncional
	 */
	public CodigoFuncional buscarPor(DadosPessoais dadosPessoais) {
		return repository.buscarPor(dadosPessoais.getId());
	}
	
	/**
	 * Método que valida um codigo na base por unidade
	 * 
	 * @param unidade representa uma unidade
	 * @return boolean
	 */
	public boolean temUnidade(UnidadeTrabalho unidade) {
		return listarTodos().stream().anyMatch( c -> c.getUnidadeTrabalho().equals(unidade));
	}
	
	/**
	 * Método que valida um codigo na base por cargo
	 * 
	 * @param cargo representa um cargo
	 * @return boolean
	 */
	public boolean temCargo(Cargo cargo) {
		return listarTodos().stream().anyMatch( c -> c.getCargo().equals(cargo) );
	}

	
}
