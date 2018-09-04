package com.gilmarcarlos.developer.gcursos.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EstiloOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnlineLog;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.SobreOnline;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.categorias.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EstiloOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineLogService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.SobreOnlineService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/eventos/online")
public class EventoOnlineAdminControler {

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
	private ImagensService imagensService;

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
				logEventoOnlineService
						.salvar(log("Imagem de destaque foi alterada", imagens.getEventoOnline()));
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
		logEventoOnlineService
				.salvar(log("Estilo da página de destaque foi alterado", novoEstilo.getEventoOnline()));
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
