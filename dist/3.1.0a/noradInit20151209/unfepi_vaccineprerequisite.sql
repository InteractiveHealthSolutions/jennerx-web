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
-- Table structure for table `vaccineprerequisite`
--

DROP TABLE IF EXISTS `vaccineprerequisite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccineprerequisite` (
  `vaccineId` smallint(6) NOT NULL,
  `vaccinePrerequisiteId` smallint(6) NOT NULL,
  `mandatory` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`vaccineId`,`vaccinePrerequisiteId`),
  KEY `vaccprereq_prereqId_vaccine_vaccineId_FK` (`vaccinePrerequisiteId`),
  KEY `FKED048EEB160DDAC0` (`vaccineId`),
  CONSTRAINT `FKED048EEB160DDAC0` FOREIGN KEY (`vaccineId`) REFERENCES `vaccine` (`vaccineId`),
  CONSTRAINT `vaccprereq_prereqId_vaccine_vaccineId_FK` FOREIGN KEY (`vaccinePrerequisiteId`) REFERENCES `vaccine` (`vaccineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccineprerequisite`
--

LOCK TABLES `vaccineprerequisite` WRITE;
/*!40000 ALTER TABLE `vaccineprerequisite` DISABLE KEYS */;
INSERT INTO `vaccineprerequisite` VALUES (3,2,1),(4,3,1),(6,5,1),(8,7,0),(9,8,1),(10,9,1),(12,11,1),(13,12,1);
/*!40000 ALTER TABLE `vaccineprerequisite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:07
