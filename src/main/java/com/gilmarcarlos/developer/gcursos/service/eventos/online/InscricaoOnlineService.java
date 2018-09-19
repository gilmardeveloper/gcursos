package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.InscricaoOnlineRepository;

@Service
public class InscricaoOnlineService {

	@Autowired
	private InscricaoOnlineRepository repository;

	public InscricaoOnline salvar(InscricaoOnline inscricao) {
		return repository.save(inscricao);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<InscricaoOnline> listarTodos() {
		return repository.listAll();
	}

	public InscricaoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void deletar(Usuario usuario) {
		if (!usuario.getInscricoesOnline().isEmpty()) {
			usuario.getInscricoesOnline().forEach(i -> deletar(i.getId()));
		}
	}

}