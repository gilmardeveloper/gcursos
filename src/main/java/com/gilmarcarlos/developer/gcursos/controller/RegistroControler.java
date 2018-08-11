package com.gilmarcarlos.developer.gcursos.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroException;
import com.gilmarcarlos.developer.gcursos.service.AutenticadorService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
public class RegistroControler {

	@Autowired
	private AutenticadorService autenticador;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(value = { "/login", "/login/" })
	public String login(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=14400");
		return "login/login-template";
	}

	@GetMapping(value = { "/registro", "/registro/" })
	public String cotacoes(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=14400");
		return "login/registro-template";
	}

	@GetMapping(value = { "/esqueceu-senha", "/esqueceu-senha/" })
	public String esqueceuSenha(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=14400");
		return "login/esqueceu-senha-template";
	}

	@GetMapping("/redefinir-senha")
	public String redefinirSenha(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=14400");
		return "login/redefinir-senha";
	}

	@PostMapping("/redefinir-senha")
	public String redefinirSenha(@RequestParam("senha") String senha, @RequestParam("token") String token,
			RedirectAttributes model) {

		try {
			Usuario usuario = autenticador.validarRedefinicao(token);
			usuarioService.redefinirSenha(usuario, senha);
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "sua senha foi redefinida com sucesso");
			return "redirect:/redefinir-senha";
		} catch (Exception e) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "um erro ocorreu");
			return "redirect:/redefinir-senha";
		}

	}
	
	
	@PostMapping("/dashboard/redefinir-senha")
	public String alterarSenha(@RequestParam("senha") String senha, @RequestParam("id") Long id,
			RedirectAttributes model) {

		try {
			Usuario usuario = usuarioService.buscarPor(id);
			usuarioService.redefinirSenha(usuario, senha);
			model.addFlashAttribute("alert", "alert alert-fill-success");
			model.addFlashAttribute("message", "sua senha foi redefinida com sucesso");
			return "redirect:/redefinir-senha";
		} catch (Exception e) {
			model.addFlashAttribute("alert", "alert alert-fill-danger");
			model.addFlashAttribute("message", "um erro ocorreu");
			return "redirect:/dashboard/";
		}

	}
	

	@PostMapping(value = { "/solicitar-nova-senha", "/solicitar-nova-senha/" })
	public String esqueceuSenha(String email, RedirectAttributes redirect) {
		try {
			autenticador.registrarRedefinicao(email);
			redirect.addFlashAttribute("alert", "alert alert-fill-success");
			redirect.addFlashAttribute("message", "Agora verifique seu email e confirme a solicitação");
			return "redirect:/esqueceu-senha";
		} catch (Exception e) {
			redirect.addFlashAttribute("alert", "alert alert-fill-danger");
			redirect.addFlashAttribute("message", "Esse email não existe ou você ainda não ativou seu cadastro");
			return "redirect:/esqueceu-senha";
		}
	}

	@PostMapping(value = { "/registrar", "/registrar" })
	public String registrar(Usuario usuario, RedirectAttributes redirect) {
		try {
			autenticador.registrarVerificacao(usuario);
			redirect.addFlashAttribute("alert", "alert alert-fill-success");
			redirect.addFlashAttribute("message", "Agora verifique seu email e confirme o cadastro");
			return "redirect:/registro";
		} catch (RegistroException e) {
			redirect.addFlashAttribute("alert", "alert alert-fill-danger");
			redirect.addFlashAttribute("message", "Você já é cadastrado");
			return "redirect:/registro";
		}
	}

	@GetMapping("/confirmar-registro/{token}")
	public String confirmar(@PathVariable("token") String token, RedirectAttributes redirect) {

		try {

			if (autenticador.validarVerificacao(token)) {
				return "redirect:/login";

			} else {

				autenticador.registrarNovaVerificacao(token);
				redirect.addFlashAttribute("alerta", "um erro ocorreu.");
				return "redirect:/";
			}

		} catch (Exception e) {

			e.printStackTrace();
			redirect.addFlashAttribute("alerta", "um erro ocorreu.");
			return "redirect:/";
		}

	}

	@GetMapping("/redefinir-senha/{token}")
	public String redefinir(@PathVariable("token") String token, RedirectAttributes redirect) {
			redirect.addFlashAttribute("token", token);
			return "redirect:/redefinir-senha";
	}

}
