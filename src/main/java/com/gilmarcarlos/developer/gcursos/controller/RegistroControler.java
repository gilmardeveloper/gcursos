package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroException;
import com.gilmarcarlos.developer.gcursos.service.auth.AutenticadorService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;
import com.gilmarcarlos.developer.gcursos.utils.RedirectUtils;

/**
 * Classe de controle para registros
 *  
 * @author Gilmar Carlos
 *
 */
@Controller
public class RegistroControler {

	@Autowired
	private AutenticadorService autenticador;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(value = { "/login", "/login/" })
	public String login() {
		return "login/login-template";
	}

	@GetMapping(value = { "/registro", "/registro/" })
	public String cotacoes() {
		return "login/registro-template";
	}

	@GetMapping(value = { "/esqueceu-senha", "/esqueceu-senha/" })
	public String esqueceuSenha() {
		return "login/esqueceu-senha-template";
	}

	@GetMapping("/redefinir-senha")
	public String redefinirSenha() {
		return "login/redefinir-senha";
	}

	@PostMapping("/redefinir-senha")
	public String redefinirSenha(@RequestParam("senha") String senha, @RequestParam("token") String token,
			RedirectAttributes model) {

		try {
			Usuario usuario = autenticador.validarRedefinicao(token);
			usuarioService.redefinirSenha(usuario, senha);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
			return "redirect:/redefinir-senha";
		} catch (Exception e) {
			RedirectUtils.mensagemError(model, ConfUtils.ALERTA_ERROR_SALVAR);
			return "redirect:/redefinir-senha";
		}

	}
	
	
	@PostMapping("/dashboard/redefinir-senha")
	public String alterarSenha(@RequestParam("senha") String senha, @RequestParam("id") Long id,
			RedirectAttributes model) {

		try {
			Usuario usuario = usuarioService.buscarPor(id);
			usuarioService.redefinirSenha(usuario, senha);
			RedirectUtils.mensagemSucesso(model, ConfUtils.ALERTA_SUCESSO_SALVAR);
			return "redirect:/redefinir-senha";
		} catch (Exception e) {
			RedirectUtils.mensagemError(model, ConfUtils.ALERTA_ERROR_SALVAR);
			return "redirect:/dashboard/";
		}

	}
	

	@PostMapping(value = { "/solicitar-nova-senha", "/solicitar-nova-senha/" })
	public String esqueceuSenha(@RequestParam("email") String email, RedirectAttributes model) {
		try {
			autenticador.registrarRedefinicao(email);
			RedirectUtils.mensagemSucesso(model, ConfUtils.CADASTRO_ALERTA_SOLICITACAO);
			return "redirect:/esqueceu-senha";
		} catch (Exception e) {
			RedirectUtils.mensagemError(model, ConfUtils.CADASTRO_ALERTA_ERROR_REDEFINIR_SENHA);
			return "redirect:/esqueceu-senha";
		}
	}

	@PostMapping(value = { "/registrar", "/registrar" })
	public String registrar(Usuario usuario, RedirectAttributes model) {
		try {
			autenticador.registrarVerificacao(usuario);
			RedirectUtils.mensagemSucesso(model, ConfUtils.CADASTRO_ALERTA_SOLICITACAO);
			return "redirect:/registro";
		} catch (RegistroException e) {
			RedirectUtils.mensagemError(model, ConfUtils.CADASTRO_ALERTA_ERROR_EMAIL_CADASTRADO);
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
				redirect.addFlashAttribute("alerta", ConfUtils.ALERTA_ERROR_GENERICO);
				return "redirect:/";
			}

		} catch (Exception e) {

			e.printStackTrace();
			redirect.addFlashAttribute("alerta", ConfUtils.ALERTA_ERROR_GENERICO);
			return "redirect:/";
		}

	}

	@GetMapping("/redefinir-senha/{token}")
	public String redefinir(@PathVariable("token") String token, RedirectAttributes redirect) {
			redirect.addFlashAttribute("token", token);
			return "redirect:/redefinir-senha";
	}

}
