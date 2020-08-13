-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: www.616team.com    Database: CASHBOOK_TEST
-- ------------------------------------------------------
-- Server version	5.7.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cash_detail`
--

DROP TABLE IF EXISTS `cash_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `header_id` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `cost` decimal(7,2) NOT NULL,
  `notes` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL,
  `entry_id` int(11) DEFAULT NULL,
  `entry_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `header_idIndex` (`header_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_header`
--

DROP TABLE IF EXISTS `cash_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_header` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cash_date` date NOT NULL,
  `quota` decimal(7,2) NOT NULL,
  `cost` decimal(7,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `date_UNIQUE` (`cash_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dream`
--

DROP TABLE IF EXISTS `dream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dream` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `title` varchar(256) COLLATE utf8mb4_bin NOT NULL,
   `dream_owner` int(11) DEFAULT NULL,
   `description` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL,
   `act_cost` decimal(7,2) DEFAULT NULL,
   `exp_cost` decimal(7,2) NOT NULL,
   `come_true_note` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL,
   `come_true` date DEFAULT NULL,
   `entry_id` int(11) DEFAULT NULL,
   `entry_datetime` datetime DEFAULT NULL,
   `deadline` date DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `id_UNIQUE` (`id`),
   KEY `come_trueIndex` (`come_true`),
   KEY `deadlineIndex` (`deadline`)
 ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setting_key` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `setting_value` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `setting_key_UNIQUE` (`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `avatar` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `open_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `session_id` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
  `allow_entry` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `open_idIndex` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `dream_pic`;
CREATE TABLE `dream_pic` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `dream_id` int(11) NOT NULL,
   `cos_key` varchar(100) COLLATE utf8mb4_bin NOT NULL,
   `cos_key_small` varchar(100) COLLATE utf8mb4_bin NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `id_UNIQUE` (`id`),
   KEY `dream_idIndex` (`dream_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-10  0:44:58
