package com.gilmarcarlos.developer.gcursos.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.model.eventos.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.AtividadePresencialService;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.DiaEventoService;
import com.gilmarcarlos.developer.gcursos.service.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialLogService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.LogEvePresencialPaginacaoService;
import com.gilmarcarlos.developer.gcursos.service.PermissoesEventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.ProgramacaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.SexoService;
import com.gilmarcarlos.developer.gcursos.service.SobreService;
import com.gilmarcarlos.developer.gcursos.service.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/eventos/presencial")
public class EventosPresencialControler {

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
	private LogEvePresencialPaginacaoService logEventoPresencialPaginacaoService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private PermissoesEventoPresencialService permissoesEvePresencialService;

	@Autowired
	private InscricaoPresencialService inscricaoPresencialService;

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
			model.addAttribute("eventos", eventoPresencialService.listarTodosPublicados());
			return "dashboard/eventos/eventos-presencial";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	@GetMapping("/detalhes/{id}")
	public String eventoDetalhes(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), 0));

		return "dashboard/eventos/eventos-presencial-detalhes";
	}

	@GetMapping("/detalhes/{id}/pagina/{page}")
	public String eventoDetalhes(@PathVariable("id") Long id, @PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		EventoPresencial evento = eventoPresencialService.buscarPor(id);

		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("evento", evento);
		model.addAttribute("dias", getDiaPaginacao(evento.getProgramacao().getId(), page));

		return "dashboard/eventos/eventos-presencial-detalhes";
	}

	@GetMapping("/atividade/{id}/inscricao")
	public String eventoDetalhesInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);
		
		if(!atividade.usuarioNaoEhResponsavelDoEvento(usuarioLogado)) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "você é o responsável pelo o evento");
			return "redirect:/dashboard/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}
		
		
		if (atividade.podeSeInscrever(usuarioLogado)) {
			
			inscricaoPresencialService.salvar(new InscricaoPresencial(usuarioLogado, atividade));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "inscrição realizada com sucesso");
			return "redirect:/dashboard/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "você não pode se inscrever em atividades de mesmo horário");
			return "redirect:/dashboard/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		}
	}
	
	@GetMapping("/atividade/{id}/cancelar/inscricao")
	public String eventoDetalhesCancelarInscricao(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		AtividadePresencial atividade = atividadePresencialService.buscarPor(id);

		if (atividade.inscrito(usuarioLogado)) {
			
			inscricaoPresencialService.deletar(atividade.getInscricao(usuarioLogado));
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "inscrição foi cancelada com sucesso");
			return "redirect:/dashboard/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "um erro ocorreu");
			return "redirect:/dashboard/eventos/presencial/detalhes/"
					+ atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getId();
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
