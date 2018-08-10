-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 10-Ago-2018 às 00:38
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
-- Estrutura da tabela `endereco_unidade`
--

CREATE TABLE `endereco_unidade` (
  `id` bigint(20) NOT NULL,
  `bairro` varchar(255) DEFAULT NULL,
  `cep` varchar(255) DEFAULT NULL,
  `cidade` varchar(255) DEFAULT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `numero` varchar(255) DEFAULT NULL,
  `uf` varchar(255) DEFAULT NULL,
  `unidade_trabalho_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `endereco_unidade`
--

INSERT INTO `endereco_unidade` (`id`, `bairro`, `cep`, `cidade`, `endereco`, `numero`, `uf`, `unidade_trabalho_id`) VALUES
(1, NULL, NULL, NULL, 'Rua Luiz Faccini, 518- Centro- Guarulhos- SP- Cep 07110-000', NULL, 'SP', 1),
(2, NULL, NULL, NULL, 'Rua Silvestre Vasconcelos Calmon, n. 92, Vila Moreira, CEP: 07020-001, Guarulhos/SP', NULL, 'SP', 2),
(3, NULL, NULL, NULL, 'RUA DONA ANTONIA, 965 - VILA DAS PALMEIRAS - GUARULHOS-SP', NULL, 'SP', 4),
(4, NULL, NULL, NULL, 'rua Oswaldo Cruz, 151', NULL, 'SP', 5),
(5, NULL, NULL, NULL, 'Rua Otávio Forghieri, 70 - Centro', NULL, 'SP', 6),
(6, NULL, NULL, NULL, 'VIELA PORTO BELO, 28-VILA CAMARGOS', NULL, 'SP', 7),
(7, NULL, NULL, NULL, 'Rua Joaquim Miranda 298 Vila Augusta Guarulhos-SP', NULL, 'SP', 9),
(8, NULL, NULL, NULL, 'RUA CARUTAPERA, 167 - GOPOUVA - GUARULHOS-SP - CEP : 07021250', NULL, 'SP', 10),
(9, NULL, NULL, NULL, 'Rua Raphael Colacioppo,80 - Jardim Bom Clima - CEP - 07196-230', NULL, 'SP', 11),
(10, NULL, NULL, NULL, 'Rua Dona Antônia, 987 - Vila das Palmeiras - Guarulhos - SP CEP:07021-000', NULL, 'SP', 12),
(11, NULL, NULL, NULL, 'Rua Das Palmeiras, 865 - Gopouva - Guarulhos', NULL, 'SP', 13),
(12, NULL, NULL, NULL, 'Rua Piracicaba, 114 - Gopouva - Guarulhos/SP', NULL, 'SP', 14),
(13, NULL, NULL, NULL, 'Rua Michael Andreas Kratz nº159 ', NULL, 'SP', 15),
(14, NULL, NULL, NULL, 'RUA CARUTAPERA 163', NULL, 'SP', 16),
(15, NULL, NULL, NULL, 'RUA CAVADAS, 412', NULL, 'SP', 19),
(16, NULL, NULL, NULL, 'RUA PROFESSORA MARIA DEL PILAR MUNHOZ BONONATO, 78 - PARQUE CECAP - GUARULHOS/SP - CEP 07190-029', NULL, 'SP', 20),
(17, NULL, NULL, NULL, 'RUA EDUARDO, 311', NULL, 'SP', 21),
(18, NULL, NULL, NULL, 'Av. Rotary, 1453 - Vila das Bandeiras', NULL, 'SP', 22),
(19, NULL, NULL, NULL, 'Rua Professor José Munhoz, 474', NULL, 'SP', 23),
(20, NULL, NULL, NULL, 'RUA MARTIM AFONSO, 130', NULL, 'SP', 25),
(21, NULL, NULL, NULL, 'Rua Osvaldo Agostinho, 17 - Ponte Grande - Guarulhos', NULL, 'SP', 26),
(22, NULL, NULL, NULL, 'RUA DOMINGOS DE ABREU, 216, JD VILA GALVÃO, CEP 07054-050, GUARULHOS -SP', NULL, 'SP', 27),
(23, NULL, NULL, NULL, 'rua Soldado Jair da Silva Tavares, 04 - Torres Tibagi - Guarulhos SP', NULL, 'SP', 28),
(24, NULL, NULL, NULL, 'av. emilio ribas, 1897', NULL, 'SP', 29),
(25, NULL, NULL, NULL, 'Av. Carlos Korkischko, 425', NULL, 'SP', 30),
(26, NULL, NULL, NULL, 'RUA ESMERALDA, N° 25 - JARDIM SANTA BÁRBARA - CEP: 07191-290', NULL, 'SP', 31),
(27, NULL, NULL, NULL, 'Rua São Miguel do Araguaia, 110 Vila Galvão- Guarulhos CEP 07052-220', NULL, 'SP', 33),
(28, NULL, NULL, NULL, 'RUA SÃO FRANCISCO, 294 - VILA GALVÃO - CEP 07071010', NULL, 'SP', 34),
(29, NULL, NULL, NULL, 'Estrada Municipal 475 Jd Belvedere', NULL, 'SP', 35),
(30, NULL, NULL, NULL, 'RUA ADOLFO VASCONCELOS NORONHA', NULL, 'SP', 36),
(31, NULL, NULL, NULL, 'RUA JAU 190 JARDIM BELA VISTA', NULL, 'SP', 37),
(32, NULL, NULL, NULL, 'Rua Pessegueiro, 111, Parque Continental 2 - Guarulhos/SP', NULL, 'SP', 38),
(33, NULL, NULL, NULL, 'Av. Silvestre Pires de Freitas, 2007 - Jardim Paraiso, Guarulhos - SP, 07143-760', NULL, 'SP', 39),
(34, NULL, NULL, NULL, 'Rua Existente , 110 - CEP :07144-285 Chácara Cabuçu', NULL, 'SP', 40),
(35, NULL, NULL, NULL, 'AVENIDA BRIGADEIRO FARIA LIMA, 1361', NULL, 'SP', 41),
(36, NULL, NULL, NULL, 'RUA DELMIRO,299 - JARDIM DOS AFONSOS - COCAIA - GUARULHOS', NULL, 'SP', 42),
(37, NULL, NULL, NULL, 'RUA SANTINA, 543', NULL, 'SP', 43),
(38, NULL, NULL, NULL, 'Rua Jaime dos Santos Augusto Filho, 125', NULL, 'SP', 44),
(39, NULL, NULL, NULL, 'Rua Itaguai, 97 , Jardim Paulista, Guarulhos', NULL, 'SP', 45),
(40, NULL, NULL, NULL, 'Rua Gama, nº 72, Cep 07145-190, Pq. Primavera, Guarulhos-SP', NULL, 'SP', 46),
(41, NULL, NULL, NULL, 'ESTRADA DAVID CORREA, 1766', NULL, 'SP', 47),
(42, NULL, NULL, NULL, 'RUA WILSON SOUZA 48 - JD ROSA DE FRANÇA - GUARULHOS - SP', NULL, 'SP', 48),
(43, NULL, NULL, NULL, 'Rua Mario Bezerra Espindola, 21', NULL, 'SP', 49),
(44, NULL, NULL, NULL, 'R. Maria Elisa, 80 Jd Tamassia 07140-130 Guarulhos, SP', NULL, 'SP', 50),
(45, NULL, NULL, NULL, 'Rua Lions, s/n - Vila Rio de Janeiro - CEP 07124-090', NULL, 'SP', 51),
(46, NULL, NULL, NULL, 'Avenida Dona Eugênia Machado da Silva, 354 Vila Galvão Cep; 07071-070', NULL, 'SP', 52),
(47, NULL, NULL, NULL, 'Avenida Serra Redonda, 203 - Cidade Seródio - CEP 07151-420', NULL, 'SP', 53),
(48, NULL, NULL, NULL, 'Rua Tapiramuta, 237 - Nova Bonsucesso - Guarulhos - SP', NULL, 'SP', 54),
(49, NULL, NULL, NULL, 'RUA TAIPU 116 JD SÃO JOÃO', NULL, 'SP', 55),
(50, NULL, NULL, NULL, 'Rua Cruz do Espirito Santo, 37 - Seródio', NULL, 'SP', 56),
(51, NULL, NULL, NULL, 'Rua Cabo D\'Antibes - 131', NULL, 'SP', 58),
(52, NULL, NULL, NULL, 'RUA NICOLINA LA PENA TURRI  ,Nº 15  JARDIM ALAMO', NULL, 'SP', 59),
(53, NULL, NULL, NULL, 'Rua Ipacaetá, 51', NULL, 'SP', 60),
(54, NULL, NULL, NULL, 'Rua Gabriela Gurgel de Freitas, 248, Pq Resid Bambi', NULL, 'SP', 61),
(55, NULL, NULL, NULL, 'RUA HILÁRIO PIRES DE FREITAS, 166 - JARDIM FORTALEZA - 07153-450', NULL, 'SP', 63),
(56, NULL, NULL, NULL, 'Rua Pocrane Nº 79', NULL, 'SP', 64),
(57, NULL, NULL, NULL, 'Rua Elias Dabarian, n310 - inocoop', NULL, 'SP', 65),
(58, NULL, NULL, NULL, 'Rua Souto Soares, 232', NULL, 'SP', 66),
(59, NULL, NULL, NULL, 'Rua Marinopolis', NULL, 'SP', 67),
(60, NULL, NULL, NULL, 'Rua São Paulo, 107 - Ponte Alta, Guarulhos/SP', NULL, 'SP', 68),
(61, NULL, NULL, NULL, 'Rua Nova York nº375 - JD Presidente Dutra', NULL, 'SP', 69),
(62, NULL, NULL, NULL, 'R. Maria Roza de Campos, 156', NULL, 'SP', 70),
(63, NULL, NULL, NULL, 'RUA RAFAEL FERNADES ,55-PARQUE SANTOS DUMONT ', NULL, 'SP', 71),
(64, NULL, NULL, NULL, 'Avenida Coqueiral, 111 Cidade Seródio, Guarulhos-SP', NULL, 'SP', 72),
(65, NULL, NULL, NULL, 'Avenida Serra da Mantiqueira, 585 Vila Carmela, CEP 07178540', NULL, 'SP', 74),
(66, NULL, NULL, NULL, 'Rua Pirajussara, 137 - Parque Jurema Guarulhos - SP CEP: 07244-130', NULL, 'SP', 75),
(67, NULL, NULL, NULL, 'Rua Souto Soares, 232', NULL, 'SP', 76),
(68, NULL, NULL, NULL, 'Rua José Inácio Gomes, 441 - Pq. Jurema- CEP:07244-270 - Guarulhos-SP ', NULL, 'SP', 77),
(69, NULL, NULL, NULL, 'avenida santa helena n.145, vila paraiso', NULL, 'SP', 79),
(70, NULL, NULL, NULL, 'RUA URUCUÍ, 398, JARDIM CIDADE ARACÍLIA, GUARULHOS. SP. CEP 07250-150', NULL, 'SP', 80),
(71, NULL, NULL, NULL, 'Av 2º Tenente Aviador Mario Luiz Figueiroa, 295', NULL, 'SP', 81),
(72, NULL, NULL, NULL, 'Rua Plácido Ivo de Mello, 68 - Cid Jardim Cumbica - Cep 07180-160', NULL, 'SP', 82),
(73, NULL, NULL, NULL, 'RUA COPAN, 28', NULL, 'SP', 83),
(74, NULL, NULL, NULL, 'AV CENTENÁRIO 446 JD CENTENÁRIO CEP 07270-000', NULL, 'SP', 84),
(75, NULL, NULL, NULL, 'RUA PORTO ALEGRE, 446 - PARQUE JANDAIA - GUARULHOS -CEP.: 07260-220', NULL, 'SP', 85),
(76, NULL, NULL, NULL, 'Avenida Venturosa, 240 Jardim Cumbica', NULL, 'SP', 86),
(77, NULL, NULL, NULL, 'Rua Sena Madureira 1177', NULL, 'SP', 87),
(78, NULL, NULL, NULL, 'RUA SÃO GERALDO DA PIEDADE, 45', NULL, 'SP', 88),
(79, NULL, NULL, NULL, 'rua primeira cruz,104, Parque das Nações', NULL, 'SP', 89),
(80, NULL, NULL, NULL, 'Rua do Poente; 200', NULL, 'SP', 90),
(81, NULL, NULL, NULL, 'Estrada da Água Chata, 979 - Normandia', NULL, 'SP', 91),
(82, NULL, NULL, NULL, 'RUA ANGELO ROBERTO ORSOMARSO, 146', NULL, 'SP', 92),
(83, NULL, NULL, NULL, 'Rua Baixio, 142 cep 08061-340 Jardim Nova Cumbica ', NULL, 'SP', 93),
(84, NULL, NULL, NULL, 'RUA JABOATÃO 84 JD DOS PIMENTAS  CEP 07272-340', NULL, 'SP', 94),
(85, NULL, NULL, NULL, 'Rua Landre Sales, 400 - Cidade Aracília - Guarulhos - CEP 07251-130', NULL, 'SP', 95),
(86, NULL, NULL, NULL, 'Rua: Rondonópolis, 161 Jd. Santo Afonso', NULL, 'SP', 96),
(87, NULL, NULL, NULL, 'RUA BARAO DE MELGAÇO,101', NULL, 'SP', 97),
(88, NULL, NULL, NULL, 'ESTRADA VELHA GUARULHOS SÃO MIGUEL, 992 JARDIM SANTA HELENA', NULL, 'SP', 98),
(89, NULL, NULL, NULL, 'Avenida Gilberto Dini, nº 558', NULL, 'SP', 100),
(90, NULL, NULL, NULL, 'Rua Miracanga, 32 - Pq Jurema', NULL, 'SP', 128),
(91, NULL, NULL, NULL, 'Avenida Atalaia do Norte, nº 576', NULL, 'SP', 137),
(92, NULL, NULL, NULL, 'Av Santa Helena, 173', NULL, 'SP', 160),
(93, NULL, NULL, NULL, 'Rua: Michael Andreas Kratz, 111', NULL, 'SP', 161),
(94, NULL, NULL, NULL, 'Avenida Bom Clima,182', NULL, 'SP', 164),
(95, NULL, NULL, NULL, 'rua teixeira mendes 166', NULL, 'SP', 166),
(96, NULL, NULL, NULL, 'Rua Santa Filomena 70 Vila Augusta ', NULL, 'SP', 169);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `endereco_unidade`
--
ALTER TABLE `endereco_unidade`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpiqir1vb821vp6k1wgxu1srj7` (`unidade_trabalho_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `endereco_unidade`
--
ALTER TABLE `endereco_unidade`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
