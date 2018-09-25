package com.gilmarcarlos.developer.gcursos.converter;

import java.sql.Blob;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gilmarcarlos.developer.gcursos.converter.exceptions.FileSpringConverterException;

/** 
 * 
 * conversor do tipo MultipartFile para Blob, ou virce-versa, no momento de persistir ou recuperar dados do db
 *
 *  
 * */
@Component
public class FileSpringConverter implements Converter<MultipartFile, Blob> {

	@Autowired
	private EntityManagerFactory emf;
	
			
	private Session getSession() {
		return emf.createEntityManager().unwrap(Session.class);
	}

	@Override
	public Blob convert(MultipartFile source) {
				
		try {
			if(source.isEmpty()) {
				throw new FileSpringConverterException();
			}
			
			if(!source.getContentType().contains("image")) {
				throw new FileSpringConverterException("não é uma imagem");
			}
			return Hibernate.getLobCreator(getSession().getSessionFactory().openSession()).createBlob(source.getBytes());
		} catch (Exception e) {
			System.out.println("erro: falha na conversão da imagem");
			e.printStackTrace();
			return null;
		} 
	}
}
