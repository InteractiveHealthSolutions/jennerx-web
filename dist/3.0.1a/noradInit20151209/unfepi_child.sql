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
-- Table structure for table `child`
--

DROP TABLE IF EXISTS `child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `child` (
  `mappedId` int(11) NOT NULL,
  `birthdate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `dateEnrolled` datetime DEFAULT NULL,
  `dateOfCompletion` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `domicile` varchar(30) DEFAULT NULL,
  `estimatedBirthdate` tinyint(1) DEFAULT NULL,
  `fatherFirstName` varchar(30) DEFAULT NULL,
  `fatherLastName` varchar(30) DEFAULT NULL,
  `fatherMiddleName` varchar(30) DEFAULT NULL,
  `firstName` varchar(30) DEFAULT NULL,
  `gender` varchar(15) NOT NULL DEFAULT 'UNKNOWN',
  `lastEditedDate` datetime DEFAULT NULL,
  `lastName` varchar(30) DEFAULT NULL,
  `middleName` varchar(30) DEFAULT NULL,
  `motherFirstName` varchar(30) DEFAULT NULL,
  `motherLastName` varchar(30) DEFAULT NULL,
  `motherMiddleName` varchar(30) DEFAULT NULL,
  `nic` varchar(20) DEFAULT NULL,
  `nicOwnerFirstName` varchar(30) DEFAULT NULL,
  `nicOwnerLastName` varchar(30) DEFAULT NULL,
  `nicOwnerRelation` varchar(255) DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'FOLLOW_UP',
  `terminationDate` datetime DEFAULT NULL,
  `terminationReason` varchar(255) DEFAULT NULL,
  `terminationReasonOther` varchar(255) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `enrollmentVaccineId` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`mappedId`),
  KEY `FK5A3F51CC79271AF` (`mappedId`),
  KEY `child_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `child_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `child_enrVaccId_vaccine_vaccId_idx` (`enrollmentVaccineId`),
  CONSTRAINT `child_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `child_enrVaccId_vaccine_vaccId` FOREIGN KEY (`enrollmentVaccineId`) REFERENCES `vaccine` (`vaccineId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `child_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `FK5A3F51CC79271AF` FOREIGN KEY (`mappedId`) REFERENCES `idmapper` (`mappedId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `child`
--

LOCK TABLES `child` WRITE;
/*!40000 ALTER TABLE `child` DISABLE KEYS */;
INSERT INTO `child` VALUES (46,'2015-12-08 00:00:00','2015-12-08 20:53:52','2015-12-08 00:00:00',NULL,'',NULL,0,'ch fhh',NULL,NULL,'chld','MALE','2015-12-08 20:55:15',NULL,NULL,NULL,NULL,NULL,'2222222222221',NULL,NULL,NULL,'FOLLOW_UP',NULL,'',NULL,43,43,1),(47,'2015-10-01 00:00:00','2015-12-08 20:56:58','2015-10-01 00:00:00',NULL,NULL,NULL,0,'f name',NULL,NULL,'ch name','MALE',NULL,NULL,NULL,NULL,NULL,NULL,'2222222222222',NULL,NULL,NULL,'FOLLOW_UP',NULL,NULL,NULL,43,NULL,1),(49,'2015-10-01 00:00:00','2015-12-08 22:01:42','2015-10-01 00:00:00',NULL,NULL,NULL,0,'father',NULL,NULL,'child','MALE',NULL,NULL,NULL,NULL,NULL,NULL,'2222222222223',NULL,NULL,NULL,'FOLLOW_UP',NULL,NULL,NULL,43,NULL,1);
/*!40000 ALTER TABLE `child` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:47
