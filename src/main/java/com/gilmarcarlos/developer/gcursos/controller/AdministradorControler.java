package com.gilmarcarlos.developer.gcursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.Cargo;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.Departamento;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUnidade;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.Sexo;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.AutorizacaoService;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.EnderecoUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.NomeColaboradorService;
import com.gilmarcarlos.developer.gcursos.service.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.SexoService;
import com.gilmarcarlos.developer.gcursos.service.TelefoneUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/")
public class AdministradorControler {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NomeColaboradorService colaboradorService;
	
	@Autowired	
	private UnidadeTrabalhoService unidadeService;
	
	@Autowired
	private DepartamentoService departamentoService;
	
	@Autowired
	private CargoService cargoService;
	
	@Autowired
	private EnderecoUnidadeService enderecoUnidadeService;
	
	@Autowired
	private TelefoneUnidadeService telefoneUnidadeService;
	
	@Autowired
	private TelefoneUsuarioService telefoneUsuarioService;
	
	@Autowired
	private SexoService sexoService;
	
	@Autowired
	private EscolaridadeService escolaridadeService;
	
	@Autowired
	private AutorizacaoService autorizacaoService;
	
	@Autowired
	private DadosPessoaisService dadosService;

	@Autowired
	private CodigoFuncionalService codigoService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	private Authentication autenticado;

	@GetMapping("usuarios/atuais")
	public String cadastrosCompleto(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("atuais", usuarioService.listarCadastrosCompleots());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("formacoes", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("autorizacoes", autorizacaoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-usuarios";
	}
	
	@PostMapping("usuarios/autorizacoes/salvar")
	public String usuariosAutorizacoesSalvar(@RequestParam("id") Long id, @RequestParam("nomeAutorizacao") String nomeAutorizacao , RedirectAttributes model) {
		
		Usuario usuario = usuarioService.atualizarAutorizacoes(usuarioService.buscarPor(id), nomeAutorizacao);
		
		notificacaoService.salvar(new Notificacao(usuario, "Administrador alterou suas permissões", IconeType.INFORMACAO, StatusType.SUCESSO, "permissões alteradas pelo " + getUsuario().getNome() + ", relogue para ter efeito"));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou as permissões do usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Permissões do usuário com email: " + usuario.getEmail() + ",  foram alteradas"));
		
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		return "redirect:/dashboard/admin/usuarios/atuais";
	}
	
	@GetMapping("usuarios/novo")
	public String usuariosCompletoNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("atuais", usuarioService.listarCadastrosCompleots());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("formacoes", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-usuario";
	}
	
	@GetMapping("usuarios/novo/funcionario")
	public String usuariosCompletoNovoFuncionario(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("formacoes", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-usuario";
	}
	
	@GetMapping("usuarios/novo/outro")
	public String usuariosCompletoNovoOutro(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("formacoes", escolaridadeService.listarTodos());
		model.addAttribute("sexos", sexoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-usuario";
	}
	
	@GetMapping("usuarios/alterar/{id}")
	public String usuariosCompletoAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("user", usuarioService.buscarPor(id));
		return "redirect:/dashboard/admin/usuarios/novo";
	}
	
	@PostMapping("usuarios/salvar")
	public String usuariosSalvar(Usuario usuario, RedirectAttributes model) {
		
		dadosService.salvar(usuario.getDadosPessoais());
		codigoService.salvar(usuario.getCodigoFuncional());
		usuarioService.atualizarDadosNoEncryptSenha(usuario);
		notificacaoService.salvar(new Notificacao(usuario, "Administrador alterou seus dados", IconeType.INFORMACAO, StatusType.SUCESSO, "seus foram alterados com sucesso por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou os dados de um usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Alterou os dados do usuário com email: " + usuario.getEmail()));
		//PushControler.notificacoes(usuario.getId(), "Adminsitrador alterou seus dados...");
		
		return "redirect:/dashboard/admin/usuarios/atuais";
	}
	
	@PostMapping("usuarios/alterar")
	public String usuariosAlterar(Usuario usuario, RedirectAttributes model) {
		
		dadosService.salvar(usuario.getDadosPessoais());
		codigoService.salvar(usuario.getCodigoFuncional());
		notificacaoService.salvar(new Notificacao(usuario, "Administrador alterou seus dados", IconeType.INFORMACAO, StatusType.SUCESSO, "seus foram alterados com sucesso por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou os dados de um usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Alterou os dados do usuário com email: " + usuario.getEmail()));
		
		model.addFlashAttribute("user", usuarioService.atualizarDadosNoEncryptSenha(usuario));
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		return "redirect:/dashboard/admin/usuarios/novo";
	}
	
	@PostMapping("usuarios/adicionar")
	public String usuariosAdicionar(Usuario usuario, RedirectAttributes model) {
		
		DadosPessoais dados = dadosService.salvar(usuario.getDadosPessoais());
		CodigoFuncional codigo = codigoService.salvar(usuario.getCodigoFuncional());
		Usuario novoUsuario = usuarioService.criarNovo(usuario);
		
		dados.setUsuario(novoUsuario);
		codigo.setUsuario(novoUsuario);
		
		dadosService.salvar(dados);
		codigoService.salvar(codigo);
		
		notificacaoService.salvar(new Notificacao(novoUsuario, "Administrador criou sua conta", IconeType.INFORMACAO, StatusType.SUCESSO, "favor altere sua senha"));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Criou um novo usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Novo usuário foi criado com email: " + usuario.getEmail() + " senha: zeus_1234@5"));
		
		model.addFlashAttribute("user", novoUsuario);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		return "redirect:/dashboard/admin/usuarios/novo";
	}
	
	@GetMapping("usuarios/deletar/{id}")
	public String usuariosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario user = usuarioService.buscarPor(id);
		List<TelefoneUsuario> telefones = user.getDadosPessoais().getTelefones();
		if(!telefones.isEmpty()) {
			telefones.forEach(t -> telefoneUsuarioService.deletar(t.getId()));
		}
		notificacaoService.salvar(new Notificacao(getUsuario(), "Deletou um usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Usuário com email: " + user.getEmail() + " foi deletado"));
		
		dadosService.deletar(user.getDadosPessoais().getId());
		codigoService.deletar(user.getCodigoFuncional().getId());
		usuarioService.deletar(id);
		
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "removido com sucesso");
		
		return "redirect:/dashboard/admin/usuarios/atuais";
	}
	
	@PostMapping("usuarios/telefones/salvar")
	public String telefonesSalvar(TelefoneUsuario telefone, RedirectAttributes model) {
		
		telefoneUsuarioService.salvar(telefone);
		notificacaoService.salvar(new Notificacao(telefone.getDadosPessoais().getUsuario(), "Administrador alterou seus dados", IconeType.INFORMACAO, StatusType.SUCESSO, "Um telefone foi salvo por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou os dados do usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Novo telefone para o usuário com email: " + telefone.getDadosPessoais().getUsuario()));
		
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		return "redirect:/dashboard/admin/usuarios/atuais";
	}
	
	@PostMapping("usuarios/telefones/excluir")
	public String telefonesExcluir(@RequestParam("id") Long id, RedirectAttributes model) {
		
		TelefoneUsuario telefone = telefoneUsuarioService.buscarPor(id); 
		notificacaoService.salvar(new Notificacao(telefone.getDadosPessoais().getUsuario(), "Administrador alterou seus dados", IconeType.INFORMACAO, StatusType.SUCESSO, "Um telefone foi excluido por " + getUsuario().getNome()));
		notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou os dados do usuário", IconeType.INFORMACAO, StatusType.SUCESSO, "Excluido telefone do usuário com email: " + telefone.getDadosPessoais().getUsuario()));
		telefoneUsuarioService.deletar(id);
		
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "removido com sucesso");
		
		return "redirect:/dashboard/admin/usuarios/atuais";
	}
	
