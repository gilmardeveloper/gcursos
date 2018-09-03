package com.gilmarcarlos.developer.gcursos.service.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.UsuarioToken;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.auth.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.auth.VerificacaoTokenRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.security.auth.user.AutenticaUsuario;
import com.gilmarcarlos.developer.gcursos.security.crypt.PasswordCrypt;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroException;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroNotEnableException;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroNotFoundException;
import com.gilmarcarlos.developer.gcursos.security.reset.passwd.AutenticaResetPasswordUsuario;
import com.gilmarcarlos.developer.gcursos.security.token.AutenticaRegistroUsuario;

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

		Usuario temporario = usuarioRepository.findByEmail(usuario.getEmail());
		if(temporario != null) {
			if(temporario.isHabilitado()) {
				throw new RegistroException(); 
			}else {
				registrarNovaVerificacao(temporario);
			}
		}else {
			usuario.setAutorizacoes(Arrays.asList(autorizacaoRepository.findByNome("ROLE_Usuario")));
			usuario.setSenha(passwordCrypt.encode(usuario.getSenha()));
			Usuario usuarioRegistrado = usuarioRepository.save(usuario);
			eventPublish.publishEvent(new AutenticaUsuario(usuarioRegistrado));
		}
	}
	
	public void registrarNovaVerificacao(Usuario usuario) {
		eventPublish.publishEvent(new AutenticaUsuario(usuario));
	}
	
	public void registrarNovaVerificacao(String token) {
		Usuario usuario = tokenRepository.findByToken(token).getUsuario();
		eventPublish.publishEvent(new AutenticaUsuario(usuario));
	}
	
	public void registrarRedefinicao(String email) {

		Usuario usuario = usuarioRepository.findByEmail(email);
		if(usuario != null) {
			if(usuario.isHabilitado()) {
				eventPublish.publishEvent(new AutenticaResetPasswordUsuario(usuario));
			}else {
				throw new RegistroNotEnableException();
			}
		}else {
			throw new RegistroNotFoundException();
		}
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
	
public Usuario validarRedefinicao(String token) throws Exception {
		
		UsuarioToken usuarioToken = tokenRepository.findByToken(token);
		if(usuarioToken == null) {
			throw new Exception("Token inválido");
		}else {
			return usuarioToken.getUsuario();
		}
		
	}

}
