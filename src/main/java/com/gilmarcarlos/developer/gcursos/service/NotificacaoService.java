package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.hibernate.dialect.SAPDBDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.repository.NotificacaoRepository;

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
			
	public List<Notificacao> listarTodos(){
		return repository.listAll();
	}

	public Notificacao buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public void foiLida(Notificacao notificacao) {
		notificacao.setFoiLido(true);
		salvar(notificacao);
	}
	
	
}
