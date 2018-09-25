package com.gilmarcarlos.developer.gcursos.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gilmarcarlos.developer.gcursos.service.eventos.online.CertificadoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.CertificadoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

/**
 * Classe de controle para imagens
 *  
 * @author Gilmar Carlos
 *
 */
@Controller
@RequestMapping("/imagens")
public class ImagensControler {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EventoPresencialService eventoPresencialService;
	
	@Autowired
	private EventoOnlineService eventoOnlineService;
	
	@Autowired
	private ImagensService imagensService;
	
	@Autowired
	private CertificadoOnlineService certificadoOnlineService;
	
	@Autowired
	private CertificadoPresencialService certificadoPresencialService;
	
	@GetMapping("/usuario/{id}/avatar.png")
	public @ResponseBody byte[] imagemUsuario(@PathVariable("id") Long id) {
		Blob imagem = usuarioService.buscarPor(id).getImagens().getImagem();
		return getImagemInputStream(imagem, 100, 100);
	}

	@GetMapping("/evento/{id}/responsavel.png")
	public @ResponseBody byte[] imagemResponsavel(@PathVariable("id") Long id) {
		Blob imagem = usuarioService.buscarPor(id).getImagens().getImagem();
		return getImagemInputStream(imagem, 100, 100);
	}
	
	@GetMapping("/evento/presencial/{id}/imagem-top.png")
	public @ResponseBody byte[] imagemTop(@PathVariable("id") Long id) {
		Blob imagem = eventoPresencialService.buscarPor(id).getImagemTopDetalhes().getImagem();
		return getImagemInputStream(imagem, 250, 1000);
	}
	
	@GetMapping("/evento/online/{id}/imagem-top.png")
	public @ResponseBody byte[] imagemOnlineTop(@PathVariable("id") Long id) {
		Blob imagem = eventoOnlineService.buscarPor(id).getImagemTopDetalhes().getImagem();
		return getImagemInputStream(imagem, 250, 1000);
	}
	
	@GetMapping("/evento/presencial/{id}/imagem-destaque.png")
	public @ResponseBody byte[] imagemDestaque(@PathVariable("id") Long id) {
		Blob imagem = eventoPresencialService.buscarPor(id).getImagemDestaque().getImagem();
		return getImagemInputStream(imagem, 293, 367);
	}
	
	@GetMapping("/evento/online/{id}/imagem-destaque.png")
	public @ResponseBody byte[] imagemOnlineDestaque(@PathVariable("id") Long id) {
		Blob imagem = eventoOnlineService.buscarPor(id).getImagemDestaque().getImagem();
		return getImagemInputStream(imagem, 293, 367);
	}
	
	@GetMapping("/evento/online/{id}/imagem-certificado.png")
	public @ResponseBody byte[] imagemOnlineCertificado(@PathVariable("id") Long id) {
		Blob imagem = certificadoOnlineService.buscarPor(id).getImagemFundo();
		return getImagemInputStream(imagem, 502, 650);
	}
	
	@GetMapping("/evento/presencial/{id}/imagem-certificado.png")
	public @ResponseBody byte[] imagemPresencialCertificado(@PathVariable("id") Long id) {
		Blob imagem = certificadoPresencialService.buscarPor(id).getImagemFundo();
		return getImagemInputStream(imagem, 502, 650);
	}
	
	private byte[] getImagemInputStream(Blob imagem, Integer altura, Integer largura) {
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, altura, largura);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
