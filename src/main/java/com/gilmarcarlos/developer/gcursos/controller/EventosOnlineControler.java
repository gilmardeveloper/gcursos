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
import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.AtividadeOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.ModuloService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.PermissoesEventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

@Controller
@RequestMapping("/dashboard/eventos/online")
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
	private AtividadeOnlineService atividadeService;

	@Autowired
	private ImagensService imagensService;

	@Autowired
	private InscricaoOnlineService inscricaoService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 3;
	private final static Integer MAXIMO_PAGES_EVENTOS = 20;

	private Page<EventoOnline> getEventoPaginacao(Integer page) {
		return eventoOnlineService.listarTodos(PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
	}

	@GetMapping({ "/", "" })
	public String eventosOnline(Model model) {

		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("eventos", eventoOnlineService.listarTodosPublicados());
			return "dashboard/eventos/eventos-online";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@GetMapping("/{id}/verifica/usuario/tem/codigo")
	@ResponseBody
	public ResponseEntity inscricoesEventoOnlinePresente(@PathVariable("id") Long id) {

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
			return "redirect:/dashboard/eventos/online/detalhes/" + id;

		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "código inválido");
			return "redirect:/dashboard/eventos/online";
		}
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você não tem permissão");
			return "redirect:/dashboard/eventos/online";
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você não tem permissão para acessar esse evento");
			return "redirect:/dashboard/eventos/online";
		}

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);

		return "dashboard/eventos/eventos-online-detalhes";
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model,
			RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();
		EventoOnline evento = eventoOnlineService.buscarPor(id);

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você não tem permissão");
			return "redirect:/dashboard/eventos/online";
		}

		if (evento.getPermissoes().precisaDeCodigo() && !evento.getPermissoes().temCodigo(usuarioLogado)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você não tem permissão para acessar esse evento");
			return "redirect:/dashboard/eventos/online";
		}

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);

		return "dashboard/eventos/eventos-online-detalhes";
	}

	@GetMapping("/iniciar/{id}")
	public String iniciarEvento(@PathVariable("id") Long id, RedirectAttributes red) {

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		Usuario usuarioLogado = getUsuario();

		if (usuarioLogado.ehResponsavel(evento)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você é o responsável pelo evento");
			return "redirect:/dashboard/eventos/online/detalhes/" + id;
		}

		if (!evento.getPermissoes().valida(usuarioLogado)) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message", "você não tem permissão");
			return "redirect:/dashboard/eventos/online";
		}

		if (evento.isInscrito(usuarioLogado)) {
			InscricaoOnline inscricao = evento.getInscricao(usuarioLogado);
			return "redirect:/dashboard/eventos/online/modulos/" + inscricao.ultimoModulo().getId() + "/atividade/"
					+ inscricao.ultimaAtividade().getPosicao();
		} else {
			inscricaoService.salvar(new InscricaoOnline(usuarioLogado, evento));
			return "redirect:/dashboard/eventos/online/iniciar/" + id;
		}

	}

	@GetMapping("/desistir/{id}")
	public String eventoDesistir(@PathVariable("id") Long id, RedirectAttributes red) {

		InscricaoOnline inscricao = eventoOnlineService.buscarPor(id).getInscricao(getUsuario());
		inscricaoService.deletar(inscricao.getId());

		return "redirect:/dashboard/eventos/online/detalhes/" + id;
	}

	@GetMapping("/modulos/{id}")
	public String modulos(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Modulo modulo = moduloService.buscarPor(id);

		if (modulo.getAtividades().isEmpty()) {
			red.addFlashAttribute("alert", "alert alert-fill-danger");
			red.addFlashAttribute("message",
					"este módulo não tem atividades, favor entre em contato com o responsável");
			return "redirect:/dashboard/admin/eventos/online/detalhes/" + modulo.getEventoOnline().getId();
		} else {
			return "redirect:/dashboard/eventos/online/modulos/" + id + "/atividade/"
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
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("modulo", modulo);
		model.addAttribute("atividade", atividade);
		model.addAttribute("evento", modulo.getEventoOnline());

		return "dashboard/eventos/eventos-online-modulos-detalhes";

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
			List<Modulo> modulos = inscricao.getModulos();
			modulos.add(modulo);
			inscricao.setModulos(modulos);
			inscricaoService.salvar(inscricao);
		}

		if (!inscricao.realizou(atividade)) {
			List<AtividadeOnline> atividades = inscricao.getAtividades();
			atividades.add(atividade);
			inscricao.setAtividades(atividades);
			inscricaoService.salvar(inscricao);
		}

		if (posicao < modulo.getAtividades().size()) {

			AtividadeOnline proximaAtividade = modulo.getAtividades().stream().filter(a -> a.getPosicao() > posicao)
					.findFirst().get();
			return "redirect:/dashboard/eventos/online/modulos/" + id + "/atividade/" + proximaAtividade.getPosicao();

		} else {

			if (modulo.getPosicao() < evento.getModulos().size()) {

				Modulo proximoModulo = evento.getModulos().stream().filter(m -> m.getPosicao() > modulo.getPosicao())
						.findFirst().get();
				
				if (!inscricao.realizou(proximoModulo)) {
					List<Modulo> modulos = inscricao.getModulos();
					modulos.add(proximoModulo);
					inscricao.setModulos(modulos);
					inscricaoService.salvar(inscricao);
				}

				return "redirect:/dashboard/eventos/online/modulos/" + proximoModulo.getId();
			} else {
				
				if(!inscricao.isFinalizado()) {
					inscricao.setFinalizado(true);
					inscricao.setDataConclusao(LocalDate.now());
					inscricaoService.salvar(inscricao);
					red.addFlashAttribute("alert", "alert alert-fill-success");
					red.addFlashAttribute("message", "você finalizou esse evento");
				}
				
				return "redirect:/dashboard/eventos/online";
			}

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
