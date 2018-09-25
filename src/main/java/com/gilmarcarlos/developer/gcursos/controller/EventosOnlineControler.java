package com.gilmarcarlos.developer.gcursos.controller;

import java.time.LocalDate;
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

import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineAtividade;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineModulo;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineAtividadeService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineModuloService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.ModuloService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

@Controller
@RequestMapping(UrlUtils.DASHBOARD_EVENTOS_ONLINE)
public class EventosOnlineControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EventoOnlineService eventoOnlineService;

	@Autowired
	private PermissoesEventoOnlineService permissoesEveOnlineService;

	@Autowired
	private ModuloService moduloService;

	@Autowired
	private InscricaoOnlineService inscricaoService;

	@Autowired
	private InscricaoOnlineModuloService inscricaoModuloService;

	@Autowired
	private InscricaoOnlineAtividadeService inscricaoAtividadeService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<EventoOnline> getEventoPaginacao(Integer page) {
		return eventoOnlineService.listarTodosPublicados(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosOnline(Model model) {

		Usuario usuarioLogado = getUsuario();
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPaginacao(0));
		
		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_ONLINE;
	}
	
	@GetMapping( "/pagina/{page}")
	public String eventosOnline(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", getEventoPaginacao(page));
		
		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_ONLINE;
	}

	@GetMapping("/{id}/verifica/usuario/tem/codigo")
	@ResponseBody
	public ResponseEntity<?> inscricoesEventoOnlinePresente(@PathVariable("id") Long id) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);
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
		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (evento.getPermissoes().getCodigo().equalsIgnoreCase(codigo)) {

			PermissoesEventoOnline permissoes = evento.getPermissoes();
			List<String> usuarios = (permissoes.getUsuariosComCodigo().isEmpty() ? new ArrayList<>()
					: permissoes.getUsuariosComCodigo());
			usuarios.add(usuarioLogado.getEmail());
			permissoes.setUsuariosComCodigo(usuarios);
			permissoesEveOnlineService.salvar(permissoes);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/detalhes/" + id;

		} else {
			RedirectUtils.mensagemError(model, "código inválido");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			RedirectUtils.mensagemError(red, ConfUtils.ALERTA_ERROR_PERMISSAO);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			RedirectUtils.mensagemError(red, ConfUtils.ALERTA_ERROR_PERMISSAO);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);

		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_ONLINE_DETALHES;
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model,
			RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			RedirectUtils.mensagemError(red, ConfUtils.ALERTA_ERROR_PERMISSAO);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			RedirectUtils.mensagemError(red, ConfUtils.ALERTA_ERROR_PERMISSAO);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("evento", evento);

		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_ONLINE_DETALHES;
	}

	@GetMapping("/iniciar/{id}")
	public String iniciarEvento(@PathVariable("id") Long id, RedirectAttributes red) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		Usuario usuarioLogado = getUsuario();

		if (usuarioLogado.ehResponsavel(evento)) {
			RedirectUtils.mensagemError(red, "você é o responsável pelo evento");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/detalhes/" + id;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			RedirectUtils.mensagemError(red, ConfUtils.ALERTA_ERROR_PERMISSAO);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
		}

		if (evento.isInscrito(usuarioLogado)) {
			InscricaoOnline inscricao = evento.getInscricao(usuarioLogado);
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/modulos/" + inscricao.ultimoModulo().getId()
					+ "/atividade/" + inscricao.ultimaAtividade().getPosicao();
		} else {
			inscricaoService.salvar(new InscricaoOnline(usuarioLogado, evento));
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/iniciar/" + id;
		}

	}

	@GetMapping("/desistir/{id}")
	public String eventoDesistir(@PathVariable("id") Long id, RedirectAttributes red) {

		InscricaoOnline inscricao = eventoOnlineService.buscarPor(id).getInscricao(getUsuario());

		if (!inscricao.getAtividades().isEmpty()) {
			inscricao.getAtividades().forEach(a -> inscricaoAtividadeService.deletar(a.getId()));
		}

		if (!inscricao.getModulos().isEmpty()) {
			inscricao.getModulos().forEach(m -> inscricaoModuloService.deletar(m.getId()));
		}

		inscricaoService.deletar(inscricao.getId());

		return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/detalhes/" + id;
	}

	@GetMapping("/modulos/{id}")
	public String modulos(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);

		if (modulo.getAtividades().isEmpty()) {
			RedirectUtils.mensagemError(red, "este módulo não tem atividades, favor entre em contato com o responsável");
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/detalhes/" + modulo.getEventoOnline().getId();
		} else {
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/modulos/" + id + "/atividade/"
					+ modulo.getAtividades().get(0).getPosicao();
		}

	}

	@GetMapping("/modulos/{id}/atividade/{posicao}")
	public String modulos(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);
		AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() == posicao).findAny()
				.get();

		Usuario usuarioLogado = getUsuario();
		
		addBaseAttributes(model, usuarioLogado);
		
		model.addAttribute("modulo", modulo);
		model.addAttribute("atividade", atividade);
		model.addAttribute("evento", modulo.getEventoOnline());

		return TemplateUtils.DASHBOARD_EVENTOS_EVENTOS_ONLINE_MODULOS_DETALHES;

	}

	@GetMapping("/modulos/{id}/proxima-atividade/{posicao}")
	public String proximaAtividade(@PathVariable("id") Long id, @PathVariable("posicao") Integer posicao, Model model,
			RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);
		AtividadeOnline atividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() == posicao).findFirst()
				.get();
		EventoOnline evento = modulo.getEventoOnline();
		InscricaoOnline inscricao = evento.getInscricao(getUsuario());

		if (!inscricao.realizou(modulo)) {
			inscricaoModuloService.salvar(new InscricaoOnlineModulo(inscricao, modulo));
		}

		if (!inscricao.realizou(atividade)) {
			inscricaoAtividadeService.salvar(new InscricaoOnlineAtividade(inscricao, atividade));
		}

		if (posicao < modulo.getAtividades().size()) {

			AtividadeOnline proximaAtividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() > posicao)
					.findFirst().get();
			return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/modulos/" + id + "/atividade/"
					+ proximaAtividade.getPosicao();

		} else {

			if (modulo.getPosicao() < evento.getModulos().size()) {

				Modulo proximoModulo = evento.getModulos().stream().filter(m -> m.getPosicao() > modulo.getPosicao())
						.findFirst().get();

				if (!inscricao.realizou(proximoModulo)) {
					inscricaoModuloService.salvar(new InscricaoOnlineModulo(inscricao, proximoModulo));
				}

				return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE + "/modulos/" + proximoModulo.getId();
			} else {

				if (!inscricao.isFinalizado()) {
					inscricao.setFinalizado(true);
					inscricao.setDataConclusao(LocalDate.now());
					inscricaoService.salvar(inscricao);
					RedirectUtils.mensagemSucesso(red, "você finalizou esse evento");
				}

				return "redirect:" + UrlUtils.DASHBOARD_EVENTOS_ONLINE;
			}

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
