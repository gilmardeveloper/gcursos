package com.gilmarcarlos.developer.gcursos.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.repository.ImagensRepository;

@Service
public class ImagensService {

	@Autowired
	private ImagensRepository repository;
	
	public Imagens salvar(Imagens imagens) {
		return repository.save(imagens);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public Imagens buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public BufferedImage verifica(BufferedImage imagem, Integer altura, Integer largura) {
		
		if(imagem.getHeight() > altura || imagem.getWidth() > largura) {
			imagem = redimensionar(imagem, largura, altura);
		}
		
		if(imagem.getHeight() < altura || imagem.getWidth() < largura) {
			imagem = redimensionar(imagem, largura, altura);
		}
		
		return imagem;
	}

	private BufferedImage redimensionar(BufferedImage imagem, Integer largura, Integer altura) {
		Image tmp = imagem.getScaledInstance(largura, altura, Image.SCALE_DEFAULT); // .SCALE_SMOOTH);
		imagem = new BufferedImage(largura, altura, BufferedImage.SCALE_DEFAULT); // .TYPE_INT_ARGB);
		Graphics2D g2d = imagem.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return imagem;
	}
	
}
