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
-- Table structure for table `contactnumber`
--

DROP TABLE IF EXISTS `contactnumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contactnumber` (
  `contactNumberId` int(11) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `mappedId` int(11) NOT NULL,
  `number` varchar(20) NOT NULL,
  `numberType` varchar(20) NOT NULL DEFAULT 'UNKNOWN',
  `ownerFirstName` varchar(30) DEFAULT NULL,
  `ownerLastName` varchar(30) DEFAULT NULL,
  `ownerRelation` varchar(100) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `telelineType` varchar(45) NOT NULL DEFAULT 'UNKNOWN',
  PRIMARY KEY (`contactNumberId`),
  KEY `contactnumber_mappedId_idmapper_mappedId_FK` (`mappedId`),
  KEY `contactnumber_ownerRelation_relationship_relationshipId_FK` (`ownerRelation`),
  KEY `contactnumber_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `contactnumber_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `contactnumber_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `contactnumber_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `contactnumber_mappedId_idmapper_mappedId_FK` FOREIGN KEY (`mappedId`) REFERENCES `idmapper` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contactnumber`
--

LOCK TABLES `contactnumber` WRITE;
/*!40000 ALTER TABLE `contactnumber` DISABLE KEYS */;
INSERT INTO `contactnumber` VALUES (1,'2015-12-08 20:53:52',NULL,NULL,46,'03345252525','PRIMARY',NULL,NULL,NULL,43,NULL,'MOBILE'),(2,'2015-12-08 20:53:52',NULL,NULL,46,'03326598754','SECONDARY',NULL,NULL,NULL,43,NULL,'UNKNOWN'),(3,'2015-12-08 20:56:58',NULL,NULL,47,'03345252525','PRIMARY',NULL,NULL,NULL,43,NULL,'MOBILE'),(4,'2015-12-08 22:01:42',NULL,NULL,49,'03345252525','PRIMARY',NULL,NULL,NULL,43,NULL,'MOBILE');
/*!40000 ALTER TABLE `contactnumber` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:15
