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
-- Table structure for table `dmp_`
--

DROP TABLE IF EXISTS `dmp_`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dmp_` (
  `dmpId` int(11) NOT NULL AUTO_INCREMENT,
  `dmpProcedureName` varchar(45) DEFAULT NULL,
  `dmpTableName` varchar(45) DEFAULT NULL,
  `dmpFileNameInit` varchar(45) DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT NULL,
  `lastDumpDate` datetime DEFAULT NULL,
  PRIMARY KEY (`dmpId`),
  UNIQUE KEY `dmpTableName_UNIQUE` (`dmpTableName`),
  UNIQUE KEY `dmpProcedureName_UNIQUE` (`dmpProcedureName`),
  UNIQUE KEY `dmpFileNameInit_UNIQUE` (`dmpFileNameInit`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dmp_`
--

LOCK TABLES `dmp_` WRITE;
/*!40000 ALTER TABLE `dmp_` DISABLE KEYS */;
INSERT INTO `dmp_` VALUES (1,'DMP_MasterAll','dmp_master_all','DMP_MASTERALL',1,'2015-12-01 11:48:51'),(2,'DMP_MasterEPIData','dmp_master_epidata','DMP_MASTER_EPIDATA',1,'2015-12-08 20:44:24');
/*!40000 ALTER TABLE `dmp_` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:40
