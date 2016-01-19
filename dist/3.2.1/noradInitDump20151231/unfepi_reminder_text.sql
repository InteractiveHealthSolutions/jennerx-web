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
-- Table structure for table `reminder_text`
--

DROP TABLE IF EXISTS `reminder_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reminder_text` (
  `reminderId` smallint(6) NOT NULL,
  `reminderText` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  KEY `FKC71AB4DAC5416964` (`reminderId`),
  CONSTRAINT `FKC71AB4DAC5416964` FOREIGN KEY (`reminderId`) REFERENCES `reminder` (`reminderId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reminder_text`
--

LOCK TABLES `reminder_text` WRITE;
/*!40000 ALTER TABLE `reminder_text` DISABLE KEYS */;
INSERT INTO `reminder_text` VALUES (2,'Apnay phool jaise bachay ki hifazat karein, [[Child.FirstName]] [[Child.LastName]] ka agla hifazati tika aaj kay din lagna hai. Mazid maloomat k lye 03158225705 par call karen (Zindagi Mahfooz Project)'),(1,'Waqt pay hifazati teekay lagwa kar apnay bachon ko beemarion se bachein, [[Child.FirstName]] [[Child.LastName]] ka agla hifazati tika kal hai. Mazid maloomat k lye 03158225705 par call karen (Zindagi Mahfooz Project)'),(3,'Apnay pyary bachon ko wqt py hifazati tikay lagwa kr beemarion se mehfooz rakhein. [[Child.FirstName]] [[Child.LastName]] ka hifazati tika pichly [[Vaccination.Day]] ko lagna tha, unhain tikay k liay jald EPI center laein. Mazid maloomat k lye 03158225705 par call karen(Zindagi Mahfooz Project)'),(4,'[[Child.FirstName]] [[Child.LastName]] ko [[Vaccination.Vaccine]] ka hifazati teeka lagnay per aapnay [[Incentive.Amount]] jeetay hain. Yeh raqam kareebi EasyPaisa shop se wasool ki jasakti hai. apko 3 din mein SMS ke zariay pesay wasool karnay ke liye mazeed maloomat di jai gi'),(4,'[[Child.FirstName]] [[Child.LastName]] ko [[Vaccination.Vaccine]] ka hifazati teeka lagnay per aapnay [[Incentive.Amount]] jeetay hain. Yeh raqam kareebi EasyPaisa shop se wasool ki jasakti hai. apko 3 din mein SMS ke zariay pesay wasool karnay ke liye mazeed maloomat di jai gi'),(5,'[[Child.FirstName]] [[Child.LastName]] ko [[Vaccination.Vaccine]] ka hifazati teeka lagnay per aapnay [[Incentive.Amount]] jeetay hain. Yeh raqam EasyPaisa ke zariye wasool karnay ke liye apna CNIC number iss number per SMS karein.');
/*!40000 ALTER TABLE `reminder_text` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:56:34
