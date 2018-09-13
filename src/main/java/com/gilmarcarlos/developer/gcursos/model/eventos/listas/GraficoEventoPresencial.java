package com.gilmarcarlos.developer.gcursos.model.eventos.listas;

import java.io.Serializable;
import java.util.List;

public class GraficoEventoPresencial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<GraficoAtividade> graficoAtividades;

	public List<GraficoAtividade> getGraficoAtividades() {
		return graficoAtividades;
	}

	public void setGraficoAtividades(List<GraficoAtividade> graficoAtividades) {
		this.graficoAtividades = graficoAtividades;
	}
	
}
