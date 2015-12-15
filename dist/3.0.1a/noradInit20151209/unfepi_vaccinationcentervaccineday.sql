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
-- Table structure for table `vaccinationcentervaccineday`
--

DROP TABLE IF EXISTS `vaccinationcentervaccineday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccinationcentervaccineday` (
  `dayNumber` smallint(6) NOT NULL,
  `vaccinationCenterId` int(11) NOT NULL,
  `vaccineId` smallint(6) NOT NULL,
  PRIMARY KEY (`dayNumber`,`vaccinationCenterId`,`vaccineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccinationcentervaccineday`
--

LOCK TABLES `vaccinationcentervaccineday` WRITE;
/*!40000 ALTER TABLE `vaccinationcentervaccineday` DISABLE KEYS */;
INSERT INTO `vaccinationcentervaccineday` VALUES (2,44,1),(2,44,2),(2,44,3),(2,44,4),(2,44,5),(2,44,6),(2,44,7),(2,44,8),(2,44,9),(2,44,10),(2,44,11),(2,44,12),(2,44,13),(3,44,1),(3,44,2),(3,44,3),(3,44,4),(3,44,5),(3,44,6),(3,44,7),(3,44,8),(3,44,9),(3,44,10),(3,44,11),(3,44,12),(3,44,13),(4,44,1),(4,44,2),(4,44,3),(4,44,4),(4,44,5),(4,44,6),(4,44,7),(4,44,8),(4,44,9),(4,44,10),(4,44,11),(4,44,12),(4,44,13),(5,44,1),(5,44,2),(5,44,3),(5,44,4),(5,44,5),(5,44,6),(5,44,7),(5,44,8),(5,44,9),(5,44,10),(5,44,11),(5,44,12),(5,44,13),(6,44,1),(6,44,2),(6,44,3),(6,44,4),(6,44,5),(6,44,6),(6,44,7),(6,44,8),(6,44,9),(6,44,10),(6,44,11),(6,44,12),(6,44,13),(7,44,1),(7,44,2),(7,44,3),(7,44,4),(7,44,5),(7,44,6),(7,44,7),(7,44,8),(7,44,9),(7,44,10),(7,44,11),(7,44,12),(7,44,13);
/*!40000 ALTER TABLE `vaccinationcentervaccineday` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:24
