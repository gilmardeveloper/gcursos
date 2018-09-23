package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DeleteEventoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventosNaoEncontradosException;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.EventoOnlineRepository;
import com.gilmarcarlos.developer.gcursos.service.email.EmailService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

@Service
public class EventoOnlineService {

	@Autowired
	private EventoOnlineRepository repository;

	@Autowired
	private ModuloService moduloService;

	@Autowired
	private AtividadeOnlineService atividadeService;

	@Autowired
	private SobreOnlineService sobreService;

	@Autowired
	private PermissoesEventoOnlineService permissoesService;

	@Autowired
	private CertificadoOnlineService certificadoService;

	@Autowired
	private EstiloOnlineService estiloService;

	@Autowired
	private ImagensService imagensService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmailService emailService;

	public EventoOnline salvar(EventoOnline eventoOnline) {

		if (eventoOnline.getId() == null) {
			eventoOnline.ativarEvento();
			eventoOnline.desativarPublicacao();
		} else {
			EventoOnline evento = repository.buscarPor(eventoOnline.getId());
			validarStatusPublicacao(eventoOnline, evento);
		}

		return repository.save(eventoOnline);
	}

	public void removerPublicacaoSeEstiverFechado(EventoOnline evento) {
		EventoOnline temp = repository.buscarPor(evento.getId());
		temp.desativarPublicacao();
		repository.save(temp);
	}

	public void deletar(Long id) throws EventosNaoEncontradosException, DeleteEventoException {

		EventoOnline evento = buscarPor(id);

		if (evento == null) {
			throw new EventosNaoEncontradosException("esse evento não existe");
		}

		if (!evento.getInscricoes().isEmpty()) {
			throw new DeleteEventoException("existem inscritos nesse evento");
		}

		if (!evento.getModulos().isEmpty()) {

			evento.getModulos().forEach(m -> {

				for (AtividadeOnline a : m.getAtividades()) {
					atividadeService.deletar(a.getId());
				}

			});

			evento.getModulos().forEach(m -> moduloService.deletar(m.getId()));
		}

		if (evento.getSobre() != null) {
			sobreService.deletar(evento.getSobre().getId());
		}

		if (evento.getEstilo() != null) {
			estiloService.deletar(evento.getEstilo().getId());
		}

		if (evento.getPermissoes() != null) {
			permissoesService.deletar(evento.getPermissoes().getId());
		}

		if (evento.getCertificadoOnline() != null) {
			certificadoService.deletar(evento.getCertificadoOnline().getId());
		}

		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEveOnlineDestaque(evento.getImagemDestaque().getId());
		}

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEveOnlineTop(evento.getImagemTopDetalhes().getId());
		}

		repository.deleteById(id);
	}

	public List<EventoOnline> listarTodos() {
		return repository.listAll();
	}

	public Page<EventoOnline> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}

	public Page<EventoOnline> listarTodos(Usuario usuario, Pageable pageable) {
		return repository.listarTodos(usuario.getId(), pageable);
	}

	public List<EventoOnline> listarTodosPublicados() {
		return repository.findByPublicadoTrue();
	}

	public Page<EventoOnline> listarTodosPublicados(Pageable pageable) {
		return repository.findByPublicadoTrue(pageable);
	}

	public EventoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void cancelar(Long id) throws DeleteEventoException {
		EventoOnline evento = buscarPor(id);

		if (!evento.getInscricoes().isEmpty()) {
			throw new DeleteEventoException("existem inscritos nesse evento");
		}

		evento.cancelarEvento();
		repository.save(evento);
	}

	public void ativar(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.ativarEvento();
		repository.save(evento);
	}

	public void cancelarPublicacao(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.desativarPublicacao();
		repository.save(evento);
	}

	public void publicar(Long id) throws EventoCanceladoException {
		
		EventoOnline evento = buscarPor(id);

		if (evento.getPermissoes() == null) {
			throw new NullPointerException("O evento não tem permissões configuradas");
		}
		
		if(evento.isCertificado() && evento.getCertificadoOnline() == null) {
			throw new NullPointerException("O evento não tem um certificado configurado");
		}
		
		if(evento.getModulos().isEmpty()) {
			throw new NullPointerException("O evento não tem módulos cadastrados");
		}
		
		if(evento.getModulos().stream().anyMatch( m -> m.getAtividades().isEmpty())) {
			throw new NullPointerException("O evento não tem atividades cadastrados");
		}
		
		List<Usuario> usuarios = usuarioService.listarCadastrosCompletos().stream().filter( u -> evento.getPermissoes().valida(u)).collect(Collectors.toList());
		List<String> emails = new ArrayList<>();
		usuarios.forEach( u -> emails.add(u.getEmail()));
		
		String[] array = emails.stream().toArray(String[]::new );
		
		evento.ativarPublicacao();
		EventoOnline novoEvento = repository.save(evento);
		
		emailService.enviarNovoEvento(array, "/dashboard/eventos/online/detalhes/" + novoEvento.getId());
		
	}

	private void validarStatusPublicacao(EventoOnline eventoOnline, EventoOnline evento) {
		if (evento.isAtivo()) {
			eventoOnline.ativarEvento();
			validarPublicacao(eventoOnline, evento);
		} else {
			eventoOnline.cancelarEvento();
			eventoOnline.desativarPublicacao();
		}
	}

	private void validarPublicacao(EventoOnline eventoOnline, EventoOnline evento) {
		if (evento.isPublicado()) {
			try {
				eventoOnline.ativarPublicacao();
			} catch (Exception e) {
				eventoOnline.desativarPublicacao();
			}
		} else {
			eventoOnline.desativarPublicacao();
		}
	}

	public List<EventoOnline> buscarPorUsuario(Long id) {
		return repository.buscarPorUsuario(id);
	}

	public Page<EventoOnline> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}

	public List<EventoDTO> listarTodosDTO() {
		List<EventoDTO> lista = new ArrayList<>();
		listarTodos().forEach(e -> lista.add(new EventoDTO(e)));
		return lista;
	}

	public List<EventoDTO> listarTodosDTO(Usuario usuario) {
		List<EventoDTO> lista = new ArrayList<>();
		usuario.getEventoOnline().forEach(e -> lista.add(new EventoDTO(e)));
		return lista;
	}

	public boolean sexoExiste(String nome) {
		return listarTodos().stream().anyMatch(e -> e.getPermissoes().getSexos().contains(nome));
	}

	public Page<EventoOnline> buscarPorUsuario(Long id, Pageable pageable) {
		return repository.buscarPorUsuario(id, pageable);
	}

	public void deletarModulo(Long id) throws DeleteEventoException {

		Modulo modulo = moduloService.buscarPor(id);

		if (modulo == null) {
			throw new DeleteEventoException("módulo não existe");
		}

		EventoOnline evento = modulo.getEventoOnline();

		if (!evento.getInscricoes().isEmpty()) {
			throw new DeleteEventoException("existem inscritos no evento");
		}

		if (!modulo.getAtividades().isEmpty()) {
			modulo.getAtividades().forEach(a -> atividadeService.deletar(a.getId()));
		}

	}

	public void deletarAtividade(Long id) throws DeleteEventoException {

		AtividadeOnline atividade = atividadeService.buscarPor(id);

		if (atividade == null) {
			throw new DeleteEventoException("atividade não existe");
		}

		EventoOnline evento = atividade.getModulo().getEventoOnline();

		if (!evento.getInscricoes().isEmpty()) {
			throw new DeleteEventoException("existem inscritos no evento");
		}
		
		atividadeService.deletar(id);
	}

}
