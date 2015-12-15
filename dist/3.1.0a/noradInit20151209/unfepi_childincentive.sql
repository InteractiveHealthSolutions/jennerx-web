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
-- Table structure for table `childincentive`
--

DROP TABLE IF EXISTS `childincentive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `childincentive` (
  `childIncentiveId` int(11) NOT NULL AUTO_INCREMENT,
  `amount` float DEFAULT NULL,
  `armId` int(11) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `consumptionDate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `hasWonIncentive` tinyint(1) DEFAULT NULL,
  `incentiveDate` datetime DEFAULT NULL,
  `incentiveStatus` varchar(30) NOT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `transactionDate` datetime DEFAULT NULL,
  `vaccinationRecordNum` int(11) NOT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `incentiveParamId` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`childIncentiveId`),
  UNIQUE KEY `vaccinationRecordNum` (`vaccinationRecordNum`),
  KEY `clott_armId_arm_armId_FK` (`armId`),
  KEY `clott_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `clott_vaccRecordNum_vaccination_vaccinationRecordNum_FK` (`vaccinationRecordNum`),
  KEY `clott_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `clott_incentiveParamId_incentiveparam_incentiveParamId_FK` (`incentiveParamId`),
  CONSTRAINT `clott_incentiveParamId_incentiveparam_incentiveParamId_FK` FOREIGN KEY (`incentiveParamId`) REFERENCES `incentiveparams` (`incentiveParamsId`),
  CONSTRAINT `clott_armId_arm_armId_FK` FOREIGN KEY (`armId`) REFERENCES `arm` (`armId`),
  CONSTRAINT `clott_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `clott_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `clott_vaccRecordNum_vaccination_vaccinationRecordNum_FK` FOREIGN KEY (`vaccinationRecordNum`) REFERENCES `vaccination` (`vaccinationRecordNum`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `childincentive`
--

LOCK TABLES `childincentive` WRITE;
/*!40000 ALTER TABLE `childincentive` DISABLE KEYS */;
INSERT INTO `childincentive` VALUES (1,150,1,NULL,NULL,'2015-12-08 20:53:52',NULL,1,'2015-12-08 00:00:00','AVAILABLE',NULL,NULL,1,43,NULL,1),(2,50,2,NULL,NULL,'2015-12-08 20:56:58',NULL,1,'2015-10-01 00:00:00','AVAILABLE',NULL,NULL,7,43,NULL,13),(3,50,2,NULL,NULL,'2015-12-08 20:57:53',NULL,1,'2015-11-18 00:00:00','AVAILABLE',NULL,NULL,8,43,NULL,14),(4,50,2,NULL,NULL,'2015-12-08 22:01:42',NULL,1,'2015-10-01 00:00:00','AVAILABLE',NULL,NULL,12,43,NULL,13);
/*!40000 ALTER TABLE `childincentive` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:28
