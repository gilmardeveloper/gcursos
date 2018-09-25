package com.gilmarcarlos.developer.gcursos.model.eventos.listas;

import java.io.Serializable;
import java.util.List;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;

/**
 * Classe auxiliar para envio de dados para graficos em relatorios
 *  
 * @author Gilmar Carlos
 *
 */
public class GraficoAtividade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String titulo;
	private Integer qtdInscritos;
	private Integer qtdPresenca = 0;
	private Integer qtdAusencia = 0;
	
	public GraficoAtividade(String titulo, List<InscricaoPresencial> list) {
		this.titulo = titulo;
		qtdInscritos = list.size();
		list.forEach( i -> {
			
			if(i.isPresente()) {
				qtdPresenca++;
			}else {
				qtdAusencia++;
			}
			
		});
	}
	
	public GraficoAtividade() {
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Integer getQtdInscritos() {
		return qtdInscritos;
	}

	public void setQtdInscritos(Integer qtdInscritos) {
		this.qtdInscritos = qtdInscritos;
	}

	public Integer getQtdPresenca() {
		return qtdPresenca;
	}

	public void setQtdPresenca(Integer qtdPresenca) {
		this.qtdPresenca = qtdPresenca;
	}

	public Integer getQtdAusencia() {
		return qtdAusencia;
	}

	public void setQtdAusencia(Integer qtdAusencia) {
		this.qtdAusencia = qtdAusencia;
	}
	
}
