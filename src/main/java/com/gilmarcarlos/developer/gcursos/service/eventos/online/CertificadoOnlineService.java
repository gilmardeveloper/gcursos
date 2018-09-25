package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.CertificadoOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.CertificadoOnlineRepository;

/**
 * Classe com serviços de persistência para entidade (CertificadoOnline)
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class CertificadoOnlineService {

	@Autowired
	private CertificadoOnlineRepository repository;
	
	public CertificadoOnline salvar(CertificadoOnline certificado) {
		return repository.save(certificado);
	}
	
	/**
	 * Método que atualiza o conteudo do certificado 
	 * 
	 * @param certificado representa um certificado
	 * @return CertificadoOnline
	 * 
	 */
	public CertificadoOnline atualizarConteudo(CertificadoOnline certificado) {
		CertificadoOnline temp = buscarPor(certificado.getEventoOnline().getId());
		temp.setConteudo(certificado.getConteudo());
		return salvar(temp);
	}
	
	/**
	 * Método que atualiza a imagem de fundo do certificado 
	 * 
	 * @param certificado representa um certificado
	 * @return CertificadoOnline
	 * 
	 */
	public CertificadoOnline atualizarImagemFundo(CertificadoOnline certificado) {
		CertificadoOnline temp = buscarPor(certificado.getEventoOnline().getId());
		temp.setImagemFundo(certificado.getImagemFundo());
		return salvar(temp);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public CertificadoOnline buscarPor(Long id) {
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

