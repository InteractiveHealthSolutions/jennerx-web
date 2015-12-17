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
-- Table structure for table `incentiveparams`
--

DROP TABLE IF EXISTS `incentiveparams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incentiveparams` (
  `incentiveParamsId` smallint(6) NOT NULL AUTO_INCREMENT,
  `amount` float DEFAULT NULL,
  `armId` int(11) NOT NULL,
  `createdDate` datetime DEFAULT NULL,
  `criteriaRangeMax` float DEFAULT NULL,
  `criteriaRangeMin` float DEFAULT NULL,
  `roleId` smallint(6) DEFAULT NULL,
  `vaccineId` smallint(6) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`incentiveParamsId`),
  KEY `incentiveparams_armId_arm_armId_FK` (`armId`),
  KEY `incentiveparams_vaccineId_vaccine_vaccineId_FK` (`vaccineId`),
  KEY `incentiveparams_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `incentiveparams_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `incentiveparams_armId_arm_armId_FK` FOREIGN KEY (`armId`) REFERENCES `arm` (`armId`),
  CONSTRAINT `incentiveparams_vaccineId_vaccine_vaccineId_FK` FOREIGN KEY (`vaccineId`) REFERENCES `vaccine` (`vaccineId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incentiveparams`
--

LOCK TABLES `incentiveparams` WRITE;
/*!40000 ALTER TABLE `incentiveparams` DISABLE KEYS */;
INSERT INTO `incentiveparams` VALUES (1,150,1,NULL,NULL,NULL,1,1,NULL),(2,150,1,NULL,NULL,NULL,1,2,NULL),(3,150,1,NULL,NULL,NULL,1,3,NULL),(4,150,1,NULL,NULL,NULL,1,4,NULL),(5,150,1,NULL,NULL,NULL,1,5,NULL),(6,150,1,NULL,NULL,NULL,1,6,NULL),(7,30,1,NULL,NULL,NULL,2,1,NULL),(8,30,1,NULL,NULL,NULL,2,2,NULL),(9,30,1,NULL,NULL,NULL,2,3,NULL),(10,30,1,NULL,NULL,NULL,2,4,NULL),(11,30,1,NULL,NULL,NULL,2,5,NULL),(12,30,1,NULL,NULL,NULL,2,6,NULL),(13,50,2,NULL,NULL,NULL,1,1,NULL),(14,50,2,NULL,NULL,NULL,1,2,NULL),(15,50,2,NULL,NULL,NULL,1,3,NULL),(16,100,2,NULL,NULL,NULL,1,4,NULL),(17,100,2,NULL,NULL,NULL,1,5,NULL),(18,150,2,NULL,NULL,NULL,1,6,NULL),(19,10,2,NULL,NULL,NULL,2,1,NULL),(20,10,2,NULL,NULL,NULL,2,2,NULL),(21,10,2,NULL,NULL,NULL,2,3,NULL),(22,20,2,NULL,NULL,NULL,2,4,NULL),(23,20,2,NULL,NULL,NULL,2,5,NULL),(24,30,2,NULL,NULL,NULL,2,6,NULL);
/*!40000 ALTER TABLE `incentiveparams` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:19
