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
-- Table structure for table `vaccinegap`
--

DROP TABLE IF EXISTS `vaccinegap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccinegap` (
  `vaccineGapTypeId` smallint(6) NOT NULL,
  `vaccineId` smallint(6) NOT NULL,
  `gapTimeUnit` varchar(20) DEFAULT NULL,
  `priority` smallint(6) NOT NULL,
  `value` smallint(6) NOT NULL,
  `mandatory` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`vaccineGapTypeId`,`vaccineId`),
  KEY `vaccinegap_vaccineId_vaccinegaptype_vaccinegaptypeId_FK` (`vaccineGapTypeId`),
  KEY `vaccine_vaccineId_vaccinegap_vaccineId_FK` (`vaccineId`),
  CONSTRAINT `vaccinegap_vaccineId_vaccinegaptype_vaccinegaptypeId_FK` FOREIGN KEY (`vaccineGapTypeId`) REFERENCES `vaccinegaptype` (`vaccineGapTypeId`),
  CONSTRAINT `vaccine_vaccineId_vaccinegap_vaccineId_FK` FOREIGN KEY (`vaccineId`) REFERENCES `vaccine` (`vaccineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccinegap`
--

LOCK TABLES `vaccinegap` WRITE;
/*!40000 ALTER TABLE `vaccinegap` DISABLE KEYS */;
INSERT INTO `vaccinegap` VALUES (1,1,'WEEK',1,0,NULL),(1,2,'WEEK',1,6,NULL),(1,3,'WEEK',1,10,NULL),(1,4,'WEEK',1,14,NULL),(1,5,'MONTH',1,9,NULL),(1,6,'MONTH',1,15,NULL),(1,7,'WEEK',1,0,NULL),(1,8,'WEEK',1,6,NULL),(1,9,'WEEK',1,10,NULL),(1,10,'WEEK',1,14,NULL),(1,11,'WEEK',1,6,NULL),(1,12,'WEEK',1,10,NULL),(1,13,'WEEK',1,14,NULL),(2,3,'WEEK',1,4,NULL),(2,4,'WEEK',1,4,NULL),(2,6,'MONTH',1,6,NULL),(2,8,'WEEK',1,4,NULL),(2,9,'WEEK',1,4,NULL),(2,10,'WEEK',1,4,NULL),(2,12,'WEEK',1,4,NULL),(2,13,'WEEK',1,4,NULL),(4,1,'WEEK',1,4,NULL),(4,2,'WEEK',1,4,NULL),(4,3,'WEEK',1,4,NULL),(4,4,'WEEK',1,4,NULL),(4,5,'WEEK',1,4,NULL),(4,6,'WEEK',1,4,NULL),(4,7,'WEEK',1,4,NULL),(4,8,'WEEK',1,4,NULL),(4,9,'WEEK',1,4,NULL),(4,10,'WEEK',1,4,NULL),(4,11,'WEEK',1,4,NULL),(4,12,'WEEK',1,4,NULL),(4,13,'WEEK',1,4,NULL),(5,1,'YEAR',1,1,NULL),(5,3,'YEAR',1,1,NULL),(5,4,'YEAR',1,1,NULL),(5,6,'YEAR',1,5,NULL),(5,7,'DAY',1,15,NULL),(5,8,'YEAR',1,5,NULL),(5,9,'YEAR',1,5,NULL),(5,10,'YEAR',1,5,NULL);
/*!40000 ALTER TABLE `vaccinegap` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:23
