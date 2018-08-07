package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gilmarcarlos.developer.gcursos.service.AutenticadorService;

@Controller
public class RegistroControler {
	
	@Autowired
	private AutenticadorService autenticador;
	
	@GetMapping(value= {"/login", "/login/"})
	public String login() {
		return "login";
	}
	
	/*@GetMapping(value= {"/registro","/registro/"})
	public String cotacoes() {
		return "registro";
	}
	

	@PostMapping(value= {"/registrar","/registrar"})
	public String registrar(Usuario usuario, RedirectAttributes redirect) {
		autenticador.registrarVerificacao(usuario);
		redirect.addFlashAttribute("alerta", "verifique seu email");
		return "redirect:/";
	}*/
	
	/*@GetMapping("/confirmar-registro")
	public String confirmar(@RequestParam("token") String token, RedirectAttributes redirect) {
		
		try {
			
			if(autenticador.validarVerificacao(token)) {
				return "redirect:/login";
				
			}else {
				
				autenticador.registrarNovaVerificacao(token);
				redirect.addFlashAttribute("alerta", "um erro ocorreu.");
				return "redirect:/";
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			redirect.addFlashAttribute("alerta", "um erro ocorreu.");
			return "redirect:/";
		}
		
	}*/

}
