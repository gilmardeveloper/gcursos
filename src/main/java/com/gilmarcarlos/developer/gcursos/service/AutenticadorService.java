package com.gilmarcarlos.developer.gcursos.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.Usuario;
import com.gilmarcarlos.developer.gcursos.model.UsuarioToken;
import com.gilmarcarlos.developer.gcursos.repository.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.repository.VerificacaoTokenRepository;
import com.gilmarcarlos.developer.gcursos.security.AutenticaRegistroUsuario;
import com.gilmarcarlos.developer.gcursos.security.AutenticaUsuario;
import com.gilmarcarlos.developer.gcursos.security.PasswordCrypt;

@Service
public class AutenticadorService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordCrypt passwordCrypt;
	
	@Autowired
	private AutorizacaoRepository autorizacaoRepository;
	
	@Autowired
	private VerificacaoTokenRepository tokenRepository;
	
	@Autowired
	private ApplicationEventPublisher eventPublish;
	
	@Autowired
	private AutenticaRegistroUsuario autenticaRegistro;
	
	public void registrarVerificacao(Usuario usuario) {

		usuario.setAutorizacoes(Arrays.asList(autorizacaoRepository.findByNome("ROLE_USER")));
		usuario.setSenha(passwordCrypt.encode(usuario.getSenha()));
		Usuario usuarioRegistrado = usuarioRepository.save(usuario);
		eventPublish.publishEvent(new AutenticaUsuario(usuarioRegistrado));
		
	}
	
	public void registrarNovaVerificacao(String token) {
		Usuario usuario = tokenRepository.findByToken(token).getUsuario();
		eventPublish.publishEvent(new AutenticaUsuario(usuario));
	}
	
	public boolean validarVerificacao(String token) throws Exception {
		
		UsuarioToken usuarioToken = tokenRepository.findByToken(token);
		
		if(usuarioToken == null) {
			throw new Exception("Token inválido");
			
		}else if(usuarioToken.expirou()) {
			return false;
			
		}else {
			autenticaRegistro.habilitar(usuarioToken.getUsuario());
			return true;
		}
		
		
	}

}
