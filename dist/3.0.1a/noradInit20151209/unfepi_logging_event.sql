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
-- Table structure for table `logging_event`
--

DROP TABLE IF EXISTS `logging_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logging_event` (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text NOT NULL,
  `logger_name` varchar(254) NOT NULL,
  `level_string` varchar(254) NOT NULL,
  `thread_name` varchar(254) DEFAULT NULL,
  `reference_flag` smallint(6) DEFAULT NULL,
  `arg0` varchar(254) DEFAULT NULL,
  `arg1` varchar(254) DEFAULT NULL,
  `arg2` varchar(254) DEFAULT NULL,
  `arg3` varchar(254) DEFAULT NULL,
  `caller_filename` varchar(254) NOT NULL,
  `caller_class` varchar(254) NOT NULL,
  `caller_method` varchar(254) NOT NULL,
  `caller_line` char(4) NOT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`event_id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logging_event`
--

LOCK TABLES `logging_event` WRITE;
/*!40000 ALTER TABLE `logging_event` DISABLE KEYS */;
INSERT INTO `logging_event` VALUES (1449567485333,'User administrator logged in','dbAppender','INFO','http-8080-2',1,'LOGIN',NULL,NULL,NULL,'LoginController.java','org.ird.unfepi.web.controller.LoginController','onSubmit','49',1),(1449589443845,'User administrator logged in','dbAppender','INFO','http-8080-1',1,'LOGIN',NULL,NULL,NULL,'LoginController.java','org.ird.unfepi.web.controller.LoginController','onSubmit','49',2),(1449589779703,'VaccinationCenter[mappedId=44;name=center 1;fullName=center 1;shortName=null;centerType=PRIVATE;idMapper=null;allowedVaccineDays=null;dateRegistered=Tue Dec 08 00:00:00 PKT 2015;description=null;createdByUserId=org.ird.unfepi.model.User@45a2f54a;createdDate=Tue Dec 08 20:49:39 PKT 2015;lastEditedByUserId=null;lastEditedDate=null]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','64',3),(1449589779723,'Vaccine=BCG;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',4),(1449589779736,'Vaccine=Measles1;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',5),(1449589779750,'Vaccine=Measles2;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',6),(1449589779782,'Vaccine=OPV0;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',7),(1449589779797,'Vaccine=OPV1;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',8),(1449589779807,'Vaccine=OPV2;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',9),(1449589779821,'Vaccine=OPV3;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',10),(1449589779832,'Vaccine=PCV1;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',11),(1449589779844,'Vaccine=PCV2;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',12),(1449589779857,'Vaccine=PCV3;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',13),(1449589779869,'Vaccine=Penta1;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',14),(1449589779879,'Vaccine=Penta2;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',15),(1449589779890,'Vaccine=Penta3;Days=[null, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATIONCENTER_ADD','administrator',NULL,'AddVaccinationCenterController.java','org.ird.unfepi.web.controller.AddVaccinationCenterController','onSubmit','68',16),(1449589901274,'Vaccinator[mappedId=45;idMapper=null;vaccinationCenterId=44;vaccinationCenter=null;firstName=test;middleName=null;lastName=one;birthdate=Sun Dec 08 00:00:00 PKT 1985;age=null;estimatedBirthdate=true;nic=0000000000000;epAccountNumber=000000000000;nicOwnerFirstName=null;nicOwnerLastName=null;nicOwnerRelation=null;domicile=null;gender=MALE;dateRegistered=Tue Dec 08 00:00:00 PKT 2015;description=;createdByUserId=org.ird.unfepi.model.User@45a2f54a;createdDate=Tue Dec 08 20:51:41 PKT 2015;lastEditedByUserId=null;lastEditedDate=null;qualification=Masters;designation=null]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_INSERT','VACCINATOR_ADD','administrator',NULL,'AddVaccinatorController.java','org.ird.unfepi.web.controller.AddVaccinatorController','onSubmit','61',17),(1449589911530,'Vaccinator[mappedId=45;idMapper=org.ird.unfepi.model.IdMapper@93686fc;vaccinationCenterId=44;vaccinationCenter=org.ird.unfepi.model.VaccinationCenter@5161a102;firstName=test;middleName=null;lastName=one;birthdate=Sun Dec 08 00:00:00 PKT 1985;age=null;estimatedBirthdate=true;nic=0000000000000;epAccountNumber=000000000000;nicOwnerFirstName=null;nicOwnerLastName=null;nicOwnerRelation=null;domicile=null;gender=MALE;dateRegistered=Tue Dec 08 00:00:00 PKT 2015;description=;createdByUserId=org.ird.unfepi.model.User@68d11de;createdDate=2015-12-08 20:51:41.0;lastEditedByUserId=org.ird.unfepi.model.User@45a2f54a;lastEditedDate=Tue Dec 08 20:51:51 PKT 2015;qualification=Masters;designation=null]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_UPDATE','VACCINATOR_CORRECT','administrator',NULL,'EditVaccinatorController.java','org.ird.unfepi.web.controller.EditVaccinatorController','onSubmit','51',18),(1449590075916,'Child[mappedId=46;idMapper=org.ird.unfepi.model.IdMapper@6206bf74;firstName=chld;middleName=null;lastName=null;birthdate=Tue Dec 08 00:00:00 PKT 2015;age=null;estimatedBirthdate=false;fatherFirstName=ch fhh;fatherMiddleName=null;fatherLastName=null;motherFirstName=null;motherMiddleName=null;motherLastName=null;nic=2222222222223;nicOwnerFirstName=null;nicOwnerLastName=null;nicOwnerRelation=null;domicile=null;gender=MALE;dateEnrolled=2015-12-08 00:00:00.0;dateOfCompletion=null;terminationDate=null;terminationReason=;status=FOLLOW_UP;enrollmentVaccineId=1;enrollmentVaccine=org.ird.unfepi.model.Vaccine@31152078;description=;createdByUserId=org.ird.unfepi.model.User@2c5af81c;createdDate=2015-12-08 20:53:52.0;lastEditedByUserId=org.ird.unfepi.model.User@45a2f54a;lastEditedDate=Tue Dec 08 20:54:35 PKT 2015]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_UPDATE','CHILD_BIOGRAPHIC_CORRECT','administrator',NULL,'EditChildController.java','org.ird.unfepi.web.controller.EditChildController','onSubmit','69',19),(1449590115477,'ChildDataBean[projectId=null;child=org.ird.unfepi.model.Child@4dd207a2;address=org.ird.unfepi.model.Address@5cd74b29;childNamed=true;birthdateOrAge=birthdate;childagey=;childagem=;childagew=;childaged=;contactPrimary=03345252525;contactSecondary=03326598754;preference=org.ird.unfepi.model.LotterySms@732eafb1;vaccinations=[org.ird.unfepi.model.Vaccination@7b4e7a17, org.ird.unfepi.model.Vaccination@6d7e937e, org.ird.unfepi.model.Vaccination@61bfa744, org.ird.unfepi.model.Vaccination@77577d0a, org.ird.unfepi.model.Vaccination@75f20e51, org.ird.unfepi.model.Vaccination@3d85d64];uuid=bf4628e0-0dc6-41f7-935a-5ebe599fcd3f]','dbAppender','INFO','http-8080-2',1,'TRANSACTION_UPDATE','CONTACT_NUMBER_CORRECT','administrator',NULL,'EditChildController.java','org.ird.unfepi.web.controller.EditChildController','onSubmit','111',20);
/*!40000 ALTER TABLE `logging_event` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:30
