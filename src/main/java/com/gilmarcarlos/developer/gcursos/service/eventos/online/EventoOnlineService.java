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

/**
 * Classe com serviços de persistência para entidade (EventoOnline)
 * 
 * @author Gilmar Carlos
 * 
 */
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
	
	/**
	 * Método que remove uma publicação se o evento estiver fechado 
	 * 
	 * @param evento representa um evento
	 * 
	 */
	public void removerPublicacaoSeEstiverFechado(EventoOnline evento) {
		EventoOnline temp = repository.buscarPor(evento.getId());
		temp.desativarPublicacao();
		repository.save(temp);
	}
	
	/**
	 * Método que deleta um evento na base por id 
	 * 
	 * @param id id de um evento
	 * @throws EventosNaoEncontradosException se evento não existir
	 * @throws DeleteEventoException se houver inscrições
	 * 
	 */
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
	
	/**
	 * Método que lista todos os eventos
	 * 
	 * @return List
	 * 
	 */
	public List<EventoOnline> listarTodos() {
		return repository.listAll();
	}
	
	/**
	 * Método que lista todos os eventos com paginação
	 * 
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoOnline> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}
	
	/**
	 * Método que busca eventos por usuario e com paginação
	 * 
	 * @param usuario
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoOnline> listarTodos(Usuario usuario, Pageable pageable) {
		return repository.listarTodos(usuario.getId(), pageable);
	}
	
	/**
	 * Método que lista todos os eventos publicados
	 * 
	 * @return List
	 * 
	 */
	public List<EventoOnline> listarTodosPublicados() {
		return repository.findByPublicadoTrue();
	}
	
	/**
	 * Método que lista todos os eventos publicados com paginação
	 * 
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoOnline> listarTodosPublicados(Pageable pageable) {
		return repository.findByPublicadoTrue(pageable);
	}
	
	/**
	 * Método que busca um evento na base por id 
	 * 
	 * @param id 
	 * @return EventoOnline
	 * 
	 */
	public EventoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que cancela um evento na base por id 
	 * 
	 * @param id 
	 * @throws DeleteEventoException se houver inscrições
	 * 
	 */
	public void cancelar(Long id) throws DeleteEventoException {
		EventoOnline evento = buscarPor(id);

		if (!evento.getInscricoes().isEmpty()) {
			throw new DeleteEventoException("existem inscritos nesse evento");
		}

		evento.cancelarEvento();
		repository.save(evento);
	}
	
	/**
	 * Método que ativa um evento na base por id 
	 * 
	 * @param id 
	 * 
	 */
	public void ativar(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.ativarEvento();
		repository.save(evento);
	}
	
	/**
	 * Método que cancela a publicação de um evento na base por id 
	 * 
	 * @param id 
	 * 
	 */
	public void cancelarPublicacao(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.desativarPublicacao();
		repository.save(evento);
	}
	
	/**
	 * Método que publica um evento na base por id 
	 * 
	 * @param id 
	 * @throws EventoCanceladoException 
	 * 
	 */
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
	
	/**
	 * Método que valida os estatus de ativação de um evento 
	 * 
	 * @param eventoOnline 
	 * @param evento 
	 * 
	 */
	private void validarStatusPublicacao(EventoOnline eventoOnline, EventoOnline evento) {
		if (evento.isAtivo()) {
			eventoOnline.ativarEvento();
			validarPublicacao(eventoOnline, evento);
		} else {
			eventoOnline.cancelarEvento();
			eventoOnline.desativarPublicacao();
		}
	}
	
	/**
	 * Método que valida os estatus da publicação de um evento 
	 * 
	 * @param eventoOnline 
	 * @param evento 
	 * 
	 */
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
	
	/**
	 * Método que lista os eventos em uma classe auxiliar 
	 * 
	 * @return List
	 */
	public List<EventoDTO> listarTodosDTO() {
		List<EventoDTO> lista = new ArrayList<>();
		listarTodos().forEach(e -> lista.add(new EventoDTO(e)));
		return lista;
	}
	
	/**
	 * Método que lista os eventos em uma classe auxiliar por usuario responsavel
	 * 
	 * @return List
	 */
	public List<EventoDTO> listarTodosDTO(Usuario usuario) {
		List<EventoDTO> lista = new ArrayList<>();
		usuario.getEventoOnline().forEach(e -> lista.add(new EventoDTO(e)));
		return lista;
	}
	
	/**
	 * Método que valida um evento por opção sexual 
	 * 
	 * @param nome nome da opção sexual selecionada nas permissões
	 * @return boolean
	 */
	public boolean sexoExiste(String nome) {
		return listarTodos().stream().anyMatch(e -> e.getPermissoes().getSexos().contains(nome));
	}
	
	/**
	 * Método que busca eventos por usuario 
	 * 
	 * @param id id do usuario
	 * @param pageable
	 * 
	 * @return Page
	 */
	public Page<EventoOnline> buscarPorUsuario(Long id, Pageable pageable) {
		return repository.buscarPorUsuario(id, pageable);
	}
	
	/**
	 * Método que deleta modulo
	 * 
	 * @param id do modulo 
	 * @throws DeleteEventoException 
	 * 
	 */
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
		
		moduloService.deletar(id);
		
	}
	
	/**
	 * Método que deleta atividade
	 * 
	 * @param id da atividade 
	 * @throws DeleteEventoException 
	 * 
	 */
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
