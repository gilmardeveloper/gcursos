package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.SexoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;

@Controller
@RequestMapping("/")
public class UsuarioCompleteCadastroControler {

	private Authentication autenticado;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private TelefoneUsuarioService telefoneUsuarioService;

	@Autowired
	private DadosPessoaisService dadosService;

	@Autowired
	private CodigoFuncionalService codigoService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private NotificacaoService notificacaoService;
	
	@GetMapping
	public String home() {
		return "login/login-template";
	}

	@GetMapping("dashboard/")
	public String painel(Model model) {
	
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		} else {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}
	}

	@GetMapping("dashboard/complete-cadastro")
	public String completeCadastro(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD;
		}

		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());

		return TemplateUtils.COMPLETE_PERFIL_COMPLETE_CADASTRO;

	}

	@PostMapping("dashboard/verifica-dados")
	public String verificaDados(@RequestParam("cpf") String cpf, RedirectAttributes model) {

		Check check = new SafeguardCheck();

		if (check.elementOf(cpf.trim(), ParametroTipo.CPF).validate().hasError()) {
			RedirectUtils.mensagemError(model, "cpf inválido");
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		DadosPessoais dados = dadosService.buscarPor(cpf);
		if (dados != null) {

			if (dados.getUsuario() != null) {
				RedirectUtils.mensagemError(model, "já existe um usuário cadastrado com esse cpf");
				return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
			}

			model.addFlashAttribute("dadosPessoais", dados);
			model.addFlashAttribute("codigoFuncional", codigoService.buscarPor(dados));
		} else {
			model.addFlashAttribute("novoCpf", cpf);
		}

		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	@PostMapping("dashboard/outros/verifica-dados")
	public String verificaOutrosDados(@RequestParam("cpf") String cpf, RedirectAttributes model) {

		Check check = new SafeguardCheck();

		if (check.elementOf(cpf.trim(), ParametroTipo.CPF).validate().hasError()) {
			RedirectUtils.mensagemError(model, "cpf inválido");
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		DadosPessoais dados = dadosService.buscarPor(cpf);
		if (dados != null) {

			if (dados.getUsuario() != null) {
				RedirectUtils.mensagemError(model, "já existe um usuário cadastrado com esse cpf");
				return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
			}

			model.addFlashAttribute("outrosDadosPessoais", dados);
			model.addFlashAttribute("outrosCodigoFuncional", codigoService.buscarPor(dados));
		} else {
			model.addFlashAttribute("outroNovoCpf", cpf);
		}
		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	@PostMapping("dashboard/confirmar-dados")
	public String confirmarDados(Usuario usuario, @RequestParam("numero") String numero, RedirectAttributes model) {

		salvarUsuario(usuario, numero);

		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	@PostMapping("dashboard/outros/confirmar-dados")
	public String confirmarOutrosDados(Usuario usuario, @RequestParam("numero") String numero,
			RedirectAttributes model) {

		salvarUsuario(usuario, numero);

		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	@PostMapping("dashboard/outros/novo/confirmar-dados")
	public String confirmarOutrosNovoDados(Usuario usuario, @RequestParam("numero") String numero,
			RedirectAttributes model) {

		salvarUsuario(usuario, numero);

		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	@PostMapping("dashboard/novo/confirmar-dados")
	public String confirmarNovoDados(Usuario usuario, @RequestParam("numero") String numero, RedirectAttributes model) {

		salvarUsuario(usuario, numero);

		return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

	private void salvarUsuario(Usuario usuario, String numero) {

		dadosService.salvarD(usuario.getDadosPessoais());
		codigoService.salvar(usuario.getCodigoFuncional());
		usuarioService.atualizarNome(usuario);

		notificacaoService.salvar(new Notificacao(usuario, "Completou o cadastro", IconeTypeUtils.INFORMACAO,
				StatusTypeUtils.SUCESSO, "seu cadastro foi concluído com sucesso"));

		if (numero.length() >= 8) {
			TelefoneUsuario telefone = telefoneUsuarioService.buscarPor(numero);
			if (telefone != null) {
				telefone.setNumero(numero);
				telefone.setDadosPessoais(usuario.getDadosPessoais());
				telefoneUsuarioService.salvar(telefone);
			} else {
				telefone = new TelefoneUsuario();
				telefone.setNumero(numero);
				telefone.setDadosPessoais(usuario.getDadosPessoais());
				telefoneUsuarioService.salvar(telefone);
			}
		}
	}

}
