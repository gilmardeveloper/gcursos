package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/usuario/")
public class UsuarioControler {

	@Autowired
	private UsuarioService usuarioService;
	private Authentication autenticado;
	
	@GetMapping("redefinir-senha")
	public String redefinirSenha(@RequestParam("senha") String senha) {
		
		if(!senha.equals("") || senha != null)
			usuarioService.redefinirSenha(getUsuario(), senha);
				
		return "dashboard/profile";
	}
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
	
}
