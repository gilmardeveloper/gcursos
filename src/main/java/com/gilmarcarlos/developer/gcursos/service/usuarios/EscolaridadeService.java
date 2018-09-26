package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.EscolaridadeExisteException;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.EscolaridadeRepository;

/**
 * Classe com serviços de persistência para entidade (Escolaridade)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class EscolaridadeService {

	@Autowired
	private EscolaridadeRepository repository;

	@Autowired
	private DadosPessoaisService dadosService;

	/**
	 * Método que salva uma opção na base
	 * 
	 * @param escolaridade
	 *            entidade que representa uma opção
	 * @return Escolaridade retorna uma escolaridade
	 * @throws EscolaridadeExisteException
	 *             se essa opção estiver presente nas permissões, no cadastro de
	 *             usuários ou eventos
	 * 
	 */
	public Escolaridade salvar(Escolaridade escolaridade) throws EscolaridadeExisteException {

		if (escolaridade.getId() != null) {
			if (dadosService.escolaridadeExiste(buscarPor(escolaridade.getId()).getNome())) {
				throw new EscolaridadeExisteException(
						"para alterar essa opção é necessário antes alterar os usuários selecionados com essa opção");
			}
		}
		return repository.save(escolaridade);
	}

	/**
	 * Método que deleta uma opção na base por id
	 * 
	 * @param id
	 *            entidade que representa uma opção
	 * @throws EscolaridadeExisteException
	 *             se essa opção estiver presente nas permissões, no cadastro de
	 *             usuários ou eventos
	 * 
	 */
	public void deletar(Long id) throws EscolaridadeExisteException {

		Escolaridade escolaridade = buscarPor(id);

		if (escolaridade == null) {
			throw new EscolaridadeExisteException("opção não foi encontrada");
		}

		if (dadosService.escolaridadeExiste(escolaridade.getNome())) {
			throw new EscolaridadeExisteException(
					"para deletar essa opção é necessário antes alterar os usuários selecionados com essa opção");
		}

		repository.deleteById(id);
	}

	/**
	 * Método que retorna uma lista com todas as opções
	 * 
	 * @return List retorna uma lista
	 * 
	 */
	public List<Escolaridade> listarTodos() {
		return repository.listAll();
	}

	/**
	 * Método que busca uma opção na base por id
	 * 
	 * @param id
	 *            id de uma opção
	 * @return Escolaridade retorna uma escolaridade
	 * 
	 */
	public Escolaridade buscarPor(Long id) {
		return repository.buscarPor(id);
	}

}
