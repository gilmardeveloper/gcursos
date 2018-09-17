package com.gilmarcarlos.developer.gcursos.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;

@ControllerAdvice
public class HandlerExceptionControler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private UsuarioService usuarioService ;
	
	private Authentication autenticado;
		
	@ExceptionHandler(MultipartException.class)
	String handleFileException(HttpServletRequest request, Throwable ex) {
		notificacaoService.salvar(new Notificacao(getUsuario(), "Erro ao alterar avatar", IconeTypeUtils.INFORMACAO,
				StatusTypeUtils.ERRO, "uma tentativa de adicionar um novo avatar falhou"));
		return "redirect:/dashboard/";
	}
	
	
	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}
}
