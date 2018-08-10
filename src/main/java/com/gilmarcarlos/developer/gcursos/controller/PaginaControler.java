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
import com.gilmarcarlos.developer.gcursos.service.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.DepartamentoService;
import com.gilmarcarlos.developer.gcursos.service.EnderecoUnidadeService;
import com.gilmarcarlos.developer.gcursos.service.EnderecoUsuarioService;
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
	private TelefoneUsuarioService telefoneUsusarioService;

	@Autowired
	private EnderecoUsuarioService enderecoUsuarioService;
	
	@Autowired
	private DadosPessoaisService dadosPessoais;
	
	@Autowired
	private CodigoFuncionalService codigoService;
	
	@GetMapping
	public String home() {
			return "login/login-template";
	}
	
	@GetMapping("dashboard/")
	public String painel(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("unidades", unidadeService.listarTodos());
		model.addAttribute("cargos", cargoService.listarTodos());
		return "complete-perfil/complete-cadastro";
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
	
	
}
