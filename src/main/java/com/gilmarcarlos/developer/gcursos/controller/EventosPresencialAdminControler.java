package com.gilmarcarlos.developer.gcursos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

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

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.EstiloPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.model.eventos.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.ListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.eventos.PermissoesEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.Sobre;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialTop;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoService;
import com.gilmarcarlos.developer.gcursos.service.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.EstiloPresencialService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialLogService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.LogEvePresencialPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.PermissoesEventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.ProgramacaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.SexoService;
import com.gilmarcarlos.developer.gcursos.service.SobreService;
import com.gilmarcarlos.developer.gcursos.service.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/eventos/presencial")
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
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private InscricaoPresencialService inscricaoPresencialService;

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

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_LOGS = 20;

	private Page<DiaEvento> getDiaPaginacao(Long id, Integer page) {
		return diaEventoPaginacaoService.listarDiasPorProgramacao(id, PageRequest.of(page, MAXIMO_PAGES));
	}

	private Page<EventoPresencialLog> getLogEvePaginacao(Long id, Integer page) {
		return logEventoPresencialPaginacaoService.listarLogsPorEvento(id, PageRequest.of(page, MAXIMO_PAGES_LOGS));
	}

	@GetMapping({ "/", "" })
	public String eventosPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("eventos", eventoPresencialService.listarTodos());
			return "dashboard/admin/eventos/base-info-evento-presencial";
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

			return "dashboard/admin/eventos/base-cadastro-evento-presencial";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoPresencial evento, RedirectAttributes model) {
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
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "salvo com sucesso");

		} catch (Exception e) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/dashboard/admin/eventos/presencial";

	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());
			model.addAttribute("evento", eventoPresencialService.buscarPor(id));
			return "dashboard/admin/eventos/base-cadastro-evento-presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoPresencialService.cancelar(id);
			eventoPresencialLogService
					.salvar(log("Evento presencial foi cancelado", eventoPresencialService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "Evento presencial foi cancelado com sucesso");
			return "redirect:/dashboard/admin/eventos/presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/ativar/{id}")
	public String ativar(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoPresencialService.ativar(id);
			eventoPresencialLogService
					.salvar(log("Evento presencial foi ativado", eventoPresencialService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "evento ativado com sucesso");
			return "redirect:/dashboard/admin/eventos/presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/publicacao/ativar/{id}")
	public String ativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			try {
				eventoPresencialService.publicar(id);
				eventoPresencialLogService
						.salvar(log("Evento presencial foi publicado", eventoPresencialService.buscarPor(id)));
				model.addFlashAttribute("alert", "alert alert-fill-success");
				model.addFlashAttribute("message", "evento publicado com sucesso");
				return "redirect:/dashboard/admin/eventos/presencial";
			} catch (Exception e) {

				model.addFlashAttribute("alert", "alert alert-fill-danger");
				model.addFlashAttribute("message", e.getMessage());
				return "redirect:/dashboard/admin/eventos/presencial";
			}
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/publicacao/desativar/{id}")
	public String desativarPublicacao(@PathVariable("id") Long id, RedirectAttributes model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			eventoPresencialService.cancelarPublicacao(id);
			eventoPresencialLogService.salvar(
					log("A publicação do evento presencial foi removida", eventoPresencialService.buscarPor(id)));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "evento teve sua publicação desativada com sucesso");
			return "redirect:/dashboard/admin/eventos/presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/estilo/{id}")
	public String estilo(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());
			model.addAttribute("evento", eventoPresencialService.buscarPor(id));
			return "dashboard/admin/eventos/base-estilo-evento-presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/imagens/destaque/salvar")
	public String salvarImagensDestaque(@Valid ImagensEventoPresencialDestaque imagens, BindingResult result,
			RedirectAttributes model) {

		if (!result.hasErrors()) {
			if (imagens.getEventoPresencial().getImagemDestaque() != null) {
				imagensService.deletarImagemEvePreDestaque(imagens.getEventoPresencial().getImagemDestaque().getId());
			}

			if (imagens != null) {
				imagensService.salvarImagemEvePresDestaque(imagens);
				eventoPresencialLogService
						.salvar(log("Imagem de destaque foi alterada", imagens.getEventoPresencial()));
			}
			return "redirect:/dashboard/admin/eventos/presencial/estilo/" + imagens.getEventoPresencial().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/presencial/estilo/" + imagens.getEventoPresencial().getId();
		}
	}

	@PostMapping("/imagens/destaque/deletar")
	public String deleatarImagensDestaque(@RequestParam("id") Long id, RedirectAttributes model) {

		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		if (evento.getImagemDestaque() != null) {
			imagensService.deletarImagemEvePreDestaque(evento.getImagemDestaque().getId());
			eventoPresencialLogService.salvar(log("Imagem de destaque foi removida", evento));

		}

		return "redirect:/dashboard/admin/eventos/presencial/estilo/" + id;
	}

	@PostMapping("/estilo/destaque/salvar")
	public String salvarEstiloDestaque(EstiloPresencial estilo, RedirectAttributes model) {

		EstiloPresencial novoEstilo = estiloPresencialService.salvar(estilo);
		eventoPresencialLogService
				.salvar(log("Estilo da página de destaque foi alterado", novoEstilo.getEventoPresencial()));
		return "redirect:/dashboard/admin/eventos/presencial/estilo/" + novoEstilo.getEventoPresencial().getId();
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {
		
		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), 0));

		return "dashboard/admin/eventos/base-detalhes-evento-presencial";
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), page));

		return "dashboard/admin/eventos/base-detalhes-evento-presencial";
	}

	@GetMapping("/detalhes/imagens/top/deletar/{id}")
	public String deletarImagensTop(@PathVariable("id") Long id, RedirectAttributes model) {

		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		if (evento.getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEvePreTop(evento.getImagemTopDetalhes().getId());
			eventoPresencialLogService.salvar(log("Imagem do topo foi removida", evento));
		}

		return "redirect:/dashboard/admin/eventos/presencial/detalhes/" + id;

	}

	@PostMapping("/detalhes/imagens/top/salvar")
	public String salvarImagensTop(@Valid ImagensEventoPresencialTop imagens, BindingResult result,
			RedirectAttributes model) {

		if (!result.hasErrors()) {
			if (imagens.getEventoPresencial().getImagemTopDetalhes() != null) {
				imagensService.deletarImagemEvePreTop(imagens.getEventoPresencial().getImagemTopDetalhes().getId());
			}
			imagensService.salvarImagemEvePresTop(imagens);
			eventoPresencialLogService.salvar(log("Imagem do topo foi alterada", imagens.getEventoPresencial()));
			return "redirect:/dashboard/admin/eventos/presencial/detalhes/" + imagens.getEventoPresencial().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/presencial/detalhes/" + imagens.getEventoPresencial().getId();

		}
	}

	@PostMapping("/detalhes/sobre/salvar")
	public String salvarSobre(Sobre sobre, RedirectAttributes model) {

		if (sobre.getEventoPresencial().getSobre() != null) {
			sobreService.deletar(sobre.getEventoPresencial().getSobre().getId());
		}

		eventoPresencialLogService.salvar(log("Informações sobre o evento foi alterado", sobre.getEventoPresencial()));
		sobreService.salvar(sobre);
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/" + sobre.getEventoPresencial().getId();

	}

	@GetMapping("/detalhes/programacao/{id}")
	public String programacao(@PathVariable("id") Long id, Model model) {

		ProgramacaoPresencial programacao = programacaoPresencialService.buscarPor(id);
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("evento", programacao.getEventoPresencial());
		model.addAttribute("dias", programacao.getDias());
		model.addAttribute("atividades", atividadePresencialService.buscarPorEvento(id));
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		return "dashboard/admin/eventos/base-cadastro-programacao-evento-presencial";
	}

	@PostMapping("/detalhes/atividades/salvar")
	public String atividadesSalvar(AtividadePresencial atividade, RedirectAttributes model) {
		try {
			atividadePresencialService.salvar(atividade);
			eventoPresencialLogService.salvar(log("Atividade: " + atividade.getTitulo() + " foi alterada",
					atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "salvo com sucesso");
			return "redirect:/dashboard/admin/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

		} catch (Exception e) {

			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", e.getMessage());
			return "redirect:/dashboard/admin/eventos/presencial/detalhes/programacao/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getId();
		}
	}

	@GetMapping("/detalhes/atividades/alterar/{id}")
	public String atividadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		model.addFlashAttribute("atividade", atividade);
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/programacao/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getId();
	}

	@GetMapping("/detalhes/atividades/deletar/{id}")
	public String atividadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		eventoPresencialLogService.salvar(log("Atividade: " + atividade.getTitulo() + " foi deletada",
				atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));
		ProgramacaoPresencial programacao = atividade.getDiaEvento().getProgramacaoPresencial();
		atividadePresencialService.deletar(id);

		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "removido com sucesso");
		model.addFlashAttribute("atividade", atividade);
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/programacao/" + programacao.getId();
	}

	@GetMapping("/logs/{id}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, Model model) {

		model.addAttribute("usuario", getUsuario());
		model.addAttribute("logs", getLogEvePaginacao(id, 0));
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		return "dashboard/admin/eventos/base-info-logs-evento-presencial";
	}

	@GetMapping("/logs/{id}/deletar")
	public String eventoPresencialLogsDeletarTodos(@PathVariable("id") Long id, RedirectAttributes model) {
		eventoPresencialService.buscarPor(id).getLogs().forEach(l -> eventoPresencialLogService.deletar(l.getId()));
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "todos os logs foram deletados");
		return "redirect:/dashboard/admin/eventos/presencial/logs/" + id;
	}

	@GetMapping("/logs/{id}/deletar/{log}")
	public String eventoPresencialLogsDeletar(@PathVariable("id") Long id, @PathVariable("log") Long log,
			RedirectAttributes model) {
		eventoPresencialLogService.deletar(log);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "log foi deletado com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/logs/" + id;
	}

	@GetMapping("/logs/{id}/pagina/{page}")
	public String eventoPresencialLogs(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		model.addAttribute("usuario", getUsuario());
		model.addAttribute("logs", getLogEvePaginacao(id, page));
		model.addAttribute("evento", eventoPresencialService.buscarPor(id));
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		return "dashboard/admin/eventos/base-info-logs-evento-presencial";
	}

	@GetMapping("/categorias")
	public String categorias(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/eventos/base-info-categorias-evento-presencial";
	}

	@GetMapping("/categorias/novo")
	public String categoriasNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/eventos/base-cadastro-categorias-evento-presencial";
	}

	@PostMapping("/categorias/salvar")
	public String categoriasSalvar(CategoriaEvento categorias, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categoria", categoriaEventoService.salvar(categorias));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/eventos/base-cadastro-categorias-evento-presencial";
	}

	@GetMapping("/categorias/alterar/{id}")
	public String categoriasAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("categoria", categoriaEventoService.buscarPor(id));
		return "redirect:/dashboard/admin/eventos/presencial/categorias/novo";
	}

	@GetMapping("/categorias/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		categoriaEventoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/categorias";
	}

	@GetMapping("/permissoes/{id}")
	private String permissoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
			model.addAttribute("evento", eventoPresencialService.buscarPor(id));
			model.addAttribute("unidades", unidadeService.listarTodos());
			model.addAttribute("escolaridades", escolaridadeService.listarTodos());
			model.addAttribute("sexos", sexoService.listarTodos());
			model.addAttribute("cargos", cargoService.listarTodos());
			return "/dashboard/admin/eventos/base-cadastro-permissoes-evento-presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/permissoes/salvar")
	private String permissoesEventoPresencialSalvar(PermissoesEventoPresencial permissoes, RedirectAttributes model) {

		permissoesEvePresencialService.salvar(permissoes);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/eventos/presencial";
	}

	@GetMapping("/inscricoes/{id}")
	private String incricoesEventoPresencial(@PathVariable("id") Long id, Model model) {
		Usuario usuarioLogado = getUsuario();
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
			model.addAttribute("evento", eventoPresencialService.buscarPor(id));
			model.addAttribute("atividades", atividadePresencialService.buscarPorEvento(id));
			return "/dashboard/admin/eventos/base-info-inscricoes-evento-presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@GetMapping("/inscricoes/{id}/cancelar/inscricao")
	public String incricoesEventoCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua inscrição cancelada da atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		notificacaoService.salvar(new Notificacao(usuarioInscrito, "Inscrição cancelada", IconeType.INFORMACAO,
				StatusType.SUCESSO, "Sua inscrição foi cancelada para a atividade " + atividade.getTitulo() + " por "
						+ getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Cancelou a inscrição do usuário", IconeType.INFORMACAO,
				StatusType.SUCESSO, "Cancelou a inscrição do usuário com email: " + usuarioInscrito.getEmail()
						+ ",  da atividade " + atividade.getTitulo()));

		inscricaoPresencialService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "inscrição foi cancelada com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/inscricoes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

	}

	@PostMapping("/inscricoes/lancar/presenca/todos")
	public String inscricoesEventoPresencaTodos(@RequestParam("id") Long id, @RequestParam("presenca") String presenca,
			RedirectAttributes model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		if (presenca.equalsIgnoreCase("presentes")) {
			atividade.getInscricoes().forEach(i -> {
				inscricaoPresencialService.confirmarPresenca(i.getId(), true);

				eventoPresencialLogService.salvar(log(
						i.getUsuario().getNome() + " teve sua presença confirmada na atividade: "
								+ atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

				notificacaoService.salvar(new Notificacao(i.getUsuario(), "Presença confirmada", IconeType.INFORMACAO,
						StatusType.SUCESSO, "Sua presença foi confirmada na atividade " + atividade.getTitulo()));

			});
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "presença confirmada para todos");
		} else {
			atividade.getInscricoes().forEach(i -> {
				inscricaoPresencialService.confirmarPresenca(i.getId(), false);

				eventoPresencialLogService.salvar(log(
						i.getUsuario().getNome() + " teve sua ausência confirmada na atividade: "
								+ atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

				notificacaoService.salvar(new Notificacao(i.getUsuario(), "Ausência confirmada", IconeType.INFORMACAO,
						StatusType.SUCESSO, "Sua presença não foi confirmada na atividade " + atividade.getTitulo()));

			});
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "ausência foi confirmada para todos");
		}

		return "redirect:/dashboard/admin/eventos/presencial/inscricoes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

	}

	@GetMapping("/inscricoes/{id}/presenca/ausente")
	public String inscricoesEventoPresencaAusente(@PathVariable("id") Long id, RedirectAttributes model) {

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua ausência confirmada na atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		notificacaoService.salvar(new Notificacao(usuarioInscrito, "Ausência confirmada", IconeType.INFORMACAO,
				StatusType.SUCESSO, "Sua presença não foi confirmada na atividade " + atividade.getTitulo()));

		inscricaoPresencialService.confirmarPresenca(id, false);

		return "redirect:/dashboard/admin/eventos/presencial/inscricoes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();

	}

	@GetMapping("/inscricoes/{id}/presenca/presente")
	@ResponseBody
	public ResponseEntity inscricoesEventoPresencaPresente(@PathVariable("id") Long id, RedirectAttributes model) {

		InscricaoPresencial inscricao = inscricaoPresencialService.buscarPor(id);
		Usuario usuarioInscrito = inscricao.getUsuario();
		AtividadePresencial atividade = inscricao.getAtividadePresencial();

		eventoPresencialLogService.salvar(
				log(usuarioInscrito.getNome() + " teve sua presença confirmada na atividade: " + atividade.getTitulo(),
						atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()));

		notificacaoService.salvar(new Notificacao(usuarioInscrito, "Presença confirmada", IconeType.INFORMACAO,
				StatusType.SUCESSO, "Sua presença foi confirmada na atividade " + atividade.getTitulo()));

		inscricaoPresencialService.confirmarPresenca(id, true);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/inscricoes/{id}/gerar/lista", produces = MediaType.APPLICATION_PDF_VALUE)
	private String gerarLista(@PathVariable("id") Long id, RedirectAttributes model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		EventoPresencial evento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

		if (evento.getImagemLogo() == null) {
			model.addFlashAttribute("alert", "alert alert-fill-warning");
			model.addFlashAttribute("message", "você prescisa adiciona o titulo do cabeçalho e as logos");
			return "redirect:/dashboard/admin/eventos/presencial/inscricoes/" + evento.getId();
		} else {
			if (!atividade.getInscricoes().isEmpty()) {
				return "redirect:/dashboard/admin/eventos/presencial/inscricoes/" + id + "/gerar/lista-presenca";
			}else {
				model.addFlashAttribute("alert", "alert alert-fill-warning");
				model.addFlashAttribute("message", "não existem inscritos para essa atividade");
				return "redirect:/dashboard/admin/eventos/presencial/inscricoes/" + evento.getId();
			}
		}

	}

	@GetMapping(value = "/inscricoes/{id}/gerar/lista-presenca", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaPresenca(@PathVariable("id") Long id, Model model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		EventoPresencial evento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

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

		if (!result.hasErrors()) {
			if (imagens.getEventoPresencial().getImagemLogo() != null) {
				imagensService.deletarImagemLogListaPresenca(imagens.getEventoPresencial().getImagemLogo().getId());
			}
			imagensService.salvarImagemLogListaPresenca(imagens);
			eventoPresencialLogService.salvar(log("Imagem do topo foi alterada", imagens.getEventoPresencial()));
			return "redirect:/dashboard/admin/eventos/presencial/inscricoes/" + imagens.getEventoPresencial().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "imagem vazia ou arquivo não é uma imagem");
			return "redirect:/dashboard/admin/eventos/presencial/incricoes/" + imagens.getEventoPresencial().getId();

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
}
