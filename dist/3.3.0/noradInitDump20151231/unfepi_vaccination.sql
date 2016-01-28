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
-- Table structure for table `vaccination`
--

DROP TABLE IF EXISTS `vaccination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccination` (
  `vaccinationRecordNum` int(11) NOT NULL AUTO_INCREMENT,
  `childId` int(11) NOT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `epiNumber` varchar(255) DEFAULT NULL,
  `height` float DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `preferredReminderTiming` varchar(255) DEFAULT NULL,
  `reasonNotTimelyVaccination` varchar(255) DEFAULT NULL,
  `vaccinationCenterId` int(11) DEFAULT NULL,
  `vaccinationDate` datetime DEFAULT NULL,
  `vaccinationDuedate` datetime NOT NULL,
  `vaccinationStatus` varchar(20) NOT NULL,
  `vaccinatorId` int(11) DEFAULT NULL,
  `vaccineId` smallint(6) NOT NULL,
  `weight` float DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `isVaccinationCenterChanged` tinyint(1) DEFAULT NULL,
  `isFirstVaccination` tinyint(1) DEFAULT NULL,
  `hasApprovedLottery` tinyint(1) DEFAULT NULL,
  `timelinessFactor` smallint(6) DEFAULT NULL,
  `timelinessStatus` varchar(20) DEFAULT NULL,
  `reasonVaccineNotGiven` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`vaccinationRecordNum`),
  KEY `vaccination_vaccinatorId_vaccinator_mappedId_FK` (`vaccinatorId`),
  KEY `vaccination_vaccinationCenterId_vaccinationcenter_mappedId_FK` (`vaccinationCenterId`),
  KEY `vaccination_childId_child_mappedId_FK` (`childId`),
  KEY `vaccination_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `vaccination_vaccineId_vaccine_vaccineId_FK` (`vaccineId`),
  KEY `vaccination_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  CONSTRAINT `vaccination_childId_child_mappedId_FK` FOREIGN KEY (`childId`) REFERENCES `child` (`mappedId`),
  CONSTRAINT `vaccination_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccination_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `vaccination_vaccinationCenterId_vaccinationcenter_mappedId_FK` FOREIGN KEY (`vaccinationCenterId`) REFERENCES `vaccinationcenter` (`mappedId`),
  CONSTRAINT `vaccination_vaccinatorId_vaccinator_mappedId_FK` FOREIGN KEY (`vaccinatorId`) REFERENCES `vaccinator` (`mappedId`),
  CONSTRAINT `vaccination_vaccineId_vaccine_vaccineId_FK` FOREIGN KEY (`vaccineId`) REFERENCES `vaccine` (`vaccineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vaccination`
--

LOCK TABLES `vaccination` WRITE;
/*!40000 ALTER TABLE `vaccination` DISABLE KEYS */;
/*!40000 ALTER TABLE `vaccination` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `unfepi`.`VaccinationTriggerBI`
BEFORE INSERT ON `unfepi`.`vaccination`
FOR EACH ROW
BEGIN

-- if it is first vaccination i.e. no record is existing plus status is not PENDING
IF NEW.isFirstVaccination IS NULL 
THEN 
	set NEW.isFirstVaccination = 
		CASE WHEN NEW.vaccinationStatus <> 'PENDING' AND not exists (select vaccineId from vaccination where childId = NEW.childId and vaccinationStatus <> 'PENDING') 
		THEN TRUE 
		ELSE FALSE END;
END IF;

IF NEW.vaccinationDate IS NOT NULL 
THEN 
	SET NEW.timelinessFactor = 
				CASE WHEN NEW.isFirstVaccination 
				THEN datediff(NEW.vaccinationDate,(select birthdate from child where mappedid=NEW.childid)) 
				ELSE datediff(NEW.vaccinationDate,NEW.vaccinationDuedate) END;
END IF;

SET NEW.timelinessStatus = 
CASE WHEN NEW.timelinessFactor is null 
THEN 'NA' 
WHEN NEW.timelinessFactor between -1000 and -4 
THEN 'EARLY' 
WHEN NEW.timelinessFactor between -3 and 7 
THEN 'TIMELY' 
WHEN NEW.timelinessFactor between 8 and 32000 
THEN 'LATE' 
ELSE 'UNKNOWN' END;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `unfepi`.`VaccinationTriggerBU`
BEFORE UPDATE ON `unfepi`.`vaccination`
FOR EACH ROW
BEGIN 

IF NEW.vaccinationDate IS NOT NULL 
THEN 
	SET NEW.timelinessFactor = 
				CASE WHEN NEW.isFirstVaccination 
				THEN datediff(NEW.vaccinationDate,(select birthdate from child where mappedid=NEW.childid)) 
				ELSE datediff(NEW.vaccinationDate,NEW.vaccinationDuedate) END;
END IF;

SET NEW.timelinessStatus = 
CASE WHEN NEW.timelinessFactor is null 
THEN 'NA' 
WHEN NEW.timelinessFactor between -1000 and -4 
THEN 'EARLY' 
WHEN NEW.timelinessFactor between -3 and 7 
THEN 'TIMELY' 
WHEN NEW.timelinessFactor between 8 and 32000 
THEN 'LATE' 
ELSE 'UNKNOWN' END;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:28
