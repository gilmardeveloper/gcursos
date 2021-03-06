package com.gilmarcarlos.developer.gcursos.model.eventos.listas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.utils.ConfUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

/**
 * 
 * Classe auxiliar que representa um relatório dinâmico que sera gerado como um arquivo de PDF
 * 
 * dependências do ireporte são necessárias e dos arquivos *.jasper
 * 
 * Os arquivos em produção devem ser copiados para fora do projeto, em local onde tenha permissões para serem lidos
 * por aplicação, no caso windows C:\\users ou C:\ e no linux /home/user/
 * 
 * A alteração do caminho deve ser realizado em (ConfUtils) na constante BASE_ARQUIVOS_EXTERNOS
 * 
 * */

@Component
public class RelatorioInscricoesOnline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer index;
	private String titulo;
	private String responsavel;
	private String nome;
	private String cpf;
	private String codigoFuncional;
	private String unidadeTrabalho;
	private String participacao;

	private List<RelatorioInscricoesOnline> lista;

	public InputStream gerar(Usuario usuario, EventoOnline evento, String tipo) {
		this.lista = new ArrayList<>();
		parse(usuario, evento, tipo);
		return gerarPdf();
	}

	private InputStream gerarPdf() {

		JRDataSource dataSource = new JRBeanCollectionDataSource(this.lista);
		Map<String, Object> parametros = new HashMap<>();
		JasperPrint print;

		try {

			print = JasperFillManager.fillReport(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_online.jasper", parametros, dataSource); 
																														
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_online.pdf")); 																				
																														

			SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);

			SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
			exportConfig.setMetadataAuthor("Desconhecido");
			exportConfig.setEncrypted(true);
			exportConfig.setAllowedPermissionsHint("PRINTING");

			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);

			exporter.exportReport();
			System.out.println("retornando o arquivo");

			return new FileInputStream(new File(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_online.pdf")); 
																							
																							
																							

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void parse(Usuario usuario, EventoOnline evento, String tipo) {

		if (usuario.temRestricao("departamento")) {

			evento.getInscricoes().forEach(i -> {
				if (usuario.temMesmoDepartamento(i.getUsuario())) {
					if (tipo.equalsIgnoreCase("todos")) {

						preencherDados(evento, i);

					} else if (tipo.equalsIgnoreCase("andamento")) {

						if (!i.isFinalizado()) {
							preencherDados(evento, i);
						}

					} else {
						if (i.isFinalizado()) {
							preencherDados(evento, i);
						}
					}
				}
			});

		} else {

			evento.getInscricoes().forEach(i -> {

				if (tipo.equalsIgnoreCase("todos")) {

					preencherDados(evento, i);

				} else if (tipo.equalsIgnoreCase("andamento")) {

					if (!i.isFinalizado()) {
						preencherDados(evento, i);
					}

				} else {
					if (i.isFinalizado()) {
						preencherDados(evento, i);
					}
				}

			});
		}
	}

	private void preencherDados(EventoOnline evento, InscricaoOnline i) {
		RelatorioInscricoesOnline presenca = new RelatorioInscricoesOnline();

		presenca.setTitulo(evento.getTitulo());
		presenca.setResponsavel(evento.getResponsavel().getNome());

		presenca.setParticipacao(evento.progresso(i.getUsuario()) + "%");

		presenca.setNome(i.getUsuario().getNome());
		presenca.setCpf(i.getUsuario().getDadosPessoais().getCpf());
		presenca.setUnidadeTrabalho(i.getUsuario().getCodigoFuncional().getUnidadeTrabalho().getNome() + " - "
				+ i.getUsuario().getCodigoFuncional().getUnidadeTrabalho().getDepartamento().getIdentidade());
		presenca.setCodigoFuncional(i.getUsuario().getCodigoFuncional().getCodigo());
		presenca.setIndex(this.lista.size() + 1);
		this.lista.add(presenca);
	}

	/*
	 * 
	 * geters and setters
	 * 
	 */

	public String getNome() {
		return nome;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCodigoFuncional() {
		return codigoFuncional;
	}

	public void setCodigoFuncional(String codigoFuncional) {
		this.codigoFuncional = codigoFuncional;
	}

	public String getUnidadeTrabalho() {
		return unidadeTrabalho;
	}

	public void setUnidadeTrabalho(String unidadeTrabalho) {
		this.unidadeTrabalho = unidadeTrabalho;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getParticipacao() {
		return participacao;
	}

	public void setParticipacao(String particiapacao) {
		this.participacao = particiapacao;
	}

}
