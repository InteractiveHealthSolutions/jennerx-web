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
-- Table structure for table `vaccine`
--

DROP TABLE IF EXISTS `vaccine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccine` (
  `vaccineId` smallint(6) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fullName` varchar(50) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `shortName` varchar(30) NOT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `maxGracePeriodDays` tinyint(6) DEFAULT NULL,
  `minGracePeriodDays` tinyint(6) DEFAULT NULL,
  `voidReason` varchar(255) DEFAULT NULL,
  `voided` tinyint(1) DEFAULT NULL,
  `voidedDate` datetime DEFAULT NULL,
  `voidedByUserId` int(11) DEFAULT NULL,
  `isSupplementary` bit(1) DEFAULT NULL,
  PRIMARY KEY (`vaccineId`),
  UNIQUE KEY `name` (`name`),
  KEY `vaccine_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `vaccine_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `vaccine_voidedByUserId_user_mappedId_FK` (`voidedByUserId`),
  CONSTRAINT `vaccine_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccine_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccine_voidedByUserId_user_mappedId_FK` FOREIGN KEY (`voidedByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccine`
--

LOCK TABLES `vaccine` WRITE;
/*!40000 ALTER TABLE `vaccine` DISABLE KEYS */;
INSERT INTO `vaccine` VALUES (1,NULL,NULL,'BCG/OPV','2013-04-03 10:53:42','BCG','BCG',NULL,43,10,3,NULL,NULL,NULL,NULL,'\0'),(2,NULL,NULL,'Penta1/OPV',NULL,'Penta1','Penta',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(3,NULL,NULL,'Penta2/OPV',NULL,'Penta2','Penta',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(4,NULL,NULL,'Penta3/OPV',NULL,'Penta3','Penta',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(5,NULL,NULL,'Measles1',NULL,'Measles1','Measles',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(6,NULL,NULL,'Measles2',NULL,'Measles2','Measles',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(7,NULL,NULL,'OPV0',NULL,'OPV0','OPV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(8,NULL,NULL,'OPV1',NULL,'OPV1','OPV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(9,NULL,NULL,'OPV2',NULL,'OPV2','OPV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(10,NULL,NULL,'OPV3',NULL,'OPV3','OPV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(11,NULL,NULL,'PCV1',NULL,'PCV1','PCV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(12,NULL,NULL,'PCV2',NULL,'PCV2','PCV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(13,NULL,NULL,'PCV3',NULL,'PCV3','PCV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'\0'),(14,NULL,NULL,'Supplementary OPV',NULL,'OPV','OPV',NULL,NULL,10,3,NULL,NULL,NULL,NULL,''),(15,NULL,NULL,'Supplementary Measles',NULL,'Measles','Measles',NULL,NULL,10,3,NULL,NULL,NULL,NULL,''),(9999,NULL,NULL,'Extra InvalidPenta Dose',NULL,'Invalid-Penta','Invalid-Penta',NULL,NULL,10,3,NULL,NULL,NULL,NULL,'');
/*!40000 ALTER TABLE `vaccine` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:29
