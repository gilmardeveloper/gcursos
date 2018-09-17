package com.gilmarcarlos.developer.gcursos.utils;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class RedirectUtils {

	public static void mensagemSucesso(RedirectAttributes red, String mensagem) {
		red.addFlashAttribute("alert", "alert alert-fill-success");
		red.addFlashAttribute("message", mensagem);
	}
	
	public static void mensagemError(RedirectAttributes red, String mensagem) {
		red.addFlashAttribute("alert", "alert alert-fill-danger");
		red.addFlashAttribute("message", mensagem);
	}
	
	public static void mensagemSucesso(Model model, String mensagem) {
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", mensagem);
	}
	
	public static void mensagemError(Model model, String mensagem) {
		model.addAttribute("alert", "alert alert-fill-danger");
		model.addAttribute("message", mensagem);
	}
	
}
