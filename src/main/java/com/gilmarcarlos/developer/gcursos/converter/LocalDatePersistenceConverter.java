package com.gilmarcarlos.developer.gcursos.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/** 
 * 
 * conversor do tipo LocalDate para sql.Date, ou virce-versa, no momento de persistir ou recuperar dados do db
 *
 *  
 * */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		if (localDate != null) {
			return Date.valueOf(localDate);
		} else {
			return null;
		}
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		if (date != null) {
			return date.toLocalDate();
		} else {
			return null;
		}
	}

}
