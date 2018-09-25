package com.gilmarcarlos.developer.gcursos.utils;

/**
 * Classe com configurações para mensagens de alerta, diretório raiz para arquivos externos, dominio,
 * corpo de mensagens de email 
 * 
 * @author Gilmar Carlos
 *
 */
public class ConfUtils {
	
	public final static String BASE_DOMINIO = "http://35.188.89.234";
	public final static String BASE_ARQUIVOS_EXTERNOS = "/home/narclk123/";

	public final static String EMAIL_EVIAR_TITULO_FINALIZAR_REGISTRO = "Confirmação de registro";
	public final static String EMAIL_EVIAR_MSG_FINALIZAR_REGISTRO = "Você se cadastrou na plataforma de ensino, para finalizar o registro, favor clicar no link ao lado ";
	public final static String EMAIL_EVIAR_TITULO_REDEFINIR_SENHA = "Redefinir senha";
	public final static String EMAIL_EVIAR_MSG_REDEFINIR_SENHA = "Uma solicitação para redefinir a senha foi realizada, caso tenha sido você, favor clicar no link ";
	public final static String EMAIL_EVIAR_TITULO_NOVO_EVENTO_PUBLICADO = "Novo evento com seu perfil";
	public final static String EMAIL_EVIAR_MSG_NOVO_EVENTO_PUBLICADO = "Um novo evento foi publicado com seu perfil, ";
	
	public final static String CADASTRO_ALERTA_SOLICITACAO = "Agora verifique seu email e confirme a solicitação";
	public final static String CADASTRO_ALERTA_ERROR_REDEFINIR_SENHA = "Esse email não existe ou você ainda não ativou seu cadastro";
	public final static String CADASTRO_ALERTA_ERROR_EMAIL_CADASTRADO = "você já é cadastrado";
	
	public final static String ALERTA_SUCESSO_SALVAR = "salvo com sucesso";
	public final static String ALERTA_SUCESSO_ALTERAR = "alterado com sucesso";
	public final static String ALERTA_SUCESSO_REMOVER = "removido com sucesso";
	public final static String ALERTA_ERROR_SALVAR = "não foi possível salvar";
	public final static String ALERTA_ERROR_ALTERAR = "não foi possível alterar";
	public final static String ALERTA_ERROR_REMOVER = "não foi possível remover";
	public final static String ALERTA_ERROR_GENERICO = "um erro ocorreu";
	public final static String ALERTA_ERROR_PERMISSAO = "você não tem permissão";
	
	public final static String ALERTA_ERROR_CPF_INVALIDO = "cpf inválido";
	public final static String ALERTA_ERROR_CPF_DUPLICADO = "já existe um usuário cadastrado com esse cpf";
	
	
	

}
