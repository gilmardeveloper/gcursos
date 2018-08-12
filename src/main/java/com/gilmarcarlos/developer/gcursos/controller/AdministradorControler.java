package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.NomeColaboradorService;
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
	
	private Authentication autenticado;

	@GetMapping("usuarios/atuais")
	public String cadastrosCompleto(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("atuais", usuarioService.listarCadastrosCompleots());
		return "dashboard/admin/base-info";
	}
	
	@GetMapping("usuarios/antigos")
	public String usuario(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("antigos", colaboradorService.listarTodos());
		return "dashboard/admin/base-info";
	}
	
	@GetMapping("unidades")
	public String unidades(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidades", unidadeService.listarTodos());
		return "dashboard/admin/base-info";
	}
	
	@GetMapping("departamentos")
	public String departamentos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("departamentos", departamentoService.listarTodos());
		return "dashboard/admin/base-info";
	}
	
	@GetMapping("cargos")
	public String cargos(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("cargos", cargoService.listarTodos());
		return "dashboard/admin/base-info";
	}
	
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}	
	
}
