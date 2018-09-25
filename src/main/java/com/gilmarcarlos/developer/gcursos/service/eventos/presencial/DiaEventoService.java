package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.ProgramacaoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.DiaEventoRepository;

/**
* Classe com serviços de persistência para entidade (DiaEvento)
* 
* @author Gilmar Carlos
*
*/
@Service
public class DiaEventoService {

	@Autowired
	private DiaEventoRepository repository;

	@Autowired
	private AtividadePresencialService atividadesService;

	public DiaEvento salvar(DiaEvento diaEvento) {
		return repository.save(diaEvento);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<DiaEvento> listarTodos() {
		return repository.listAll();
	}

	public DiaEvento buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	/**
	 * Método que gera dias do evento baseado na data de inicio e termino  
	 * 
	 * @param evento representa um evento 
	 * @param programacao representa uma programacao
	 * 
	 */
	public void gerarDiasDeEvento(EventoPresencial evento, ProgramacaoPresencial programacao) {

		LocalDate primeiroDia = evento.getDataInicio();
		LocalDate proximoDia = evento.getDataInicio();
		LocalDate ultimoDia = evento.getDataTermino();

		DiaEvento primerioDiaEvento = new DiaEvento();
		primerioDiaEvento.setData(primeiroDia);
		primerioDiaEvento.setProgramacaoPresencial(programacao);
		salvar(primerioDiaEvento);

		while (proximoDia.isBefore(ultimoDia)) {
			proximoDia = proximoDia.plusDays(1);
			DiaEvento proximoDiaEvento = new DiaEvento();
			proximoDiaEvento.setData(proximoDia);
			proximoDiaEvento.setProgramacaoPresencial(programacao);
			salvar(proximoDiaEvento);
		}

	}
	
	/**
	 * Método que altera dias do evento baseado na data de inicio e termino,  
	 * 
	 * @param evento representa um evento 
	 * @param programacao representa uma programacao
	 * 
	 */
	public void alterarDiasDeEvento(EventoPresencial evento, ProgramacaoPresencial programacao) {

		LocalDate primeiroDia = evento.getDataInicio();
		LocalDate ultimoDia = evento.getDataTermino();
				
		for (DiaEvento dia : programacao.getDias()) {
			deleteSeForaDoInicioFim(primeiroDia, ultimoDia, dia);
		}
		
		gerarDias(primeiroDia, ultimoDia.plusDays(1), evento);
	}
	
	/**
	 * Método que deleta dias do evento baseado na data de inicio e termino,  
	 * 
	 * @param primeiroDia data de inicio do primeiro dia
	 * @param ultimoDia data de termino referente ao ultimo dia
	 * @param dia representa um dia
	 * 
	 */
	private void deleteSeForaDoInicioFim(LocalDate primeiroDia, LocalDate ultimoDia, DiaEvento dia) {
		
		if (dia.getData().isAfter(ultimoDia) || dia.getData().isBefore(primeiroDia)) {
				if (dia.getAtividades() != null) {
					atividadesService.deletePorDia(dia.getId());
				}
				deletar(dia.getId());
		}
		
	}
	
	/**
	 * Método que gera dias do evento baseado na data de inicio e termino,  
	 * 
	 * @param primeiroDia data de inicio do primeiro dia
	 * @param ultimoDia data de termino referente ao ultimo dia
	 * @param evento representa um evento
	 * 
	 */
	private void gerarDias(LocalDate proximoDia, LocalDate ultimoDia, EventoPresencial evento) {
		
		List<LocalDate> lista = new ArrayList<>();
		evento.getProgramacao().getDias().forEach(d -> lista.add(d.getData()));
		
		if(!lista.contains(proximoDia) && proximoDia.isBefore(ultimoDia)) {
			DiaEvento dia = new DiaEvento();
			dia.setData(proximoDia);
			dia.setProgramacaoPresencial(evento.getProgramacao());
			salvar(dia);
			gerarDias(proximoDia.plusDays(1), ultimoDia, evento);
		}
		
		if(lista.contains(proximoDia) && proximoDia.isBefore(ultimoDia)) {
			gerarDias(proximoDia.plusDays(1), ultimoDia, evento);
		}
	}

}
