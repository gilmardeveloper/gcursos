package com.gilmarcarlos.developer.gcursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.eventos.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.CategoriaEventoService;
import com.gilmarcarlos.developer.gcursos.service.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/admin/eventos/presencial")
public class EventosControler {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EventoPresencialService eventoPresencialService;
	
	@Autowired
	private CategoriaEventoService categoriaEventoService;

	private Authentication autenticado;

	@GetMapping
	public String novo(Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("categorias", categoriaEventoService.listarTodos());
			/*
			 * model.addAttribute("sexos", sexoService.listarTodos());
			 * model.addAttribute("escolaridades", escolaridadeService.listarTodos());
			 * model.addAttribute("cargos", cargoService.listarTodos());
			 * model.addAttribute("unidades", unidadeService.listarTodos());
			 */
			return "/dashboard/admin/eventos/base-cadastro-curso-presencial";
		} else {
			return "redirect:/dashboard/complete-cadastro";
		}
	}
	
	@PostMapping("/salvar")
	public String eventoPresencialSalvar(EventoPresencial evento, RedirectAttributes model) {
		
		model.addFlashAttribute("evento", eventoPresencialService.salvar(evento));
		
		return "redirect:/dashboard/admin/eventos/presencial";
	}
	
	@GetMapping("/categorias")
	public String categorias(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categorias", categoriaEventoService.listarTodos());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/eventos/base-info-categorias-evento-presencial";
	}
	
	@GetMapping("categorias/novo")
	public String categoriasNovo(Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notificacoes",getUsuario().getNotificaoesNaoLidas());
		return "dashboard/admin/eventos/base-cadastro-categorias-evento-presencial";
	}
	
	@PostMapping("categorias/salvar")
	public String categoriasSalvar(CategoriaEvento categorias, Model model) {
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("categoria", categoriaEventoService.salvar(categorias));
		model.addAttribute("alert", "alert alert-fill-success");
		model.addAttribute("message", "salvo com sucesso");
		return "dashboard/admin/eventos/base-cadastro-categorias-evento-presencial";
	}
	
	@GetMapping("categorias/alterar/{id}")
	public String categoriasAlterar(@PathVariable("id") Long id, RedirectAttributes model) {
		model.addFlashAttribute("categoria", categoriaEventoService.buscarPor(id));
		return "redirect:/dashboard/admin/eventos/presencial/categorias/novo";
	}
	
	@GetMapping("categorias/deletar/{id}")
	public String cargosDeletar(@PathVariable("id") Long id, RedirectAttributes model) {
		categoriaEventoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/admin/eventos/presencial/categorias";
	}
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

}
