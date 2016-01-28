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
-- Table structure for table `notifier`
--

DROP TABLE IF EXISTS `notifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifier` (
  `notifierId` int(11) NOT NULL AUTO_INCREMENT,
  `columnsHeaderList` varchar(500) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `notifierCron` varchar(255) NOT NULL,
  `notifierMessage` varchar(500) DEFAULT NULL,
  `notifierName` varchar(255) NOT NULL,
  `notifierQuery` varchar(2000) DEFAULT NULL,
  `notifierStatus` varchar(20) NOT NULL,
  `notifierSubject` varchar(255) DEFAULT NULL,
  `notifierType` varchar(20) NOT NULL,
  `queryDescription` varchar(500) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `notifierFooterQuery` varchar(2000) DEFAULT NULL,
  `notifierHeaderQuery` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`notifierId`),
  UNIQUE KEY `notifierName` (`notifierName`),
  KEY `notifier_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `notifier_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `notifier_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `notifier_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifier`
--

LOCK TABLES `notifier` WRITE;
/*!40000 ALTER TABLE `notifier` DISABLE KEYS */;
INSERT INTO `notifier` VALUES (4,'Scheme, AmountWon,EPCharges,AmountDue,CNIC,CellNumber,IncentiveRealizedDate,ConsumptionDate,TransactionDate,ProgramId,ChildName,CenterId,Center,IncentiveRecordCreatedDate,IncentiveStatus,IncentiveRecordId,Vaccine,VaccineDate,VaccineStatus,VaccinatorId,Vaccinator\r ',NULL,NULL,NULL,'0 15 16 ? * *',NULL,'CaregiverIncentivesDailyNotifier','CALL ChildIncentiveRealizedCalculate(CURDATE(), CURDATE())','ACTIVE','NORAD[daily incentive csv]: Caregiver Incentives Realized','EMAIL_CSV',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `notifier` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:00
