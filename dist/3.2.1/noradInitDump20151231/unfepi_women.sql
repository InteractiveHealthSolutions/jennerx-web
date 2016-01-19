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
-- Table structure for table `women`
--

DROP TABLE IF EXISTS `women`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `women` (
  `mappedId` int(11) NOT NULL,
  `birthdate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `dateEnrolled` datetime DEFAULT NULL,
  `dateOfCompletion` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `domicile` varchar(30) DEFAULT NULL,
  `enrollmentVaccineId` smallint(6) DEFAULT NULL,
  `estimatedBirthdate` tinyint(1) DEFAULT NULL,
  `fatherFirstName` varchar(30) DEFAULT NULL,
  `fatherLastName` varchar(30) DEFAULT NULL,
  `fatherMiddleName` varchar(30) DEFAULT NULL,
  `firstName` varchar(30) DEFAULT NULL,
  `husbandFirstName` varchar(30) DEFAULT NULL,
  `husbandLastName` varchar(30) DEFAULT NULL,
  `husbandMiddleName` varchar(30) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `lastName` varchar(30) DEFAULT NULL,
  `middleName` varchar(30) DEFAULT NULL,
  `nic` varchar(30) DEFAULT NULL,
  `nicOwnerFirstName` varchar(30) DEFAULT NULL,
  `nicOwnerLastName` varchar(30) DEFAULT NULL,
  `nicOwnerRelation` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `terminationDate` datetime DEFAULT NULL,
  `terminationReason` varchar(255) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`mappedId`),
  KEY `FK6C107FEC79271AF` (`mappedId`),
  KEY `women_enrVaccId_vaccine_vaccId` (`enrollmentVaccineId`),
  KEY `women_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `women_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `FK6C107FEC79271AF` FOREIGN KEY (`mappedId`) REFERENCES `idmapper` (`mappedId`),
  CONSTRAINT `women_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `women_enrVaccId_vaccine_vaccId` FOREIGN KEY (`enrollmentVaccineId`) REFERENCES `vaccine` (`vaccineId`),
  CONSTRAINT `women_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `women`
--

LOCK TABLES `women` WRITE;
/*!40000 ALTER TABLE `women` DISABLE KEYS */;
/*!40000 ALTER TABLE `women` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:45
