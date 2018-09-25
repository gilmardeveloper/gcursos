package com.gilmarcarlos.developer.gcursos.utils;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Classe para o envio de mensagens de alertas da plataforma
 * 
 * @author Gilmar Carlos
 *
 */
public class RedirectUtils {
	
	/**
	 * Método que envia alertas de sucesso com redirecionamento de páginas
	 * 
	 * @param red recurso que transporta a mensagem 
	 * @param mensagem mensagem de alerta
	 * 
	 */
	public static void mensagemSucesso(RedirectAttributes red, String mensagem) {
		red.addFlashAttribute("alert", "alert alert-fill-success");
		red.addFlashAttribute("message", mensagem);
	}
	
	/**
	 * Método que envia alertas de erro com redirecionamento de páginas
	 * 
	 * @param red recurso que transporta a mensagem 
	 * @param mensagem mensagem de alerta
	 * 
	 */
	public static void mensagemError(RedirectAttributes red, String mensagem) {
		red.addFlashAttribute("alert", "alert alert-fill-danger");
		red.addFlashAttribute("message", mensagem);
	}
	
	/**
	 * Método que envia alertas de sucesso
	 * 
	 * @param model recurso que transporta a mensagem 
	 * @param mensagem mensagem de alerta
	 * 
	 */
	public static void mensagemSucesso(Model model, String mensagem) {
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", mensagem);
	}
	

	/**
	 * Método que envia alertas de erro
	 * 
	 * @param model recurso que transporta a mensagem 
	 * @param mensagem mensagem de alerta
	 * 
	 */
	public static void mensagemError(Model model, String mensagem) {
		model.addAttribute("alert", "alert alert-fill-danger");
		model.addAttribute("message", mensagem);
	}
	
}
