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
-- Table structure for table `dailysummary`
--

DROP TABLE IF EXISTS `dailysummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dailysummary` (
  `serialNumber` int(11) NOT NULL AUTO_INCREMENT,
  `bcgEnrolledTotal` int(11) DEFAULT NULL,
  `bcgEnrolledWithBoth` int(11) DEFAULT NULL,
  `bcgEnrolledWithLottery` int(11) DEFAULT NULL,
  `bcgEnrolledWithNone` int(11) DEFAULT NULL,
  `bcgEnrolledWithReminder` int(11) DEFAULT NULL,
  `bcgVisited` int(11) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `measles1Followuped` int(11) DEFAULT NULL,
  `measles1Visited` int(11) DEFAULT NULL,
  `measles2Followuped` int(11) DEFAULT NULL,
  `measles2Visited` int(11) DEFAULT NULL,
  `penta1EnrolledTotal` int(11) DEFAULT NULL,
  `penta1EnrolledWithBoth` int(11) DEFAULT NULL,
  `penta1EnrolledWithLottery` int(11) DEFAULT NULL,
  `penta1EnrolledWithNone` int(11) DEFAULT NULL,
  `penta1EnrolledWithReminder` int(11) DEFAULT NULL,
  `penta1Followuped` int(11) DEFAULT NULL,
  `penta1Visited` int(11) DEFAULT NULL,
  `penta2Followuped` int(11) DEFAULT NULL,
  `penta2Visited` int(11) DEFAULT NULL,
  `penta3Followuped` int(11) DEFAULT NULL,
  `penta3Visited` int(11) DEFAULT NULL,
  `summaryDate` datetime NOT NULL,
  `totalEnrolled` int(11) DEFAULT NULL,
  `totalFollowuped` int(11) DEFAULT NULL,
  `totalVisited` int(11) DEFAULT NULL,
  `vaccinationCenterId` int(11) NOT NULL,
  `vaccinatorId` int(11) NOT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `ttGivenTotal` int(11) DEFAULT NULL,
  `opvGivenTotal` int(11) DEFAULT NULL,
  PRIMARY KEY (`serialNumber`),
  KEY `dailysummary_vaccinatorId_vaccinator_mappedId_FK` (`vaccinatorId`),
  KEY `dailysummary_vaccinationCenterId_vaccinationcenter_mappedId_FK` (`vaccinationCenterId`),
  KEY `dailysummary_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `dailysummary_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `dailysummary_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `dailysummary_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `dailysummary_vaccinationCenterId_vaccinationcenter_mappedId_FK` FOREIGN KEY (`vaccinationCenterId`) REFERENCES `vaccinationcenter` (`mappedId`),
  CONSTRAINT `dailysummary_vaccinatorId_vaccinator_mappedId_FK` FOREIGN KEY (`vaccinatorId`) REFERENCES `vaccinator` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dailysummary`
--

LOCK TABLES `dailysummary` WRITE;
/*!40000 ALTER TABLE `dailysummary` DISABLE KEYS */;
/*!40000 ALTER TABLE `dailysummary` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:41
