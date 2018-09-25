package com.gilmarcarlos.developer.gcursos.service.notificacoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.notifcacoes.NotificacaoRepository;

/**
 * Classe com serviços de persistência para entidade (Notificacao) 
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoRepository repository;

	public Notificacao salvar(Notificacao notificacao) {
		return repository.save(notificacao);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<Notificacao> listarTodos() {
		return repository.listAll();
	}

	public Notificacao buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void foiLida(Notificacao notificacao) {
		notificacao.setFoiLido(true);
		salvar(notificacao);
	}

	public void deletar(Usuario usuario) {
		if (!usuario.getNotificacoes().isEmpty()) {
			usuario.getNotificacoes().forEach(n -> deletar(n.getId()));
		}
	}

}
