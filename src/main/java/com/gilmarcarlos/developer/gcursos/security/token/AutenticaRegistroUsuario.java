package com.gilmarcarlos.developer.gcursos.security.token;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.auth.UsuarioToken;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.auth.VerificacaoTokenRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.service.email.EmailService;

/**
 * Classe com serviços de autenticação de usuários que implementa a interface (AutenticaToken)
 * 
 * @author Gilmar Carlos
 *
 */
@Component
public class AutenticaRegistroUsuario implements AutenticaToken {

	@Autowired
	private VerificacaoTokenRepository tokenRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void criarVerificacao(Usuario usuario) {
		String token = gerarToken();
		tokenRepository.save(new UsuarioToken(usuario, token));
		emailService.enviarConfirmacaoDeCadastro(usuario, token);
	}
	
	/**
	 * Método que gera um token aleatório
	 * 
	 * @param usuario
	 * 
	 */
	public String gerarToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void habilitar(Usuario usuario) {
		usuario.setHabilitado(true);
		usuarioRepository.save(usuario);		
	}
	
	/**
	 * Método que cria uma nova solicitação para redefinição de senha
	 * 
	 * @param usuario
	 * 
	 */
	public void resetPassword(Usuario usuario) {
		String token = gerarToken();
		tokenRepository.save(new UsuarioToken(usuario, token));
		emailService.enviarSolicitacaoDeNovaSenha(usuario, token);
	}

}
