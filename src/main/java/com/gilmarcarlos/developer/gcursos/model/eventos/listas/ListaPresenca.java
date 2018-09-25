package com.gilmarcarlos.developer.gcursos.model.eventos.listas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
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
 * Classe auxiliar que representa uma lista de presença dinâmico que sera gerado como um arquivo de PDF
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
public class ListaPresenca implements  Serializable{

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
	
	private String cabecalhoTitulo;
	private InputStream imagemEsquerda;
	private InputStream imagemDireita;
	
	
	private List<ListaPresenca> lista;
	
	public InputStream gerar(AtividadePresencial atividade) {
		this.lista = new ArrayList<>();
		parse(atividade);
		return gerarPdf();
	}

	private InputStream gerarPdf() {
				
		
		JRDataSource dataSource = new JRBeanCollectionDataSource(this.lista);
		Map<String, Object> parametros = new HashMap<>();
		JasperPrint print;
		
		try {
			
			print = JasperFillManager.fillReport(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "lista_presenca.jasper", parametros, dataSource);  // em produção alterar para "/caminho_da_pasta/" + "arquivo.jasper"
			JRPdfExporter exporter = new JRPdfExporter();
			 
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(
			  new SimpleOutputStreamExporterOutput(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "lista_presenca.pdf"));  // em produção alterar para "/caminho_da_pasta/arquivo.pdf"
			 
			SimplePdfReportConfiguration reportConfig
			  = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);
			 
			SimplePdfExporterConfiguration exportConfig
			  = new SimplePdfExporterConfiguration();
			exportConfig.setMetadataAuthor("Desconhecido");
			exportConfig.setEncrypted(true);
			exportConfig.setAllowedPermissionsHint("PRINTING");
			 
			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);
			 
			exporter.exportReport();
			System.out.println("retornando o arquivo");
			
			return new FileInputStream(new File(ConfUtils.BASE_ARQUIVOS_EXTERNOS + "lista_presenca.pdf")); // em produção alterar para "/caminho_da_pasta/" + "arquivo.pdf"
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void parse(AtividadePresencial atividade) {
		atividade.getInscricoes().forEach( i -> {
			
			ListaPresenca presenca = new ListaPresenca();
			EventoPresencial eventoPresencial = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial();
			
			presenca.setTitulo(eventoPresencial.getTitulo());
			presenca.setAtividade(atividade.getTitulo());
			presenca.setData(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(atividade.getDiaEvento().getData()));
			presenca.setHoraInicio(atividade.getHoraInicio());
			presenca.setHoraFim(atividade.getHoraFim());
			presenca.setSala(atividade.getSala());
			presenca.setVagas(String.valueOf(atividade.getVagas()));
			presenca.setResponsavel(atividade.getNomeResponsavel());
			
			try {
				presenca.setImagemEsquerda(eventoPresencial.getImagemLogo().getImagemEsquerda().getBinaryStream());
				presenca.setImagemDireita(eventoPresencial.getImagemLogo().getImagemDireita().getBinaryStream());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			presenca.setCabecalhoTitulo(eventoPresencial.getImagemLogo().getTitulo());
			
			presenca.setNome(i.getUsuario().getNome());
			presenca.setCpf(i.getUsuario().getDadosPessoais().getCpf());
			presenca.setUnidadeTrabalho(i.getUsuario().getCodigoFuncional().getUnidadeTrabalho().getNome() + " - " + i.getUsuario().getCodigoFuncional().getUnidadeTrabalho().getDepartamento().getIdentidade());
			presenca.setCodigoFuncional(i.getUsuario().getCodigoFuncional().getCodigo());
			presenca.setIndex(this.lista.size() + 1);
			this.lista.add(presenca);
			
		});
		
	}
	
	/* 
	 * 
	 * geters and setters
	 * 
	 * */
	
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

	public InputStream getImagemEsquerda() {
		return imagemEsquerda;
	}

	public void setImagemEsquerda(InputStream imagemEsquerda) {
		this.imagemEsquerda = imagemEsquerda;
	}

	public InputStream getImagemDireita() {
		return imagemDireita;
	}

	public void setImagemDireita(InputStream imagemDireita) {
		this.imagemDireita = imagemDireita;
	}

	public String getCabecalhoTitulo() {
		return cabecalhoTitulo;
	}

	public void setCabecalhoTitulo(String cabecalhoTitulo) {
		this.cabecalhoTitulo = cabecalhoTitulo;
	}
	
}

