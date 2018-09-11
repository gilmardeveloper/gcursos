package com.gilmarcarlos.developer.gcursos.converter;

import java.time.LocalDate;
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
public class LocalDateSpringConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String value) {
		if(value != null) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(value, formatter);
		}else {
			return null;
		}
	}

}
