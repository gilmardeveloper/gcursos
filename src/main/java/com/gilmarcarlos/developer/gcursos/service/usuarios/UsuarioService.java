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
import com.gilmarcarlos.developer.gcursos.service.locais.CodigoFuncionalService;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;

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
	

	// @CacheEvict(value="postCache", allEntries=true)
	public Usuario salvar(Usuario usuario) throws UsuarioExisteException {
		if (emailExiste(usuario)) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		return repository.save(usuario);
	}
	
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
			
			Usuario novoUsuario = repository.save(usuario);
			DadosPessoais dados = usuario.getDadosPessoais();
			CodigoFuncional codigo = usuario.getCodigoFuncional();
			
			dados.setUsuario(novoUsuario);
			codigo.setUsuario(novoUsuario);
			
			codigoService.salvar(codigo);
			dadosService.salvar(dados);
		
			return novoUsuario;
	}

	public Usuario atualizarDados(Usuario usuario) throws UsuarioExisteException {

		if (emailExiste(usuario)) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}

		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(passwordCrypt.encode(buscarPor(usuario.getId()).getSenha()));
		return repository.save(usuario);
	}

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

	// @CacheEvict(value="postCache", allEntries=true)
	public Usuario atualizarNome(Usuario usuario) {

		Usuario temp = repository.findOne(usuario.getId());
		temp.setNome(usuario.getNome());
		return repository.save(temp);
	}

	// @CacheEvict(value="postCache", allEntries=true)
	public Usuario redefinirSenha(Usuario usuario) {

		Usuario temp = repository.findOne(usuario.getId());
		// usuario.setAutorizacoes(temp.getAutorizacoes());
		// usuario.setHabilitado(temp.isHabilitado());
		temp.setSenha(passwordCrypt.encode(usuario.getSenha()));

		return repository.save(temp);
	}

	// @CacheEvict(value="postCache", allEntries=true)
	public Usuario redefinirSenha(Usuario usuario, String senha) throws SenhaNotNullException {
		
		if (senha.equals("") || senha == null) {
			throw new SenhaNotNullException();
		}
		
		usuario.setSenha(passwordCrypt.encode(senha));
		return repository.save(usuario);
	}

	// @CacheEvict(value="postCache", allEntries=true)
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
		
		inscricaoService.deletar(usuario);
		inscricaoOnlineService.deletar(usuario);
		telefoneService.deletar(usuario);
		permissoesService.deletar(usuario);
		dadosService.deletar(usuario.getDadosPessoais().getId());
		codigoService.deletar(usuario.getCodigoFuncional().getId());
		repository.deleteById(id);
	}

	public Usuario buscarPor(Long id) {
		return repository.findOne(id);
	}

	public Usuario buscarPor(String email) {
		return repository.findByEmail(email);
	}

	// @Cacheable("postCache")
	public List<Usuario> listarTodos() {
		return repository.listAll();
	}

	// @Cacheable("postCache")
	public List<Usuario> listarCadastrosCompletos() {
		return repository.listCadastrosCompleto();
	}
	
	public List<Usuario> listarCadastrosCompletos(Long departamento) {
		return repository.listCadastrosCompleto(departamento);
	}

	public void atualizarDados(Usuario usuario, String autorizacaoNome) {

		Usuario temp = repository.findOne(usuario.getId());
		usuario.setSenha(temp.getSenha());
		usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome(autorizacaoNome)));
		usuario.setHabilitado(temp.isHabilitado());

		repository.save(usuario);

	}

	public Usuario atualizarAutorizacoes(Usuario usuario, String nomeAutorizacao) {
		List<Autorizacao> autorizacoes = new ArrayList<>();
		autorizacoes.add(autorizacaoRespository.findByNome(nomeAutorizacao));
		usuario.setAutorizacoes(autorizacoes);
		return repository.save(usuario);
	}

	public Boolean emailExiste(String email, Long id) {
		return repository.existsByEmail(email, id);
	}

	public Boolean emailExiste(Usuario usuario) {
		if(usuario.getId() != null) {
			return emailExiste(usuario.getEmail(), usuario.getId());
		}else {
			return repository.existsByEmail(usuario.getEmail());
		}
	}

	public Page<Usuario> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}

	public List<UsuarioDTO> listarUsuariosDTO(Usuario usuario) {

		List<UsuarioDTO> usuarios = new ArrayList<>();
		if (usuario.temRestricao("departamento")) {
			listarCadastrosCompletos(usuario.getPermissoes().getDepartamento()).forEach(u -> usuarios.add(new UsuarioDTO(u)));
		} else {
			listarCadastrosCompletos().forEach(u -> usuarios.add(new UsuarioDTO(u)));
		}

		return usuarios;
	}

	public Page<Usuario> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}
	
	public Page<Usuario> buscarPor(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPor(departamento, id, pageable);
	}

	public Page<Usuario> buscarPorUnidade(Long id, Pageable pageable) {
		return repository.buscarPorUnidade(id, pageable);
	}

	public Page<Usuario> buscarPorUnidade(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPorUnidade(departamento, id, pageable);
	}
	
	public Page<Usuario> buscarPorCargo(Long id, Pageable pageable) {
		return repository.buscarPorCargo(id, pageable);
	}
	
	public Page<Usuario> buscarPorCargo(Long departamento, Long id, Pageable pageable) {
		return repository.buscarPorCargo(departamento, id, pageable);
	}

	public Page<Usuario> buscarPorDepartamento(Long id, Pageable pageable) {
		return repository.buscarPorDepartamento(id, pageable);
	}

	public boolean sexoExiste(String nome) {
		return repository.existsBySexo(nome);
	}

	public boolean escolaridadeExiste(String nome) {
		return repository.existsByEscolaridade(nome);
	}

	


}
