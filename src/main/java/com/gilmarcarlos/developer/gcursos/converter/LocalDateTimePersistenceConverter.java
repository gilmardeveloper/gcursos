package com.gilmarcarlos.developer.gcursos.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/* 
 * 
 * conversor do tipo LocalDateTime para sql.Time, ou virce-versa, no momento de persistir ou recuperar dados do db
 *
 *  
 * */
@Converter(autoApply = true)
public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp>{

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		return Timestamp.valueOf(localDateTime);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return timestamp.toLocalDateTime();
	}

}

