package com.gilmarcarlos.developer.gcursos.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DeleteEventoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.CertificadoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EstiloOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnlineLog;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.ModuloDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.OrdenarHelper;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.SobreOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.exceptions.PosicaoExisteException;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.categorias.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.AtividadeOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.CertificadoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EstiloOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineLogService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineAtividadeService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineModuloService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.ModuloService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.SobreOnlineService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.SexoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

@Controller
@RequestMapping(UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE)
public class EventosOnlineAdminControler {

	@Autowired
	private CategoriaEventoService categoriaEventoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EventoOnlineLogService logEventoOnlineService;

	@Autowired
	private EventoOnlineService eventoOnlineService;

	@Autowired
	private SobreOnlineService sobreService;

	@Autowired
	private EstiloOnlineService estiloOnlineService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private PermissoesEventoOnlineService permissoesEveOnlineService;

	@Autowired
	private ModuloService moduloService;

	@Autowired
	private AtividadeOnlineService atividadeService;

	@Autowired
	private ImagensService imagensService;

	@Autowired
	private InscricaoOnlineService inscricaoService;

	@Autowired
	private InscricaoOnlineModuloService inscricaoModuloService;

	@Autowired
	private InscricaoOnlineAtividadeService inscricaoAtividadeService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private CertificadoOnlineService certificadoService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<EventoOnline> getEventoPaginacao(Integer page) {
		return eventoOnlineService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoOnline> getEventoPaginacao(Usuario usuario, Integer page) {
		return eventoOnlineService.listarTodos(usuario, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoOnlineLog> getLogEvePaginacao(Long id, Integer page) {
		return logEventoOnlineService.listarLogsPorEvento(id, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoOnline> getEventoOnlineIdPaginacao(Long id) {
		return eventoOnlineService.buscarPor(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosOnline(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPaginacao(usuarioLogado, 0));
		} else {
			model.addAttribute("eventos", getEventoPaginacao(0));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_EVENTO_ONLINE;

	}

	@GetMapping({ "/pagina/{page}" })
	public String eventosOnline(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPaginacao(usuarioLogado, page));
		} else {
			model.addAttribute("eventos", getEventoPaginacao(page));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_EVENTO_ONLINE;
	}

	@GetMapping("/{id}")
	public String eventoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("eventoOpcoes", eventoOnlineService.listarTodos());
		model.addAttribute("eventos", getEventoOnlineIdPaginacao(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_EVENTO_ONLINE;
	}

	@GetMapping("/novo")
	public String novo(Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeCriar("eventosOnline") || !usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if(usuarioLogado.getDadosPessoais().getTelefones().isEmpty()) {
			RedirectUtils.mensagemError(red, "você precisa ter pelo menos um telefone cadastrado");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("categorias", categoriaEventoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_CADASTRO_EVENTO_ONLINE;
	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeCriar("eventosOnline") || !usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_CADASTRO_EVENTO_ONLINE;
	}

	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoOnline evento, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("eventosOnline") || !usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline novoEvento = eventoOnlineService.salvar(evento);
		logEventoOnlineService.salvar(log("Evento online criado", novoEvento));

		RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;

	}

	@GetMapping("/estilo/{id}")
	public String estilo(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_ESTILO_EVENTO_ONLINE;
	}

	@PostMapping("/imagens/destaque/salvar")
	public String salvarImagensDestaque(@Valid ImagensEventoOnlineDestaque imagens, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {
			if (imagens.getEventoOnline().getImagemDestaque() != null) {
				imagensService.deletarImagemEvePreDestaque(imagens.getEventoOnline().getImagemDestaque().getId());
			}

			if (imagens != null) {
				imagensService.salvarImagemEveOnlineDestaque(imagens);
				logEventoOnlineService.salvar(log("Imagem de destaque foi alterada", imagens.getEventoOnline()));
			}
		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/estilo/" + imagens.getEventoOnline().getId();
	}

	@PostMapping("/imagens/destaque/deletar")
	public String deletarImagensDestaque(@RequestParam("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEveOnlineDestaque(evento.getImagemDestaque().getId());
			logEventoOnlineService.salvar(log("Imagem de destaque foi removida", evento));

		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/estilo/" + id;
	}

	@PostMapping("/estilo/destaque/salvar")
	public String salvarEstiloDestaque(EstiloOnline estilo, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EstiloOnline novoEstilo = estiloOnlineService.salvar(estilo);
		logEventoOnlineService.salvar(log("Estilo da página de destaque foi alterado", novoEstilo.getEventoOnline()));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/estilo/"
				+ novoEstilo.getEventoOnline().getId();
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_DETALHES_EVENTO_ONLINE;
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", evento);

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_DETALHES_EVENTO_ONLINE;
	}

	@GetMapping("/detalhes/imagens/top/deletar/{id}")
	public String deletarImagensTop(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEveOnlineTop(evento.getImagemTopDetalhes().getId());
			logEventoOnlineService.salvar(log("Imagem do topo foi removida", evento));
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + id;

	}

	@PostMapping("/detalhes/imagens/top/salvar")
	public String salvarImagensTop(@Valid ImagensEventoOnlineTop imagens, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {
			if (imagens.getEventoOnline().getImagemTopDetalhes() != null) {
				imagensService.deletarImagemEveOnlineTop(imagens.getEventoOnline().getImagemTopDetalhes().getId());
			}
			imagensService.salvarImagemEveOnlineTop(imagens);
			logEventoOnlineService.salvar(log("Imagem do topo foi alterada", imagens.getEventoOnline()));
		} else {
			RedirectUtils.mensagemError(model, "imagem vazia ou arquivo não é uma imagem");
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + imagens.getEventoOnline().getId();
	}

	@PostMapping("/detalhes/sobre/salvar")
	public String salvarSobre(SobreOnline sobre, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (sobre.getEventoOnline().getSobre() != null) {
			sobreService.deletar(sobre.getEventoOnline().getSobre().getId());
		}

		logEventoOnlineService.salvar(log("Informações sobre o evento foi alterado", sobre.getEventoOnline()));
		sobreService.salvar(sobre);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + sobre.getEventoOnline().getId();

	}

	@GetMapping("/logs/{id}")
	public String eventoOnlineLogs(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("logs", getLogEvePaginacao(id, 0));
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_LOGS_EVENTO_ONLINE;
	}

	@GetMapping("/logs/{id}/deletar")
	public String eventoOnlineLogsDeletarTodos(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("logs")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoOnlineService.buscarPor(id).getLogs().forEach(l -> logEventoOnlineService.deletar(l.getId()));
		RedirectUtils.mensagemSucesso(model, "todos os logs foram deletados");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/logs/" + id;
	}

	@GetMapping("/logs/{id}/deletar/{log}")
	public String eventoOnlineLogsDeletar(@PathVariable("id") Long id, @PathVariable("log") Long log,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("logs")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		logEventoOnlineService.deletar(log);
		RedirectUtils.mensagemSucesso(model, "log foi deletado com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/logs/" + id;
	}

	@GetMapping("/logs/{id}/pagina/{page}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("logs", getLogEvePaginacao(id, page));
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_LOGS_EVENTO_ONLINE;
	}

	@GetMapping("/permissoes/{id}")
	private String permissoesEventoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", eventoOnlineService.buscarPor(id));
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_CADASTRO_PERMISSOES_EVENTO_ONLINE;
	}

	@PostMapping("/permissoes/salvar")
	private String permissoesEventoOnlineSalvar(PermissoesEventoOnline permissoes, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		permissoesEveOnlineService.salvar(permissoes);
		RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
	}

	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			eventoOnlineService.cancelar(id);
			logEventoOnlineService.salvar(log("Evento presencial foi cancelado", eventoOnlineService.buscarPor(id)));
			RedirectUtils.mensagemSucesso(model, "Evento online foi cancelado com sucesso");
		} catch (DeleteEventoException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
	}

	@GetMapping("/ativar/{id}")
	public String ativar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoOnlineService.ativar(id);
		logEventoOnlineService.salvar(log("Evento presencial foi ativado", eventoOnlineService.buscarPor(id)));
		RedirectUtils.mensagemSucesso(model, "evento ativado com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
	}

	@GetMapping("/publicacao/ativar/{id}")
	public String ativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			eventoOnlineService.publicar(id);
			logEventoOnlineService.salvar(log("Evento presencial foi publicado", eventoOnlineService.buscarPor(id)));
			RedirectUtils.mensagemSucesso(model, "evento publicado com sucesso");
		} catch (Exception e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
	}

	@GetMapping("/publicacao/desativar/{id}")
	public String desativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		eventoOnlineService.cancelarPublicacao(id);
		logEventoOnlineService
				.salvar(log("A publicação do evento presencial foi removida", eventoOnlineService.buscarPor(id)));
		RedirectUtils.mensagemSucesso(model, "evento teve sua publicação desativada com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
	}

	@PostMapping("/modulos/salvar")
	public String modulosSalvar(Modulo modulo, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			moduloService.salvar(modulo);
			logEventoOnlineService
					.salvar(log("Módulo: " + modulo.getTitulo() + " foi alterado", modulo.getEventoOnline()));

		} catch (PosicaoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + modulo.getEventoOnline().getId();
	}

	@PostMapping("/modulos/deletar")
	public String modulosDeletar(@RequestParam("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		Modulo modulo = moduloService.buscarPor(id);
		try {
			logEventoOnlineService
					.salvar(log("Módulo: " + modulo.getTitulo() + " foi excluído", modulo.getEventoOnline()));
			eventoOnlineService.deletarModulo(id);
		} catch (DeleteEventoException e) {
			logEventoOnlineService
			.salvar(log("Falha na tentativa de excluir o módulo: " + modulo.getTitulo(), modulo.getEventoOnline()));
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + modulo.getEventoOnline().getId();
	}

	@GetMapping("/modulos/{id}")
	public String modulos(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		Modulo modulo = moduloService.buscarPor(id);

		if (modulo.getAtividades().isEmpty()) {
			RedirectUtils.mensagemError(model, "você precisa adicionar atividades antes");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/"
					+ modulo.getEventoOnline().getId();
		} else {
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/modulos/" + id + "/atividade/"
					+ modulo.getAtividades().get(0).getPosicao();
		}

	}

	@PostMapping(value = "/modulos/ordenar")
	@ResponseBody
	public ResponseEntity<?> modulosOrdenar(@RequestBody List<OrdenarHelper> dados) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		moduloService.ordenar(dados);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/modulos/{id}/atividade/{posicao}")
	public String modulos(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		Modulo modulo = moduloService.buscarPor(id);
		AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() == posicao).findAny()
				.get();

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("modulo", modulo);
		model.addAttribute("atividade", atividade);
		model.addAttribute("evento", modulo.getEventoOnline());

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_DETALHES_MODULO_EVENTO_ONLINE;

	}

	@GetMapping("/modulos/{id}/proxima-atividade/{posicao}")
	public String proximaAtividade(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		Modulo modulo = moduloService.buscarPor(id);
		EventoOnline evento = modulo.getEventoOnline();

		if (posicao < modulo.getAtividades().size()) {

			AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() > posicao)
					.findFirst().get();
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/modulos/" + id + "/atividade/"
					+ atividade.getPosicao();

		} else {

			if (modulo.getPosicao() < evento.getModulos().size()) {
				Modulo proximoModulo = evento.getModulos().stream().filter(m -> m.getPosicao() > modulo.getPosicao())
						.findFirst().get();
				return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/modulos/" + proximoModulo.getId();
			} else {
				RedirectUtils.mensagemSucesso(model, "você finalizou esse evento");
				return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE;
			}

		}

	}

	@GetMapping("/atividades/{id}")
	public String atividades(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!usuarioLogado.podeVisualizar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (evento.getModulos().isEmpty()) {

			RedirectUtils.mensagemError(model, "você precisa adicionar um módulo antes");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/" + id;

		} else {

			addBaseAttributes(model, usuarioLogado);

			model.addAttribute("evento", evento);
			model.addAttribute("modulos", evento.getModulos());
			model.addAttribute("atividades", atividadeService.buscarPorEvento(id));

			return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_CADASTRO_ATIVIDADES_EVENTO_ONLINE;
		}
	}

	@PostMapping("/atividades/salvar")
	public String atividadesSalvar(AtividadeOnline atividade, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			atividadeService.salvar(atividade);
			logEventoOnlineService.salvar(log("Atividade: " + atividade.getTitulo() + " foi alterada",
					atividade.getModulo().getEventoOnline()));
			RedirectUtils.mensagemSucesso(model, "salvo com sucesso");
		} catch (PosicaoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/detalhes/"
				+ atividade.getModulo().getEventoOnline().getId();
	}

	@PostMapping(value = "/atividades/ordenar")
	@ResponseBody
	public ResponseEntity<?> atividadesOrdenar(@RequestBody List<OrdenarHelper> dados) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		atividadeService.ordenar(dados);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/atividades/alterar/{id}")
	public String atividadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadeOnline atividade = atividadeService.buscarPor(id);
		model.addFlashAttribute("atividade", atividade);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/atividades/"
				+ atividade.getModulo().getEventoOnline().getId();
	}

	@GetMapping("/atividades/deletar/{id}")
	public String atividadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadeOnline atividade = atividadeService.buscarPor(id);
		logEventoOnlineService.salvar(
				log("Atividade: " + atividade.getTitulo() + " foi deletada", atividade.getModulo().getEventoOnline()));
		EventoOnline evento = atividade.getModulo().getEventoOnline();
		
		try {
			eventoOnlineService.deletarAtividade(id);
			RedirectUtils.mensagemSucesso(model, "removido com sucesso");
		} catch (DeleteEventoException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}


		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/atividades/" + evento.getId();
	}

	@PostMapping("/atividades/conteudo/salvar")
	public String atividadeConteudoSalvar(@RequestParam("id") Long id, @RequestParam("conteudo") String conteudo,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		AtividadeOnline atividade = atividadeService.buscarPor(id);
		try {
			atividade.setConteudo(conteudo);
			atividadeService.salvar(atividade);
		} catch (PosicaoExisteException e) {
			RedirectUtils.mensagemSucesso(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/modulos/" + atividade.getModulo().getId()
				+ "/atividade/" + atividade.getPosicao();

	}

	@GetMapping(value = "/modulo/dto/{id}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ModuloDTO moduloDTO(@PathVariable("id") Long id) {
		return new ModuloDTO(moduloService.buscarPor(id));
	}

	@GetMapping(value = "/dto", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<EventoDTO> eventoDTO() {

		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.temRestricao("responsabilidade")) {
			return eventoOnlineService.listarTodosDTO(usuarioLogado);
		} else {
			return eventoOnlineService.listarTodosDTO();
		}
	}

	@GetMapping("/inscricoes/{id}")
	private String incricoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", eventoOnlineService.buscarPor(id));
		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_INFO_INSCRICOES_EVENTO_ONLINE;
	}

	@GetMapping("/inscricoes/{id}/cancelar/inscricao")
	public String incricoesEventoCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("inscricoes")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		InscricaoOnline inscricao = inscricaoService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		EventoOnline evento = inscricao.getEventoOnline();

		logEventoOnlineService.salvar(log(
				usuarioInscrito.getNome() + " teve sua inscrição cancelada do evento: " + evento.getTitulo(), evento));

		notificacaoService.salvar(new Notificacao(usuarioInscrito, "Inscrição cancelada", IconeTypeUtils.INFORMACAO,
				StatusTypeUtils.SUCESSO,
				"Sua inscrição foi cancelada para o evento " + evento.getTitulo() + " por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(usuarioLogado, "Cancelou a inscrição do usuário",
				IconeTypeUtils.INFORMACAO, StatusTypeUtils.SUCESSO, "Cancelou a inscrição do usuário com email: "
						+ usuarioInscrito.getEmail() + ",  do evento " + evento.getTitulo()));

		if (!inscricao.getAtividades().isEmpty()) {
			inscricao.getAtividades().forEach(a -> inscricaoAtividadeService.deletar(a.getId()));
		}

		if (!inscricao.getModulos().isEmpty()) {
			inscricao.getModulos().forEach(m -> inscricaoModuloService.deletar(m.getId()));
		}

		inscricaoService.deletar(id);
		RedirectUtils.mensagemSucesso(model, "inscrição foi cancelada com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/inscricoes/" + evento.getId();

	}

	@GetMapping("/certificado/{id}")
	public String certificadoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("evento", eventoOnlineService.buscarPor(id));

		return TemplateUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE_BASE_CERTIFICADO_EVENTO_ONLINE;
	}

	@PostMapping("/certificado/fundo/salvar")
	public String salvarImagensCertificado(@Valid CertificadoOnline certificado, BindingResult result,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!result.hasErrors()) {
			if (certificado.getId() != null) {
				certificadoService.atualizarImagemFundo(certificado);
			} else {
				certificadoService.salvar(certificado);
			}
		} else {
			RedirectUtils.mensagemSucesso(model, "imagem vazia ou arquivo não é uma imagem");
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/certificado/"
				+ certificado.getEventoOnline().getId();
	}

	@PostMapping("/certificado/conteudo/salvar")
	public String salvarConteudoCertificado(CertificadoOnline certificado, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("eventosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		certificadoService.atualizarConteudo(certificado);
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/certificado/"
				+ certificado.getEventoOnline().getId();
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

	private EventoOnlineLog log(String mensagem, EventoOnline evento) {
		EventoOnlineLog eventoOnlineLog = new EventoOnlineLog();
		eventoOnlineLog.setData(LocalDate.now());
		eventoOnlineLog
				.setMsg(mensagem + " :: usuário: " + getUsuario().getNome() + " :: email: " + getUsuario().getEmail());
		eventoOnlineLog.setEventoOnline(evento);
		return eventoOnlineLog;
	}

	private void addBaseAttributes(Model model, Usuario usuarioLogado) {
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("mensagens", usuarioLogado.getMensagensNaoLidas());
	}
}