	@GetMapping("usuarios/antigos")
	public String usuario(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("antigos", colaboradorService.listarTodos());
		return "dashboard/admin/base-info-usuarios";
	}
	
	@GetMapping("usuarios/opcoes")
	public String usuarioOpcoes(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("opcoes", sexoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-usuarios";
	}
	
	@GetMapping("usuarios/opcoes/novo")
	public String usuarioOpcoesNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-sexo";
	}
	
	@PostMapping("usuarios/opcoes/salvar")
	public String usuariosOpcoesSalvar(Sexo sexo, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("sexo", sexoService.salvar(sexo));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/base-cadastro-sexo";
	}
	
	@GetMapping("usuarios/opcoes/alterar/{id}")
	public String usuariosOpcoesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("sexo", sexoService.buscarPor(id));
		return "redirect:/dashboard/admin/usuarios/opcoes/novo";
	}
	
	@GetMapping("usuarios/opcoes/deletar/{id}")
	public String usuariosOpcoesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		sexoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/usuarios/opcoes";
	}
	
	@GetMapping("usuarios/escolaridades")
	public String usuarioEscolaridades(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("escolaridades", escolaridadeService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-usuarios";
	}
	
	@GetMapping("usuarios/escolaridades/novo")
	public String usuarioEscolaridadesNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-escolaridade";
	}
	
	@PostMapping("usuarios/escolaridades/salvar")
	public String usuariosEscolaridadesSalvar(Escolaridade escolaridade, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("escolaridade", escolaridadeService.salvar(escolaridade));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/base-cadastro-escolaridade";
	}
	
	@GetMapping("usuarios/escolaridades/alterar/{id}")
	public String usuariosEscolaridadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("escolaridade", escolaridadeService.buscarPor(id));
		return "redirect:/dashboard/admin/usuarios/escolaridades/novo";
	}
	
	@GetMapping("usuarios/escolaridades/deletar/{id}")
	public String usuariosEscolaridadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		escolaridadeService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/usuarios/escolaridades";
	}
	
	@GetMapping("unidades")
	public String unidades(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("deps", departamentoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-locais";
	}
	
	@GetMapping("unidades/novo")
	public String unidadesNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamentos", departamentoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-unidades";
	}
	
	@PostMapping("unidades/salvar")
	public String unidadesSalvar(UnidadeTrabalho unidade, RedirectAttributes model) {
		
	
		EnderecoUnidade endereco = unidade.getEndereco();
		enderecoUnidadeService.salvar(endereco);
		unidadeService.salvar(unidade);
		
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		
		return "redirect:/dashboard/admin/unidades";
	}
	
	@PostMapping("unidades/salvar/novo")
	public String unidadesSalvarNovo(UnidadeTrabalho unidade, @RequestParam("numero") String numero, Model model) {
		
		UnidadeTrabalho temp = new UnidadeTrabalho();
		
		temp.setNome(unidade.getNome());
		temp.setDepartamento(unidade.getDepartamento());
		temp.setEmail(unidade.getEmail());
		temp.setGerente(unidade.getGerente());
		temp.setQtdFuncionarios(unidade.getQtdFuncionarios());
		unidadeService.salvar(temp);
		
		EnderecoUnidade endereco = unidade.getEndereco();
		endereco.setUnidadeTrabalho(temp);
		enderecoUnidadeService.salvar(endereco);
		
		if(numero.length() > 8) {
			TelefoneUnidade telefone = new TelefoneUnidade();
			telefone.setNumero(numero);
			telefone.setSetor("Recepção");
			telefone.setUnidadeTrabalho(temp);
			telefoneUnidadeService.salvar(telefone);
		}
		
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidade", unidade);
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/base-cadastro-unidades";
	}
	
	@GetMapping("unidades/alterar/{id}")
	public String unidadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("unidade", unidadeService.buscarPor(id));
		return "redirect:/dashboard/admin/unidades/novo";
	}
	
	@GetMapping("unidades/deletar/{id}")
	public String unidadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		telefoneUnidadeService.deletarByUnidade(id);
		enderecoUnidadeService.deletarByUnidade(id);
		unidadeService.deletar(id);
		
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/unidades";
	}
	
	@PostMapping("unidades/telefones/salvar")
	public String unidadesTelefonesSalvar(TelefoneUnidade telefone, RedirectAttributes model) {
		telefoneUnidadeService.salvar(telefone);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");
		
		return "redirect:/dashboard/admin/unidades";
	}
	
	@PostMapping("unidades/telefones/excluir")
	public String unidadesTelefonesExcluir(TelefoneUnidade telefone, RedirectAttributes model) {
		if(telefone.getUnidadeTrabalho().podeExcluirTelefone()) {
			telefoneUnidadeService.deletar(telefone.getId());
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "excluido com sucesso");
			return "redirect:/dashboard/admin/unidades";
		}else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "para excluir, a unidade precisa ter no mínimo dois telefones");
			return "redirect:/dashboard/admin/unidades";
		}
		
	}
	
	@GetMapping("departamentos")
	public String departamentos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamentos", departamentoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-locais";
	}
	
	@GetMapping("departamentos/novo")
	public String departamentosNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-departamentos";
	}
	
	@PostMapping("departamentos/salvar")
	public String departamentosSalvar(Departamento departamento, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamento", departamentoService.salvar(departamento));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/base-cadastro-departamentos";
	}
	
	@GetMapping("departamentos/alterar/{id}")
	public String departamentosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("departamento", departamentoService.buscarPor(id));
		return "redirect:/dashboard/admin/departamentos/novo";
	}
	
	@GetMapping("departamentos/deletar/{id}")
	public String departamentosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		departamentoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/departamentos";
	}
	
	@GetMapping("cargos")
	public String cargos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-info-locais";
	}
	
	@GetMapping("cargos/novo")
	public String cargosNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/base-cadastro-cargos";
	}
	
	@PostMapping("cargos/salvar")
	public String cargosSalvar(Cargo cargo, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargo", cargoService.salvar(cargo));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/base-cadastro-cargos";
	}
	
	@GetMapping("cargos/alterar/{id}")
	public String cargosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("cargo", cargoService.buscarPor(id));
		return "redirect:/dashboard/admin/cargos/novo";
	}
	
	@GetMapping("cargos/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		cargoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/cargos";
	}
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
	

	
}
