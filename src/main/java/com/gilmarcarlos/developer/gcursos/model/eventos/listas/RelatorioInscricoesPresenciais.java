package com.gilmarcarlos.developer.gcursos.model.eventos.listas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
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

/*
 * 
 * Classe auxiliar que representa um Contrato dinâmico que sera gerado como um arquivo de PDF
 * 
 * É manipulado a partir da view contratos-info.html, e a requisição é disparada a partir do botão gerar
 * 
 * As requisições e respostas são tratadas pelo controle @ContratoController
 * 
 * dependências do ireporte são necessárias e dos arquivos novo_contrato.jasper
 * 
 * por medida de compatibilidade existem dois arquivos .jasper, um para windows e outro para linux, sua configuração
 * deve ser realizada de forma manual no metodo gerarPdf()
 * 
 * Os arquivos em produção devem ser copiados para fora do projeto, em local onde tenha permissões para serem lidos
 * por aplicação, no caso windows C:\\users ou C:\ e no linux /home/user/
 * 
 * */

@Component
public class RelatorioInscricoesPresenciais implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer index;
	private String titulo;
	private String atividade;
	private String data;
	private String horaInicio;
	private String horaFim;
	private String sala;
	private String vagas;
	private String responsavel;
	private String nome;
	private String cpf;
	private String codigoFuncional;
	private String unidadeTrabalho;
	private String participacao;

	private List<RelatorioInscricoesPresenciais> lista;

	/* metodo publico para realizar o parse e gerar o contrato */
	public InputStream gerar(Usuario usuario, AtividadePresencial atividade, String tipo) {
		this.lista = new ArrayList<>();
		parse(usuario, atividade, tipo);
		return gerarPdf();
	}

	/*
	 * 
	 * metodo privado para gerar o pdf para windows usar o arquivo
	 * novo_contrato.jasper para linux usar o arquivo novo_contrato_linux.jasper os
	 * dois arquivos se encontram na raiz do projeto, em produção esses arquivos
	 * devem ser copiados para fora da pasta raiz do projeto
	 * 
	 * os arquivos em pdf são salvos na pasta raiz, e em produção na pasta que você
	 * especificar, o nome do arquivo é o numero do cpf do inquilino.pdf
	 * 
	 */
	private InputStream gerarPdf() {

		JRDataSource dataSource = new JRBeanCollectionDataSource(this.lista);
		Map<String, Object> parametros = new HashMap<>();
		JasperPrint print;

		try {

			print = JasperFillManager.fillReport(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_presencial.jasper", parametros,
					dataSource); 
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(
					new SimpleOutputStreamExporterOutput(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_presencial.pdf")); 
																											

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

			return new FileInputStream(new File(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "relatorio_inscricoes_evento_presencial.pdf")); 

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 * Transforma todos os campos necessários para gerar o pdf, em campos de string
	 * 
	 */
	private void parse(Usuario usuario, AtividadePresencial atividade, String tipo) {

		if (usuario.temRestricao("departamento")) {

			atividade.getInscricoes().forEach(i -> {
				if (usuario.temMesmoDepartamento(i.getUsuario())) {
					if (tipo.equalsIgnoreCase("todos")) {

						preencherDados(atividade, i);

					} else if (tipo.equalsIgnoreCase("ausentes")) {

						if (!i.isPresente()) {
							preencherDados(atividade, i);
						}

					} else {
						if (i.isPresente()) {
							preencherDados(atividade, i);
						}
					}
				}
			});

		} else {

			atividade.getInscricoes().forEach(i -> {

				if (tipo.equalsIgnoreCase("todos")) {

					preencherDados(atividade, i);

				} else if (tipo.equalsIgnoreCase("ausentes")) {

					if (!i.isPresente()) {
						preencherDados(atividade, i);
					}

				} else {
					if (i.isPresente()) {
						preencherDados(atividade, i);
					}
				}

			});
		}

	}

	private void preencherDados(AtividadePresencial atividade, InscricaoPresencial i) {
		RelatorioInscricoesPresenciais presenca = new RelatorioInscricoesPresenciais();
		EventoPresencial eventoPresencial = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();

		presenca.setTitulo(eventoPresencial.getTitulo());
		presenca.setAtividade(atividade.getTitulo());
		presenca.setData(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(atividade.getDiaEvento().getData()));
		presenca.setHoraInicio(atividade.getHoraInicio());
		presenca.setHoraFim(atividade.getHoraFim());
		presenca.setSala(atividade.getSala());
		presenca.setVagas(String.valueOf(atividade.getVagas()));
		presenca.setResponsavel(atividade.getNomeResponsavel());

		presenca.setParticipacao((i.isPresente() ? "PRESENTE" : "AUSENTE"));

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

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getVagas() {
		return vagas;
	}

	public void setVagas(String vagas) {
		this.vagas = vagas;
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
