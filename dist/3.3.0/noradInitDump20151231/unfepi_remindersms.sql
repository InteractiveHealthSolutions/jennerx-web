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
-- Table structure for table `remindersms`
--

DROP TABLE IF EXISTS `remindersms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remindersms` (
  `rsmsRecordNum` int(11) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `dayNumber` smallint(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `dueDate` datetime NOT NULL,
  `hoursDifference` int(11) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `originator` varchar(20) DEFAULT NULL,
  `recipient` varchar(20) DEFAULT NULL,
  `referenceNumber` varchar(255) DEFAULT NULL,
  `reminderId` smallint(6) DEFAULT NULL,
  `reminderStatus` varchar(20) DEFAULT NULL,
  `sentDate` datetime DEFAULT NULL,
  `smsCancelReason` varchar(255) DEFAULT NULL,
  `smsCancelReasonOther` varchar(255) DEFAULT NULL,
  `text` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `vaccinationRecordNum` int(11) NOT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`rsmsRecordNum`),
  UNIQUE KEY `referenceNumber_UNIQUE` (`referenceNumber`),
  KEY `remindersms_reminderId_reminder_reminderId_FK` (`reminderId`),
  KEY `remindersms_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `remindersms_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `remindersms_vaccRecNumIndex` (`vaccinationRecordNum`),
  CONSTRAINT `remindersms_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `remindersms_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `remindersms_reminderId_reminder_reminderId_FK` FOREIGN KEY (`reminderId`) REFERENCES `reminder` (`reminderId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remindersms`
--

LOCK TABLES `remindersms` WRITE;
/*!40000 ALTER TABLE `remindersms` DISABLE KEYS */;
/*!40000 ALTER TABLE `remindersms` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:37