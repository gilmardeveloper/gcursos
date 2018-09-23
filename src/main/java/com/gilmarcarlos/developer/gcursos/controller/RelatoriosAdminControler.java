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
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

@Controller
@RequestMapping(UrlUtils.DASHBOARD_ADMIN_RELATORIOS)
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
	private RelatorioInscricoesPresenciais relatoriosPresenciais;
	
	@Autowired
	private CargoService cargoService;
	
	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private RelatorioInscricoesOnline relatoriosOnline;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES_EVENTOS = 20;
	
	private Page<Usuario> getUsuarioPaginacao(Integer page) {
		return usuarioService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioIdPaginacao(Long id) {
		return usuarioService.buscarPor(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioIdPaginacao(Long departamento, Long id) {
		return usuarioService.buscarPor(departamento, id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioUnidadesPaginacao(Long id) {
		return usuarioService.buscarPorUnidade(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioUnidadesPaginacao(Long departamento, Long id) {
		return usuarioService.buscarPorUnidade(departamento, id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioCargosPaginacao(Long id) {
		return usuarioService.buscarPorCargo(id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<Usuario> getUsuarioCargosPaginacao(Long departamento, Long id) {
		return usuarioService.buscarPorCargo(departamento, id, PageRequest.of(0, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoOnline> getEventoOnlinePaginacao(Usuario usuario, Integer page) {
		return eventoOnlineService.listarTodos(usuario, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoOnline> getEventoOnlinePaginacao(Integer page) {
		return eventoOnlineService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	private Page<EventoPresencial> getEventoPresencialPaginacao(Integer page) {
		return eventoPresencialService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	private Page<EventoPresencial> getEventoPresencialPaginacao(Usuario usuario, Integer page) {
		return eventoPresencialService.listarTodos(usuario, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
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
	
	private Page<EventoPresencial> getEventoPresencialPeriodoPaginacao(Usuario usuario, LocalDate inicio, LocalDate termino)
			throws DataFinalMenorException, EventosNaoEncontradosException {
		Page<EventoPresencial> pages = eventoPresencialService.buscarPor(usuario, inicio, termino,
				PageRequest.of(0, MAXIMO_PAGES_EVENTOS));

		if (pages.getNumberOfElements() == 0) {
			throw new EventosNaoEncontradosException();
		}

		return pages;
	}
	
	private Page<Usuario> getUsuarioDepartamentoPaginacao(Long id, Integer page) {
		return usuarioService.buscarPorDepartamento(id, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}
	
	@GetMapping("/inscricoes/usuarios")
	public String inscricaoUsuario(Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("usuarios", getUsuarioDepartamentoPaginacao(usuarioLogado.getPermissoes().getDepartamento(), 0));
		} else {
			model.addAttribute("usuarios", getUsuarioPaginacao(0));
		}
		
		addBaseAttributes(model, usuarioLogado);
		addLocaisAttributes(model);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_USUARIO;
	}

	
	@GetMapping("/inscricoes/usuarios/pagina/{page}")
	public String inscricaoUsuario(@PathVariable("page")Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("usuarios", getUsuarioDepartamentoPaginacao(usuarioLogado.getPermissoes().getDepartamento(), page));
		} else {
			model.addAttribute("usuarios", getUsuarioPaginacao(page));
		}

		addBaseAttributes(model, usuarioLogado);
		addLocaisAttributes(model);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_USUARIO;
	}
	
	@GetMapping("/inscricoes/usuarios/{id}")
	public String inscricaoUsuario(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("usuarios", getUsuarioIdPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id));
		} else {
			model.addAttribute("usuarios", getUsuarioIdPaginacao(id));
		}
		
		addBaseAttributes(model, usuarioLogado);
		addLocaisAttributes(model);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_USUARIO;
	}
	
	@GetMapping("/inscricoes/usuarios/unidades/{id}")
	public String inscricaoUsuarioUnidades(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("usuarios", getUsuarioUnidadesPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id));
		} else {
			model.addAttribute("usuarios", getUsuarioUnidadesPaginacao(id));
		}
		
		
		addBaseAttributes(model, usuarioLogado);
		addLocaisAttributes(model);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_USUARIO;
	}
	
	@GetMapping("/inscricoes/usuarios/cargos/{id}")
	public String inscricaoUsuarioCargos(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("usuarios", getUsuarioCargosPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id));
		} else {
			model.addAttribute("usuarios", getUsuarioCargosPaginacao(id));
		}

		addBaseAttributes(model, usuarioLogado);
		addLocaisAttributes(model);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_USUARIO;
	}
	
	@GetMapping("/inscricoes/usuario/detalhes/{id}")
	public String inscricoesUsuarioDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosUsuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		Usuario user = usuarioService.buscarPor(id);
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("user", user);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_DETALHES_INSCRICOES_USUARIO; 
	}
	
	@GetMapping("/inscricoes/presenciais")
	public String inscricaoPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPresencialPaginacao(usuarioLogado, 0));
		} else {
			model.addAttribute("eventos", getEventoPresencialPaginacao(0));
		}
		
		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_PRESENCIAIS;
	}
	
	@GetMapping("/inscricoes/presenciais/pagina/{page}")
	public String inscricaoPresencial(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoPresencialPaginacao(usuarioLogado, page));
		} else {
			model.addAttribute("eventos", getEventoPresencialPaginacao(page));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_PRESENCIAIS;
	}

	@GetMapping("/inscricoes/presenciais/{id}")
	public String inscricaoPresencial(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPresencialIdPaginacao(id));

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_PRESENCIAIS;
	}

	@PostMapping("/inscricoes/presenciais/periodo")
	public String incricaoPresencial(@RequestParam("dataInicio") LocalDate dataInicio,
			@RequestParam("dataTermino") LocalDate dataTermino, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		try {
			
			if (usuarioLogado.temRestricao("responsabilidade")) {
				model.addAttribute("eventos", getEventoPresencialPeriodoPaginacao(usuarioLogado, dataInicio, dataTermino));
			} else {
				model.addAttribute("eventos", getEventoPresencialPeriodoPaginacao(dataInicio, dataTermino));
			}
			
			addBaseAttributes(model, usuarioLogado);

		} catch (DataFinalMenorException | EventosNaoEncontradosException e) {
			RedirectUtils.mensagemError(red, e.getMessage());
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_RELATORIOS + "/inscricoes/presenciais";
		}

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_PRESENCIAIS;
	}

	@GetMapping("/inscricoes/presenciais/detalhes/{id}")
	public String inscricoesDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosPresenciais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		List<AtividadePresencial> atividades = atividadePresencialService.buscarPorEvento(id);
		
		List<GraficoAtividade> graficoAtividades = new ArrayList<>();
		atividades.forEach(a -> graficoAtividades.add(new GraficoAtividade(a.getTitulo(), a.getInscricoes())));
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("atividades", atividades);
		model.addAttribute("evento", evento);
		model.addAttribute("graficoAtividades", graficoAtividades);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_DETALHES_INSCRICOES_PRESENCIAIS;
	}

	@PostMapping(value = "/inscricoes/presenciais/gerar/relatorio")
	private String gerarLista(@RequestParam("id") Long id, @RequestParam("tipo") String tipo, RedirectAttributes model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		EventoPresencial evento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

		if (!atividade.getInscricoes().isEmpty()) {
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_RELATORIOS + "/inscricoes/presenciais/" + id + "/gerar/relatorio-inscritos/" + tipo;
		} else {
			RedirectUtils.mensagemError(model, "não existem inscritos para essa atividade");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_PRESENCIAL + "/inscricoes/" + evento.getId();
		}
	}

	@GetMapping(value = "/inscricoes/presenciais/{id}/gerar/relatorio-inscritos/{tipo}", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaPresenca(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, Model model) {

		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		InputStream pdfLista = relatoriosPresenciais.gerar(getUsuario(), atividade, tipo);

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
		
		if (!usuarioLogado.podeVisualizar("relatoriosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoOnlinePaginacao(usuarioLogado, 0));
		} else {
			model.addAttribute("eventos", getEventoOnlinePaginacao(0));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_ONLINE;
	}
	
	@GetMapping("/inscricoes/online/pagina/{page}")
	public String inscricaoOnline(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		if (usuarioLogado.temRestricao("responsabilidade")) {
			model.addAttribute("eventos", getEventoOnlinePaginacao(usuarioLogado, page));
		} else {
			model.addAttribute("eventos", getEventoOnlinePaginacao(page));
		}

		addBaseAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_ONLINE;
	}
	
	@GetMapping("/inscricoes/online/{id}")
	public String inscricaoOnline(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoOnlineIdPaginacao(id));

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_CONSULTA_INSCRICOES_ONLINE;
	}
	
	@GetMapping("/inscricoes/online/detalhes/{id}")
	public String inscricoesOnlineDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.podeVisualizar("relatoriosOnline")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		EventoOnline evento = eventoOnlineService.buscarPor(id);
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);

		return TemplateUtils.DASHBOARD_ADMIN_RELATORIOS_DETALHES_INSCRICOES_ONLINE;
	}
	
	@PostMapping(value = "/inscricoes/online/gerar/relatorio")
	private String gerarListaOnline(@RequestParam("id") Long id, @RequestParam("tipo") String tipo, RedirectAttributes model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (!evento.getInscricoes().isEmpty()) {
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_RELATORIOS + "/inscricoes/online/" + id + "/gerar/relatorio-inscritos/" + tipo;
		} else {
			RedirectUtils.mensagemError(model, "não existem inscritos para esse evento");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_EVENTOS_ONLINE + "/online/inscricoes/" + evento.getId();
		}
	}

	@GetMapping(value = "/inscricoes/online/{id}/gerar/relatorio-inscritos/{tipo}", produces = MediaType.APPLICATION_PDF_VALUE)
	private @ResponseBody byte[] gerarListaProgresso(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, Model model) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);

		InputStream pdfLista = relatoriosOnline.gerar(getUsuario(), evento, tipo);

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
	
	private void addLocaisAttributes(Model model) {
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
	}

	private void addBaseAttributes(Model model, Usuario usuarioLogado) {
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacaoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("mensagens", usuarioLogado.getMensagensNaoLidas());
	}
	
}
