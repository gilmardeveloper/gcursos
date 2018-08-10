-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 10-Ago-2018 às 00:37
-- Versão do servidor: 5.7.21-log
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gcursos`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `unidade_trabalho`
--

CREATE TABLE `unidade_trabalho` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gerente` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `qtd_funcionarios` bigint(20) DEFAULT NULL,
  `departamento_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `unidade_trabalho`
--

INSERT INTO `unidade_trabalho` (`id`, `email`, `gerente`, `nome`, `qtd_funcionarios`, `departamento_id`) VALUES
(1, 'equipetecnica1@gmail.com', 'Kelly Cristina Bergo Moreira', 'RS CENTRO - SEDE', 59, 1),
(2, 'oficinastear@gmail.com', 'Denise Castanho Antunes', 'PROJETO TEAR', 24, 1),
(3, '', '', 'CENTRO DE CONTROLE DE ZOONOSES', 0, 5),
(4, 'CERESI.GUARULHOS@GMAIL.COM', 'VIVIANE GONÇALVES ALVES', 'CENTRO DE REFERÊNCIA DO IDOSO - CERESI', 34, 5),
(5, 'rh.ambcrianca@gmail.com', 'HAYDÉE BARRETO AGUIAR SILVA', 'AMBULATÓRIO DA CRIANÇA', 120, 1),
(6, 'blhguarulhos@gmail.com', 'Simone Irami Predeus Martins de Sá', 'BANCO DE LEITE', 16, 5),
(7, 'campd.guarulhos@gmail.com', 'ELISANGELA ARANTES DE SOUZA SIQUEIRA', 'CAMPD', 28, 1),
(8, '', '', 'CAPS ARCO-IRIS', 0, 3),
(9, 'capsad.2@gmail.com', 'Solange Aparecida Bená', 'CAPS AD - DR. ARNALDO BRAVO', 68, 1),
(10, 'caps2.osoriocesar@gmail.com', 'VANDYMEIRE GONÇALVES SANTOS', 'CAPS - HD - DR. OSÓRIO CÉSAR', 32, 2),
(11, 'capsbomclima@gmail.com', 'Sandra Regina Azevedo de Melo', 'CAPS - BOM CLIMA', 23, 1),
(12, 'gerencia.cemeg@gmail.com', 'EDILENE COUTO DE MORAES', 'CEMEG - CENTRO', 216, 1),
(13, 'ceriiguarulhos@gmail.com', 'Christina Iglesias', 'CER II', 67, 1),
(14, 'ctaguarulhos@gmail.com', 'MARINA NAIRISMAGI ALVES', 'CENTRO DE TESTAGEM E ACOLHIMENTO', 43, 1),
(15, 'centro.macedo@gmail.com', 'Luis Arlindo Coutinho Caetano', 'CEO MACEDO', 40, 1),
(16, 'cerestguarulhos@gmail.com', 'walquiria de cerqueira costa kasaz', 'CEREST', 30, 1),
(17, '', '', 'FARMÁCIA POPULAR', 0, 1),
(18, '', '', 'POLICLÍNICA JD. PARAVENTI', 0, 1),
(19, 'cavadasubs@gmail.com', 'ELISABETE ELIAS', 'UBS CAVADAS', 30, 1),
(20, 'CSCECAP@GMAIL.COM', 'MÁRCIA ROSA VIANNA', 'UBS CECAP', 50, 1),
(21, 'UBSFMONTANHA@GMAIL.COM', 'FRANCISCO JUNIOR DE SOUZA', 'UBS FLOR DA MONTANHA', 60, 1),
(22, 'ubsitapegica@gmail.com', 'Raquel Tychoniuk', 'UBS ITAPEGICA', 31, 1),
(23, 'ubsmunhoz@gmail.com', 'Mônica Aparecida Josefik Celestino', 'UBS JARDIM MUNHOZ', 47, 1),
(24, '', '', 'UBS JARDIM PARAVENTI', 0, 1),
(25, 'jdvilagalvao@gmail.com', 'CRISTIANE ALVES BRAGA', 'UBS JARDIM VILA GALVÃO', 29, 1),
(26, 'UBSPONTEGRANDE@GMAIL.COM', 'ELISABETE FERREIRA GOMES PEREIRA', 'UBS PONTE GRANDE', 35, 1),
(27, 'USFSAORAFAELUSF@GMAIL.COM', 'SARA CORSI ZANZERE', 'UBS SÃO RAFAEL', 54, 1),
(28, 'ubssaoricardo@gmail.com', 'Vânia Nunes Bastos Gamberini', 'UBS SÃO RICARDO', 35, 1),
(29, 'ubstranquilidade@gmail.com', 'jeanete aparecida valerio de freitas alves', 'UBS TRANQUILIDADE', 53, 1),
(30, 'ubs.vlbarros@gmail.com', 'Dagmar da Rocha Strefezzi', 'UBS VILA BARROS', 53, 1),
(31, 'ubsvlfatima@gmail.com', 'ELISÂNGELA DOS SANTOS', 'UBS VILA FÁTIMA', 46, 1),
(32, '', '', 'CONSULTORIO NA RUA - UBS FLOR DA MONTANHA', 0, 1),
(33, 'diretoriacantareira@gmail.com', 'Virgínia Adelaide dos Santos Frassei', 'RS CANTAREIRA - SEDE', 40, 2),
(34, 'ceovilagalvao@gmail.com', 'VALÉRIA OLIANI ANDRADE', 'CEO VILA GALVÃO', 25, 2),
(35, 'ubsbelvedere@gmail.com', 'Maria Regina do Nascimento', 'UBS BELVEDERE', 72, 2),
(36, 'ubscambara@gmail.com', 'SELMA CRISTINA PINHEIRO PEREIRA DA SILVA', 'UBS CAMBARÁ', 27, 2),
(37, 'ubscmartins@gmail.com', 'EXPEDITA MARIA DE ARAUJO D´ALOIA', 'UBS CIDADE MARTINS', 45, 2),
(38, 'ubscontinental@gmail.com', 'GERALDO DA SILVA PINTO', 'UBS CONTINENTAL', 50, 2),
(39, 'ubsacacio@gmail.com', 'JUNE LIZ DIAS SOUSA', 'UBS JARDIM ACACIO', 67, 2),
(40, 'ubscabucu@gmail.com', 'Victor Hugo Frozel Sena', 'UBS JARDIM CABUÇU', 50, 2),
(41, 'UBSJOVAIA2@GMAIL.COM', 'AMANDA LOOS AGRA TAKADA', 'UBS JARDIM JOVAIA', 64, 2),
(42, 'ubsmorros2017@gmail.com', 'ADRIANA SINGNORINI', 'UBS MORROS', 36, 2),
(43, 'ubsnovo.recreio@gmail.com', 'Elisangela Eliene Medeiros Rodrigues', 'UBS NOVO RECREIO', 15, 2),
(44, 'ubspalmira@gmail.com', 'Adriano Gentil de Freitas', 'UBS PALMIRA', 64, 2),
(45, 'ubsjardimpaulistaguarulhos@gmail.com', 'Elisabete Reis dos Prazeres ', 'UBS PAULISTA', 47, 2),
(46, 'ubsprimavera@gmail.com', 'ALESSANDRA APARECIDA DE OLIVEIRA ', 'UBS PRIMAVERA', 62, 2),
(47, 'ubsrecreio@gmail.com', 'EDVAN GOMES DE MELO E SILVA', 'UBS RECREIO SÃO JORGE', 63, 2),
(48, 'rosadefranca@gmail.com', 'VERA MARIA BERVANGER', 'UBS ROSA DE FRANÇA', 55, 2),
(49, 'ubss.lidia@gmail.com', 'Adriana Singnorini', 'UBS SANTA LIDIA', 52, 2),
(50, 'ubstaboao@gmail.com', 'PRISCILA RAMOS BEBIANO', 'UBS TABOÃO', 32, 2),
(51, 'ubsvilario@gmail.com', 'FABIANA DE BRITO BUENO', 'UBS VILA RIO DE JANEIRO', 37, 2),
(52, 'ubsvlgalvao@gmail.com', 'Sherlaine Lopes Medeiros de Almeida', 'UBS VILA GALVÃO', 43, 2),
(53, 'regiao3@guarulhos.sp.gov.br', 'Rodrigo Almada de Araujo', 'RS SÃO JOÃO/BONSUCESSO - SEDE', 40, 3),
(54, 'ubsnovabonsucesso@gmail.com', 'ROSILEINE PINHEIRO LEMES LOPES', 'UBS NOVA BONSUCESSO', 90, 3),
(55, 'danielapereira@guarulhos.gov.sp.br', 'DANIELA DE PAULA PEREIRA', 'CEMEG - SÃO JOÃO', 63, 3),
(56, 'ceos.joao@gmail.com', 'ALEXANDRA APARECIDA GRITTI', 'CEO JARDIM SÃO JOÃO', 25, 3),
(57, '', '', 'POLICLINICA BONSUCESSO', 0, 3),
(58, 'ubsaguaazul@gmail.com', 'ITAMAR PIRES COSTA', 'UBS AGUA AZUL', 26, 3),
(59, 'ubsalamo02@gmail.com', 'MIRUNA MORAES MELO', 'UBS ALAMO', 22, 3),
(60, 'ubsallankardec@gmail.com', 'Joyce Lenz Grobel', 'UBS ALLAN KARDEC', 36, 3),
(61, 'ubsbambi@gmail.com', 'Aparecida de Oliveira Gines', 'UBS BAMBI', 27, 3),
(62, '', '', 'UBS BANANAL', 0, 3),
(63, 'ubsfortaleza@gmail.com', 'ELCI BOZZA DE ARAUJO', 'UBS FORTALEZA', 49, 3),
(64, 'ubshveloso@gmail.com', 'Anderson Dias Lacerda', 'UBS HAROLDO VELOSO', 84, 3),
(65, 'ubinocoop@gmail.com', 'Gislaine Cristina ALmeida de Oliveira', 'UBS INOCOOP', 43, 3),
(66, 'ubslavras@gmail.com', 'Janete de Jesus Franco', 'UBS LAVRAS', 34, 3),
(67, 'ubsmarinopolis@gmail.com', 'HELEN VANESSA DE ARAÚJO FERREIRA LEME', 'UBS MARINOPOLIS', 59, 3),
(68, 'ubspalta@gmail.com', 'Fátima Rosângela Vieira', 'UBS PONTE ALTA', 67, 3),
(69, 'ubspresdutra@gmail.com', 'ELCI BOZZA DE ARAUJO', 'UBS PRESIDENTE DUTRA', 35, 3),
(70, 'ubssantapaula@gmail.com', 'SUELY CORREIA DE BRITO', 'UBS SANTA PAULA', 50, 3),
(71, 'ubss.dumont@gmail.com', 'CASSIA APARECIDA SERRANO ', 'UBS SANTOS DUMONT', 46, 3),
(72, 'ubsserodio@gmail.com', 'FERNANDO APARECIDO DA CONCEIÇÃO', 'UBS SERODIO', 59, 3),
(73, '', '', 'UBS SOBERANA', 0, 3),
(74, 'ubscarmela@gmail.com', 'Anelisa de Oliveira Mendes dos Santos', 'UBS VILA CARMELA', 33, 3),
(75, 'regional4sede@gmail.com', 'Marilene Carbone de Carvalho', 'RS PIMENTAS/CUMBICA - SEDE', 33, 4),
(76, 'ubslavras@gmail.com', 'Janete de Jesus Franco', 'ACAD.SUS-SEMEANDO SAÚDE', 34, 4),
(77, 'dutra.ceoangelica@gmail.com', 'Marta Lopes Nogueira', 'CEO JARDIM ANGELICA', 39, 4),
(78, '', '', 'POLICLINICA DONA LUIZA', 0, 4),
(79, 'dutra.alvorada@gmail.com', 'FABIO JOSE DUARTE DOS SANTOS', 'UBS ALVORADA', 58, 4),
(80, 'dutra.aracilia@gmail.com', 'ALINE OLIVEIRA SANTOS LARA', 'UBS ARACILIA', 26, 4),
(81, 'dutra.mariomacca@gmail.com', 'EDNÉIA DAS GRAÇAS CRISTINO ROMEIRO REIS', 'UBS CUMBICA', 56, 4),
(82, 'dutra.cummins@gmail.com', 'GISELE GOMES GIAMPIETRO', 'UBS CUMMINS', 32, 4),
(83, 'dutra.dinamarca@gmail.com', 'MARCIA REGINA DE BRITO SANCHEZ', 'UBS DINAMARCA', 30, 4),
(84, 'dutra.ambulatoriodonaluiza@gmail.com', 'MARINALVA LOPES FERREIRA DE OLIVEIRA', 'UBS DONA LUIZA', 33, 4),
(85, 'dutra.jandaia@gmail.com', 'MÁRCIO JOSÉ DOS SANTOS', 'UBS JANDAIA', 38, 4),
(86, 'dutra.cumbica1@gmail.com', 'Ana Cristina Viana Pereira', 'UBS JD. CUMBICA I', 53, 4),
(87, 'dutra.cumbica2@gmail.com', 'Tânia Valéria de Oliveira Gurgel Praxedes', 'UBS JD. CUMBICA II', 59, 4),
(88, 'dutra.jacy@gmail.com', 'DANIELA BAHIA LIMA', 'UBS JD. JACY', 69, 4),
(89, 'dutra.jurema@gmail.com', 'ANDREA APARECIDA DE JESUS', 'UBS JUREMA', 40, 4),
(90, 'dutra.marcosfreire@gmail.com', 'DÉBORA CRISTINA DE SOUZA FERREIRA', 'UBS MARCOS FREIRE', 78, 4),
(91, 'dutra.normandia@gmail.com', 'LUCIANA SOUZA MOTA', 'UBS NORMANDIA', 64, 4),
(92, 'dutra.novacidade@gmail.com', 'KÁTIA DARBELLO DA SILVA', 'UBS NOVA CIDADE', 78, 4),
(93, 'dutra.novacumbica@gmail.com', 'CLAUDIA BRITO GARCIA AMANCIO', 'UBS NOVA CUMBICA', 35, 4),
(94, 'DUTRA.PIMENTAS@GMAIL.COM', 'ALEXANDRE ANDRE DA PAIXÃO RIBEIRO', 'UBS PIMENTAS', 37, 4),
(95, 'dutra.piratininga@gmail.com', 'VERA LUCIA BARBOSA', 'UBS PIRATININGA', 22, 4),
(96, 'dutra.santoafonso@gmail.com', 'MARIA ISABEL DIAS BEZERRA', 'UBS SANTO AFONSO', 33, 4),
(97, 'DUTRA.SOIMCO@GMAIL.COM', 'CINTIA APARECIDA RAMOS LIMA', 'UBS SOINCO', 47, 4),
(98, 'dutra.uirapuru@gmail.com', 'MARLENE RODRIGUES BEZERRA DA SILVA', 'UBS UIRAPURU', 38, 4),
(99, '', '', 'UNAO', 0, 4),
(100, 'escolasusguarulhos@googlegroups.com', 'WALTER DE FREITAS', 'ESCOLA SUS', 24, 5),
(101, '', '', 'ASSEMBLEIA LEGISLATIVA DO ESTADO DE SAO PAULO', 0, 7),
(102, '', '', 'CAIXA BENFICENTE PADRE BENTO', 0, 7),
(103, '', '', 'CARTÓRIO ELEITORAL - 176ª ZONA - LUIZ FACCINI', 0, 7),
(104, '', '', 'CFSS - COORDENAD. DO FUNDO SOCIAL DE SOLIDARIEDADE', 0, 7),
(105, '', '', 'SAM - DEPARTAMENTO DE RECURSOS HUMANOS', 0, 7),
(106, '', '', 'SAM - DEPARTAMENTO DE TRANSPORTES INTERNOS', 0, 7),
(107, '', '', 'SAM - DEPARTAMENTO DE TRANSPORTES INTERNOS-BASE II', 0, 7),
(108, '', '', 'SDAS - SECRET DESENV E ASSIST SOCIAL', 0, 7),
(109, '', '', 'SE - DEE', 0, 7),
(110, '', '', 'SE - EPG - JOSAFÁ TITO FIGUEIREDO', 0, 7),
(111, '', '', 'SE - EPG - ZÉLIA GATTAI', 0, 7),
(112, '', '', 'SE - MERENDA ESCOLAR', 0, 7),
(113, '', '', 'SECRETARIA DE EDUCAÇÃO', 0, 7),
(114, '', '', 'SETOR DE PERÍCIAS MÉDICO LEGAL', 0, 7),
(115, '', '', 'SGM - SECRETARIA DE GOVERNO MUNICIPAL', 0, 7),
(116, '', '', 'SMA - SECRETARIA DO MEIO AMBIENTE', 0, 7),
(117, '', '', 'SMA - ZOOLÓGICO', 0, 7),
(118, '', '', 'SR - SECRETARIA DO TRABALHO E CTMO', 0, 7),
(119, '', '', 'AG. DE FISCALIZAÇÃO', 0, 5),
(120, '', '', 'ALMOXARIFADO DE MEDICAMENTOS', 0, 5),
(121, '', '', 'FORÇA TAREFA DE COMBATE A DENGUE', 0, 5),
(122, '', '', 'GABINETE - COMAD - CONSELHO MUN POL DROGAS', 0, 5),
(123, '', '', 'GESTÃO DE BENS PATRIMONIAIS E TRANSPORTES', 0, 5),
(124, '', '', 'HMCA - HOSPITAL MUNICIPAL DA CRIANÇA E DO ADOLESCENTE', 0, 6),
(125, '', '', 'HMU - HOSPITAL MUNICIPAL DE URGÊNCIAS', 0, 6),
(126, '', '', 'MJJM - MATERNIDADE JESUS, JOSÉ E MARIA', 0, 6),
(127, '', '', 'HOSPITAL PADRE BENTO - GUARULHOS', 0, 6),
(128, 'dutra.saecarloscruz@gmail.com', 'Luciana Aparecida Congo da Costa', 'SAE CARLOS CRUZ', 20, 4),
(129, '', '', 'SEÇÃO TECNICA DE VERIFICAÇÃO DE ÓBITOS', 0, 7),
(130, '', '', 'SECRETARIA DA SAUDE - SEDE', 0, 5),
(131, '', '', 'SAMU', 0, 5),
(132, '', '', 'STT - SECRETARIA DE TRANSPORTES E TRÂNSITO', 0, 7),
(133, '', '', 'SE - DOEP - CENTROS MUNICIPAIS', 0, 7),
(134, '', '', 'SSP - UNIDADE ADM. II (PIMENTAS)', 0, 7),
(135, '', '', 'NÃO SOU FUNCIONÁRIO', 0, 7),
(136, '', '', 'OUTROS', 0, 7),
(137, 'cemeg.pimentascumbica@gmail.com', 'Meire Duarte Silveira', 'CEMEG - PIMENTAS', 50, 4),
(138, '', '', 'NAV - REGIÃO I/II', 0, 1),
(139, '', '', 'NAV - REGIÃO III/IV', 0, 3),
(140, '', '', 'EMAP', 0, 1),
(141, '', '', 'EMAD', 0, 1),
(142, '', '', 'NASF MUNHOZ', 0, 1),
(143, '', '', 'NASF VILA BARROS', 0, 1),
(144, '', '', 'NASF ACACIO', 0, 2),
(145, '', '', 'NASF CABUÇU', 0, 2),
(146, '', '', 'NASF PALMIRA', 0, 2),
(147, '', '', 'NASF PRIMAVERA', 0, 2),
(148, '', '', 'NASF JOVAIA', 0, 2),
(149, '', '', 'NASF BANANAL', 0, 3),
(150, '', '', 'NASF BONSUCESSO', 0, 3),
(151, '', '', 'NASF SOBERANA', 0, 3),
(152, '', '', 'NASF HAROLDO VELOSO', 0, 3),
(153, '', '', 'NASF PONTE ALTA', 0, 3),
(154, '', '', 'NASF ARACÍLIA', 0, 4),
(155, '', '', 'NASF CUMMINS', 0, 4),
(156, '', '', 'NASF CUMBICA', 0, 4),
(157, '', '', 'NASF NOVA CIDADE', 0, 4),
(158, '', '', 'NASF MARCOS FREIRE', 0, 4),
(159, '', '', 'HMPB - Hospital Municipal Pimentas Bonsucesso', 0, 6),
(160, 'capsalvorecer@saudedafamilia.org', 'Iana Profeta Ribeiro', 'CAPS ALVORECER', 45, 4),
(161, 'capsrecriar@gmail.com', 'MARCIA MENDES DE MATTOS', 'CAPS RECRIAR', 29, 1),
(162, '', '', 'RESIDÊNCIA TERAPEUTICA', 0, 1),
(163, '', '', 'CONSULTORIO NA RUA - UBS NOVA CIDADE', 0, 4),
(164, 'cemegcantareira@gmail.com', 'Cristiane Frare Alves', 'CEMEG - CANTAREIRA', 57, 2),
(165, '', '', 'PA ALVORADA', 0, 4),
(166, 'upapaulista17@gmail.com', 'maria aparecida nunes sampaio jones', 'UPA PAULISTA', 289, 2),
(167, '', '', 'UPA SÃO JOÃO/BONSUCESSO', 0, 3),
(168, '', '', 'UPA CUMBICA', 0, 4),
(169, 'cempicsgru@gmail.com', 'Eneida da Silva Bernardo', 'CEMPICS GUARULHOS', 14, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `unidade_trabalho`
--
ALTER TABLE `unidade_trabalho`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq9c6rj09rlgjvnc292f4gjnpp` (`departamento_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `unidade_trabalho`
--
ALTER TABLE `unidade_trabalho`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=170;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
