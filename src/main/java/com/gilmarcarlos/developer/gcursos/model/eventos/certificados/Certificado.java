package com.gilmarcarlos.developer.gcursos.model.eventos.certificados;

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

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.InscricaoPresencial;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

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
public class Certificado implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String nome;
	private String tituloEvento;
	private String cargaHoraria;
	private String qtdAtividades;
	private String periodo;
	
	private InputStream imagemFundo;
	
	
	private List<Certificado> lista;
	
	/* metodo publico para realizar o parse e gerar o contrato */
	public InputStream gerar(EventoPresencial evento, Usuario usuario) {
		this.lista = new ArrayList<>();
		parse(evento, usuario);
		return gerarPdf();
	}

	/* 
	 * 
	 * metodo privado para gerar o pdf
	 * para windows usar o arquivo novo_contrato.jasper para linux usar o arquivo novo_contrato_linux.jasper
	 * os dois arquivos se encontram na raiz do projeto, em produção esses arquivos devem ser copiados para fora
	 * da pasta raiz do projeto
	 * 
	 * os arquivos em pdf são salvos na pasta raiz, e em produção na pasta que você especificar, o nome do arquivo
	 * é o numero do cpf do inquilino.pdf
	 *  
	 * */
	private InputStream gerarPdf() {
				
		
		JRDataSource dataSource = new JRBeanCollectionDataSource(this.lista);
		Map<String, Object> parametros = new HashMap<>();
		JasperPrint print;
		
		try {
			
			print = JasperFillManager.fillReport("certificado.jasper", parametros, dataSource);  // em produção alterar para "/caminho_da_pasta/" + "arquivo.jasper"
			JRPdfExporter exporter = new JRPdfExporter();
			 
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(
			  new SimpleOutputStreamExporterOutput("certificado.pdf"));  // em produção alterar para "/caminho_da_pasta/arquivo.pdf"
			 
			SimplePdfReportConfiguration reportConfig
			  = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);
			 
			SimplePdfExporterConfiguration exportConfig
			  = new SimplePdfExporterConfiguration();
			exportConfig.setMetadataAuthor("Gilmar Carlos");
			exportConfig.setEncrypted(true);
			exportConfig.setAllowedPermissionsHint("PRINTING");
			 
			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);
			 
			exporter.exportReport();
			System.out.println("retornando o arquivo");
			
			return new FileInputStream(new File("certificado.pdf")); // em produção alterar para "/caminho_da_pasta/" + "arquivo.pdf"
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 * Transforma todos os campos necessários para gerar o pdf, em campos de string
	 * 
	 * */
	private void parse(EventoPresencial evento, Usuario usuario) {
		
		Certificado certificado = new Certificado();
		certificado.setNome(usuario.getNome());
		certificado.setTituloEvento(evento.getTitulo());
		certificado.setCargaHoraria("Com carga horária estimada em " + evento.getCargaHoraria() + " horas");
		certificado.setPeriodo("com início em " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(evento.getDataInicio()) + " e concluído em " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(evento.getDataTermino()));
		int qtdAtividades = 0;
		
		for(InscricaoPresencial i : evento.getInscricoes()){
			if(i.getUsuario().equals(usuario)) {
				qtdAtividades += (i.isPresente() ? 1 : 0);
			}
		}
		
		if(qtdAtividades == 1) {
			certificado.setQtdAtividades("Finalizou uma atividade no evento");
		}else {
			certificado.setQtdAtividades("Finalizou " + qtdAtividades + " atividades no evento");
		}
		
		certificado.setImagemFundo(null);
		
		this.lista.add(certificado);
	}

	
	/* 
	 * 
	 * geters and setters
	 * 
	 * */
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTituloEvento() {
		return tituloEvento;
	}
	
	public void setTituloEvento(String tituloEvento) {
		this.tituloEvento = tituloEvento;
	}
	
	public String getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public String getQtdAtividades() {
		return qtdAtividades;
	}
	
	public void setQtdAtividades(String qtdAtividades) {
		this.qtdAtividades = qtdAtividades;
	}
	
	public String getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public InputStream getImagemFundo() {
		return imagemFundo;
	}
	
	public void setImagemFundo(InputStream imagemFundo) {
		this.imagemFundo = imagemFundo;
	}
	
}

