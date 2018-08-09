package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PaginaControler {
	
	@GetMapping
	public String home() {
		return "login/login-template";
	}
	
	
	@GetMapping("dashboard/")
	public String painel() {
		return "complete-perfil/complete-cadastro";
	}
	
	
	
	
}
