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
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.locais.CargoService;
import com.gilmarcarlos.developer.gcursos.service.locais.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.locais.EnderecoUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.locais.TelefoneUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.locais.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.UrlUtils;

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
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("deps", departamentoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());

		for (UnidadeTrabalho u : unidadeService.listarTodosSemFones()) {

			if (u.getTelefones().isEmpty()) {
				TelefoneUnidade telefone = new TelefoneUnidade();
				telefone.setNumero("(00)0000-0000");
				telefone.setSetor("-");
				telefone.setUnidadeTrabalho(u);
				telefoneUnidadeService.salvar(telefone);

			}

			if (u.getEndereco() == null) {
				EnderecoUnidade endereco = new EnderecoUnidade();
				endereco.setUnidadeTrabalho(u);
				enderecoUnidadeService.salvar(endereco);
			}

		}

		return "dashboard/admin/locais/base-info-locais";
	}

	@GetMapping("/unidades/novo")
	public String unidadesNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamentos", departamentoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/locais/base-cadastro-unidades";
	}

	@PostMapping("/unidades/salvar")
	public String unidadesSalvar(UnidadeTrabalho unidade, RedirectAttributes model) {

		EnderecoUnidade endereco = unidade.getEndereco();
		enderecoUnidadeService.salvar(endereco);
		unidadeService.salvar(unidade);

		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/unidades";
	}

	@PostMapping("/unidades/salvar/novo")
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

		if (numero.length() > 8) {
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
		return "dashboard/admin/locais/base-cadastro-unidades";
	}

	@GetMapping("/unidades/alterar/{id}")
	public String unidadesAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("unidade", unidadeService.buscarPor(id));
		return "redirect:/dashboard/admin/unidades/novo";
	}

	@GetMapping("/unidades/deletar/{id}")
	public String unidadesDeletar(@PathVariable("id") Long id, RedirectAttributes model) {

		telefoneUnidadeService.deletarByUnidade(id);
		enderecoUnidadeService.deletarByUnidade(id);
		unidadeService.deletar(id);

		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/unidades";
	}

	@PostMapping("/unidades/telefones/salvar")
	public String unidadesTelefonesSalvar(TelefoneUnidade telefone, RedirectAttributes model) {
		telefoneUnidadeService.salvar(telefone);
		model.addFlashAttribute("alert", "alert alert-fill-success");
		model.addFlashAttribute("message", "salvo com sucesso");

		return "redirect:/dashboard/admin/unidades";
	}

	@PostMapping("/unidades/telefones/excluir")
	public String unidadesTelefonesExcluir(TelefoneUnidade telefone, RedirectAttributes model) {
		if (telefone.getUnidadeTrabalho().podeExcluirTelefone()) {
			telefoneUnidadeService.deletar(telefone.getId());
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "excluido com sucesso");
			return "redirect:/dashboard/admin/unidades";
		} else {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "para excluir, a unidade precisa ter no mínimo dois telefones");
			return "redirect:/dashboard/admin/unidades";
		}

	}

	@GetMapping("/departamentos")
	public String departamentos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamentos", departamentoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/locais/base-info-locais";
	}

	@GetMapping("/departamentos/novo")
	public String departamentosNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/locais/base-cadastro-departamentos";
	}

	@PostMapping("/departamentos/salvar")
	public String departamentosSalvar(Departamento departamento, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamento", departamentoService.salvar(departamento));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/locais/base-cadastro-departamentos";
	}

	@GetMapping("/departamentos/alterar/{id}")
	public String departamentosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("departamento", departamentoService.buscarPor(id));
		return "redirect:/dashboard/admin/departamentos/novo";
	}

	@GetMapping("/departamentos/deletar/{id}")
	public String departamentosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		departamentoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/departamentos";
	}

	@GetMapping("/cargos")
	public String cargos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargos", cargoService.listarTodos());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/locais/base-info-locais";
	}

	@GetMapping("/cargos/novo")
	public String cargosNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes", getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/locais/base-cadastro-cargos";
	}

	@PostMapping("/cargos/salvar")
	public String cargosSalvar(Cargo cargo, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargo", cargoService.salvar(cargo));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/locais/base-cadastro-cargos";
	}

	@GetMapping("/cargos/alterar/{id}")
	public String cargosAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("cargo", cargoService.buscarPor(id));
		return "redirect:/dashboard/admin/cargos/novo";
	}

	@GetMapping("/cargos/deletar/{id}")
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
