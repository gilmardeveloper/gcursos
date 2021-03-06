package com.gilmarcarlos.developer.gcursos.service.notificacoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.notifications.Mensagens;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.notifcacoes.MensagensRepository;

/**
 * Classe com serviços de persistência para entidade (Mensagens) 
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class MensagensService {

	@Autowired
	private MensagensRepository repository;

	public Mensagens salvar(Mensagens mensagens) {
		return repository.save(mensagens);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<Mensagens> listarTodos() {
		return repository.listAll();
	}

	public Mensagens buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void foiLida(Mensagens mensagens) {
		mensagens.setFoiLido(true);
		salvar(mensagens);
	}

	public void deletar(Usuario usuario) {
		
		if (!usuario.getMensagens().isEmpty()) {
			usuario.getMensagens().forEach(m -> deletar(m.getId()));
		}
		
		listarTodos().stream().filter( m -> m.getDestinatario().equals(usuario)).forEach( m -> deletar(m.getId()));
	}

}
