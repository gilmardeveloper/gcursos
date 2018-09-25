package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.UsuarioDTO;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.CpfExisteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioDeleteException;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioExisteException;
import com.gilmarcarlos.developer.gcursos.repository.auth.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.security.crypt.PasswordCrypt;
import com.gilmarcarlos.developer.gcursos.security.exception.SenhaNotNullException;
import com.gilmarcarlos.developer.gcursos.service.auth.PermissoesService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.InscricaoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.InscricaoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.locais.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.MensagensService;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;

/**
 * Classe com serviços de persistência para entidade (Usuario)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class UsuarioService {

	@Autowired
	private PasswordCrypt passwordCrypt;

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private DadosPessoaisService dadosService;
	
	@Autowired
	private CodigoFuncionalService codigoService;

	@Autowired
	private AutorizacaoRepository autorizacaoRespository;
	
	@Autowired
	private PermissoesService permissoesService;
	
	@Autowired
	private InscricaoPresencialService inscricaoService;
	
	@Autowired
	private InscricaoOnlineService inscricaoOnlineService;
	
	@Autowired
	private TelefoneUsuarioService telefoneService;

	@Autowired
	private NotificacaoService notificacoesService;
	
	@Autowired
	private MensagensService mensagensService;
	
	@Autowired
	private ImagensService imagensService;
	
	/**
	 * Método que salva um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * @throws UsuarioExisteException se o usuario com mesmo email já existe
	 */
	public Usuario salvar(Usuario usuario) throws UsuarioExisteException {
		if (emailExiste(usuario)) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		return repository.save(usuario);
	}
	
	/**
	 * Método que salva um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * @throws UsuarioExisteException se o usuario com mesmo email já existe
	 * @throws CpfExisteException se o usuario com mesmo cpf já existe
	 * 
	 */
	public Usuario criarNovo(Usuario usuario) throws UsuarioExisteException, CpfExisteException {

			
			if(emailExiste(usuario)){
				throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
			}
			
			if(dadosService.cpfExiste(usuario.getDadosPessoais())) {
				throw new CpfExisteException("já existe um usuário cadastrado com esse cpf");
			}
			
			usuario.setHabilitado(true);
			usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome("ROLE_Usuario")));
			usuario.setSenha(passwordCrypt.encode("zeus_1234@5"));
			
			DadosPessoais dados = dadosService.salvar(usuario.getDadosPessoais());
			CodigoFuncional codigo = codigoService.salvar(usuario.getCodigoFuncional());
			Usuario novoUsuario = repository.save(usuario);
			
			dados.setUsuario(novoUsuario);
			codigo.setUsuario(novoUsuario);
			
			codigoService.salvar(codigo);
			dadosService.salvar(dados);
		
			return novoUsuario;
	}
	
	/**
	 * Método que atualiza dados de um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * @throws UsuarioExisteException se o usuario com mesmo email já existe
	 * 
	 */
	public Usuario atualizarDados(Usuario usuario) throws UsuarioExisteException {

		if (emailExiste(usuario)) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}

		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(passwordCrypt.encode(buscarPor(usuario.getId()).getSenha()));
		return repository.save(usuario);
	}
	
	/**
	 * Método que atualiza dados de um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * @throws UsuarioExisteException se o usuario com mesmo email já existe
	 * @throws CpfExisteException se o usuario com mesmo cpf já existe
	 * 
	 */
	public Usuario atualizarDadosNoEncryptSenha(Usuario usuario) throws UsuarioExisteException, CpfExisteException{

		Check check = new SafeguardCheck();

		if (emailExiste(usuario)) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		
		if(dadosService.cpfExiste(usuario.getDadosPessoais()) || check.elementOf(usuario.getDadosPessoais().getCpf().trim(), ParametroTipo.CPF).validate().hasError()) {
			throw new CpfExisteException("já existe um usuário cadastrado com esse cpf ou cpf inválido");
		}
		
		dadosService.salvar(usuario.getDadosPessoais());
		
		if(usuario.getCodigoFuncional() != null) {
			codigoService.salvar(usuario.getCodigoFuncional());
		}

		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(buscarPor(usuario.getId()).getSenha());
		
		return repository.save(usuario);
	}

	/**
	 * Método que atualiza o nome de um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * 
	 */
	public Usuario atualizarNome(Usuario usuario) {

		Usuario temp = repository.findOne(usuario.getId());
		temp.setNome(usuario.getNome());
		return repository.save(temp);
	}
	
	/**
	 * Método que atualiza a senha de um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * 
	 */
	public Usuario redefinirSenha(Usuario usuario) {

		Usuario temp = repository.findOne(usuario.getId());
		// usuario.setAutorizacoes(temp.getAutorizacoes());
		// usuario.setHabilitado(temp.isHabilitado());
		temp.setSenha(passwordCrypt.encode(usuario.getSenha()));

		return repository.save(temp);
	}
	
	/**
	 * Método que atualiza a senha de um usuario na base 
	 * 
	 * @param usuario entidade que representa o usuário 
	 * @return Usuario retorna um usuario
	 * @throws SenhaNotNullException se a senha for null ou vazio
	 * 
	 */
	public Usuario redefinirSenha(Usuario usuario, String senha) throws SenhaNotNullException {
		
		if (senha.equals("") || senha == null) {
			throw new SenhaNotNullException();
		}
		
		usuario.setSenha(passwordCrypt.encode(senha));
		return repository.save(usuario);
	}
	
	/**
	 * Método que deleta um usuario na base pela id
	 * 
	 * @param id id de um usuário 
	 * @throws UsuarioExisteException se o usuario não existir
	 * @throws UsuarioDeleteException se o usuário for responsável por algum evento
	 * 
	 */
	public void deletar(Long id) throws UsuarioExisteException, UsuarioDeleteException {
		
		Usuario usuario = buscarPor(id);
		
		if(usuario == null) {
			throw new UsuarioExisteException("usuário não existe");
		}
		
		if(!usuario.getEventoOnline().isEmpty()) {
			throw new UsuarioDeleteException("usuário é responsável por eventos online, antes de deletar, é necessário alterar a responsabilidade do evento, ou excluir o evento.");
		}
		
		if(!usuario.getEventoPresencial().isEmpty()) {
			throw new UsuarioDeleteException("usuário é responsável por eventos presenciais, antes de deletar, é necessário alterar a responsabilidade do evento, ou excluir o evento.");
		}
		
		notificacoesService.deletar(usuario);
		mensagensService.deletar(usuario);
		
		inscricaoService.deletar(usuario);
		inscricaoOnlineService.deletar(usuario);
		
		permissoesService.deletar(usuario);
		
		telefoneService.deletar(usuario);
		dadosService.deletar(usuario.getDadosPessoais().getId());
		codigoService.deletar(usuario.getCodigoFuncional().getId());
		
		imagensService.deletar(usuario);
				
		repository.deleteById(id);
	}
	
	/**
	 * Método para buscar um usuario na base por id 
	 * 
	 * @param id id de um usuário 
	 * @return Usuario retorna um usuario
	 * 
	 */
	public Usuario buscarPor(Long id) {
		return repository.findOne(id);
	}
	
	/**
	 * Método para buscar um usuario na base por email 
	 * 
	 * @param email email de um usuário 
	 * @return Usuario retorna um usuario
	 * 
	 */
	public Usuario buscarPor(String email) {
		return repository.findByEmail(email);
	}

	/**
	 * Método para listar todos os usuarios na base 
	 * 
	 * @return List retorna uma lista de usuarios
	 * 
	 */
	public List<Usuario> listarTodos() {
		return repository.listAll();
	}

	/**
	 * Método para listar todos os usuarios com cadastro completo na base 
	 * 
	 * @return List retorna uma lista de usuarios
	 * 
	 */
	public List<Usuario> listarCadastrosCompletos() {
		return repository.listCadastrosCompleto();
	}
	
	/**
	 * Método para listar todos os usuarios com cadastro completo na base por departamento
	 * 
	 * @return List retorna uma lista de usuarios
	 * 
	 */
	public List<Usuario> listarCadastrosCompletos(Long departamento) {
		return repository.listCadastrosCompleto(departamento);
	}
	
	/**
	 * Método para alterar a autorização do usuario
	 * 
	 * @param usuario representa um usuario
	 * @param autorizacaoNome representa uma autorização
	 * 
	 */
	public void atualizarDados(Usuario usuario, String autorizacaoNome) {

		Usuario temp = repository.findOne(usuario.getId());
		usuario.setSenha(temp.getSenha());
		usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome(autorizacaoNome)));
		usuario.setHabilitado(temp.isHabilitado());

		repository.save(usuario);

	}
	
	/**
	 * Método para alterar a autorização do usuario
	 * 
	 * @param usuario representa um usuario
	 * @param autorizacaoNome representa uma autorização
	 * @return Usuario retorna um usuario
	 */
	public Usuario atualizarAutorizacoes(Usuario usuario, String nomeAutorizacao) {
		List<Autorizacao> autorizacoes = new ArrayList<>();
		autorizacoes.add(autorizacaoRespository.findByNome(nomeAutorizacao));
		usuario.setAutorizacoes(autorizacoes);
		return repository.save(usuario);
	}
	
	/**
	 * Método para validar um usuario por email e a id
	 * 
	 * @param email email de um usuario
	 * @param id id de um usuario
	 * @return Boolean retorna <code>true</code> se verdadeiro 
	 */
	public Boolean emailExiste(String email, Long id) {
		return repository.existsByEmail(email, id);
	}
	
	/**
	 * Método para validar um usuario por email
	 * 
	 * @param usuario representa um usuario
	 * @return Boolean retorna <code>true</code> se verdadeiro 
	 */
	public Boolean emailExiste(Usuario usuario) {
		if(usuario.getId() != null) {
			return emailExiste(usuario.getEmail(), usuario.getId());
		}else {
			return repository.existsByEmail(usuario.getEmail());
		}
	}
	
	/**
	 * Método para listar todos os usuarios com cadastro completo na base
	 * 
	 * @param pageable recurso de paginação
	 * @return Page retorna uma lista de usuarios paginada
	 * 
	 */
	public Page<Usuario> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}
	
	/**
	 * Método para listar todos os usuarios com cadastro completo na base em uma classe auxiliar
	 * 
	 * @param usuario representa um usuario
	 * @return List retorna uma lista de usuarios 
	 * 
	 */
	public List<UsuarioDTO> listarUsuariosDTO(Usuario usuario) {

		List<UsuarioDTO> usuarios = new ArrayList<>();
		if (usuario.temRestricao("departamento")) {
			listarCadastrosCompletos(usuario.getPermissoes().getDepartamento()).forEach(u -> usuarios.add(new UsuarioDTO(u)));
		} else {
			listarCadastrosCompletos().forEach(u -> usuarios.add(new UsuarioDTO(u)));
		}

		return usuarios;
	}
	
	/**
	 * Método para buscar um usuario na base por id 
	 * 
	 * @param id id de um usuário 
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por departamento
	 * 
	 * @param id id de um usuário 
	 * @param departamento representa o id de um departamento
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPor(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPor(departamento, id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por unidade
	 * 
	 * @param id id de uma unidade 
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPorUnidade(Long id, Pageable pageable) {
		return repository.buscarPorUnidade(id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por unidade e por departamento
	 * 
	 * @param id id de uma unidade 
	 * @param departamento id de um departamento
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPorUnidade(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPorUnidade(departamento, id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por cargo
	 * 
	 * @param id id de um cargo 
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPorCargo(Long id, Pageable pageable) {
		return repository.buscarPorCargo(id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por cargo e por departamento
	 * 
	 * @param id id de um cargo 
	 * @param departamento id de um departamento 
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPorCargo(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPorCargo(departamento, id, pageable);
	}
	
	/**
	 * Método para buscar um usuario na base por por departamento
	 * 
	 * @param id id de um departamento 
	 * @param pageable recursos de paginação
	 * @return Page retorna uma lista paginada
	 * 
	 */
	public Page<Usuario> buscarPorDepartamento(Long id, Pageable pageable) {
		return repository.buscarPorDepartamento(id, pageable);
	}
	
	/**
	 * Método para validar um usuario por sexo
	 * 
	 * @param nome representa uma opção sexual
	 * @return boolean retorna <code>true</code> se verdadeiro 
	 * 
	 */
	public boolean sexoExiste(String nome) {
		return repository.existsBySexo(nome);
	}
	
	/**
	 * Método para validar um usuario por escolaridade
	 * 
	 * @param nome representa uma escolaridade
	 * @return boolean retorna <code>true</code> se verdadeiro 
	 * 
	 */
	public boolean escolaridadeExiste(String nome) {
		return repository.existsByEscolaridade(nome);
	}

	


}
