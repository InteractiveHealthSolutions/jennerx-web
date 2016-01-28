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
-- Table structure for table `vaccinator`
--

DROP TABLE IF EXISTS `vaccinator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccinator` (
  `mappedId` int(11) NOT NULL,
  `birthdate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `dateRegistered` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `designation` varchar(50) DEFAULT NULL,
  `domicile` varchar(30) DEFAULT NULL,
  `estimatedBirthdate` tinyint(1) DEFAULT NULL,
  `firstName` varchar(30) DEFAULT NULL,
  `gender` varchar(15) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `lastName` varchar(30) DEFAULT NULL,
  `middleName` varchar(30) DEFAULT NULL,
  `nic` varchar(20) DEFAULT NULL,
  `nicOwnerFirstName` varchar(30) DEFAULT NULL,
  `nicOwnerLastName` varchar(30) DEFAULT NULL,
  `nicOwnerRelation` varchar(100) DEFAULT NULL,
  `qualification` varchar(50) DEFAULT NULL,
  `vaccinationCenterId` int(11) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `epAccountNumber` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`mappedId`),
  KEY `FK224E4406C79271AF` (`mappedId`),
  KEY `vaccinator_vaccinationCenterId_vaccinationcenter_mappedId_FK` (`vaccinationCenterId`),
  KEY `vaccinator_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `vaccinator_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `FK224E4406C79271AF` FOREIGN KEY (`mappedId`) REFERENCES `idmapper` (`mappedId`),
  CONSTRAINT `vaccinator_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccinator_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccinator_vaccinationCenterId_vaccinationcenter_mappedId_FK` FOREIGN KEY (`vaccinationCenterId`) REFERENCES `vaccinationcenter` (`mappedId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccinator`
--

LOCK TABLES `vaccinator` WRITE;
/*!40000 ALTER TABLE `vaccinator` DISABLE KEYS */;
/*!40000 ALTER TABLE `vaccinator` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:06
