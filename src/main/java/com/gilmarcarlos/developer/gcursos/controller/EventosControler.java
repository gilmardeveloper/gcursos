package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.Sobre;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.ProgramacaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.SobreService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/eventos/presencial")
public class EventosControler {

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
	private DiaEventoPaginacaoService diaEventoPaginacaoService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;

	private Page<DiaEvento> getDiaPaginacao(Long id, Integer page) {
		return diaEventoPaginacaoService.listarDiasPorProgramacao(id, PageRequest.of(page, MAXIMO_PAGES));
	}

	@GetMapping({"/", ""})
	public String eventosPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("eventos", eventoPresencialService.listarTodos());
			return "/dashboard/eventos/base-info-evento-presencial";
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

			return "/dashboard/eventos/base-cadastro-evento-presencial";
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
			model.addAttribute("evento", eventoPresencialService.buscarPor(id));
			return "/dashboard/eventos/base-cadastro-evento-presencial";
		} else {
			return "redirect:/dashboard/admin/complete-cadastro";
		}
	}

	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoPresencial evento, RedirectAttributes model) {

		EventoPresencial novoEvento = eventoPresencialService.salvar(evento);

		if (evento.getProgramacao() == null) {
			
			ProgramacaoPresencial programacao = new ProgramacaoPresencial();
			programacao.setEventoPresencial(novoEvento);
			ProgramacaoPresencial novaProgramacao = programacaoPresencialService.salvar(programacao);
			diaEventoService.gerarDiasDeEvento(novoEvento, novaProgramacao);
			
		}else {
			diaEventoService.alterarDiasDeEvento(novoEvento, novoEvento.getProgramacao());
		}
		
		model.addFlashAttribute("categorias", categoriaEventoService.listarTodos());

		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/eventos/presencial";

	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), 0));

		return "/dashboard/eventos/base-detalhes-evento-presencial";
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), page));

		return "/dashboard/eventos/base-detalhes-evento-presencial";
	}

	@PostMapping("/detalhes/imagens/top/salvar")
	public String salvarImagensTop(ImagensEventoPresencial imagens, RedirectAttributes model) {

		if (imagens.getEventoPresencial().getImagemTopDetalhes() != null) {
			imagensService.deletarImagemEvePreTop(imagens.getEventoPresencial().getImagemTopDetalhes().getId());
		}

		imagensService.salvarImagemEvePresTop(imagens);
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/" + imagens.getEventoPresencial().getId();

	}

	@PostMapping("/detalhes/sobre/salvar")
	public String salvarSobre(Sobre sobre, RedirectAttributes model) {

		if (sobre.getEventoPresencial().getSobre() != null) {
			sobreService.deletar(sobre.getEventoPresencial().getSobre().getId());
		}

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

		return "dashboard/eventos/base-cadastro-programacao-evento-presencial";
	}

	@PostMapping("/detalhes/atividades/salvar")
	public String atividadesSalvar(AtividadePresencial atividade, RedirectAttributes model) {
		atividadePresencialService.salvar(atividade);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "salvo com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/"
				+ atividade.getDiaEvento().getProgramacaoPresencial().getId();
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
		ProgramacaoPresencial programacao = atividade.getDiaEvento().getProgramacaoPresencial();
		atividadePresencialService.deletar(id);
		
		model.addFlashAttribute("atividade", atividade);
		return "redirect:/dashboard/admin/eventos/presencial/detalhes/programacao/"
		+ programacao.getId();
	}
	
	@GetMapping("/categorias")
	public String categorias(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/eventos/base-info-categorias-evento-presencial";
	}

	@GetMapping("categorias/novo")
	public String categoriasNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/eventos/base-cadastro-categorias-evento-presencial";
	}

	@PostMapping("categorias/salvar")
	public String categoriasSalvar(CategoriaEvento categorias, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categoria", categoriaEventoService.salvar(categorias));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/eventos/base-cadastro-categorias-evento-presencial";
	}

	@GetMapping("categorias/alterar/{id}")
	public String categoriasAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("categoria", categoriaEventoService.buscarPor(id));
		return "redirect:/dashboard/admin/eventos/presencial/categorias/novo";
	}

	@GetMapping("categorias/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		categoriaEventoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/categorias";
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

}
