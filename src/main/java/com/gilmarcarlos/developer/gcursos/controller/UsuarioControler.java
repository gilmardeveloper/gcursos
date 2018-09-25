package com.gilmarcarlos.developer.gcursos.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
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

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.notifications.Mensagens;
import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioExisteException;
import com.gilmarcarlos.developer.gcursos.security.exception.SenhaNotNullException;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.MensagensHelper;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.MensagensService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.SexoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;
import com.gilmarcarlos.developer.gcursos.utils.NotificacaoUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;
/**
 * Classe de controle para gestão de usuarios
 *  
 * @author Gilmar Carlos
 *
 */
@Controller
@RequestMapping(UrlUtils.DASHBOARD_USUARIO)
public class UsuarioControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private DadosPessoaisService dadosService;

	@Autowired
	private TelefoneUsuarioService telefoneService;

	@Autowired
	private CodigoFuncionalService codigoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private MensagensService mensagensService;

	@Autowired
	private ImagensService imagensService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private InscricaoPresencialService inscricoesService;

	@Autowired
	private EventoPresencialService eventoService;

	@Autowired
	private EventoOnlineService eventoOnlineService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	private Authentication autenticado;

	private final static Integer MAXIMO_PAGES = 20;

	private Page<InscricaoPresencial> getInscricoesPaginacao(Integer page) {
		return inscricoesService.listarTodos(getUsuario().getId(), PageRequest.of(page, MAXIMO_PAGES));
	}

	@GetMapping("/perfil")
	public String painel(Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);

		addBaseCadastroUsaurioAttributes(model);

		addNotificacoesAttributes(model, usuarioLogado);

		return TemplateUtils.DASHBOARD_USUARIO_PROFILE;

	}

	@PostMapping("/redefinir-senha")
	public String redefinirSenha(@RequestParam("senha") String senha) {

		Usuario usuarioLogado = getUsuario();

		try {
			usuarioService.redefinirSenha(usuarioLogado, senha);
			NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Alteração de senha",
					"senha foi alterada com sucesso");
		} catch (SenhaNotNullException e) {
			NotificacaoUtils.error(notificacaoService, usuarioLogado, "Alteração de senha", e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;
	}

	@PostMapping("/dados-pessoais/salvar")
	public String dadosPessoaisSalvar(Usuario usuario, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		try {

			Usuario usuarioAtualizado = usuarioService.atualizarDadosNoEncryptSenha(usuario);
			NotificacaoUtils.sucesso(notificacaoService, usuarioAtualizado, "Dados pessoais alterados",
					"dados foram alterados com sucesso");

		} catch (CpfExisteException | UsuarioExisteException e) {
			NotificacaoUtils.error(notificacaoService, usuarioLogado, "Falha em alterar dados", e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;
	}

	@GetMapping("/dados-pessoais/verificar/{cpf}")
	@ResponseBody
	public ResponseEntity<?> dadosPessoaisVerificarCPf(@PathVariable("cpf") String cpf) {
		
		Check check = new SafeguardCheck();
		
		if (check.elementOf(cpf.trim(), ParametroTipo.CPF).validate().hasError()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		if (dadosService.cpfExiste(cpf, getUsuario())) {
			return new ResponseEntity<>(HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}

	}

	@PostMapping("/dados-profissionais/salvar")
	public String dadosProfissionaisSalvar(CodigoFuncional codigo, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		codigoService.salvar(codigo);
		NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Dados profissionais alterados",
				"dados foram alterados com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;
	}

	@PostMapping("/telefones/salvar")
	public String telefonesSalvar(TelefoneUsuario telefone, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		telefoneService.salvar(telefone);
		NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Telefone salvo", "telefone salvo com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;
	}

	@PostMapping("/telefones/excluir")
	public String telefonesExcluir(@RequestParam("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();
		telefoneService.deletar(id);
		NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Telefone excluído",
				"telefone removido com sucesso");
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;
	}

	@GetMapping("/notificacoes")
	public String notificacoes(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		getUsuario().getNotificaoesNaoLidas().forEach(n -> notificacaoService.foiLida(n));
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("notifications", usuarioLogado.getNotificacoes());

		return TemplateUtils.DASHBOARD_USUARIO_NOTIFICACOES;
	}

	@GetMapping("/notificacoes/deletar")
	public String ndeletarTodas(RedirectAttributes model) {

		getUsuario().getNotificacoes().forEach(n -> notificacaoService.deletar(n.getId()));
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);

		return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/notificacoes";
	}

	@GetMapping("/notificacoes/deletar/{id}")
	public String deletar(@PathVariable("id") Long id, RedirectAttributes model) {

		notificacaoService.deletar(id);
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/notificacoes";
	}

	@GetMapping("/mensagens")
	public String mensagens(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		usuarioLogado.getMensagensNaoLidas().forEach(m -> mensagensService.foiLida(m));

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("messages", usuarioLogado.getMensagens());

		return TemplateUtils.DASHBOARD_USUARIO_MENSAGENS;
	}

	@PostMapping("/mensagens/responder")
	@ResponseBody
	public ResponseEntity<?> novaMensagem(@RequestBody MensagensHelper mensagem) {

		if (mensagem.getDestinatario() == null || mensagem.getTitulo().length() == 0
				|| mensagem.getMensagem().length() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {

			Mensagens temp = mensagensService.buscarPor(mensagem.getDestinatario());
			Usuario usuarioLogado = getUsuario();
			Usuario destinatario = temp.getDestinatario();

			mensagensService.salvar(new Mensagens(destinatario, usuarioLogado,
					mensagem.getTitulo() + temp.getMensagem(), mensagem.getMensagem()));

			return new ResponseEntity<>(HttpStatus.OK);
		}

	}

	@GetMapping("/mensagens/deletar")
	public String mensDeletarTodas(RedirectAttributes model) {
		getUsuario().getMensagens().forEach(m -> mensagensService.deletar(m.getId()));
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/mensagens";
	}

	@GetMapping("/mensagens/deletar/{id}")
	public String msgDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		mensagensService.deletar(id);
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/mensagens";
	}

	@GetMapping("/avatar.png")
	public @ResponseBody byte[] imagemFrente() {
		Blob imagem = getUsuario().getImagens().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 100, 100);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/salvar/imagens")
	public String salvar(@Valid Imagens imagens, BindingResult result, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!result.hasErrors()) {
			if (usuarioLogado.getImagens() != null) {
				imagensService.deletar(getUsuario().getImagens().getId());
			}
			imagens.setUsuario(usuarioLogado);
			imagensService.salvar(imagens);
			NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Alterou o seu avatar",
					"uma nova imagem de avatar foi adicionada");
		} else {
			NotificacaoUtils.error(notificacaoService, usuarioLogado, "Falha ao alterar o seu avatar",
					"tentou adicionar imagem vazia ou outro arquivo como avatar");
		}
		return "redirect:" + UrlUtils.DASHBOARD_USUARIO_PERFIL;

	}

	@GetMapping("/minhas/inscricoes/presenciais")
	public String minhaInscricoes(Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("inscricoes", getInscricoesPaginacao(0));

		return TemplateUtils.DASHBOARD_USUARIO_MINHAS_INSCRICOES_PRESENCIAIS;
	}

	@GetMapping("/minhas/inscricoes/presenciais/pagina/{page}")
	public String minhaInscricoes(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("inscricoes", getInscricoesPaginacao(page));

		return TemplateUtils.DASHBOARD_USUARIO_MINHAS_INSCRICOES_PRESENCIAIS;
	}

	@GetMapping("/eventos/presenciais/progresso")
	public String eventosPresencialProgresso(Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		Page<EventoPresencial> eventos = eventoService.buscarPorUsuario(usuarioLogado.getId(), PageRequest.of(0, MAXIMO_PAGES));

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", eventos);

		return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_PRESENCIAIS_PROGRESSO;
	}
	
	@GetMapping("/eventos/presenciais/progresso/pagina/{page}")
	public String eventosPresencialProgresso(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		Page<EventoPresencial> eventos = eventoService.buscarPorUsuario(usuarioLogado.getId(), PageRequest.of(page, MAXIMO_PAGES));

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", eventos);

		return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_PRESENCIAIS_PROGRESSO;
	}
	
	@GetMapping("/eventos/online/progresso")
	public String eventosOnlineProgresso(Model model) {

		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		Page<EventoOnline> eventos = eventoOnlineService.buscarPorUsuario(usuarioLogado.getId(), PageRequest.of(0, MAXIMO_PAGES));

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", eventos);

		return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_ONLINE_PROGRESSO;
	}
	
	@GetMapping("/eventos/online/progresso/pagina/{page}")
	public String eventosOnlineProgresso(@PathVariable("page") Integer page, Model model) {
		
		Usuario usuarioLogado = getUsuario();
		
		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		Page<EventoOnline> eventos = eventoOnlineService.buscarPorUsuario(usuarioLogado.getId(), PageRequest.of(page, MAXIMO_PAGES));

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("eventos", eventos);

		return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_ONLINE_PROGRESSO;
	}

	@GetMapping("/eventos/online/certificado/{id}")
	public String certificadoOnline(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		EventoOnline evento = eventoOnlineService.buscarPor(id);
		
		if (evento.progresso(usuarioLogado) < 75) {
			RedirectUtils.mensagemError(red, "você precisa ter 75% de progresso para imprimir o certificado");
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/eventos/online/progresso";
		} else {
			model.addAttribute("evento", evento);
			addBaseAttributes(model, usuarioLogado);
			return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_ONLINE_CERTIFICADO;
		}

	}

	@GetMapping("/eventos/presenciais/verificar/certificado/{id}")
	private String verificarAssiduidade(@PathVariable("id") Long id, RedirectAttributes model) {

		EventoPresencial evento = eventoService.buscarPor(id);
		Usuario usuarioLogado = getUsuario();

		if (evento.assiduidade(usuarioLogado) < 75) {
			RedirectUtils.mensagemError(model, "você precisa ter 75% de assiduidade para imprimir o certificado");
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/eventos/presenciais/progresso";
		} else {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/eventos/presenciais/certificado/" + id;
		}

	}

	@GetMapping(value = "/eventos/presenciais/certificado/{id}")
	private String gerarListaPresenca(@PathVariable("id") Long id, Model model, RedirectAttributes red) {

		Usuario usuarioLogado = getUsuario();

		EventoPresencial evento = eventoService.buscarPor(id);
		
		if (evento.assiduidade(usuarioLogado) < 75) {
			RedirectUtils.mensagemError(red, "você precisa ter 75% de assiduidade para imprimir o certificado");
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO + "/eventos/presenciais/progresso";
		} else {
			model.addAttribute("evento", evento);
			addBaseAttributes(model, usuarioLogado);
			return TemplateUtils.DASHBOARD_USUARIO_EVENTOS_PRESENCIAIS_CERTIFICADO;
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

	private void addNotificacoesAttributes(Model model, Usuario usuarioLogado) {

		int naoVisualizadas = usuarioLogado.getNotificaoesNaoLidas().size();
		int visualizadas = usuarioLogado.getNotificaoesLidas().size();

		model.addAttribute("lidas", visualizadas);
		model.addAttribute("naoLidas", naoVisualizadas);
		model.addAttribute("todos", visualizadas + naoVisualizadas);
	}

	private void addBaseCadastroUsaurioAttributes(Model model) {
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
	}
}
