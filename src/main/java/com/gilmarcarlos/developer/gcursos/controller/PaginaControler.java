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

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.EnderecoUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.EnderecoUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.SexoService;
import com.gilmarcarlos.developer.gcursos.service.TelefoneUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/")
public class PaginaControler {

	private Authentication autenticado;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private TelefoneUnidadeService telefoneUnidadeService;

	@Autowired
	private EnderecoUnidadeService enderecoUnidadeService;

	@Autowired
	private TelefoneUsuarioService telefoneUsuarioService;

	@Autowired
	private EnderecoUsuarioService enderecoUsuarioService;

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
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if(usuarioLogado.isPerfilCompleto()) {
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
		return "dashboard/profile";
		}else {
			return "redirect:/dashboard/complete-cadastro";
		}

	}

	@GetMapping("dashboard/complete-cadastro")
	public String completeCadastro(Model model) {
		Usuario usuarioLogado = getUsuario();
		if(usuarioLogado.isPerfilCompleto()) {
			return "redirect:/dashboard/";
		}
		
		model.addAttribute("usuario", usuarioLogado);
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		return "complete-perfil/complete-cadastro";

	}

	@PostMapping("dashboard/verifica-dados")
	public String verificaDados(@RequestParam("cpf") String cpf, RedirectAttributes model) {

		System.err.println(cpf);
		DadosPessoais dados = dadosService.buscarPor(cpf);
		if (dados != null) {
			model.addFlashAttribute("dadosPessoais", dados);
			model.addFlashAttribute("codigoFuncional", codigoService.buscarPor(dados));
		} else {
			model.addFlashAttribute("novoCpf", cpf);
		}
		return "redirect:/dashboard/complete-cadastro";
	}

	@PostMapping("dashboard/outros/verifica-dados")
	public String verificaOutrosDados(@RequestParam("cpf") String cpf, RedirectAttributes model) {
		System.err.println(cpf);
		DadosPessoais dados = dadosService.buscarPor(cpf);
		if (dados != null) {
			model.addFlashAttribute("outrosDadosPessoais", dados);
			model.addFlashAttribute("outrosCodigoFuncional", codigoService.buscarPor(dados));
		} else {
			model.addFlashAttribute("outroNovoCpf", cpf);
		}
		return "redirect:/dashboard/complete-cadastro";
	}

	@PostMapping("dashboard/confirmar-dados")
	public String confirmarDados(Usuario usuario, @RequestParam("numero") String numero, RedirectAttributes model) {

		salvarUsuario(usuario, numero);
		return "redirect:/dashboard/complete-cadastro";
	}

	@PostMapping("dashboard/outros/confirmar-dados")
	public String confirmarOutrosDados(Usuario usuario, @RequestParam("numero") String numero,
			RedirectAttributes model) {
		salvarUsuario(usuario, numero);
		return "redirect:/dashboard/complete-cadastro";
	}

	@PostMapping("dashboard/outros/novo/confirmar-dados")
	public String confirmarOutrosNovoDados(Usuario usuario, @RequestParam("numero") String numero,
			RedirectAttributes model) {

		salvarUsuario(usuario, numero);
		return "redirect:/dashboard/complete-cadastro";
	}

	@PostMapping("dashboard/novo/confirmar-dados")
	public String confirmarNovoDados(Usuario usuario, @RequestParam("numero") String numero, RedirectAttributes model) {

		salvarUsuario(usuario, numero);

		return "redirect:/dashboard/complete-cadastro";
	}

	// dashboard/redefinir-senha

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

	private void salvarUsuario(Usuario usuario, String numero) {
		dadosService.salvar(usuario.getDadosPessoais());
		codigoService.salvar(usuario.getCodigoFuncional());
		usuarioService.atualizarNome(usuario);
		notificacaoService.salvar(new Notificacao(usuario, "Completou o cadastro", IconeType.INFORMACAO, StatusType.SUCESSO, "seu cadastro foi concluído com sucesso"));
		
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
