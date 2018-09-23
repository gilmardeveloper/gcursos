package com.gilmarcarlos.developer.gcursos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventosNaoEncontradosException;
import com.gilmarcarlos.developer.gcursos.model.eventos.listas.ListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.CertificadoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EstiloPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.PermissoesEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.Sobre;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.exceptions.CategoriaException;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialTop;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.categorias.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.CertificadoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.DiaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EstiloPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialLogService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.LogEvePresencialPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.PermissoesEventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.ProgramacaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.SobreService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.SexoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.NotificacaoUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

@Controller
@RequestMapping(UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL)
public class EventosPresencialAdminControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EventoPresencialService eventoPresencialService;

	@Autowired
	private CategoriaEventoService categoriaEventoService;

	@Autowired
	private ImagensService imagensService;

	@Autowired
	private SobreService sobreService;

	@Autowired
	private ProgramacaoPresencialService programacaoPresencialService;

	@Autowired
	private AtividadePresencialService atividadePresencialService;

	@Autowired
	private DiaEventoService diaEventoService;

	@Autowired
	private EventoPresencialLogService eventoPresencialLogService;

	@Autowired
	private DiaEventoPaginacaoService diaEventoPaginacaoService;

	@Autowired
	private EstiloPresencialService estiloPresencialService;

	@Autowired
	private LogEvePresencialPaginacaoService logEventoPresencialPaginacaoService;

	@Autowired
	private InscricaoPresencialService inscricaoPresencialService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private PermissoesEventoPresencialService permissoesEvePresencialService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private ListaPresenca listaPresenca;

	@Autowired
	private CertificadoPresencialService certificadoService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<DiaEvento> getDiaPaginacao(Long id, Integer page) {
		return diaEventoPaginacaoService.listarDiasPorProgramacao(id, PageRequest.of(page, MAXIMO_PAGES));
	}

	private Page<EventoPresencial> getEventoPaginacao(Integer page) {
		return eventoPresencialService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoPresencialLog> getLogEvePaginacao(Long id, Integer page) {
		return logEventoPresencialPaginacaoService.listarLogsPorEvento(id, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoPresencial> getEventoPresencialIdPaginacao(Long id) {
		return eventoPresencialService.buscarPor(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoPresencial> getEventoPresencialPeriodoPaginacao(LocalDate inicio, LocalDate termino)
			throws DataFinalMenorException, EventosNaoEncontradosException {
		Page<EventoPresencial> pages = eventoPresencialService.buscarPor(inicio, termino,
				PageRequest.of(0, MAXIMO_PAGES_EVENTOS));

		if (pages.getNumberOfElements() == 0) {
			throw new EventosNaoEncontradosException();
		}

		return pages;
	}
	
	private Page<EventoPresencial> getEventoPeriodoPaginacao(Usuario usuario, LocalDate inicio, LocalDate termino)
			throws DataFinalMenorException, EventosNaoEncontradosException {
		Page<EventoPresencial> pages = eventoPresencialService.buscarPor(usuario, inicio, termino,
				PageRequest.of(0, MAXIMO_PAGES_EVENTOS));

		if (pages.getNumberOfElements() == 0) {
			throw new EventosNaoEncontradosException();
		}

		return pages;
	}
	
	private Page<EventoPresencial> getEventoPaginacao(Usuario usuario, Integer page) {
		return eventoPresencialService.listarTodos(usuario, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPaginacao(usuarioLogado, 0));
		} else {
			model.addAttribute("eventos", getEventoPaginacao(0));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_EVENTO_PRESENCIAL;
	}

	@GetMapping({ "/pagina/{page}" })
	public String eventosPresencial(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPaginacao(usuarioLogado, page));
		} else {
			model.addAttribute("eventos", getEventoPaginacao(page));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_EVENTO_PRESENCIAL;
	}

	@GetMapping("/{id}")
	public String eventoPresencial(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPresencialIdPaginacao(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_EVENTO_PRESENCIAL;
	}

	@PostMapping("/periodo")
	public String eventoPresencial(@RequestParam("dataInicio") LocalDate dataInicio,
			@RequestParam("dataTermino") LocalDate dataTermino, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			
			if (usuarioLogado.temRestricao("responsabilidade")) {
				model.addAttribute("eventos", getEventoPeriodoPaginacao(usuarioLogado, dataInicio, dataTermino));
			} else {
				model.addAttribute("eventos", getEventoPresencialPeriodoPaginacao(dataInicio, dataTermino));
			}
			addBaseAttributes(model, usuarioLogado);

		} catch (DataFinalMenorException | EventosNaoEncontradosException e) {
			RedirectUtils.mensagemError(red, e.getMessage());
		}

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_EVENTO_PRESENCIAL;
	}

	@GetMapping("/novo")
	public String novo(Model model, RedirectAttributes red) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeCriar("eventosPresenciais") || !usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if(usuarioLogado.getDadosPessoais().getTelefones().isEmpty()) {
			RedirectUtils.mensagemError(red, "você precisa ter pelo menos um telefone cadastrado");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_EVENTO_PRESENCIAL;
	}

	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoPresencial evento, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("eventosPresenciais") || !usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {

			EventoPresencial novoEvento = eventoPresencialService.salvar(evento);

			if (evento.getProgramacao() == null) {

				eventoPresencialLogService.salvar(log("Evento presencial criado", novoEvento));
				ProgramacaoPresencial programacao = new ProgramacaoPresencial();
				programacao.setEventoPresencial(novoEvento);
				ProgramacaoPresencial novaProgramacao = programacaoPresencialService.salvar(programacao);
				diaEventoService.gerarDiasDeEvento(novoEvento, novaProgramacao);

			} else {
				eventoPresencialLogService.salvar(log("Evento presencial foi alterado", novoEvento));
				diaEventoService.alterarDiasDeEvento(novoEvento, novoEvento.getProgramacao());
			}

			RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		} catch (Exception e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;

	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeCriar("eventosPresenciais") || !usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_EVENTO_PRESENCIAL;
	}

	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoPresencialService.cancelar(id);
		eventoPresencialLogService
				.salvar(log("Evento presencial foi cancelado", eventoPresencialService.buscarPor(id)));

		RedirectUtils.mensagemSucesso(model, "Evento presencial foi cancelado com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;
	}

	@GetMapping("/ativar/{id}")
	public String ativar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoPresencialService.ativar(id);
		eventoPresencialLogService.salvar(log("Evento presencial foi ativado", eventoPresencialService.buscarPor(id)));
		RedirectUtils.mensagemSucesso(model, "evento ativado com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;

	}

	@GetMapping("/publicacao/ativar/{id}")
	public String ativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			eventoPresencialService.publicar(id);
			eventoPresencialLogService
					.salvar(log("Evento presencial foi publicado", eventoPresencialService.buscarPor(id)));

			RedirectUtils.mensagemSucesso(model, "evento publicado com sucesso");
		} catch (Exception e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;
	}

	@GetMapping("/publicacao/desativar/{id}")
	public String desativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoPresencialService.cancelarPublicacao(id);
		eventoPresencialLogService
				.salvar(log("A publicação do evento presencial foi removida", eventoPresencialService.buscarPor(id)));
		RedirectUtils.mensagemSucesso(model, "evento teve sua publicação desativada com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;
	}

	@GetMapping("/estilo/{id}")
	public String estilo(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_ESTILO_EVENTO_PRESENCIAL;
	}

	@PostMapping("/imagens/destaque/salvar")
	public String salvarImagensDestaque(@Valid ImagensEventoPresencialDestaque imagens, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {
			if (imagens.getEventoPresencial().getImagemDestaque() != null) {
				imagensService.deletarImagemEvePreDestaque(imagens.getEventoPresencial().getImagemDestaque().getId());
			}

			if (imagens != null) {
				imagensService.salvarImagemEvePresDestaque(imagens);
				eventoPresencialLogService
						.salvar(log("Imagem de destaque foi alterada", imagens.getEventoPresencial()));
			}
		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/estilo/"
				+ imagens.getEventoPresencial().getId();

	}

	@PostMapping("/imagens/destaque/deletar")
	public String deleatarImagensDestaque(@RequestParam("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEvePreDestaque(evento.getImagemDestaque().getId());
			eventoPresencialLogService.salvar(log("Imagem de destaque foi removida", evento));

		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/estilo/" + id;
	}

	@PostMapping("/estilo/destaque/salvar")
	public String salvarEstiloDestaque(EstiloPresencial estilo, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EstiloPresencial novoEstilo = estiloPresencialService.salvar(estilo);
		eventoPresencialLogService
				.salvar(log("Estilo da página de destaque foi alterado", novoEstilo.getEventoPresencial()));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/estilo/"
				+ novoEstilo.getEventoPresencial().getId();
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), 0));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_DETALHES_EVENTO_PRESENCIAL;
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), page));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_DETALHES_EVENTO_PRESENCIAL;
	}

	@GetMapping("/detalhes/imagens/top/deletar/{id}")
	public String deletarImagensTop(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEvePreTop(evento.getImagemTopDetalhes().getId());
			eventoPresencialLogService.salvar(log("Imagem do topo foi removida", evento));
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/" + id;

	}

	@PostMapping("/detalhes/imagens/top/salvar")
	public String salvarImagensTop(@Valid ImagensEventoPresencialTop imagens, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {

			if (imagens.getEventoPresencial().getImagemTopDetalhes() != null) {
				imagensService.deletarImagemEvePreTop(imagens.getEventoPresencial().getImagemTopDetalhes().getId());
			}

			imagensService.salvarImagemEvePresTop(imagens);
			eventoPresencialLogService.salvar(log("Imagem do topo foi alterada", imagens.getEventoPresencial()));

		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/"
				+ imagens.getEventoPresencial().getId();
	}

	@PostMapping("/detalhes/sobre/salvar")
	public String salvarSobre(Sobre sobre, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (sobre.getEventoPresencial().getSobre() != null) {
			sobreService.deletar(sobre.getEventoPresencial().getSobre().getId());
		}

		eventoPresencialLogService.salvar(log("Informações sobre o evento foi alterado", sobre.getEventoPresencial()));
		sobreService.salvar(sobre);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/"
				+ sobre.getEventoPresencial().getId();

	}

	@GetMapping("/detalhes/programacao/{id}")
	public String programacao(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		ProgramacaoPresencial programacao = programacaoPresencialService.buscarPor(id);

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", programacao.getEventoPresencial());
		model.addAttribute("dias", programacao.getDias());

		model.addAttribute("atividades", atividadePresencialService.buscarPorEvento(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_PROGRAMACAO_EVENTO_PRESENCIAL;
	}

	@PostMapping("/detalhes/atividades/salvar")
	public String atividadesSalvar(AtividadePresencial atividade, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {

			atividadePresencialService.salvar(atividade);
			eventoPresencialLogService.salvar(log("Atividade: " + atividade.getTitulo() + " foi alterada",
					atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

			RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

		} catch (Exception e) {

			RedirectUtils.mensagemError(model, e.getMessage());
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/programacao/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getId();
		}
	}

	@GetMapping("/detalhes/atividades/alterar/{id}")
	public String atividadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		model.addFlashAttribute("atividade", atividade);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/programacao/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getId();
	}

	@GetMapping("/detalhes/atividades/deletar/{id}")
	public String atividadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		eventoPresencialLogService.salvar(log("Atividade: " + atividade.getTitulo() + " foi deletada",
				atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		ProgramacaoPresencial programacao = atividade.getDiaEvento().getProgramacaoPresencial();
		atividadePresencialService.deletar(id);

		RedirectUtils.mensagemSucesso(model, "removido com sucesso");
		model.addFlashAttribute("atividade", atividade);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/detalhes/programacao/"
				+ programacao.getId();
	}

	@GetMapping("/logs/{id}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("logs", getLogEvePaginacao(id, 0));
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_LOGS_EVENTO_PRESENCIAL;
	}

	@GetMapping("/logs/{id}/deletar")
	public String eventoPresencialLogsDeletarTodos(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoPresencialService.buscarPor(id).getLogs().forEach(l -> eventoPresencialLogService.deletar(l.getId()));
		RedirectUtils.mensagemSucesso(model, "todos os logs foram deletados");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/logs/" + id;
	}

	@GetMapping("/logs/{id}/deletar/{log}")
	public String eventoPresencialLogsDeletar(@PathVariable("id") Long id, @PathVariable("log") Long log,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoPresencialLogService.deletar(log);
		RedirectUtils.mensagemSucesso(model, "log foi deletado com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/logs/" + id;
	}

	@GetMapping("/logs/{id}/pagina/{page}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("logs", getLogEvePaginacao(id, page));
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_LOGS_EVENTO_PRESENCIAL;
	}

	@GetMapping("/categorias")
	public String categorias(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("categorias")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_CATEGORIAS_EVENTO_PRESENCIAL;
	}

	@GetMapping("/categorias/novo")
	public String categoriasNovo(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeCriar("categorias") || !usuarioLogado.podeAlterar("categorias")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_CATEGORIAS_EVENTO_PRESENCIAL;
	}

	@PostMapping("/categorias/salvar")
	public String categoriasSalvar(CategoriaEvento categorias, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("categorias") || !usuarioLogado.podeAlterar("categorias")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categoria", categoriaEventoService.salvar(categorias));

		RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_CATEGORIAS_EVENTO_PRESENCIAL;
	}

	@GetMapping("/categorias/alterar/{id}")
	public String categoriasAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("categorias") || !usuarioLogado.podeAlterar("categorias")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		model.addFlashAttribute("categoria", categoriaEventoService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/categorias/novo";
	}

	@GetMapping("/categorias/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("categorias")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			categoriaEventoService.deletar(id);
			RedirectUtils.mensagemSucesso(model, "removido com sucesso");
		} catch (CategoriaException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/categorias";
	}

	@GetMapping("/permissoes/{id}")
	private String permissoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", eventoPresencialService.buscarPor(id));
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CADASTRO_PERMISSOES_EVENTO_PRESENCIAL;
	}

	@PostMapping("/permissoes/salvar")
	private String permissoesEventoPresencialSalvar(PermissoesEventoPresencial permissoes, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		permissoesEvePresencialService.salvar(permissoes);
		RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;
	}

	@GetMapping("/inscricoes/{id}")
	private String incricoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", eventoPresencialService.buscarPor(id));
		model.addAttribute("atividades", atividadePresencialService.buscarPorEvento(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_INFO_INSCRICOES_EVENTO_PRESENCIAL;
	}

	@GetMapping("/inscricoes/{id}/cancelar/inscricao")
	public String incricoesEventoCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("inscricoes")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua inscrição cancelada da atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		NotificacaoUtils.sucesso(notificacaoService, usuarioInscrito, "Inscrição cancelada",
				"Sua inscrição foi cancelada para a atividade " + atividade.getTitulo() + " por "
						+ usuarioLogado.getNome());

		NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Cancelou a inscrição do usuário",
				"Cancelou a inscrição do usuário com email: " + usuarioInscrito.getEmail() + ",  da atividade "
						+ atividade.getTitulo());

		inscricaoPresencialService.deletar(id);

		RedirectUtils.mensagemSucesso(model, "inscrição foi cancelada com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

	}

	@PostMapping("/inscricoes/lancar/presenca/todos")
	public String inscricoesEventoPresencaTodos(@RequestParam("id") Long id, @RequestParam("presenca") String presenca,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("presenca")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		if (presenca.equalsIgnoreCase("presentes")) {
			atividade.getInscricoes().forEach(i -> {
				inscricaoPresencialService.confirmarPresenca(i.getId(), true);

				eventoPresencialLogService.salvar(log(
						i.getUsuario().getNome() + " teve sua presença confirmada na atividade: "
								+ atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

				NotificacaoUtils.sucesso(notificacaoService, i.getUsuario(), "Presença confirmada",
						"Sua presença foi confirmada na atividade " + atividade.getTitulo());

			});
			RedirectUtils.mensagemSucesso(model, "presença confirmada para todos");
		} else {
			atividade.getInscricoes().forEach(i -> {
				inscricaoPresencialService.confirmarPresenca(i.getId(), false);

				eventoPresencialLogService.salvar(log(
						i.getUsuario().getNome() + " teve sua ausência confirmada na atividade: "
								+ atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

				NotificacaoUtils.sucesso(notificacaoService, i.getUsuario(), "Ausência confirmada",
						"Sua presença não foi confirmada na atividade " + atividade.getTitulo());

			});
			RedirectUtils.mensagemSucesso(model, "ausência foi confirmada para todos");
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

	}

	@GetMapping("/inscricoes/{id}/presenca/ausente")
	@ResponseBody
	public ResponseEntity<?> inscricoesEventoPresencaAusente(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("presenca")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua ausência confirmada na atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		NotificacaoUtils.sucesso(notificacaoService, usuarioInscrito, "Ausência confirmada",
				"Sua presença não foi confirmada na atividade " + atividade.getTitulo());

		inscricaoPresencialService.confirmarPresenca(id, false);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/inscricoes/{id}/presenca/presente")
	@ResponseBody
	public ResponseEntity<?> inscricoesEventoPresencaPresente(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("presenca")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua presença confirmada na atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		NotificacaoUtils.sucesso(notificacaoService, usuarioInscrito, "Presença confirmada",
				"Sua presença foi confirmada na atividade " + atividade.getTitulo());

		inscricaoPresencialService.confirmarPresenca(id, true);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/inscricoes/{id}/gerar/lista", produces = MediaType.APPLICATION_PDF_VALUE)
	private String gerarLista(@PathVariable("id") Long id, RedirectAttributes model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		EventoPresencial evento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

		if (evento.getImagemLogo() == null) {
			RedirectUtils.mensagemError(model, "você prescisa adiciona o titulo do cabeçalho e as logos");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/" + evento.getId();
		} else {
			if (!atividade.getInscricoes().isEmpty()) {
				return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/" + id
						+ "/gerar/lista-presenca";
			} else {
				RedirectUtils.mensagemError(model, "não existem inscritos para essa atividade");
				return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/" + evento.getId();
			}
		}

	}

	@GetMapping(value = "/inscricoes/{id}/gerar/lista-presenca", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaPresenca(@PathVariable("id") Long id, Model model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		InputStream pdfLista = listaPresenca.gerar(atividade);

		try {
			return IOUtils.toByteArray(pdfLista);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@PostMapping("/lista/presenca/logo/salvar")
	public String salvarImagensLogo(@Valid ImagensLogoListaPresenca imagens, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {
			if (imagens.getEventoPresencial().getImagemLogo() != null) {
				imagensService.deletarImagemLogListaPresenca(imagens.getEventoPresencial().getImagemLogo().getId());
			}
			imagensService.salvarImagemLogListaPresenca(imagens);
			eventoPresencialLogService.salvar(log("Imagem do topo foi alterada", imagens.getEventoPresencial()));
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/"
					+ imagens.getEventoPresencial().getId();
		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL;

		}
	}

	@GetMapping("/certificado/{id}")
	public String certificadoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL_BASE_CERTIFICADO_EVENTO_PRESENCIAL;
	}

	@PostMapping("/certificado/fundo/salvar")
	public String salvarImagensCertificado(@Valid CertificadoPresencial certificado, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {

			if (certificado.getId() != null) {
				certificadoService.atualizarImagemFundo(certificado);
			} else {
				certificadoService.salvar(certificado);
			}

		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/certificado/"
				+ certificado.getEventoPresencial().getId();
	}

	@PostMapping("/certificado/conteudo/salvar")
	public String salvarConteudoCertificado(CertificadoPresencial certificado, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		certificadoService.atualizarConteudo(certificado);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/certificado/"
				+ certificado.getEventoPresencial().getId();
	}

	@GetMapping(value = "/dto", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<EventoDTO> eventoDTO() {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.temRestricao("responsabilidade")) {
			return eventoPresencialService.listarTodosDTO(usuarioLogado);
		} else {
			return eventoPresencialService.listarTodosDTO();
		}
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

	private EventoPresencialLog log(String mensagem, EventoPresencial evento) {
		EventoPresencialLog eventoPresencialLog = new EventoPresencialLog();
		eventoPresencialLog.setData(LocalDate.now());
		eventoPresencialLog
				.setMsg(mensagem + " :: usuário: " + getUsuario().getNome() + " :: email: " + getUsuario().getEmail());
		eventoPresencialLog.setEventoPresencial(evento);
		return eventoPresencialLog;
	}

	private void addBaseAttributes(Model model, Usuario usuarioLogado) {
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("mensagens", usuarioLogado.getMensagensNaoLidas());
	}
}
