package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventosNaoEncontradosException;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.EventoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.InscricaoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.service.email.EmailService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;

/**
 * Classe com serviços de persistência para entidade (EventoPresencial)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class EventoPresencialService {

	@Autowired
	private EventoPresencialRepository repository;
	
	@Autowired
	private InscricaoPresencialRepository inscricoesRepository;
	
	@Autowired
	private NotificacaoService notificacoes;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private SobreService sobreService;
	
	@Autowired
	private PermissoesEventoPresencialService permissoesService;

	@Autowired
	private CertificadoPresencialService certificadoService;

	@Autowired
	private EstiloPresencialService estiloService;
	
	@Autowired
	private ImagensService imagensService;
	
	@Autowired
	private DiaEventoService diaService;
	
	@Autowired
	private AtividadePresencialService atividadeService;
	
	@Autowired
	private ProgramacaoPresencialService programacaoService;
	
	@Autowired
	private EmailService emailService;
	
	/**
	 * Método que salva um evento na base 
	 * 
	 * @param eventoPresencial representa um evento
	 * @return EventoPresencial
	 * @throws DataFinalMenorException se a data final for menor que a inicial
	 * 
	 */
	public EventoPresencial salvar(EventoPresencial eventoPresencial) throws DataFinalMenorException {

		if (eventoPresencial.getDataTermino().isBefore(eventoPresencial.getDataInicio())) {
			throw new DataFinalMenorException();
		}

		if (eventoPresencial.getId() == null) {
			eventoPresencial.ativarEvento();
			eventoPresencial.desativarPublicacao();
		} else {
			EventoPresencial evento = repository.buscarPor(eventoPresencial.getId());
			validarStatusPublicacao(eventoPresencial, evento);
		}

		return repository.save(eventoPresencial);
	}
	
	/**
	 * Método que remove uma publicação se o evento estiver fechado 
	 * 
	 * @param evento representa um evento
	 * 
	 */
	public void removerPublicacaoSeEstiverFechado(EventoPresencial evento) {
		EventoPresencial temp = repository.buscarPor(evento.getId());
		temp.desativarPublicacao();
		repository.save(temp);
	}
	
	/**
	 * Método que deleta um evento na base por id 
	 * 
	 * @param id id de um evento
	 * @throws EventosNaoEncontradosException se evento não existir
	 * 
	 */
	public void deletar(Long id) throws EventosNaoEncontradosException {
		
		EventoPresencial evento = buscarPor(id);
		
		if (evento == null) {
			throw new EventosNaoEncontradosException("esse evento não existe");
		}

		
		if(!evento.getProgramacao().getDias().isEmpty()) {
			
			evento.getProgramacao().getDias().forEach( d -> {
				
				if(!d.getAtividades().isEmpty()) {
					d.getAtividades().forEach(a -> atividadeService.deletar(a.getId()));
				}
				
				diaService.deletar(d.getId());
				
			});
		}
		
		programacaoService.deletar(evento.getProgramacao().getId());

		if (evento.getSobre() != null) {
			sobreService.deletar(evento.getSobre().getId());
		}

		if (evento.getEstilo() != null) {
			estiloService.deletar(evento.getEstilo().getId());
		}

		if (evento.getPermissoes() != null) {
			permissoesService.deletar(evento.getPermissoes().getId());
		}

		if (evento.getCertificadoPresencial() != null) {
			certificadoService.deletar(evento.getCertificadoPresencial().getId());
		}

		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEvePreDestaque(evento.getImagemDestaque().getId());
		}

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEvePreTop(evento.getImagemTopDetalhes().getId());
		}
		
		if (evento.getImagemLogo() != null) {
			imagensService.deletarImagemLogListaPresenca(evento.getImagemLogo().getId());
		}
		
		repository.deleteById(id);
	}
	
	/**
	 * Método que lista todos os eventos
	 * 
	 * @return List
	 * 
	 */
	public List<EventoPresencial> listarTodos() {

		removerPublicaoSeEstiverFechado();
		return repository.listAll();
	}
	
	/**
	 * Método que lista todos os eventos com paginação
	 * 
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoPresencial> listarTodos(Pageable pageable) {

		removerPublicaoSeEstiverFechado();
		return repository.listarTodos(pageable);
	}
	
	/**
	 * Método que busca eventos por id e com paginação
	 * 
	 * @param id
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoPresencial> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}
	
	/**
	 * Método que busca um evento na base por periodo 
	 * 
	 * @param inicio data de inicio
	 * @param termino data de termino
	 * @param pageable recursos de paginação
	 * @return Page
	 * @throws DataFinalMenorException se a data final for menor que a inicial
	 * 
	 */
	public Page<EventoPresencial> buscarPor(LocalDate inicio, LocalDate termino, Pageable pageable) throws DataFinalMenorException {
		if(termino.isBefore(inicio)) {
			throw new DataFinalMenorException();
		}
		return repository.buscarPor(inicio, termino, pageable);
	}
	
	/**
	 * Método que busca um evento na base por periodo e usuario responsavel pelo evento
	 * 
	 * @param usuario 
	 * @param inicio data de inicio
	 * @param termino data de termino
	 * @param pageable recursos de paginação
	 * @return Page
	 * @throws DataFinalMenorException se a data final for menor que a inicial
	 * 
	 */
	public Page<EventoPresencial> buscarPor(Usuario usuario, LocalDate inicio, LocalDate termino, Pageable pageable) throws DataFinalMenorException {
		if(termino.isBefore(inicio)) {
			throw new DataFinalMenorException();
		}
		return repository.buscarPor(usuario.getId(), inicio, termino, pageable);
	}
	
	/**
	 * Método que remove uma publicação de um evento se ele estiver fechado 
	 * 
	 * 
	 */
	private void removerPublicaoSeEstiverFechado() {
		for (EventoPresencial evento : repository.listAll()) {
			if (evento.isFechado() && evento.isPublicado()) {
				evento.desativarPublicacao();
				repository.save(evento);
			}
		}
	}
	
	/**
	 * Método que lista todos os eventos publicados
	 * 
	 * @return List
	 * 
	 */
	public List<EventoPresencial> listarTodosPublicados() {
		return repository.findByPublicadoTrue();
	}
	
	/**
	 * Método que lista todos os eventos publicados com paginação
	 * 
	 * @param pageable
	 * @return Page
	 * 
	 */
	public Page<EventoPresencial> listarTodosPublicados(Pageable pageable) {
		return repository.findByPublicadoTrue(pageable);
	}
	
	/**
	 * Método que busca um evento na base por id 
	 * 
	 * @param id 
	 * @return EventoPresencial
	 * 
	 */
	public EventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que busca uma lista de eventos na base por usuario 
	 * 
	 * @param id id do usuario
	 * @return List
	 * 
	 */
	public List<EventoPresencial> buscarPorUsuario(Long id) {
		return repository.buscarPorUsuario(id);
	}
	
	/**
	 * Método que cancela um evento na base por id 
	 * 
	 * @param id 
	 * 
	 */
	public void cancelar(Long id) {
		
		EventoPresencial evento = buscarPor(id);
		evento.cancelarEvento();
		repository.save(evento);
		
		if(!evento.getInscricoes().isEmpty()) {
			evento.getInscricoes().forEach( i -> {
				notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeTypeUtils.INFORMACAO, StatusTypeUtils.INFORMACAO, "sua inscrição foi cancelada, porque o evento foi cancelado ( " + i.getEventoPresencial().getTitulo() + " )"));
				inscricoesRepository.deleteById(i.getId());
			});
		}
		
	}
	
	/**
	 * Método que ativa um evento na base por id 
	 * 
	 * @param id 
	 * 
	 */
	public void ativar(Long id) {
		EventoPresencial evento = buscarPor(id);
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
		EventoPresencial evento = buscarPor(id);
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
		EventoPresencial evento = buscarPor(id);

		if (evento.getPermissoes() == null) {
			throw new NullPointerException("O evento não tem permissões configuradas");
		}
		
		if(evento.isCertificado() && evento.getCertificadoPresencial() == null) {
			throw new NullPointerException("O evento não tem um certificado configurado");
		}
		
		if(evento.getProgramacao().getDias().stream().anyMatch(d -> d.getAtividades().isEmpty())) {
			throw new NullPointerException("O evento não tem atividades cadastradas");
		}

		evento.ativarPublicacao();
		EventoPresencial novoEvento = repository.save(evento);
		
		List<Usuario> usuarios = usuarioService.listarCadastrosCompletos().stream().filter( u -> evento.getPermissoes().valida(u)).collect(Collectors.toList());
		List<String> emails = new ArrayList<>();
		usuarios.forEach( u -> emails.add(u.getEmail()));
		
		String[] array = emails.stream().toArray(String[]::new );
		
		emailService.enviarNovoEvento(array, "/dashboard/eventos/presenciais/detalhes/" + novoEvento.getId());

	}
	
	/**
	 * Método que valida os estatus de ativação de um evento 
	 * 
	 * @param eventoPresencial 
	 * @param evento 
	 * 
	 */
	private void validarStatusPublicacao(EventoPresencial eventoPresencial, EventoPresencial evento) {
		if (evento.isAtivo()) {
			eventoPresencial.ativarEvento();
			validarPublicacao(eventoPresencial, evento);
		} else {
			eventoPresencial.cancelarEvento();
			eventoPresencial.desativarPublicacao();
		}
	}
	
	/**
	 * Método que valida os estatus da publicação de um evento 
	 * 
	 * @param eventoPresencial 
	 * @param evento 
	 * 
	 */
	private void validarPublicacao(EventoPresencial eventoPresencial, EventoPresencial evento) {
		if (evento.isPublicado()) {
			try {
				eventoPresencial.ativarPublicacao();
			} catch (Exception e) {
				eventoPresencial.desativarPublicacao();
			}
		} else {
			eventoPresencial.desativarPublicacao();
		}
	}

	/**
	 * Método que lista os eventos em uma classe auxiliar 
	 * 
	 * @return List
	 */
	public List<EventoDTO> listarTodosDTO() {
		List<EventoDTO> lista = new ArrayList<>();
		listarTodos().forEach( e -> lista.add(new EventoDTO(e)));
		return lista;
	}
	
	/**
	 * Método que lista os eventos em uma classe auxiliar por usuario responsavel
	 * 
	 * @return List
	 */
	public List<EventoDTO> listarTodosDTO(Usuario usuario) {
		List<EventoDTO> lista = new ArrayList<>();
		usuario.getEventoPresencial().forEach( e -> lista.add(new EventoDTO(e)));
		return lista;
	}
	
	/**
	 * Método que valida um evento por opção sexual 
	 * 
	 * @param nome nome da opção sexual selecionada nas permissões
	 * @return boolean
	 */
	public boolean sexoExiste(String nome) {
		return listarTodos().stream().anyMatch( e -> e.getPermissoes().getSexos().contains(nome));
	}
	
	/**
	 * Método que busca eventos por usuario 
	 * 
	 * @param id id do usuario
	 * @param pageable
	 * 
	 * @return Page
	 */
	public Page<EventoPresencial> buscarPorUsuario(Long id, Pageable pageable) {
		return repository.buscarPorUsuario(id, pageable);
	}
	
	/**
	 * Método que busca eventos por usuario responsavel
	 * 
	 * @param usuario 
	 * @param pageable
	 * 
	 * @return Page
	 */
	public Page<EventoPresencial> listarTodos(Usuario usuario, Pageable pageable) {
		return repository.buscarPorResponsavel(usuario.getId(), pageable);
	}

	

}
