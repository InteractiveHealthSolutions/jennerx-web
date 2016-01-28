CREATE DATABASE  IF NOT EXISTS `unfepi` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `unfepi`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: unfepi
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dailysummaryvaccinegiven`
--

DROP TABLE IF EXISTS `dailysummaryvaccinegiven`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dailysummaryvaccinegiven` (
  `summaryVaccineId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `vaccineId` smallint(5) unsigned DEFAULT NULL,
  `quantityGiven` smallint(5) unsigned NOT NULL,
  `vaccineName` varchar(45) NOT NULL,
  `quantityUnit` varchar(25) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `vaccineExists` tinyint(1) NOT NULL,
  `encounterType` varchar(45) DEFAULT 'UNKNOWN',
  `dailySummaryId` int(11) NOT NULL,
  PRIMARY KEY (`summaryVaccineId`),
  KEY `fk_dailysummaryvaccinegiven_dailysummary1` (`dailySummaryId`),
  CONSTRAINT `fk_dailysummaryvaccinegiven_dailysummary1` FOREIGN KEY (`dailySummaryId`) REFERENCES `dailysummary` (`serialNumber`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dailysummaryvaccinegiven`
--

LOCK TABLES `dailysummaryvaccinegiven` WRITE;
/*!40000 ALTER TABLE `dailysummaryvaccinegiven` DISABLE KEYS */;
/*!40000 ALTER TABLE `dailysummaryvaccinegiven` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:19
