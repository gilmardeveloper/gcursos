package com.gilmarcarlos.developer.gcursos.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/* 
 * 
 * conversor do tipo LocalDate para String, ou virce-versa, entre as requisições do spring
 *
 *  
 * */
@Component
public class LocalDateTimeSpringConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return LocalDateTime.parse(value, formatter);
	}

}
