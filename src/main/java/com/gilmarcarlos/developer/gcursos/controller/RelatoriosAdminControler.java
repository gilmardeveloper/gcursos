package com.gilmarcarlos.developer.gcursos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventosNaoEncontradosException;
import com.gilmarcarlos.developer.gcursos.model.eventos.listas.GraficoAtividade;
import com.gilmarcarlos.developer.gcursos.model.eventos.listas.RelatorioInscricoesOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.listas.RelatorioInscricoesPresenciais;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.DiaEventoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/relatorios")
public class RelatoriosAdminControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EventoOnlineService eventoOnlineService;

	@Autowired
	private EventoPresencialService eventoPresencialService;

	@Autowired
	private AtividadePresencialService atividadePresencialService;

	@Autowired
	private DiaEventoService diaEventoService;

	@Autowired
	private DiaEventoPaginacaoService diaEventoPaginacaoService;
	
	@Autowired
	private RelatorioInscricoesPresenciais relatoriosPresenciais;
	
	@Autowired
	private CargoService cargoService;
	
	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private RelatorioInscricoesOnline relatoriosOnline;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_EVENTOS = 20;
	
	private Page<Usuario> getUsuarioPaginacao(Integer page) {
		return usuarioService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioIdPaginacao(Long id) {
		return usuarioService.buscarPor(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioUnidadesPaginacao(Long id) {
		return usuarioService.buscarPorUnidade(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioCargosPaginacao(Long id) {
		return usuarioService.buscarPorCargo(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoOnline> getEventoOnlinePaginacao(Integer page) {
		return eventoOnlineService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<DiaEvento> getDiaPaginacao(Long id, Integer page) {
		return diaEventoPaginacaoService.listarDiasPorProgramacao(id, PageRequest.of(page, MAXIMO_PAGES));
	}

	private Page<EventoPresencial> getEventoPresencialPaginacao(Integer page) {
		return eventoPresencialService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoOnline> getEventoOnlineIdPaginacao(Long id) {
		return eventoOnlineService.buscarPor(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
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
	
	@GetMapping("/inscricoes/usuarios")
	public String inscricaoUsuario(Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuarios", getUsuarioPaginacao(0));
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());

		return "dashboard/admin/relatorios/consulta_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/usuarios/pagina/{page}")
	public String inscricaoUsuario(@PathVariable("page")Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuarios", getUsuarioPaginacao(page));
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());

		return "dashboard/admin/relatorios/consulta_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/usuarios/{id}")
	public String inscricaoUsuario(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuarios", getUsuarioIdPaginacao(id));
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());

		return "dashboard/admin/relatorios/consulta_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/usuarios/unidades/{id}")
	public String inscricaoUsuarioUnidades(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuarios", getUsuarioUnidadesPaginacao(id));
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());

		return "dashboard/admin/relatorios/consulta_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/usuarios/cargos/{id}")
	public String inscricaoUsuarioCargos(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuarios", getUsuarioCargosPaginacao(id));
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());

		return "dashboard/admin/relatorios/consulta_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/usuario/detalhes/{id}")
	public String inscricoesUsuarioDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		Usuario user = usuarioService.buscarPor(id);
		
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("user", user);

		return "dashboard/admin/relatorios/detalhes_inscricoes_usuario";
	}
	
	@GetMapping("/inscricoes/presenciais")
	public String inscricaoPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoPresencialPaginacao(0));

		return "dashboard/admin/relatorios/consulta_inscricoes_presenciais";
	}
	
	@GetMapping("/inscricoes/presenciais/pagina/{page}")
	public String inscricaoPresencial(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoPresencialPaginacao(page));

		return "dashboard/admin/relatorios/consulta_inscricoes_presenciais";
	}

	@GetMapping("/inscricoes/presenciais/{id}")
	public String inscricaoPresencial(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoPresencialIdPaginacao(id));

		return "dashboard/admin/relatorios/consulta_inscricoes_presenciais";
	}

	@PostMapping("/inscricoes/presenciais/periodo")
	public String incricaoPresencial(@RequestParam("dataInicio") LocalDate dataInicio,
			@RequestParam("dataTermino") LocalDate dataTermino, Model model, RedirectAttributes red) {

		try {

			Usuario usuarioLogado = getUsuario();
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
			model.addAttribute("eventos", getEventoPresencialPeriodoPaginacao(dataInicio, dataTermino));

		} catch (DataFinalMenorException | EventosNaoEncontradosException e) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", e.getMessage());
			return "redirect:/dashboard/admin/relatorios/inscricoes/presenciais";
		}

		return "dashboard/admin/relatorios/consulta_inscricoes_presenciais";
	}

	@GetMapping("/inscricoes/presenciais/detalhes/{id}")
	public String inscricoesDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		List<AtividadePresencial> atividades = atividadePresencialService.buscarPorEvento(id);
		
		List<GraficoAtividade> graficoAtividades = new ArrayList<>();
		
		atividades.forEach(a -> graficoAtividades.add(new GraficoAtividade(a.getTitulo(), a.getInscricoes())));
		
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("atividades", atividades);
		model.addAttribute("evento", evento);
		model.addAttribute("graficoAtividades", graficoAtividades);

		return "dashboard/admin/relatorios/detalhes_inscricoes_presenciais";
	}

	@PostMapping(value = "/inscricoes/presenciais/gerar/relatorio")
	private String gerarLista(@RequestParam("id") Long id, @RequestParam("tipo") String tipo, RedirectAttributes model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		EventoPresencial evento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

		if (!atividade.getInscricoes().isEmpty()) {
			return "redirect:/dashboard/admin/relatorios/inscricoes/presenciais/" + id + "/gerar/relatorio-inscritos/" + tipo;
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-warning");
			model.addFlashAttribute("message", "não existem inscritos para essa atividade");
			return "redirect:/dashboard/admin/eventos/presencial/inscricoes/" + evento.getId();
		}
	}

	@GetMapping(value = "/inscricoes/presenciais/{id}/gerar/relatorio-inscritos/{tipo}", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaPresenca(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, Model model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		InputStream pdfLista = relatoriosPresenciais.gerar(atividade, tipo);

		try {
			return IOUtils.toByteArray(pdfLista);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@GetMapping("/inscricoes/online")
	public String inscricaoOnline(Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoOnlinePaginacao(0));

		return "dashboard/admin/relatorios/consulta_inscricoes_online";
	}
	
	@GetMapping("/inscricoes/online/pagina/{page}")
	public String inscricaoOnline(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoOnlinePaginacao(page));

		return "dashboard/admin/relatorios/consulta_inscricoes_online";
	}
	
	@GetMapping("/inscricoes/online/{id}")
	public String inscricaoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("eventos", getEventoOnlineIdPaginacao(id));

		return "dashboard/admin/relatorios/consulta_inscricoes_online";
	}
	
	@GetMapping("/inscricoes/online/detalhes/{id}")
	public String inscricoesOnlineDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);
		
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("evento", evento);

		return "dashboard/admin/relatorios/detalhes_inscricoes_online";
	}
	
	@PostMapping(value = "/inscricoes/online/gerar/relatorio")
	private String gerarListaOnline(@RequestParam("id") Long id, @RequestParam("tipo") String tipo, RedirectAttributes model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (!evento.getInscricoes().isEmpty()) {
			return "redirect:/dashboard/admin/relatorios/inscricoes/online/" + id + "/gerar/relatorio-inscritos/" + tipo;
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-warning");
			model.addFlashAttribute("message", "não existem inscritos para esse evento");
			return "redirect:/dashboard/admin/eventos/online/inscricoes/" + evento.getId();
		}
	}

	@GetMapping(value = "/inscricoes/online/{id}/gerar/relatorio-inscritos/{tipo}", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaProgresso(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, Model model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		InputStream pdfLista = relatoriosOnline.gerar(evento, tipo);

		try {
			return IOUtils.toByteArray(pdfLista);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
}
