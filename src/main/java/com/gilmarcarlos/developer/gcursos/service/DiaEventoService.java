package com.gilmarcarlos.developer.gcursos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.DiaEventoRepository;

@Service
public class DiaEventoService {

	@Autowired
	private DiaEventoRepository repository;
	
	public DiaEvento salvar(DiaEvento diaEvento) {
		return repository.save(diaEvento);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<DiaEvento> listarTodos(){
		return repository.listAll();
	}

	public DiaEvento buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public void gerarDiasDeEvento(EventoPresencial evento, ProgramacaoPresencial programacao) {
		
		LocalDate primeiroDia = evento.getDataInicio();
		LocalDate proximoDia = evento.getDataInicio();
		LocalDate ultimoDia = evento.getDataTermino();
		
		DiaEvento primerioDiaEvento = new DiaEvento();
		primerioDiaEvento.setData(primeiroDia);
		primerioDiaEvento.setProgramacaoPresencial(programacao);
		salvar(primerioDiaEvento);
		
		while(proximoDia.isBefore(ultimoDia)) {
			proximoDia = proximoDia.plusDays(1);
			DiaEvento proximoDiaEvento = new DiaEvento();
			proximoDiaEvento.setData(proximoDia);
			proximoDiaEvento.setProgramacaoPresencial(programacao);
			salvar(proximoDiaEvento);
		}
		
		
	}
	
}
