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

/**
 * Classe com serviços de persistência de registro de usuários
 * 
 * @author Gilmar Carlos
 *
 */
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

	/**
	 * Método que valida um novo registro de usuário
	 * 
	 * @param usuario
	 * 
	 */
	public void registrarVerificacao(Usuario usuario) {

		Usuario temporario = usuarioRepository.findByEmail(usuario.getEmail());
		if (temporario != null) {
			if (temporario.isHabilitado()) {
				throw new RegistroException();
			} else {
				registrarNovaVerificacao(temporario);
			}
		} else {
			usuario.setAutorizacoes(Arrays.asList(autorizacaoRepository.findByNome("ROLE_Usuario")));
			usuario.setSenha(passwordCrypt.encode(usuario.getSenha()));
			Usuario usuarioRegistrado = usuarioRepository.save(usuario);
			eventPublish.publishEvent(new AutenticaUsuario(usuarioRegistrado));
		}
	}

	/**
	 * Método que dispara um evento para criar uma nova validação de um novo registro por usuário
	 * 
	 * @param usuario
	 * 
	 */
	public void registrarNovaVerificacao(Usuario usuario) {
		eventPublish.publishEvent(new AutenticaUsuario(usuario));
	}
	
	/**
	 * Método que dispara um evento para criar uma nova validação de um novo registro por token
	 * 
	 * @param token
	 * 
	 */
	public void registrarNovaVerificacao(String token) {
		Usuario usuario = tokenRepository.findByToken(token).getUsuario();
		eventPublish.publishEvent(new AutenticaUsuario(usuario));
	}

	/**
	 * Método que dispara um evento para criar uma nova senha 
	 * 
	 * @param token
	 * 
	 */
	public void registrarRedefinicao(String email) {

		Usuario usuario = usuarioRepository.findByEmail(email);
		if (usuario != null) {
			if (usuario.isHabilitado()) {
				eventPublish.publishEvent(new AutenticaResetPasswordUsuario(usuario));
			} else {
				throw new RegistroNotEnableException();
			}
		} else {
			throw new RegistroNotFoundException();
		}
	}
	
	/**
	 * Método que valida uma confirmação de registro por token
	 * 
	 * @param token
	 * @throws Exception se o token for inválido
	 * 
	 */
	public boolean validarVerificacao(String token) throws Exception {

		UsuarioToken usuarioToken = tokenRepository.findByToken(token);

		if (usuarioToken == null) {
			throw new Exception("Token inválido");

		} else if (usuarioToken.expirou()) {
			return false;
		} else {
			autenticaRegistro.habilitar(usuarioToken.getUsuario());
			return true;
		}

	}
	
	/**
	 * Método que valida uma confirmação de redefinição de senha por token
	 * 
	 * @param token
	 * @throws Exception se o token for inválido
	 * 
	 */
	public Usuario validarRedefinicao(String token) throws Exception {

		UsuarioToken usuarioToken = tokenRepository.findByToken(token);
		if (usuarioToken == null) {
			throw new Exception("Token inválido");
		} else {
			return usuarioToken.getUsuario();
		}

	}

}
