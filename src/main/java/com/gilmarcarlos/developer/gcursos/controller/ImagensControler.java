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

import com.gilmarcarlos.developer.gcursos.service.eventos.online.EventoOnlineService;
import com.gilmarcarlos.developer.gcursos.service.eventos.presencial.EventoPresencialService;
import com.gilmarcarlos.developer.gcursos.service.imagens.ImagensService;
import com.gilmarcarlos.developer.gcursos.service.usuarios.UsuarioService;

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
	
	
	@GetMapping("/evento/{id}/responsavel.png")
	public @ResponseBody byte[] imagemResponsavel(@PathVariable("id") Long id) {
		Blob imagem = usuarioService.buscarPor(id).getImagens().getImagem();
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
	
	@GetMapping("/evento/presencial/{id}/imagem-top.png")
	public @ResponseBody byte[] imagemTop(@PathVariable("id") Long id) {
		Blob imagem = eventoPresencialService.buscarPor(id).getImagemTopDetalhes().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 250, 1000);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/evento/online/{id}/imagem-top.png")
	public @ResponseBody byte[] imagemOnlineTop(@PathVariable("id") Long id) {
		Blob imagem = eventoOnlineService.buscarPor(id).getImagemTopDetalhes().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 250, 1000);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/evento/presencial/{id}/imagem-destaque.png")
	public @ResponseBody byte[] imagemDestaque(@PathVariable("id") Long id) {
		Blob imagem = eventoPresencialService.buscarPor(id).getImagemDestaque().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 293, 367);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/evento/online/{id}/imagem-destaque.png")
	public @ResponseBody byte[] imagemOnlineDestaque(@PathVariable("id") Long id) {
		Blob imagem = eventoOnlineService.buscarPor(id).getImagemDestaque().getImagem();
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(imagem.getBinaryStream());
			bufferedImage = imagensService.verifica(bufferedImage, 293, 367);
			ImageIO.write(bufferedImage, "png", byteOutStream);
			return byteOutStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
