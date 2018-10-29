package com.gilmarcarlos.developer.gcursos.security.dataloader;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.auth.Permissoes;
import com.gilmarcarlos.developer.gcursos.model.auth.Privilegio;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.auth.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.auth.PermissoesRepository;
import com.gilmarcarlos.developer.gcursos.repository.auth.PrivilegioRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.security.crypt.PasswordCrypt;

/**
 * Classe para criar uma conta administrativa na primeira vez a plataforma roda
 * 
 * @author Gilmar Carlos
 *
 */
@Component
public class CredencialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PermissoesRepository permissoesRepository;

	@Autowired
	private AutorizacaoRepository autorizacaoRepository;

	@Autowired
	private PrivilegioRepository privelegioRepository;

	@Autowired
	private PasswordCrypt passwordCrypt;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;

		Privilegio adminPrivilege = createPrivilegeIfNotFound("ROLE_Administrador");
		Privilegio userPrivilege = createPrivilegeIfNotFound("ROLE_Usuario");

		createRoleIfNotFound("ROLE_Administrador", Arrays.asList(adminPrivilege));
		createRoleIfNotFound("ROLE_Usuario", Arrays.asList(userPrivilege));

		Autorizacao adminRole = autorizacaoRepository.findByNome("ROLE_Administrador");

		Usuario user = new Usuario();
		user.setNome("Administrador");
		user.setSenha(passwordCrypt.encode("admin"));
		user.setEmail("admin_email@gmail.com");
		user.setAutorizacoes(Arrays.asList(adminRole));
		user.setHabilitado(true);

		if (usuarioRepository.findByEmail(user.getEmail()) != null)
			user = usuarioRepository.findByEmail(user.getEmail());

		Usuario temp = usuarioRepository.save(user);

		createIfNotExistPermissoes(temp);

		alreadySetup = true;
	}
	
	/**
	 * Método que cria permissões para o administrador se não existir 
	 * 
	 * @param temp representa um usuario temporario 
	 * 
	 */
	private void createIfNotExistPermissoes(Usuario temp) {

		if (temp.getPermissoes() == null) {
			Permissoes permissoes = new Permissoes();
			permissoes.setCriar(Arrays.asList("tudo"));
			permissoes.setVisualizar(Arrays.asList("tudo"));
			permissoes.setAlterar(Arrays.asList("tudo"));
			permissoes.setDeletar(Arrays.asList("tudo"));
			permissoes.setUsuario(temp);
			permissoesRepository.save(permissoes);
		}
	}
	
	/**
	 * Método que cria privilegios para o administrador se não existir 
	 * 
	 * @param nome representa um privilegio
	 * 
	 */
	@Transactional
	private Privilegio createPrivilegeIfNotFound(String nome) {

		Privilegio privilegio = privelegioRepository.findByNome(nome);
		if (privilegio == null) {
			privilegio = new Privilegio(nome);
			privelegioRepository.save(privilegio);
		}
		return privilegio;
	}

	/**
	 * Método que cria autorizações para o administrador se não existir 
	 * 
	 * @param name representa uma autorização
	 * @param privilegios uma lista de privilegios
	 * 
	 */
	@Transactional
	private Autorizacao createRoleIfNotFound(String name, List<Privilegio> privilegios) {

		Autorizacao autorizacao = autorizacaoRepository.findByNome(name);
		if (autorizacao == null) {
			autorizacao = new Autorizacao(name);
			autorizacao.setPrivilegios(privilegios);
			autorizacaoRepository.save(autorizacao);
		}
		return autorizacao;
	}
}
