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
-- Table structure for table `_tmptmasterepidatacsvvaccination`
--

DROP TABLE IF EXISTS `_tmptmasterepidatacsvvaccination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_tmptmasterepidatacsvvaccination` (
  `childId` int(11) NOT NULL,
  `vaccinationrecordnum` int(11) NOT NULL DEFAULT '0',
  `vaccinationDuedate` datetime NOT NULL,
  `vaccinationDate` datetime DEFAULT NULL,
  `vaccinationStatus` varchar(20) NOT NULL,
  `timelinessStatus` varchar(20) DEFAULT NULL,
  `timelinessFactor` smallint(6) DEFAULT NULL,
  `daysPastDueDate` varchar(7) CHARACTER SET utf8 DEFAULT NULL,
  `epiNumber` varchar(255) DEFAULT NULL,
  `vaccinationCreatedDate` datetime DEFAULT NULL,
  `vaccinationCenterPid` varchar(255),
  `vaccinatorPid` varchar(255),
  `vaccineId` smallint(6) NOT NULL,
  `vaccineName` varchar(30),
  `hasApprovedLottery` tinyint(1) DEFAULT NULL,
  `isFirstVaccination` tinyint(1) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `incentiveStatus` varchar(30),
  `consumptionDate` datetime DEFAULT NULL,
  `incentiveDate` datetime DEFAULT NULL,
  `transactionDate` datetime DEFAULT NULL,
  `VaccineReminder1Status` varchar(20) DEFAULT NULL,
  `VaccineReminder1Duedate` datetime,
  `VaccineReminder1SentDate` datetime DEFAULT NULL,
  `VaccineReminder1CreatedDate` datetime DEFAULT NULL,
  `VaccineReminder1CancelReason` varchar(255) DEFAULT NULL,
  `VaccineReminder1Text` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `VaccineReminder1ReferenceNumber` varchar(255) DEFAULT NULL,
  `VaccineReminder1Recipient` varchar(20) DEFAULT NULL,
  `VaccineReminder1dayNumber` smallint(6),
  `VaccineReminder1ReminderId` smallint(6) DEFAULT NULL,
  `VaccineReminder2Status` varchar(20) DEFAULT NULL,
  `VaccineReminder2Duedate` datetime,
  `VaccineReminder2SentDate` datetime DEFAULT NULL,
  `VaccineReminder2CreatedDate` datetime DEFAULT NULL,
  `VaccineReminder2CancelReason` varchar(255) DEFAULT NULL,
  `VaccineReminder2Text` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `VaccineReminder2ReferenceNumber` varchar(255) DEFAULT NULL,
  `VaccineReminder2Recipient` varchar(20) DEFAULT NULL,
  `VaccineReminder2dayNumber` smallint(6),
  `VaccineReminder2ReminderId` smallint(6) DEFAULT NULL,
  `VaccineReminder3Status` varchar(20) DEFAULT NULL,
  `VaccineReminder3Duedate` datetime,
  `VaccineReminder3SentDate` datetime DEFAULT NULL,
  `VaccineReminder3CreatedDate` datetime DEFAULT NULL,
  `VaccineReminder3CancelReason` varchar(255) DEFAULT NULL,
  `VaccineReminder3Text` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `VaccineReminder3ReferenceNumber` varchar(255) DEFAULT NULL,
  `VaccineReminder3Recipient` varchar(20) DEFAULT NULL,
  `VaccineReminder3dayNumber` smallint(6),
  `VaccineReminder3ReminderId` smallint(6) DEFAULT NULL,
  `IncentiveReminderStatus` varchar(20) DEFAULT NULL,
  `IncentiveReminderDuedate` datetime,
  `IncentiveReminderSentDate` datetime DEFAULT NULL,
  `IncentiveReminderCreatedDate` datetime DEFAULT NULL,
  `IncentiveReminderCancelReason` varchar(255) DEFAULT NULL,
  `IncentiveReminderText` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `IncentiveReminderReferenceNumber` varchar(255) DEFAULT NULL,
  `IncentiveReminderRecipient` varchar(20) DEFAULT NULL,
  `IncentiveReminderdayNumber` smallint(6),
  `IncentiveReminderReminderId` smallint(6) DEFAULT NULL,
  `IncentiveConsumedReminderStatus` varchar(20) DEFAULT NULL,
  `IncentiveConsumedReminderDuedate` datetime,
  `IncentiveConsumedReminderSentDate` datetime DEFAULT NULL,
  `IncentiveConsumedReminderCreatedDate` datetime DEFAULT NULL,
  `IncentiveConsumedReminderCancelReason` varchar(255) DEFAULT NULL,
  `IncentiveConsumedReminderText` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `IncentiveConsumedReminderReferenceNumber` varchar(255) DEFAULT NULL,
  `IncentiveConsumedReminderRecipient` varchar(20) DEFAULT NULL,
  `IncentiveConsumedReminderdayNumber` smallint(6),
  `IncentiveConsumedReminderReminderId` smallint(6) DEFAULT NULL,
  `responseCount` bigint(21) DEFAULT '0',
  `responseText` text CHARACTER SET utf8,
  KEY `vaccinationrecordnum` (`vaccinationrecordnum`),
  KEY `childId` (`childId`),
  KEY `vaccineId` (`vaccineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_tmptmasterepidatacsvvaccination`
--

LOCK TABLES `_tmptmasterepidatacsvvaccination` WRITE;
/*!40000 ALTER TABLE `_tmptmasterepidatacsvvaccination` DISABLE KEYS */;
/*!40000 ALTER TABLE `_tmptmasterepidatacsvvaccination` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:21
