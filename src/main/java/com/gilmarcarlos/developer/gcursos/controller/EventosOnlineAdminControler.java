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
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.categorias.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.AtividadeOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.CertificadoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EstiloOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineLogService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
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

@Controller
@RequestMapping("/dashboard/admin/eventos/online")
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
	private NotificacaoService notificacaoService;
	
	@Autowired
	private CertificadoOnlineService certificadoService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<EventoOnline> getEventoPaginacao(Integer page) {
		return eventoOnlineService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoOnlineLog> getLogEvePaginacao(Long id, Integer page) {
		return logEventoOnlineService.listarLogsPorEvento(id, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosOnline(Model model) {

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("eventos", getEventoPaginacao(0));
			return "dashboard/admin/eventos/online/base-info-evento-online";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@GetMapping({ "/pagina/{page}" })
	public String eventosOnline(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("eventos", getEventoPaginacao(page));
			return "dashboard/admin/eventos/online/base-info-evento-online";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());

			return "dashboard/admin/eventos/online/base-cadastro-evento-online";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());
			model.addAttribute("evento", eventoOnlineService.buscarPor(id));
			return "dashboard/admin/eventos/online/base-cadastro-evento-online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoOnline evento, RedirectAttributes model) {

		EventoOnline novoEvento = eventoOnlineService.salvar(evento);
		logEventoOnlineService.salvar(log("Evento online criado", novoEvento));

		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/eventos/online";

	}

	@GetMapping("/estilo/{id}")
	public String estilo(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());
			model.addAttribute("evento", eventoOnlineService.buscarPor(id));
			return "dashboard/admin/eventos/online/base-estilo-evento-online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/imagens/destaque/salvar")
	public String salvarImagensDestaque(@Valid ImagensEventoOnlineDestaque imagens, BindingResult result,
			RedirectAttributes model) {

		if (!result.hasErrors()) {
			if (imagens.getEventoOnline().getImagemDestaque() != null) {
				imagensService.deletarImagemEvePreDestaque(imagens.getEventoOnline().getImagemDestaque().getId());
			}

			if (imagens != null) {
				imagensService.salvarImagemEveOnlineDestaque(imagens);
				logEventoOnlineService.salvar(log("Imagem de destaque foi alterada", imagens.getEventoOnline()));
			}
			return "redirect:/dashboard/admin/eventos/online/estilo/" + imagens.getEventoOnline().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/online/estilo/" + imagens.getEventoOnline().getId();
		}
	}

	@PostMapping("/imagens/destaque/deletar")
	public String deleatarImagensDestaque(@RequestParam("id") Long id, RedirectAttributes model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEveOnlineDestaque(evento.getImagemDestaque().getId());
			logEventoOnlineService.salvar(log("Imagem de destaque foi removida", evento));

		}

		return "redirect:/dashboard/admin/eventos/online/estilo/" + id;
	}

	@PostMapping("/estilo/destaque/salvar")
	public String salvarEstiloDestaque(EstiloOnline estilo, RedirectAttributes model) {

		EstiloOnline novoEstilo = estiloOnlineService.salvar(estilo);
		logEventoOnlineService.salvar(log("Estilo da página de destaque foi alterado", novoEstilo.getEventoOnline()));
		return "redirect:/dashboard/admin/eventos/online/estilo/" + novoEstilo.getEventoOnline().getId();
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);

		return "dashboard/admin/eventos/online/base-detalhes-evento-online";
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);

		return "dashboard/admin/eventos/online/base-detalhes-evento-online";
	}

	@GetMapping("/detalhes/imagens/top/deletar/{id}")
	public String deletarImagensTop(@PathVariable("id") Long id, RedirectAttributes model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEveOnlineTop(evento.getImagemTopDetalhes().getId());
			logEventoOnlineService.salvar(log("Imagem do topo foi removida", evento));
		}

		return "redirect:/dashboard/admin/eventos/online/detalhes/" + id;

	}

	@PostMapping("/detalhes/imagens/top/salvar")
	public String salvarImagensTop(@Valid ImagensEventoOnlineTop imagens, BindingResult result,
			RedirectAttributes model) {

		if (!result.hasErrors()) {
			if (imagens.getEventoOnline().getImagemTopDetalhes() != null) {
				imagensService.deletarImagemEveOnlineTop(imagens.getEventoOnline().getImagemTopDetalhes().getId());
			}
			imagensService.salvarImagemEveOnlineTop(imagens);
			logEventoOnlineService.salvar(log("Imagem do topo foi alterada", imagens.getEventoOnline()));
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + imagens.getEventoOnline().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + imagens.getEventoOnline().getId();

		}
	}

	@PostMapping("/detalhes/sobre/salvar")
	public String salvarSobre(SobreOnline sobre, RedirectAttributes model) {

		if (sobre.getEventoOnline().getSobre() != null) {
			sobreService.deletar(sobre.getEventoOnline().getSobre().getId());
		}

		logEventoOnlineService.salvar(log("Informações sobre o evento foi alterado", sobre.getEventoOnline()));
		sobreService.salvar(sobre);
		return "redirect:/dashboard/admin/eventos/online/detalhes/" + sobre.getEventoOnline().getId();

	}

	@GetMapping("/logs/{id}")
	public String eventoOnlineLogs(@PathVariable("id") Long id, Model model) {

		model.addAttribute("usuario", getUsuario());
		model.addAttribute("logs", getLogEvePaginacao(id, 0));
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		return "dashboard/admin/eventos/online/base-info-logs-evento-online";
	}

	@GetMapping("/logs/{id}/deletar")
	public String eventoOnlineLogsDeletarTodos(@PathVariable("id") Long id, RedirectAttributes model) {
		eventoOnlineService.buscarPor(id).getLogs().forEach(l -> logEventoOnlineService.deletar(l.getId()));
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "todos os logs foram deletados");
		return "redirect:/dashboard/admin/eventos/online/logs/" + id;
	}

	@GetMapping("/logs/{id}/deletar/{log}")
	public String eventoOnlineLogsDeletar(@PathVariable("id") Long id, @PathVariable("log") Long log,
			RedirectAttributes model) {
		logEventoOnlineService.deletar(log);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "log foi deletado com sucesso");
		return "redirect:/dashboard/admin/eventos/online/logs/" + id;
	}

	@GetMapping("/logs/{id}/pagina/{page}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		model.addAttribute("usuario", getUsuario());
		model.addAttribute("logs", getLogEvePaginacao(id, page));
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		return "dashboard/admin/eventos/online/base-info-logs-evento-online";
	}

	@GetMapping("/permissoes/{id}")
	private String permissoesEventoOnline(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
			model.addAttribute("evento", eventoOnlineService.buscarPor(id));
			model.addAttribute("unidades", unidadeService.listarTodos());
			model.addAttribute("escolaridades", escolaridadeService.listarTodos());
			model.addAttribute("sexos", sexoService.listarTodos());
			model.addAttribute("cargos", cargoService.listarTodos());
			return "dashboard/admin/eventos/online/base-cadastro-permissoes-evento-online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/permissoes/salvar")
	private String permissoesEventoOnlineSalvar(PermissoesEventoOnline permissoes, RedirectAttributes model) {

		permissoesEveOnlineService.salvar(permissoes);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/eventos/online";
	}

	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoOnlineService.cancelar(id);
			logEventoOnlineService.salvar(log("Evento presencial foi cancelado", eventoOnlineService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "Evento online foi cancelado com sucesso");
			return "redirect:/dashboard/admin/eventos/online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/ativar/{id}")
	public String ativar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoOnlineService.ativar(id);
			logEventoOnlineService.salvar(log("Evento presencial foi ativado", eventoOnlineService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "evento ativado com sucesso");
			return "redirect:/dashboard/admin/eventos/online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/publicacao/ativar/{id}")
	public String ativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			try {
				eventoOnlineService.publicar(id);
				logEventoOnlineService
						.salvar(log("Evento presencial foi publicado", eventoOnlineService.buscarPor(id)));
				model.addFlashAttribute("alert", "alert alert-fill-success");
				model.addFlashAttribute("message", "evento publicado com sucesso");
				return "redirect:/dashboard/admin/eventos/online";
			} catch (Exception e) {

				model.addFlashAttribute("alert", "alert alert-fill-danger");
				model.addFlashAttribute("message", e.getMessage());
				return "redirect:/dashboard/admin/eventos/online";
			}
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/publicacao/desativar/{id}")
	public String desativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoOnlineService.cancelarPublicacao(id);
			logEventoOnlineService
					.salvar(log("A publicação do evento presencial foi removida", eventoOnlineService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "evento teve sua publicação desativada com sucesso");
			return "redirect:/dashboard/admin/eventos/online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/modulos/salvar")
	public String modulosSalvar(Modulo modulo, RedirectAttributes model) {

		try {
			moduloService.salvar(modulo);
			logEventoOnlineService
					.salvar(log("Módulo: " + modulo.getTitulo() + " foi alterado", modulo.getEventoOnline()));
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + modulo.getEventoOnline().getId();

		} catch (PosicaoExisteException e) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", e.getMessage());
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + modulo.getEventoOnline().getId();
		}
	}

	@PostMapping("/modulos/deletar")
	public String modulosDeletar(@RequestParam("id") Long id, RedirectAttributes model) {

		Modulo modulo = moduloService.buscarPor(id);
		EventoOnline evento = modulo.getEventoOnline();

		if (!modulo.getAtividades().isEmpty()) {
			modulo.getAtividades().forEach(a -> atividadeService.deletar(a.getId()));
		}

		logEventoOnlineService.salvar(log("Módulo: " + modulo.getTitulo() + " foi excluído", modulo.getEventoOnline()));
		moduloService.deletar(id);

		return "redirect:/dashboard/admin/eventos/online/detalhes/" + evento.getId();
	}

	@GetMapping("/modulos/{id}")
	public String modulos(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);

		if (modulo.getAtividades().isEmpty()) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você precisa adicionar atividades antes");
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + modulo.getEventoOnline().getId();
		} else {
			return "redirect:/dashboard/admin/eventos/online/modulos/" + id + "/atividade/"
					+ modulo.getAtividades().get(0).getPosicao();
		}

	}

	@PostMapping(value = "/modulos/ordenar")
	@ResponseBody
	public ResponseEntity<?> modulosOrdenar(@RequestBody List<OrdenarHelper> dados) {
		moduloService.ordenar(dados);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/modulos/{id}/atividade/{posicao}")
	public String modulos(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);
		AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() == posicao).findAny()
				.get();

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("modulo", modulo);
		model.addAttribute("atividade", atividade);
		model.addAttribute("evento", modulo.getEventoOnline());

		return "dashboard/admin/eventos/online/base-detalhes-modulo-evento-online";

	}

	@GetMapping("/modulos/{id}/proxima-atividade/{posicao}")
	public String proximaAtividade(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);
		EventoOnline evento = modulo.getEventoOnline();

		if (posicao < modulo.getAtividades().size()) {

			AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() > posicao)
					.findFirst().get();
			return "redirect:/dashboard/admin/eventos/online/modulos/" + id + "/atividade/" + atividade.getPosicao();

		} else {

			if (modulo.getPosicao() < evento.getModulos().size()) {
				Modulo proximoModulo = evento.getModulos().stream().filter(m -> m.getPosicao() > modulo.getPosicao())
						.findFirst().get();
				return "redirect:/dashboard/admin/eventos/online/modulos/" + proximoModulo.getId();
			} else {
				red.addFlashAttribute("alert", "alert alert-fill-success");
				red.addFlashAttribute("message", "você finalizou esse evento");
				return "redirect:/dashboard/admin/eventos/online";
			}

		}

	}

	@GetMapping("/atividades/{id}")
	public String atividades(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (evento.getModulos().isEmpty()) {

			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você precisa adicionar um módulo antes");
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + id;

		} else {

			Usuario usuarioLogado = getUsuario();
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("evento", evento);
			model.addAttribute("modulos", evento.getModulos());
			model.addAttribute("atividades", atividadeService.buscarPorEvento(id));
			model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());

			return "dashboard/admin/eventos/online/base-cadastro-atividades-evento-online";
		}
	}

	@PostMapping("/atividades/salvar")
	public String atividadesSalvar(AtividadeOnline atividade, RedirectAttributes model) {

		try {
			atividadeService.salvar(atividade);
			logEventoOnlineService.salvar(log("Atividade: " + atividade.getTitulo() + " foi alterada",
					atividade.getModulo().getEventoOnline()));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "salvo com sucesso");
			return "redirect:/dashboard/admin/eventos/online/detalhes/"
					+ atividade.getModulo().getEventoOnline().getId();
		} catch (PosicaoExisteException e) {
			logEventoOnlineService.salvar(log("Atividade: " + atividade.getTitulo() + " foi alterada",
					atividade.getModulo().getEventoOnline()));
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", e.getMessage());
			return "redirect:/dashboard/admin/eventos/online/detalhes/"
					+ atividade.getModulo().getEventoOnline().getId();
		}

	}

	@PostMapping(value = "/atividades/ordenar")
	@ResponseBody
	public ResponseEntity<?> atividadesOrdenar(@RequestBody List<OrdenarHelper> dados) {

		atividadeService.ordenar(dados);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/atividades/alterar/{id}")
	public String atividadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		AtividadeOnline atividade = atividadeService.buscarPor(id);
		model.addFlashAttribute("atividade", atividade);
		return "redirect:/dashboard/admin/eventos/online/atividades/" + atividade.getModulo().getEventoOnline().getId();
	}

	@GetMapping("/atividades/deletar/{id}")
	public String atividadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		AtividadeOnline atividade = atividadeService.buscarPor(id);
		logEventoOnlineService.salvar(
				log("Atividade: " + atividade.getTitulo() + " foi deletada", atividade.getModulo().getEventoOnline()));
		EventoOnline evento = atividade.getModulo().getEventoOnline();
		atividadeService.deletar(id);

		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "removido com sucesso");
		model.addFlashAttribute("atividade", atividade);

		return "redirect:/dashboard/admin/eventos/online/atividades/" + evento.getId();
	}

	@PostMapping("/atividades/conteudo/salvar")
	public String atividadeConteudoSalvar(@RequestParam("id") Long id, @RequestParam("conteudo") String conteudo,
			RedirectAttributes model) {

		AtividadeOnline atividade = atividadeService.buscarPor(id);
		try {
			atividade.setConteudo(conteudo);
			atividadeService.salvar(atividade);
			return "redirect:/dashboard/admin/eventos/online/modulos/" + atividade.getModulo().getId() + "/atividade/"
					+ atividade.getPosicao();
		} catch (PosicaoExisteException e) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", e.getMessage());
			return "redirect:/dashboard/admin/eventos/online/modulos/" + atividade.getModulo().getId() + "/atividade/"
					+ atividade.getPosicao();

		}

	}

	@GetMapping(value = "/modulo/dto/{id}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ModuloDTO moduloDTO(@PathVariable("id") Long id) {
		return new ModuloDTO(moduloService.buscarPor(id));
	}

	@GetMapping("/inscricoes/{id}")
	private String incricoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
			model.addAttribute("evento", eventoOnlineService.buscarPor(id));
			return "dashboard/admin/eventos/online/base-info-inscricoes-evento-online";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/inscricoes/{id}/cancelar/inscricao")
	public String incricoesEventoCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		InscricaoOnline inscricao = inscricaoService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		EventoOnline evento = inscricao.getEventoOnline();

		logEventoOnlineService.salvar(log(
				usuarioInscrito.getNome() + " teve sua inscrição cancelada do evento: " + evento.getTitulo(), evento));

		notificacaoService.salvar(new Notificacao(usuarioInscrito, "Inscrição cancelada", IconeType.INFORMACAO,
				StatusType.SUCESSO,
				"Sua inscrição foi cancelada para o evento " + evento.getTitulo() + " por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Cancelou a inscrição do usuário", IconeType.INFORMACAO,
				StatusType.SUCESSO, "Cancelou a inscrição do usuário com email: " + usuarioInscrito.getEmail()
						+ ",  do evento " + evento.getTitulo()));

		inscricaoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "inscrição foi cancelada com sucesso");

		return "redirect:/dashboard/admin/eventos/online/inscricoes/" + evento.getId();

	}
	
	@GetMapping("/certificado/{id}")
	public String certificadoOnline(@PathVariable("id") Long id, Model model) {
		
		Usuario usuarioLogado = getUsuario();
				
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("evento", eventoOnlineService.buscarPor(id));
		
		return "dashboard/admin/eventos/online/base-certificado-evento-online";
	}
	
	@PostMapping("/certificado/fundo/salvar")
	public String salvarImagensCertificado(@Valid CertificadoOnline certificado, BindingResult result,
			RedirectAttributes model) {

		if (!result.hasErrors()) {
			if (certificado.getId() != null) {
				certificadoService.atualizarImagemFundo(certificado);
			}else {
				certificadoService.salvar(certificado);
			}
			return "redirect:/dashboard/admin/eventos/online/certificado/" + certificado.getEventoOnline().getId();
		} else {
			
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/online/certificado/" + certificado.getEventoOnline().getId();
		}
	}
	
	@PostMapping("/certificado/conteudo/salvar")
	public String salvarConteudoCertificado(CertificadoOnline certificado, RedirectAttributes model) {
			certificadoService.atualizarConteudo(certificado);
			return "redirect:/dashboard/admin/eventos/online/certificado/" + certificado.getEventoOnline().getId();
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

}
