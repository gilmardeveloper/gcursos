package com.gilmarcarlos.developer.gcursos.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.CodigoFuncional;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioExisteException;
import com.gilmarcarlos.developer.gcursos.service.CargoService;
import com.gilmarcarlos.developer.gcursos.service.CodigoFuncionalService;
import com.gilmarcarlos.developer.gcursos.service.DadosPessoaisService;
import com.gilmarcarlos.developer.gcursos.service.EscolaridadeService;
import com.gilmarcarlos.developer.gcursos.service.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.service.SexoService;
import com.gilmarcarlos.developer.gcursos.service.TelefoneUsuarioService;
import com.gilmarcarlos.developer.gcursos.service.UnidadeTrabalhoService;
import com.gilmarcarlos.developer.gcursos.service.UsuarioService;

@Controller
@RequestMapping("/dashboard/usuario/")
public class UsuarioControler {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private DadosPessoaisService dadosService;

	@Autowired
	private TelefoneUsuarioService telefoneService;

	@Autowired
	private CodigoFuncionalService codigoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private ImagensService imagensService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private SexoService sexoService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private UnidadeTrabalhoService unidadeService;

	private Authentication autenticado;

	@GetMapping("perfil")
	public String painel(Model model) {
		Usuario usuarioLogado = getUsuario();
		model.addAttribute("notificacoes", usuarioLogado.getNotificaoesNaoLidas());

		int naoVisualizadas = usuarioLogado.getNotificaoesNaoLidas().size();
		int visualizadas = usuarioLogado.getNotificaoesLidas().size();
		if (usuarioLogado.isPerfilCompleto()) {
			model.addAttribute("usuario", usuarioLogado);
			model.addAttribute("sexos", sexoService.listarTodos());
			model.addAttribute("escolaridades", escolaridadeService.listarTodos());
			model.addAttribute("cargos", cargoService.listarTodos());
			model.addAttribute("unidades", unidadeService.listarTodos());
			model.addAttribute("lidas", visualizadas);
			model.addAttribute("naoLidas", naoVisualizadas);
			model.addAttribute("todos", visualizadas + naoVisualizadas);
			return "dashboard/usuario/profile";

		} else {
			return "redirect:/dashboard/complete-cadastro";
		}

	}

	@PostMapping("redefinir-senha")
	public String redefinirSenha(@RequestParam("senha") String senha) {

		if (!senha.equals("") || senha != null) {
			usuarioService.redefinirSenha(getUsuario(), senha);
			notificacaoService.salvar(new Notificacao(getUsuario(), "Alteração de senha", IconeType.INFORMACAO,
					StatusType.SUCESSO, "senha foi alterada com sucesso"));
		}

		return "redirect:/dashboard/";
	}

	@PostMapping("dados-pessoais/salvar")
	public String dadosPessoaisSalvar(Usuario usuario, RedirectAttributes model) {

		try {

			DadosPessoais dados = usuario.getDadosPessoais();
			dadosService.salvarD(dados);
			usuarioService.atualizarDadosNoEncryptSenha(usuario);
			notificacaoService.salvar(new Notificacao(getUsuario(), "Dados pessoais alterados", IconeType.INFORMACAO,
					StatusType.SUCESSO, "dados foram alterados com sucesso"));
			return "redirect:/dashboard/";

		} catch (UsuarioExisteException e) {
			notificacaoService.salvar(new Notificacao(getUsuario(), "Falha em alterar dados", IconeType.INFORMACAO,
					StatusType.ERRO, e.getMessage()));
			return "redirect:/dashboard/";
		}
	}

	@PostMapping("dados-profissionais/salvar")
	public String dadosProfissionaisSalvar(CodigoFuncional codigo, RedirectAttributes model) {

		codigoService.salvar(codigo);
		notificacaoService.salvar(new Notificacao(getUsuario(), "Dados profissionais alterados", IconeType.INFORMACAO,
				StatusType.SUCESSO, "dados foram alterados com sucesso"));
		return "redirect:/dashboard/";
	}

	@PostMapping("telefones/salvar")
	public String telefonesSalvar(TelefoneUsuario telefone, RedirectAttributes model) {

		telefoneService.salvar(telefone);
		notificacaoService.salvar(new Notificacao(getUsuario(), "Telefone salvo", IconeType.INFORMACAO,
				StatusType.SUCESSO, "telefone salvo com sucesso"));
		return "redirect:/dashboard/";
	}

	@PostMapping("telefones/excluir")
	public String telefonesExcluir(@RequestParam("id") Long id, RedirectAttributes model) {

		telefoneService.deletar(id);
		notificacaoService.salvar(new Notificacao(getUsuario(), "Telefone excluído", IconeType.INFORMACAO,
				StatusType.SUCESSO, "telefone removido com sucesso"));
		return "redirect:/dashboard/";
	}

	@GetMapping("notificacoes")
	public String notificacoes(Model model) {

		getUsuario().getNotificaoesNaoLidas().forEach(n -> notificacaoService.foiLida(n));
		model.addAttribute("usuario", getUsuario());
		model.addAttribute("notifications", getUsuario().getNotificacoes());
		return "dashboard/usuario/notificacoes";
	}

	@GetMapping("notificacoes/deletar")
	public String ndeletarTodas(RedirectAttributes model) {
		getUsuario().getNotificacoes().forEach(n -> notificacaoService.deletar(n.getId()));
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removidos com sucesso");
		return "redirect:/dashboard/usuario/notificacoes";
	}

	@GetMapping("notificacoes/deletar/{id}")
	public String deletar(@PathVariable("id") Long id, RedirectAttributes model) {
		notificacaoService.deletar(id);
		model.addFlashAttribute("alert", "alert alert-fill-success alert-dismissible fade show");
		model.addFlashAttribute("message", "removido com sucesso");
		return "redirect:/dashboard/usuario/notificacoes";
	}

	@GetMapping("/avatar.png")
	public @ResponseBody byte[] imagemFrente() {
		Blob imagem = getUsuario().getImagens().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 100, 100);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/salvar/imagens")
	public String salvar(@Valid Imagens imagens, BindingResult result, RedirectAttributes model) {
		if (!result.hasErrors()) {
			if (getUsuario().getImagens() != null) {
				imagensService.deletar(getUsuario().getImagens().getId());
			}
			imagens.setUsuario(getUsuario());
			imagensService.salvar(imagens);
			notificacaoService.salvar(new Notificacao(getUsuario(), "Alterou o seu avatar", IconeType.INFORMACAO,
					StatusType.SUCESSO, "uma nova imagem de avatar foi adicionada"));
		} else {
			notificacaoService
					.salvar(new Notificacao(getUsuario(), "Falha ao alterar o seu avatar", IconeType.INFORMACAO,
							StatusType.ERRO, "tentou adicionar imagem vazia ou outro arquivo como avatar"));
		}
		return "redirect:/dashboard/";

	}

	private Usuario getUsuario() {
		autenticado = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPor(autenticado.getName());
		return usuario;
	}

}
