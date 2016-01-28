CREATE DATABASE  IF NOT EXISTS `smstarseel` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `smstarseel`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: smstarseel
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
-- Table structure for table `inboundmessage`
--

DROP TABLE IF EXISTS `inboundmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inboundmessage` (
  `inboundId` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `originator` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `recieveDate` datetime DEFAULT NULL,
  `recipient` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `referenceNumber` varchar(255) CHARACTER SET latin1 NOT NULL,
  `status` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `systemProcessingEndDate` datetime DEFAULT NULL,
  `systemProcessingStartDate` datetime DEFAULT NULL,
  `systemRecieveDate` datetime DEFAULT NULL,
  `text` varchar(2000) DEFAULT NULL,
  `type` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`inboundId`),
  UNIQUE KEY `referenceNumber` (`referenceNumber`),
  KEY `FKC9483A6E658E14D0` (`projectId`),
  CONSTRAINT `FKC9483A6E658E14D0` FOREIGN KEY (`projectId`) REFERENCES `project` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inboundmessage`
--

LOCK TABLES `inboundmessage` WRITE;
/*!40000 ALTER TABLE `inboundmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `inboundmessage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:14