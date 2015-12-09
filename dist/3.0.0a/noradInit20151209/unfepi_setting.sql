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
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `settingId` smallint(6) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `displayName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`settingId`),
  UNIQUE KEY `name` (`name`),
  KEY `CreatorUserIdFK` (`createdByUserId`),
  KEY `EditorUserIdFK` (`lastEditedByUserId`),
  CONSTRAINT `CreatorUserIdFK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `EditorUserIdFK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
INSERT INTO `setting` VALUES (1,NULL,NULL,NULL,'vaccine.name-db.penta1','penta1',NULL,NULL,NULL),(2,NULL,NULL,NULL,'vaccine.name-db.penta2','penta2',NULL,NULL,NULL),(3,NULL,NULL,NULL,'vaccine.name-db.penta3','penta3',NULL,NULL,NULL),(4,NULL,NULL,NULL,'vaccine.name-db.bcg','bcg',NULL,NULL,NULL),(5,NULL,NULL,NULL,'vaccine.name-db.measles1','measles1',NULL,NULL,NULL),(6,NULL,NULL,NULL,'vaccine.name-db.measles2','measles2',NULL,NULL,NULL),(7,NULL,NULL,NULL,'vaccine.next-vaccine.penta1','penta2',NULL,NULL,NULL),(8,NULL,NULL,NULL,'vaccine.next-vaccine.penta2','penta3',NULL,NULL,NULL),(9,NULL,NULL,NULL,'vaccine.next-vaccine.bcg','penta1',NULL,NULL,NULL),(10,NULL,NULL,NULL,'vaccine.next-vaccine.measles1','measles2',NULL,NULL,NULL),(11,NULL,NULL,NULL,'vaccine.next-vaccine.penta3','measles1',NULL,NULL,NULL),(12,NULL,NULL,NULL,'vaccine.next-vaccine.measles2','n/a',NULL,NULL,NULL),(13,NULL,NULL,NULL,'project.start-date','2012-06-19',NULL,NULL,NULL),(14,NULL,NULL,NULL,'project.target-enrollments','15000',NULL,NULL,NULL),(15,NULL,NULL,NULL,'project.target-events','75000',NULL,NULL,NULL),(16,NULL,NULL,NULL,'reminder.pusher.push-cron','0 0 0/4 * * ?',NULL,NULL,NULL),(17,NULL,NULL,NULL,'reminder.updater.update-cron','0 0/5 * * * ?',NULL,NULL,NULL),(18,NULL,NULL,NULL,'notifier.reschedular.resch-cron','0 0 0/6 * * ?',NULL,NULL,NULL),(19,NULL,NULL,NULL,'response.reader.read-cron','0 0/5 * * * ?',NULL,NULL,NULL),(20,NULL,NULL,NULL,'reminder.default-reminder-text','baraey meherbani bataey gaey waqt pay bachay ko vacination lagwa len',NULL,NULL,NULL),(21,NULL,NULL,NULL,'cellnumber.number-length-without-zero','10',NULL,NULL,NULL),(22,NULL,NULL,NULL,'downloadable.report-path','/usr/share/tomcat6/reportsunfepi/',NULL,NULL,NULL),(23,NULL,NULL,NULL,'incentive.vaccinator-lottery.interval','14',NULL,NULL,NULL),(24,NULL,NULL,NULL,'incentive.vaccinator-lottery.time','18:00:00',NULL,NULL,NULL),(25,NULL,NULL,NULL,'incentive.storekeeper-lottery.interval','14',NULL,NULL,NULL),(26,NULL,NULL,NULL,'incentive.storekeeper-incentive.time','18:00:00',NULL,NULL,NULL),(27,NULL,NULL,NULL,'admin.email-address','maimoona.kausar@irdinformatics.org',NULL,NULL,NULL),(28,NULL,NULL,NULL,'mobile.app.version','1.2.0',NULL,NULL,NULL),(29,NULL,NULL,NULL,'arm.arm1.max-records','15000',NULL,NULL,NULL),(30,NULL,NULL,NULL,'arm.arm2.max-records','15000',NULL,NULL,NULL);
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:18:35
