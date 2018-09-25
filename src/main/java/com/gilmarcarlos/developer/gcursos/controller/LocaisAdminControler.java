package com.gilmarcarlos.developer.gcursos.controller;

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

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;
import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;
import com.gilmarcarlos.developer.gcursos.model.locais.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.model.locais.TelefoneUnidade;
import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.CargoExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.CargoNotFoundException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.DepartamentoExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.DepartamentoNotFoundException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.UnidadeExisteException;
import com.gilmarcarlos.developer.gcursos.model.locais.exceptions.UnidadeNotFoundException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.locais.EnderecoUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.locais.TelefoneUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;
import com.gilmarcarlos.developer.gcursos.utils.TemplateUtils;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

/**
 * Classe de controle para gestão de cargos, unidades de trabalho e departamento
 *  
 * @author Gilmar Carlos
 *
 */
@Controller
@RequestMapping(UrlUtils.DASHBOARD_ADMIN_LOCAIS)
public class LocaisAdminControler {

	@Autowired
	private UsuarioService usuarioService;

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

	private Authentication autenticado;

	@GetMapping("/unidades")
	public String unidades(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		addBaseAttributes(model, usuarioLogado);

		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("deps", departamentoService.listarTodos());

		seUnidadeNaoTemEnderecoOuTelefone();

		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_INFO_LOCAIS;
	}

	@GetMapping("/unidades/novo")
	public String unidadesNovo(Model model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("departamentos", departamentoService.listarTodos());

		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_UNIDADES;
	}

	@PostMapping("/unidades/salvar")
	public String unidadesSalvar(UnidadeTrabalho unidade, RedirectAttributes model) {

		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("unidades") || !usuarioLogado.podeAlterar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		try {
			unidadeService.salvar(unidade);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		} catch (UnidadeExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}


		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades";
	}

	@PostMapping("/unidades/salvar/novo")
	public String unidadesSalvarNovo(UnidadeTrabalho unidade, @RequestParam("numero") String numero, Model model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("unidades") || !usuarioLogado.podeAlterar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
				
		try {
			
			UnidadeTrabalho temp = unidadeService.salvar(unidade);
			
			if (numero.length() > 8) {
				TelefoneUnidade telefone = new TelefoneUnidade();
				telefone.setNumero(numero);
				telefone.setSetor("Recepção");
				telefone.setUnidadeTrabalho(temp);
				telefoneUnidadeService.salvar(telefone);
			}
			
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		
		} catch (UnidadeExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("unidade", unidade);

		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_UNIDADES;
	}

	@GetMapping("/unidades/alterar/{id}")
	public String unidadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("unidade", unidadeService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades/novo";
	}

	@GetMapping("/unidades/deletar/{id}")
	public String unidadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
				
		try {
			unidadeService.deletar(id);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (UnidadeNotFoundException | UnidadeExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades";
	}

	@PostMapping("/unidades/telefones/salvar")
	public String unidadesTelefonesSalvar(TelefoneUnidade telefone, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		telefoneUnidadeService.salvar(telefone);
		RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);

		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades";
	}

	@PostMapping("/unidades/telefones/excluir")
	public String unidadesTelefonesExcluir(TelefoneUnidade telefone, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeAlterar("unidades")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		
		if (telefone.getUnidadeTrabalho().podeExcluirTelefone()) {
			telefoneUnidadeService.deletar(telefone.getId());
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades";
		} else {
			RedirectUtils.mensagemError(model, "para excluir, a unidade precisa ter no mínimo dois telefones");
			return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/unidades";
		}

	}

	@GetMapping("/departamentos")
	public String departamentos(Model model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("departamentos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("departamentos", departamentoService.listarTodos());
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_INFO_LOCAIS;
	}

	@GetMapping("/departamentos/novo")
	public String departamentosNovo(Model model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("departamentos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_DEPARTAMENTOS;
	}

	@PostMapping("/departamentos/salvar")
	public String departamentosSalvar(Departamento departamento, Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("departamentos") || !usuarioLogado.podeAlterar("departamentos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		
		try {
			model.addAttribute("departamento", departamentoService.salvar(departamento));
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		} catch (DepartamentoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_DEPARTAMENTOS;
	}

	@GetMapping("/departamentos/alterar/{id}")
	public String departamentosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("departamentos") || !usuarioLogado.podeAlterar("departamentos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		model.addFlashAttribute("departamento", departamentoService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/departamentos/novo";
	}

	@GetMapping("/departamentos/deletar/{id}")
	public String departamentosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("departamentos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		try {
			departamentoService.deletar(id);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (DepartamentoNotFoundException | DepartamentoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/departamentos";
	}

	@GetMapping("/cargos")
	public String cargos(Model model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeVisualizar("cargos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		model.addAttribute("cargos", cargoService.listarTodos());
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_INFO_LOCAIS;
	}

	@GetMapping("/cargos/novo")
	public String cargosNovo(Model model) {
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("cargos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_CARGOS;
	}

	@PostMapping("/cargos/salvar")
	public String cargosSalvar(Cargo cargo, Model model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("cargos") || !usuarioLogado.podeAlterar("cargos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		addBaseAttributes(model, usuarioLogado);
		try {
			model.addAttribute("cargo", cargoService.salvar(cargo));
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
		} catch (CargoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		
		return TemplateUtils.DASHBOARD_ADMIN_LOCAIS_BASE_CADASTRO_CARGOS;
	}

	@GetMapping("/cargos/alterar/{id}")
	public String cargosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeCriar("cargos") || !usuarioLogado.podeAlterar("cargos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
				
		model.addFlashAttribute("cargo", cargoService.buscarPor(id));
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/cargos/novo";
	}

	@GetMapping("/cargos/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		
		Usuario usuarioLogado = getUsuario();

		if (!usuarioLogado.podeDeletar("cargos")) {
			return "redirect:" + UrlUtils.DASHBOARD_USUARIO_DASHBOARD;
		}

		if (!usuarioLogado.isPerfilCompleto()) {
			return "redirect:" + UrlUtils.DASHBOARD_COMPLETE_CADASTRO;
		}
		
		try {
			cargoService.deletar(id);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_REMOVER);
		} catch (CargoNotFoundException | CargoExisteException e) {
			RedirectUtils.mensagemError(model, e.getMessage());
		}
		return "redirect:" + UrlUtils.DASHBOARD_ADMIN_LOCAIS + "/cargos";
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
	
	private void seUnidadeNaoTemEnderecoOuTelefone() {
		for (UnidadeTrabalho u : unidadeService.listarTodosSemFones()) {
			seUnidadeNaoTemTelefone(u);
			seUnidadeNaoTemEndereco(u);
		}
	}

	private void seUnidadeNaoTemTelefone(UnidadeTrabalho u) {
		
		if (u.getTelefones().isEmpty()) {
			TelefoneUnidade telefone = new TelefoneUnidade();
			telefone.setNumero("(00)0000-0000");
			telefone.setSetor("-");
			telefone.setUnidadeTrabalho(u);
			telefoneUnidadeService.salvar(telefone);

		}
	}

	private void seUnidadeNaoTemEndereco(UnidadeTrabalho u) {
		if (u.getEndereco() == null) {
			EnderecoUnidade endereco = new EnderecoUnidade();
			endereco.setUnidadeTrabalho(u);
			enderecoUnidadeService.salvar(endereco);
		}
	}
}
