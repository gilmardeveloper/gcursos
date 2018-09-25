package com.gilmarcarlos.developer.gcursos.service.imagens;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.images.Imagens;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialDestaque;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialTop;
import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.ImagensEventoOnlineDestaqueRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.ImagensEventoOnlineTopRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.ImagensEventoPresencialDestaqueRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.ImagensEventoPresencialTopRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.ImagensLogoListaPresencaRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.ImagensRepository;

/**
 * Classe com serviços de persistência para entidade de imagens
 * 
 * @author Gilmar Carlos
 *
 */
@Service
public class ImagensService {

	@Autowired
	private ImagensRepository repository;
	
	@Autowired
	private ImagensEventoPresencialTopRepository repositoryImagensEvePresTop;
	
	@Autowired
	private ImagensEventoOnlineTopRepository repositoryImagensEveOnlineTop;
	
	@Autowired
	private ImagensEventoOnlineDestaqueRepository repositoryImagensEveOnlineDestaque;
	
	@Autowired
	private ImagensEventoPresencialDestaqueRepository repositoryImagensEvePresDestaque;
	
	@Autowired
	private ImagensLogoListaPresencaRepository repositoryImagensLogoListaPresenca;
	
	/**
	 * Método que salva uma imagem
	 * 
	 * @param imagens representa uma imagem
	 * @return Imagens 
	 * 
	 */
	public Imagens salvar(Imagens imagens) {
		return repository.save(imagens);
	}
	
	/**
	 * Método que deleta uma imagem por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	/**
	 * Método que busca uma imagem por id
	 * 
	 * @param id id de uma imagem
	 * @return Imagens 
	 * 
	 */
	public Imagens buscarPor(Long id) {
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
	
	/**
	 * Método que deleta uma imagem do topo de um evento presencial por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletarImagemEvePreTop(Long id) {
		repositoryImagensEvePresTop.deleteById(id);
	}
	
	/**
	 * Método que salva uma imagem do topo de um evento presencial 
	 * 
	 * @param imagens representa uma imagem
	 * @return ImagensEventoPresencialTop
	 * 
	 */
	public ImagensEventoPresencialTop salvarImagemEvePresTop(ImagensEventoPresencialTop imagens){
			return repositoryImagensEvePresTop.save(imagens);
	}
	
	/**
	 * Método que deleta uma imagem de destaque de um evento presencial por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletarImagemEvePreDestaque(Long id) {
		repositoryImagensEvePresDestaque.deleteById(id);
	}
	
	/**
	 * Método que salva uma imagem de destaque de um evento presencial 
	 * 
	 * @param imagens representa uma imagem
	 * @return ImagensEventoPresencialDestaque
	 * 
	 */
	public ImagensEventoPresencialDestaque salvarImagemEvePresDestaque(ImagensEventoPresencialDestaque imagens) {
		return repositoryImagensEvePresDestaque.save(imagens);
	}
	
	/**
	 * Método que deleta uma imagem que contem a logo e titulo de um evento presencial por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletarImagemLogListaPresenca(Long id) {
		repositoryImagensLogoListaPresenca.deleteById(id);
	}
	
	/**
	 * Método que salva uma imagem que contem logo e titulo de um evento presencial 
	 * 
	 * @param imagens representa uma imagem
	 * @return ImagensLogoListaPresenca
	 * 
	 */
	public ImagensLogoListaPresenca salvarImagemLogListaPresenca(ImagensLogoListaPresenca imagens) {
		return repositoryImagensLogoListaPresenca.save(imagens);
	}
	
	/**
	 * Método que deleta uma imagem do topo de um evento online por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletarImagemEveOnlineTop(Long id) {
		repositoryImagensEveOnlineTop.deleteById(id);
	}
	
	/**
	 * Método que salva uma imagem do topo de um evento online 
	 * 
	 * @param imagens representa uma imagem
	 * @return ImagensEventoOnlineTop
	 * 
	 */
	public ImagensEventoOnlineTop salvarImagemEveOnlineTop(ImagensEventoOnlineTop imagens){
		return repositoryImagensEveOnlineTop.save(imagens);
	}
	
	/**
	 * Método que deleta uma imagem de destaque de um evento online por id
	 * 
	 * @param id id de uma imagem
	 * 
	 */
	public void deletarImagemEveOnlineDestaque(Long id) {
		repositoryImagensEveOnlineDestaque.deleteById(id);
	}
	
	/**
	 * Método que salva uma imagem de destaque de um evento online 
	 * 
	 * @param imagens representa uma imagem
	 * @return ImagensEventoOnlineDestaque
	 * 
	 */
	public ImagensEventoOnlineDestaque salvarImagemEveOnlineDestaque(ImagensEventoOnlineDestaque imagens) {
		return repositoryImagensEveOnlineDestaque.save(imagens);
	}

	/**
	 * Método que deleta uma imagem por usuario
	 * 
	 * @param usuario representa um usuario
	 * 
	 */
	public void deletar(Usuario usuario) {
		if(usuario.getImagens() != null) {
			deletar(usuario.getImagens().getId());
		}				
	}
}	

