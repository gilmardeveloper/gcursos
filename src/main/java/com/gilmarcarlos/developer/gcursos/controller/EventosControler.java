package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/eventos/presencial")
public class EventosControler {

	@Autowired
	private UsuarioService usuarioService;

	private Authentication autenticado;

	@GetMapping
	public String novo(Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			/*
			 * model.addAttribute("sexos", sexoService.listarTodos());
			 * model.addAttribute("escolaridades", escolaridadeService.listarTodos());
			 * model.addAttribute("cargos", cargoService.listarTodos());
			 * model.addAttribute("unidades", unidadeService.listarTodos());
			 */
			return "/dashboard/admin/cursos/base-cadastro-curso-presencial";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

}
