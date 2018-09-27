package com.gilmarcarlos.developer.gcursos.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.auth.Permissoes;
import com.gilmarcarlos.developer.gcursos.model.notifications.Mensagens;
import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;
import com.gilmarcarlos.developer.gcursos.model.type.Sexo;
import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.UsuarioDTO;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.EscolaridadeExisteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.SexoExisteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioDeleteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioExisteException;
import com.gilmarcarlos.developer.gcursos.service.auth.AutorizacaoService;
import com.gilmarcarlos.developer.gcursos.service.auth.PermissoesService;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.MensagensHelper;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.MensagensService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.SexoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;
import com.gilmarcarlos.developer.gcursos.utils.NotificacaoUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

/**
 * Classe de controle para gestão de usuarios
 *  
 * @author Gilmar Carlos
 *
 */
@Controller
@RequestMapping(UrlUtils.DASHBOARD_ADMIN_USUARIOS)
public class UsuariosAdminControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private TelefoneUsuarioService telefoneUsuarioService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private AutorizacaoService autorizacaoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private MensagensService mensagensService;

	@Autowired
	private PermissoesService permissoesService;

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

	private Page<Usuario> getUsuarioDepartamentoPaginacao(Long id, Integer page) {
		return usuarioService.buscarPorDepartamento(id, PageRequest.of(page, MAXIMO_PAGES_EVENTOS));
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

	
	@GetMapping("/atuais")
	public String cadastrosCompleto(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (usuarioLogado.temRestricao("departamento")) {
			modelBaseAttributes(usuarioLogado,
					getUsuarioDepartamentoPaginacao(usuarioLogado.getPermissoes().getDepartamento(), 0), model);
		} else {
			modelBaseAttributes(usuarioLogado, getUsuarioPaginacao(0), model);
		}

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/atuais/{id}")
	public String cadastrosCompleto(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (usuarioLogado.temRestricao("departamento")) {
			modelBaseAttributes(usuarioLogado, getUsuarioIdPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id),
					model);
		} else {
			modelBaseAttributes(usuarioLogado, getUsuarioIdPaginacao(id), model);
		}

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/atuais/cargos/{id}")
	public String cadastrosCompletoCargos(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (usuarioLogado.temRestricao("departamento")) {
			modelBaseAttributes(usuarioLogado,
					getUsuarioCargosPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id), model);
		} else {
			modelBaseAttributes(usuarioLogado, getUsuarioCargosPaginacao(id), model);
		}

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/atuais/unidades/{id}")
	public String cadastrosCompletoUnidades(@PathVariable("id") Long id, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (usuarioLogado.temRestricao("departamento")) {
			modelBaseAttributes(usuarioLogado,
					getUsuarioUnidadesPaginacao(usuarioLogado.getPermissoes().getDepartamento(), id), model);
		} else {
			modelBaseAttributes(usuarioLogado, getUsuarioUnidadesPaginacao(id), model);
		}

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/atuais/pagina/{page}")
	public String cadastrosCompleto(@PathVariable("page") Integer page, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		if (usuarioLogado.temRestricao("departamento")) {
			modelBaseAttributes(usuarioLogado,
					getUsuarioDepartamentoPaginacao(usuarioLogado.getPermissoes().getDepartamento(), page), model);
		} else {
			modelBaseAttributes(usuarioLogado, getUsuarioPaginacao(page), model);
		}

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@PostMapping("/autorizacoes/salvar")
	public String usuariosAutorizacoesSalvar(@RequestParam("id") Long id,
			@RequestParam("nomeAutorizacao") String nomeAutorizacao, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("permissoes")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		Usuario usuario = usuarioService.atualizarAutorizacoes(usuarioService.buscarPor(id), nomeAutorizacao);

		NotificacaoUtils.sucesso(notificacaoService, usuario, "Administrador alterou suas permissões",
				"permissões alteradas pelo " + getUsuario().getNome() + ", relogue para ter efeito");

		NotificacaoUtils.sucesso(notificacaoService, getUsuario(), "Alterou as permissões do usuário",
				"Permissões do usuário com email: " + usuario.getEmail() + ",  foram alteradas");

		RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@GetMapping("/novo")
	public String usuariosCompletoNovo(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios") || !usuarioLogado.podeAlterar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		addUnidadesAttributes(usuarioLogado, model);
		addOpcoesAttributes(model);
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_USUARIOS;
	}

	@GetMapping("/novo/funcionario")
	public String usuariosCompletoNovoFuncionario(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios") || !usuarioLogado.podeCriar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		addUnidadesAttributes(usuarioLogado, model);
		addOpcoesAttributes(model);
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_USUARIOS;
	}

	@GetMapping("/novo/outro")
	public String usuariosCompletoNovoOutro(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios") || !usuarioLogado.podeCriar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", getUsuario());

		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("unidades", unidadeService.listarTodos(usuarioLogado.getPermissoes().getDepartamento()));
		} else {
			model.addAttribute("unidades", unidadeService.listarTodos());
		}

		addOpcoesAttributes(model);
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_USUARIOS;
	}

	@GetMapping("/alterar/{id}")
	public String usuariosCompletoAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addFlashAttribute("user", usuarioService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/novo";
	}

	@PostMapping("/salvar")
	public String usuariosSalvar(Usuario usuario, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {

			usuarioService.atualizarDadosNoEncryptSenha(usuario);

			NotificacaoUtils.sucesso(notificacaoService, usuario, "Administrador alterou seus dados",
					"seus foram alterados com sucesso por " + getUsuario().getNome());

			NotificacaoUtils.sucesso(notificacaoService, getUsuario(), "Alterou os dados de um usuário",
					"Alterou os dados do usuário com email: " + usuario.getEmail());

		} catch (UsuarioExisteException | CpfExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@PostMapping("/alterar")
	public String usuariosAlterar(Usuario usuario, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {

			Usuario usuarioAtualizado = usuarioService.atualizarDadosNoEncryptSenha(usuario);

			NotificacaoUtils.sucesso(notificacaoService, usuarioAtualizado, "Administrador alterou seus dados",
					"seus foram alterados com sucesso por " + usuarioLogado.getNome());

			NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Alterou os dados de um usuário",
					"Alterou os dados do usuário com email: " + usuarioAtualizado.getEmail());
			
			model.addFlashAttribute("user", usuarioAtualizado);
			RedirectUtils.mensagemSucesso(model, "salvo com sucesso");

		} catch (UsuarioExisteException | CpfExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/novo";
	}

	@PostMapping("/adicionar")

	public String usuariosAdicionar(Usuario usuario, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {

			Usuario novoUsuario = usuarioService.criarNovo(usuario);

			NotificacaoUtils.sucesso(notificacaoService, novoUsuario, "Administrador criou sua conta",
					"favor altere sua senha");
			NotificacaoUtils.sucesso(notificacaoService, getUsuario(), "Criou um novo usuário",
					"Novo usuário foi criado com email: " + usuario.getEmail() + " senha: zeus_1234@5");

			model.addFlashAttribute("user", novoUsuario);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);

		} catch (Exception e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/novo";
	}

	@GetMapping("/deletar/{id}")
	public String usuariosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		try {

			Usuario user = usuarioService.buscarPor(id);
			NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Deletou um usuário",
					"Usuário com email: " + user.getEmail() + " foi deletado");

			usuarioService.deletar(id);
			
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (UsuarioExisteException | UsuarioDeleteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@PostMapping("/telefones/salvar")
	public String telefonesSalvar(TelefoneUsuario telefone, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		telefoneUsuarioService.salvar(telefone);

		NotificacaoUtils.sucesso(notificacaoService, telefone.getDadosPessoais().getUsuario(),
				"Administrador alterou seus dados", "Um telefone foi salvo por " + getUsuario().getNome());
		NotificacaoUtils.sucesso(notificacaoService, getUsuario(), "Alterou os dados do usuário",
				"Novo telefone para o usuário com email: " + telefone.getDadosPessoais().getUsuario());

		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@PostMapping("/telefones/excluir")
	public String telefonesExcluir(@RequestParam("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("usuarios")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		TelefoneUsuario telefone = telefoneUsuarioService.buscarPor(id);
		Usuario usuario = telefone.getDadosPessoais().getUsuario();

		try {
			telefoneUsuarioService.deletar(id);
			NotificacaoUtils.sucesso(notificacaoService, usuario,
					"Administrador alterou seus dados", "Um telefone foi excluido por " + usuarioLogado.getNome());
			NotificacaoUtils.sucesso(notificacaoService, usuarioLogado, "Alterou os dados do usuário",
					"Excluido telefone do usuário com email: " + usuario.getEmail());
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (UsuarioDeleteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@GetMapping("/opcoes")
	public String usuarioOpcoes(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("opcoesSexuais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("opcoes", sexoService.listarTodos());
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/opcoes/novo")
	public String usuarioOpcoesNovo(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("opcoesSexuais") || !usuarioLogado.podeAlterar("opcoesSexuais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_SEXO;
	}

	@PostMapping("/opcoes/salvar")
	public String usuariosOpcoesSalvar(Sexo sexo, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("opcoesSexuais") || !usuarioLogado.podeAlterar("opcoesSexuais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
		try {

			model.addAttribute("usuario",usuarioLogado);
			model.addAttribute("sexo", sexoService.salvar(sexo));
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		} catch (SexoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_SEXO;
	}

	@GetMapping("/opcoes/alterar/{id}")
	public String usuariosOpcoesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("opcoesSexuais") || !usuarioLogado.podeAlterar("opcoesSexuais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addFlashAttribute("sexo", sexoService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/opcoes/novo";
	}

	@GetMapping("/opcoes/deletar/{id}")
	public String usuariosOpcoesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("opcoesSexuais")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			sexoService.deletar(id);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (SexoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/opcoes";
	}

	@GetMapping("/escolaridades")
	public String usuarioEscolaridades(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("escolaridade")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		addNotificacoesAttributes(usuarioLogado, model);

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_INFO_USUARIOS;
	}

	@GetMapping("/escolaridades/novo")
	public String usuarioEscolaridadesNovo(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("escolaridade") || !usuarioLogado.podeAlterar("escolaridade")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		addNotificacoesAttributes(usuarioLogado, model);
		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_ESCOLARIDADE;
	}

	@PostMapping("/escolaridades/salvar")
	public String usuariosEscolaridadesSalvar(Escolaridade escolaridade, Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("escolaridade") || !usuarioLogado.podeAlterar("escolaridade")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("escolaridade", escolaridadeService.salvar(escolaridade));
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		} catch (EscolaridadeExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_ESCOLARIDADE;
	}

	@GetMapping("/escolaridades/alterar/{id}")
	public String usuariosEscolaridadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("escolaridade") || !usuarioLogado.podeAlterar("escolaridade")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		model.addFlashAttribute("escolaridade", escolaridadeService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/escolaridades/novo";
	}

	@GetMapping("/escolaridades/deletar/{id}")
	public String usuariosEscolaridadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("escolaridade")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		try {
			escolaridadeService.deletar(id);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (EscolaridadeExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/escolaridades";
	}

	@PostMapping("/mensagens/nova")
	@ResponseBody
	public ResponseEntity<?> novaMensagem(@RequestBody MensagensHelper mensagem) {

		if (mensagem.getDestinatario() == null || mensagem.getTitulo().length() == 0
				|| mensagem.getMensagem().length() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {

			Usuario usuarioLogado = getUsuario();
			Usuario destinatario = usuarioService.buscarPor(mensagem.getDestinatario());
			mensagensService
					.salvar(new Mensagens(destinatario, usuarioLogado, mensagem.getTitulo(), mensagem.getMensagem()));

			return new ResponseEntity<>(HttpStatus.OK);
		}

	}

	@GetMapping("/permissoes/{id}")
	private String permissoesUsuarios(@PathVariable("id") Long id, Model model) {

		Usuario user = usuarioService.buscarPor(id);
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("permissoes")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		model.addAttribute("usuario", usuarioLogado);
		addNotificacoesAttributes(usuarioLogado, model);

		model.addAttribute("user", user);
		model.addAttribute("departamentos", departamentoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_USUARIOS_BASE_CADASTRO_PERMISSOES;

	}

	@PostMapping("/permissoes/salvar")
	private String permissoesUsuariosSalvar(Permissoes permissoes, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("permissoes")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		permissoesService.salvar(permissoes);
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_USUARIOS + "/atuais";
	}

	@GetMapping(value = "/dto", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<UsuarioDTO> moduloDTO() {
		return usuarioService.listarUsuariosDTO(getUsuario());
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
	
	private void modelBaseAttributes(Usuario usuarioLogado, Page<Usuario> paginacao, Model model) {

		model.addAttribute("atuais", paginacao);
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("autorizacoes", autorizacaoService.listarTodos());

		addUnidadesAttributes(usuarioLogado, model);
		addOpcoesAttributes(model);
		addNotificacoesAttributes(usuarioLogado, model);

	}

	private void addOpcoesAttributes(Model model) {
		model.addAttribute("formacoes", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
	}

	private void addUnidadesAttributes(Usuario usuarioLogado, Model model) {
		model.addAttribute("cargos", cargoService.listarTodos());
		if (usuarioLogado.temRestricao("departamento")) {
			model.addAttribute("unidades", unidadeService.listarTodos(usuarioLogado.getPermissoes().getDepartamento()));
		} else {
			model.addAttribute("unidades", unidadeService.listarTodos());
		}
	}

	private void addNotificacoesAttributes(Usuario usuarioLogado, Model model) {
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		model.addAttribute("mensagens", usuarioLogado.getMensagensNaoLidas());
	}
}
