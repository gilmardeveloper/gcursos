package com.gilmarcarlos.developer.gcursos.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/* 
 * 
 * conversor do tipo LocalDate para sql.Date, ou virce-versa, no momento de persistir ou recuperar dados do db
 *
 *  
 * */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return Date.valueOf(localDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		return date.toLocalDate();
	}

}

