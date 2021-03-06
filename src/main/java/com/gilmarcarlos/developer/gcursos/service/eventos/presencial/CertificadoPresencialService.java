package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.CertificadoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.CertificadoPresencialRepository;
/**
 * Classe com serviços de persistência para entidade (CertificadoPresencial)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class CertificadoPresencialService {

	@Autowired
	private CertificadoPresencialRepository repository;
	
	public CertificadoPresencial salvar(CertificadoPresencial certificado) {
		return repository.save(certificado);
	}
	
	/**
	 * Método que atualiza o conteudo do certificado 
	 * 
	 * @param certificado representa um certificado
	 * @return CertificadoPresencial
	 * 
	 */
	public CertificadoPresencial atualizarConteudo(CertificadoPresencial certificado) {
		CertificadoPresencial temp = buscarPor(certificado.getEventoPresencial().getId());
		temp.setConteudo(certificado.getConteudo());
		return salvar(temp);
	}
	
	/**
	 * Método que atualiza a imagem de fundo do certificado 
	 * 
	 * @param certificado representa um certificado
	 * @return CertificadoPresencial
	 * 
	 */
	public CertificadoPresencial atualizarImagemFundo(CertificadoPresencial certificado) {
		CertificadoPresencial temp = buscarPor(certificado.getEventoPresencial().getId());
		temp.setImagemFundo(certificado.getImagemFundo());
		return salvar(temp);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public CertificadoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que verifica as dimenssões de uma imagem
	 * 
	 * @param imagem representa um buffer
	 * @param altura representa a altura de uma imagem
	 * @param largura representa a largura de uma imagem
	 * @return BufferedImage
	 * 
	 */
	public BufferedImage verifica(BufferedImage imagem, Integer altura, Integer largura) {
		
		if(imagem.getHeight() > altura || imagem.getWidth() > largura) {
			imagem = redimensionar(imagem, largura, altura);
		}
		
		if(imagem.getHeight() < altura || imagem.getWidth() < largura) {
			imagem = redimensionar(imagem, largura, altura);
		}
		
		return imagem;
	}
	
	/**
	 * Método que redimensiona as dimenssões de uma imagem
	 * 
	 * @param imagem representa um buffer
	 * @param altura representa a altura de uma imagem
	 * @param largura representa a largura de uma imagem
	 * @return BufferedImage
	 * 
	 */
	private BufferedImage redimensionar(BufferedImage imagem, Integer largura, Integer altura) {
		Image tmp = imagem.getScaledInstance(largura, altura, Image.SCALE_DEFAULT); // .SCALE_SMOOTH);
		imagem = new BufferedImage(largura, altura, BufferedImage.SCALE_DEFAULT); // .TYPE_INT_ARGB);
		Graphics2D g2d = imagem.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return imagem;
	}

}	

