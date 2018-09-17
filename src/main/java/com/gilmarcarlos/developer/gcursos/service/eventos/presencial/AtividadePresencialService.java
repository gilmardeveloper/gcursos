package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.HoraFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.AtividadePresencialRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.InscricaoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;

@Service
public class AtividadePresencialService {

	@Autowired
	private AtividadePresencialRepository repository;

	@Autowired
	private InscricaoPresencialRepository inscricoesRepository;

	@Autowired
	private NotificacaoService notificacoes;

	public AtividadePresencial salvar(AtividadePresencial atividade) throws HoraFinalMenorException {
		AtividadePresencial old = buscarPor(atividade.getId());

		LocalTime oldHoraInicio = old.getTimeInicio();
		LocalTime oldHoraFim = old.getTimeFim();

		LocalTime horaInicio = atividade.getTimeInicio();
		LocalTime horaFim = atividade.getTimeFim();
		
		LocalTime horaIncioEvento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()
				.getTimeAbertura();
		LocalTime horaFimEvento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial()
				.getTimeTermino();

		if (horaFim.isBefore(horaInicio)) {
			throw new HoraFinalMenorException();
		}

		if (horaInicio.isBefore(horaIncioEvento) || horaFim.isAfter(horaFimEvento)) {
			throw new HoraFinalMenorException("a hora inicial ou final da atividade excede o horário do evento");
		}
		

		if (!oldHoraInicio.equals(horaInicio) || !oldHoraFim.equals(horaFim)
				|| !old.getDiaEvento().equals(atividade.getDiaEvento())){

			System.err.println(oldHoraInicio + " " + horaInicio);
			
			if (!old.getInscricoes().isEmpty()) {
				old.getInscricoes().forEach(i -> {
					notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeTypeUtils.INFORMACAO,
							StatusTypeUtils.INFORMACAO,
							"sua inscrição foi cancelada porque a atividade teve seu horário alterado, atvidade: "
									+ old.getTitulo() + " evento: " + i.getEventoPresencial().getTitulo()));
					inscricoesRepository.deleteById(i.getId());

				});
			}

		}
		
		return repository.save(atividade);
	}

	public void deletar(Long id) {
		AtividadePresencial atividade = buscarPor(id);

		if (!atividade.getInscricoes().isEmpty()) {
			atividade.getInscricoes().forEach(i -> {
				notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeTypeUtils.INFORMACAO,
						StatusTypeUtils.INFORMACAO,
						"sua inscrição foi cancelada porque a atividade foi excluída do evento, atvidade: "
								+ atividade.getTitulo() + " evento: " + i.getEventoPresencial().getTitulo()));
				inscricoesRepository.deleteById(i.getId());

			});
		}

		repository.deleteById(id);
	}

	public List<AtividadePresencial> listarTodos() {
		return repository.listAll();
	}

	public AtividadePresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public List<AtividadePresencial> buscarPorEvento(Long id) {
		return repository.buscarPorEvento(id);
	}
	
	public List<AtividadePresencial> buscarPorDia(Long id) {
		return repository.buscarPorDia(id);
	}

	public void deletePorDia(Long id) {
		
		for(AtividadePresencial atividade : buscarPorDia(id)) {
			
			if (!atividade.getInscricoes().isEmpty()) {
				atividade.getInscricoes().forEach(i -> {
					notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeTypeUtils.INFORMACAO,
							StatusTypeUtils.INFORMACAO,
							"sua inscrição foi cancelada porque a atividade foi excluída do evento, atvidade: "
									+ atividade.getTitulo() + " evento: " + i.getEventoPresencial().getTitulo()));
					inscricoesRepository.deleteById(i.getId());

				});
			}
			
		}

		repository.deletePorDia(id);

	}

}
