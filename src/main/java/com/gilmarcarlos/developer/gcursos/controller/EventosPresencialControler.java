package com.gilmarcarlos.developer.gcursos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.PermissoesEventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.PermissoesEventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

@Controller
@RequestMapping(UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS)
public class EventosPresencialControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EventoPresencialService eventoPresencialService;

	@Autowired
	private AtividadePresencialService atividadePresencialService;

	@Autowired
	private DiaEventoPaginacaoService diaEventoPaginacaoService;

	@Autowired
	private PermissoesEventoPresencialService permissoesEvePresencialService;

	@Autowired
	private InscricaoPresencialService inscricaoPresencialService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<DiaEvento> getDiaPaginacao(Long id, Integer page) {
		return diaEventoPaginacaoService.listarDiasPorProgramacao(id, PageRequest.of(page, MAXIMO_PAGES));
	}
	
	private Page<EventoPresencial> getEventoPaginacao(Integer page) {
		return eventoPresencialService.listarTodosPublicados(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosPresencial(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPaginacao(0));
		
		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_PRESENCIAL;
	}

	
	@GetMapping( "/pagina/{page}")
	public String eventosPresencial(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPaginacao(page));
		
		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_PRESENCIAL;
	}

	@GetMapping("/{id}/verifica/usuario/tem/codigo")
	@ResponseBody
	public ResponseEntity<?> inscricoesEventoPresencaPresente(@PathVariable("id") Long id) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		
		if (evento.getPermissoes().temCodigo(usuarioLogado)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/detalhes")
	public String eventoDetalhesComCodigo(@RequestParam("id") Long id, @RequestParam("codigo") String codigo,
			RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		if (evento.getPermissoes().getCodigo().equalsIgnoreCase(codigo)) {

			PermissoesEventoPresencial permissoes = evento.getPermissoes();
			List<String> usuarios = (permissoes.getUsuariosComCodigo().isEmpty() ? new ArrayList<>()
					: permissoes.getUsuariosComCodigo());
			usuarios.add(usuarioLogado.getEmail());
			permissoes.setUsuariosComCodigo(usuarios);
			permissoesEvePresencialService.salvar(permissoes);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/" + id;

		} else {
			RedirectUtils.mensagemError(model, "código inválido");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS;
		}
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			RedirectUtils.mensagemError(red, "você não tem permissão");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS;
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			RedirectUtils.mensagemError(red, "você não tem permissão para acessar esse evento");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS;
		}

		addBaseAttributes(model, usuarioLogado);
		
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), 0));

		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_PRESENCIAL_DETALHES; 
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model,
			RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			RedirectUtils.mensagemError(red, "você não tem permissão");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS;
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			RedirectUtils.mensagemError(red, "você não tem permissão para acessar esse evento");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS;
		}

		addBaseAttributes(model, usuarioLogado);
		
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), page));

		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_PRESENCIAL_DETALHES; 
	}

	@GetMapping("/atividade/{id}/inscricao")
	public String eventoDetalhesInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		if (atividade.naoTemVagas()) {
			RedirectUtils.mensagemError(model, "Atividade não tem mais vagas");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}

		if (!atividade.usuarioNaoEhResponsavelDoEvento(usuarioLogado)) {
			RedirectUtils.mensagemError(model, "você é o responsável pelo o evento");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}

		if (atividade.podeSeInscrever(usuarioLogado)) {

			inscricaoPresencialService.salvar(new InscricaoPresencial(usuarioLogado, atividade));
			RedirectUtils.mensagemSucesso(model, "inscrição realizada com sucesso");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		} else {
			RedirectUtils.mensagemError(model, "você não pode se inscrever em atividades de mesmo horário");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}
	}

	@GetMapping("/atividade/{id}/cancelar/inscricao")
	public String eventoDetalhesCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		if (atividade.isInscrito(usuarioLogado)) {

			inscricaoPresencialService.deletar(atividade.getInscricao(usuarioLogado));
			RedirectUtils.mensagemSucesso(model, "inscrição foi cancelada com sucesso");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		} else {
			RedirectUtils.mensagemError(model, "um erro ocorreu");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_PRESENCIAIS + "/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
	
	private void addBaseAttributes(Model model, Usuario usuarioLogado) {
		
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("mensagens", usuarioLogado.getMensagensNaoLidas());
	}
}
