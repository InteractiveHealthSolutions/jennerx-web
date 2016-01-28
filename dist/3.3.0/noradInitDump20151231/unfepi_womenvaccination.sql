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
-- Table structure for table `womenvaccination`
--

DROP TABLE IF EXISTS `womenvaccination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womenvaccination` (
  `vaccinationRecordNum` int(11) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `epiNumber` varchar(255) DEFAULT NULL,
  `height` float DEFAULT NULL,
  `isFirstVaccination` tinyint(1) DEFAULT NULL,
  `isVaccinationCenterChanged` tinyint(1) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `mappedId` int(11) DEFAULT NULL,
  `reasonNotTimelyVaccination` varchar(255) DEFAULT NULL,
  `timelinessFactor` smallint(6) DEFAULT NULL,
  `timelinessStatus` int(11) DEFAULT NULL,
  `vaccinationCenterId` int(11) DEFAULT NULL,
  `vaccinationDate` datetime DEFAULT NULL,
  `vaccinationDuedate` datetime DEFAULT NULL,
  `vaccinationStatus` varchar(20) DEFAULT NULL,
  `vaccinatorId` int(11) DEFAULT NULL,
  `vaccineId` smallint(6) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `womenId` int(11) DEFAULT NULL,
  PRIMARY KEY (`vaccinationRecordNum`),
  KEY `vaccinationId_vaccinatorId_vaccinator_mappedId_FK` (`vaccinatorId`),
  KEY `vaccinationWomen_womenId_women_mappedId_FK` (`womenId`),
  KEY `vaccinationEdit_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `vaccinationId_vaccineId_vaccine_vaccineId_FK` (`vaccineId`),
  KEY `vaccinationUser_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `vaccinationEdit_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccinationId_vaccinatorId_vaccinator_mappedId_FK` FOREIGN KEY (`vaccinatorId`) REFERENCES `vaccinator` (`mappedId`),
  CONSTRAINT `vaccinationId_vaccineId_vaccine_vaccineId_FK` FOREIGN KEY (`vaccineId`) REFERENCES `vaccine` (`vaccineId`),
  CONSTRAINT `vaccinationUser_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccinationWomen_womenId_women_mappedId_FK` FOREIGN KEY (`womenId`) REFERENCES `idmapper` (`mappedId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womenvaccination`
--

LOCK TABLES `womenvaccination` WRITE;
/*!40000 ALTER TABLE `womenvaccination` DISABLE KEYS */;
/*!40000 ALTER TABLE `womenvaccination` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:50
