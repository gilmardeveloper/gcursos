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
-- Estrutura da tabela `telefone_unidade`
--

CREATE TABLE `telefone_unidade` (
  `id` bigint(20) NOT NULL,
  `numero` varchar(255) DEFAULT NULL,
  `setor` varchar(255) DEFAULT NULL,
  `unidade_trabalho_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `telefone_unidade`
--

INSERT INTO `telefone_unidade` (`id`, `numero`, `setor`, `unidade_trabalho_id`) VALUES
(1, '(11) 2087-7580', 'Recepção', 1),
(2, '(11) 2409-2200', 'Recepção', 2),
(3, '(11) 2472-5487', 'Recepção', 4),
(4, '(11) 2468-8845', 'Recepção', 5),
(5, '(11) 2408-4991', 'Recepção', 6),
(6, '(11) 2087-7100', 'Recepção', 7),
(7, '(11) 2414-0240', 'Recepção', 9),
(8, '(11) 2472-5496', 'Recepção', 10),
(9, '(11) 2443-1127', 'Recepção', 11),
(10, '(11) 2472-5499', 'Recepção', 12),
(11, '(11) 2472-5493', 'Recepção', 13),
(12, '(11) 2451-3052', 'Recepção', 14),
(13, '(11) 2409-8776', 'Recepção', 15),
(14, '(11) 2472-5495', 'Recepção', 16),
(15, '(11) 2425-2919', 'Recepção', 19),
(16, '(11) 2463-2984', 'Recepção', 20),
(17, '(11) 2459-2819', 'Recepção', 21),
(18, '(11) 2408-6968', 'Recepção', 22),
(19, '(11) 2425-3279', 'Recepção', 23),
(20, '(11) 2497-1334', 'Recepção', 25),
(21, '(11) 2425-3307', 'Recepção', 26),
(22, '(11) 2422-0773', 'Recepção', 27),
(23, '(11) 2497-3909', 'Recepção', 28),
(24, '(11) 2425-2799', 'Recepção', 29),
(25, '(11) 2404-3732', 'Recepção', 30),
(26, '(11) 2409-9112', 'Recepção', 31),
(27, '(11) 2464-2480', 'Recepção', 33),
(28, '(11) 2497-1352', 'Recepção', 34),
(29, '(11) 2402-1877', 'Recepção', 35),
(30, '(11) 2408-5423', 'Recepção', 36),
(31, '(11) 2406-4201', 'Recepção', 37),
(32, '(11) 2456-7946', 'Recepção', 38),
(33, '(11) 2492-1616', 'Recepção', 39),
(34, '(11) 2403-6131', 'Recepção', 40),
(35, '(11) 2401-4808', 'Recepção', 41),
(36, '(11) 2404-5444', 'Recepção', 42),
(37, '(11) 2408-6962', 'Recepção', 43),
(38, '(11) 2485-7077', 'Recepção', 44),
(39, '(11) 2458-0477', 'Recepção', 45),
(40, '(11) 2492-1133', 'Recepção', 46),
(41, '(11) 2407-7353', 'Recepção', 47),
(42, '(11) 2455-9479', 'Recepção', 48),
(43, '(11) 2492-8990', 'Recepção', 49),
(44, '(11) 2402-9062', 'Recepção', 50),
(45, '(11) 2457-4664', 'Recepção', 51),
(46, '(11) 2404-0902', 'Recepção', 52),
(47, '(11) 2467-6614', 'Recepção', 53),
(48, '(11) 2453-2163', 'Recepção', 54),
(49, '(11) 2086-2280', 'Recepção', 55),
(50, '(11) 2466-6561', 'Recepção', 56),
(51, '(11) 2436-6236', 'Recepção', 58),
(52, '(11) 2438-4496', 'Recepção', 59),
(53, '(11) 2486-7994', 'Recepção', 60),
(54, '(11) 2436-4102', 'Recepção', 61),
(55, '(11) 2467-5360', 'Recepção', 63),
(56, '(11) 2469-7006', 'Recepção', 64),
(57, '(11) 2431-9940', 'Recepção', 65),
(58, '(11) 2467-5792', 'Recepção', 66),
(59, '(11) 2431-7456', 'Recepção', 67),
(60, '(11) 2439-8303', 'Recepção', 68),
(61, '(11) 2432-5649', 'Recepção', 69),
(62, '(11) 2463-3405', 'Recepção', 70),
(63, '(11) 2466-6220', 'Recepção', 71),
(64, '(11) 2469-5129', 'Recepção', 72),
(65, '(11) 2436-0985', 'Recepção', 74),
(66, '(11) 2303-4240', 'Recepção', 75),
(67, '(11) 2467-5792', 'Recepção', 76),
(68, '(11) 2496-2526', 'Recepção', 77),
(69, '(11) 2498-1510', 'Recepção', 79),
(70, '(11) 2087-2810', 'Recepção', 80),
(71, '(11) 2483-2079', 'Recepção', 81),
(72, '(11) 2446-1554', 'Recepção', 82),
(73, '(11) 9915-3230', 'Recepção', 83),
(74, '(11) 2303-4164', 'Recepção', 84),
(75, '(11) 2499-5702', 'Recepção', 85),
(76, '(11) 2481-9508', 'Recepção', 86),
(77, '(11) 2412-2179', 'Recepção', 87),
(78, '(11) 2498-3142', 'Recepção', 88),
(79, '(11) 2486-5113', 'Recepção', 89),
(80, '(11) 2780-2793', 'Recepção', 90),
(81, '(11) 2480-1202', 'Recepção', 91),
(82, '(11) 2492-1517', 'Recepção', 92),
(83, '(11) 2412-1510', 'Recepção', 93),
(84, '(11) 2499-3050', 'Recepção', 94),
(85, '(11) 4641-3029', 'Recepção', 95),
(86, '(11) 2412-1330', 'Recepção', 96),
(87, '(11) 2446-4835', 'Recepção', 97),
(88, '(11) 2412-1044', 'Recepção', 98),
(89, '(11) 2304-6446', 'Recepção', 100),
(90, '(11) 2498-7323', 'Recepção', 128),
(91, '(11) 2461-3281', 'Recepção', 137),
(92, '(11) 2229-9790', 'Recepção', 160),
(93, '(11) 2440-0336', 'Recepção', 161),
(94, '(11) 2229-8396', 'Recepção', 164),
(95, '(11) 2088-1400', 'Recepção', 166),
(96, '(11) 2087-3751', 'Recepção', 169);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `telefone_unidade`
--
ALTER TABLE `telefone_unidade`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKoblyfmsi2tfnp1p8ebak600u7` (`unidade_trabalho_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `telefone_unidade`
--
ALTER TABLE `telefone_unidade`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
