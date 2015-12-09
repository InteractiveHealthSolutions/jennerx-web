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
INSERT INTO `notifier` VALUES (1,'Center ID,Center Name,Total,BCG,Penta-1,Penta-2,Penta-3,Measles-1,Measles-2',NULL,NULL,NULL,'0 15 9 ? * MON','','EnrollmentSummaryByVaccinationCenterPDF','CALL SummaryEnrByCenterCohort()','ACTIVE','EPIUNFP[weekly autogen PDF]: Summary Enrollment by Vaccination Center','EMAIL_PDF',NULL,NULL,NULL,NULL,NULL),(2,'Vaccine,Total,Pending,Sent,Missed,Cancelled,Not Applicable,Number of Mothers',NULL,NULL,NULL,'0 15 9 ? * MON','','SmsReminderSummaryCSV','select v.name, count(r.rsmsrecordnum) totaldue \r\n,sum(CASE WHEN r.reminderstatus IN (\'pending\',\'logged\') THEN 1 ELSE 0 END) pendingrem\r\n,sum(CASE WHEN r.reminderstatus IN (\'sent\') THEN 1 ELSE 0 END) sentrem\r\n,sum(CASE WHEN r.reminderstatus IN (\'missed\',\'failed\') THEN 1 ELSE 0 END) failedrem\r\n,sum(CASE WHEN r.reminderstatus IN (\'cancelled\') THEN 1 ELSE 0 END) cancelledrem\r\n,sum(CASE WHEN r.reminderstatus IN (\'na\') THEN 1 ELSE 0 END) notapplicable\r\n,count(distinct vn.childid) mothers from vaccine v \r\nleft join (vaccination vn join remindersms r on vn.vaccinationrecordnum=r.vaccinationrecordnum and date(r.duedate) <= date(curdate())) \r\non v.vaccineid=vn.vaccineid \r\ngroup by v.vaccineid ','ACTIVE','EPIUNFP[weekly autogen CSV]: Summary of SMS Reminders','EMAIL_CSV',NULL,NULL,NULL,NULL,'select \'All vaccines\' as allvaccines,\r\ncount(rsmsrecordnum) total ,\r\n(select count(rsmsrecordnum) from remindersms where reminderstatus IN (\'pending\',\'logged\') and date(duedate) <= curdate()) pendingrem ,\r\n(select count(rsmsrecordnum) from remindersms where reminderstatus IN (\'sent\') and date(duedate) <= curdate()) sentrem ,\r\n(select count(rsmsrecordnum) from remindersms where reminderstatus IN (\'missed\',\'failed\') and  date(duedate) <= curdate()) failedrem ,\r\n(select count(rsmsrecordnum) from remindersms where reminderstatus IN (\'cancelled\') and date(duedate) <= curdate()) cancelledrem ,\r\n(select count(rsmsrecordnum) from remindersms where reminderstatus IN (\'na\') and date(duedate) <= curdate()) notapplicable ,\r\ncount(distinct vn.childid) mothers\r\nfrom remindersms r join vaccination vn on r.vaccinationrecordnum=vn.vaccinationrecordnum where date(duedate) <= curdate()'),(3,'ChildID,Approved Lottery,Vaccine ID,Vaccine Name,Vaccination Due Date,Vaccination Date,EncounterOrForm Type',NULL,NULL,NULL,'0 15 9 ? * MON','','ChildrenLotteriesNotPerformedCSV','select idm.programId, v.hasapprovedlottery\n,v.vaccineid,vc.name vaccinename\n,v.vaccinationDueDate, v.vaccinationdate,\n(case when\n	( vaccinationDate is not null \n		and date(vaccinationDate) = (select min(date(vaccinationDate)) from vaccination where childId= v.childId) \n		and vaccinationRecordNum = (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate))\n	)\nthen \'ENROLLMENT\'\nElse \'FOLLOWUP\' \nend\n) as \'EncounterOrFormType\'\nfrom vaccination v \nleft join lostlottery l on v.childid=l.childMappedID and v.vaccineid=l.vaccineid\nleft join transaction t on v.childid=t.childid and v.vaccineid=t.vaccineid \nleft join idmapper idm on v.childid=idm.mappedid \nleft join vaccine vc on v.vaccineid= vc.vaccineid \nwhere vaccinationstatus=\'vaccinated\' \nand v.hasApprovedLottery=true \nand not exists(select * from lostlottery where childmappedid=v.childid and vaccineid=v.vaccineid) \nand not exists(select * from transaction where childid=v.childid and vaccineid=v.vaccineid)','SUSPENDED','EPIUNFP[weekly autogen CSV]: Children Lotteries Not Performed','EMAIL_CSV',NULL,NULL,NULL,NULL,NULL),(4,'Scheme, AmountWon,EPCharges,AmountDue,CNIC,CellNumber,IncentiveRealizedDate,ConsumptionDate,TransactionDate,ProgramId,ChildName,CenterId,Center,IncentiveRecordCreatedDate,IncentiveStatus,IncentiveRecordId,Vaccine,VaccineDate,VaccineStatus,VaccinatorId,Vaccinator\r ',NULL,NULL,NULL,'0 15 16 ? * *',NULL,'CaregiverIncentivesDailyNotifier','CALL ChildIncentiveRealizedCalculate(CURDATE(), CURDATE())','ACTIVE','NORAD[daily incentive csv]: Caregiver Incentives Realized','EMAIL_CSV',NULL,NULL,NULL,NULL,NULL);
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

-- Dump completed on 2015-12-09 10:19:06
