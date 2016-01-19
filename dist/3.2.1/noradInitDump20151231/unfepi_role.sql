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
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `roleId` smallint(6) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `rolename` varchar(30) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `rolename` (`rolename`),
  KEY `role_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `role_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `role_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `role_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,NULL,NULL,NULL,'CHILD',NULL,NULL),(2,NULL,NULL,NULL,'VACCINATOR',NULL,NULL),(3,NULL,NULL,NULL,'VACCINATION_CENTER',NULL,NULL),(4,NULL,NULL,NULL,'STOREKEEPER',NULL,NULL),(5,NULL,NULL,NULL,'ADMIN',NULL,NULL),(6,NULL,'','2013-01-28 16:21:46','PROGRAM_MANAGER',NULL,43),(7,'2012-06-18 14:30:40','','2013-06-17 17:09:53','RESEARCH_ASSOCIATE',43,43),(8,'2012-06-25 11:09:47','','2013-03-06 10:54:11','MONITOR',43,43),(9,'2012-06-27 15:24:30','','2012-10-25 12:51:39','GUEST_PREVILIGED',43,43),(10,'2012-06-27 15:25:19','',NULL,'GUEST_UNPREVILIGED',43,NULL),(11,'2012-07-24 10:59:21','','2012-10-22 17:43:25','INTEREN',43,43),(12,'2012-07-26 12:07:27','','2013-01-22 15:15:37','INTEREN_PREVILIGED',43,43),(13,'2012-11-02 13:04:01','','2013-06-18 14:37:23','DBA',43,43),(14,'2013-07-26 11:16:26','Data managment support officer',NULL,'DMSO',43,NULL),(15,NULL,NULL,NULL,'Daemon',43,NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:11
