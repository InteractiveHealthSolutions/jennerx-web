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
-- Table structure for table `communicationnote`
--

DROP TABLE IF EXISTS `communicationnote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `communicationnote` (
  `communicationNoteId` int(11) NOT NULL AUTO_INCREMENT,
  `communicationEventType` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `eventDate` datetime DEFAULT NULL,
  `probeClass` varchar(255) DEFAULT NULL,
  `probeId` varchar(255) DEFAULT NULL,
  `problem` varchar(255) DEFAULT NULL,
  `problemGroup` varchar(255) DEFAULT NULL,
  `receiver` varchar(255) DEFAULT NULL,
  `solution` varchar(255) DEFAULT NULL,
  `solutionGroup` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `communicationCategory` varchar(255) DEFAULT NULL,
  `groupId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`communicationNoteId`),
  KEY `commnote_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `commnote_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `communicationnote`
--

LOCK TABLES `communicationnote` WRITE;
/*!40000 ALTER TABLE `communicationnote` DISABLE KEYS */;
/*!40000 ALTER TABLE `communicationnote` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:32
