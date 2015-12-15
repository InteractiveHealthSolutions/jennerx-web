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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `addressId` int(11) NOT NULL AUTO_INCREMENT,
  `addArea` varchar(30) DEFAULT NULL,
  `addColony` varchar(30) DEFAULT NULL,
  `addDistrict` varchar(30) DEFAULT NULL,
  `addHouseNumber` varchar(30) DEFAULT NULL,
  `addLandmark` varchar(50) DEFAULT NULL,
  `addSector` varchar(30) DEFAULT NULL,
  `addStreet` varchar(30) DEFAULT NULL,
  `addUc` varchar(30) DEFAULT NULL,
  `addressType` varchar(20) DEFAULT NULL,
  `addtown` varchar(50) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `cityName` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `mappedId` int(11) NOT NULL,
  `phone1` varchar(20) DEFAULT NULL,
  `phone1Owner` varchar(30) DEFAULT NULL,
  `phone2` varchar(20) DEFAULT NULL,
  `phone2Owner` varchar(30) DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `region` varchar(30) DEFAULT NULL,
  `weburl` varchar(100) DEFAULT NULL,
  `zipcode` varchar(15) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`addressId`),
  KEY `address_mappedId_IdMapper_mappedId_FK` (`mappedId`),
  KEY `address_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `address_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `address_cityId_location_locationId_FK` (`cityId`),
  CONSTRAINT `address_cityId_location_locationId_FK` FOREIGN KEY (`cityId`) REFERENCES `location` (`locationId`),
  CONSTRAINT `address_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `address_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `address_mappedId_IdMapper_mappedId_FK` FOREIGN KEY (`mappedId`) REFERENCES `idmapper` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,NULL,'',NULL,'','','','','Urban-2','PRIMARY','Hafizabad Town',328,'',NULL,'2015-12-08 20:53:52',NULL,NULL,NULL,NULL,46,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,43,NULL),(2,NULL,'',NULL,'','','','','NARIN WALA','PRIMARY','Hafizabad Town',328,NULL,NULL,'2015-12-08 20:56:58',NULL,NULL,NULL,NULL,47,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,43,NULL),(3,NULL,'',NULL,'','','','','CHACK CHATTHA','PRIMARY','Hafizabad Town',328,NULL,NULL,'2015-12-08 22:01:42',NULL,NULL,NULL,NULL,49,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,43,NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:04
