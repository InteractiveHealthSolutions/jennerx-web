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
-- Table structure for table `variablesetting`
--

DROP TABLE IF EXISTS `variablesetting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `variablesetting` (
  `rangeLower` float DEFAULT NULL,
  `rangeUpper` float DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `element` varchar(45) DEFAULT NULL,
  `value` varchar(2000) DEFAULT NULL,
  `uid` varchar(45) DEFAULT NULL,
  `serialId` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`serialId`),
  UNIQUE KEY `uid_UNIQUE` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variablesetting`
--

LOCK TABLES `variablesetting` WRITE;
/*!40000 ALTER TABLE `variablesetting` DISABLE KEYS */;
INSERT INTO `variablesetting` VALUES (0,1000,'EP_CHARGES',NULL,'15',NULL,1,NULL),(1001,2500,'EP_CHARGES',NULL,'30',NULL,2,NULL),(2501,4000,'EP_CHARGES',NULL,'45',NULL,3,NULL),(4001,6000,'EP_CHARGES',NULL,'60',NULL,4,NULL),(6001,8000,'EP_CHARGES',NULL,'75',NULL,5,NULL),(8001,10000,'EP_CHARGES',NULL,'90',NULL,6,NULL),(10001,13000,'EP_CHARGES',NULL,'105',NULL,7,NULL),(13001,16000,'EP_CHARGES',NULL,'120',NULL,8,NULL),(16001,20000,'EP_CHARGES',NULL,'135',NULL,9,NULL),(20001,50000,'EP_CHARGES',NULL,'150',NULL,10,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','BCG',NULL,11,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','PENTA1',NULL,12,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','PENTA2',NULL,13,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','PENTA3',NULL,14,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','MEASLES1',NULL,15,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','MEASLES2',NULL,16,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','OPV0',NULL,17,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','OPV1',NULL,18,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','OPV2',NULL,19,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','OPV3',NULL,20,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','TT1',NULL,21,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','TT2',NULL,22,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','TT3',NULL,23,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','TT4',NULL,24,NULL),(NULL,NULL,'ENCOUNTER_TYPE','DAILY_SUMMARY','TT5',NULL,25,NULL),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','Select \'[ID]\' \n  UNION \n  SELECT CONCAT(\'[\',id.identifier,\']\') ID FROM idmapper i \n  left join role r on i.roleId=r.roleId \n  left join identifier id on i.mappedId=id.mappedId and id.preferred \n  WHERE r.rolename = \'child\' AND NOT (id.identifier REGEXP \'^[0-9]{14}\');',NULL,26,'INVALID_CHILD_ID'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,27,'INVALID_CHILD_ID'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','SELECT COUNT(id.identifier) CountID \n FROM idmapper i \n  left join role r on i.roleId=r.roleId \n  left join identifier id on i.mappedId=id.mappedId and id.preferred \n  WHERE r.rolename = \'child\' AND NOT (id.identifier REGEXP \'^[0-9]{14}\');',NULL,28,'INVALID_CHILD_ID'),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','SELECT \'Program ID\', \'[Number]\',\'Number Type\' \n  UNION  \n  SELECT id.identifier,CONCAT(\'[\',c.number,\']\'),c.numberType FROM contactnumber c \n  JOIN idmapper i ON c.mappedid=i.mappedid \n  left join identifier id on i.mappedId=id.mappedId and id.preferred \n  WHERE (NOT c.number REGEXP \'03[0-9]{9}\' AND c.numberType=\'primary\') \n  OR (NOT c.number REGEXP \'0[0-9]{9,10}\' AND c.numberType=\'secondary\');',NULL,29,'INVALID_CONTACT_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,30,'INVALID_CONTACT_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','SELECT count(*) FROM contactnumber c \n  JOIN idmapper i ON c.mappedid=i.mappedid \n  left join identifier id on i.mappedId=id.mappedId and id.preferred \n  WHERE (NOT c.number REGEXP \'03[0-9]{9}\' AND c.numberType=\'primary\') \n  OR (NOT c.number REGEXP \'0[0-9]{9,10}\' AND c.numberType=\'secondary\');',NULL,31,'INVALID_CONTACT_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','SELECT \'Record Number\',\'Vaccination Status\',\'Child ID\',\'Vaccination DueDate\',\'Vaccination Date\',\'Vaccine ID\',\'Vaccine Name\',\'EPI Number\' \r   UNION \r   (SELECT CAST(v.vaccinationrecordnum AS CHAR(20)),v.vaccinationstatus,id.identifier,\r  CAST(v.vaccinationduedate AS CHAR(20)),CAST(v.vaccinationdate AS CHAR(20)),\r   CAST(v.vaccineid AS CHAR(5)),vc.name,v.epinumber \r   FROM vaccination v \r   LEFT JOIN idmapper i ON v.childid=i.mappedid \r   left join identifier id on i.mappedId=id.mappedId and id.preferred \r   LEFT JOIN vaccine vc ON v.vaccineId=vc.vaccineId \r   WHERE EXISTS (SELECT vaccinationrecordnum FROM vaccination WHERE vaccinationrecordnum=v.vaccinationrecordnum AND \r   (length(epinumber)<>8 OR epiNumber NOT LIKE \'201%\') AND vaccinationstatus = \'vaccinated\'));',NULL,32,'INVALID_EPI_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,33,'INVALID_EPI_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','SELECT COUNT(*)  \r FROM vaccination v \r WHERE EXISTS (SELECT vaccinationrecordnum FROM vaccination WHERE vaccinationrecordnum=v.vaccinationrecordnum AND \r (length(epinumber)<>8 OR epiNumber NOT LIKE \'201%\') AND vaccinationstatus = \'vaccinated\')',NULL,34,'INVALID_EPI_NUMBER'),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','select \'Vaccination Record Num\', \'Child ID (only for DMU)\',\'Child Program ID\',\'Created Date\',\n  \'Vaccine ID\',\'Vaccine Name\',\'Vaccination Duedate\',\'Vaccination Date\',\'Vaccination Status\',\'Data Entry By\',\'Is First Vaccination?\' \n  UNION\n  (select CAST(v.vaccinationRecordNum AS CHAR(10)), CAST(v.childid AS CHAR(10)),chid.identifier,CAST(v.createdDate AS CHAR(20)),\n  CAST(v.vaccineId AS CHAR(20)),vac.name,CAST(v.vaccinationDuedate AS CHAR(20)),CAST(v.vaccinationDate AS CHAR(20)),\n  v.vaccinationStatus,u.username DataEntryBy,CAST(v.isFirstVaccination AS CHAR(10)) \n  from vaccination v left join idmapper i on v.childid=i.mappedid \n  left join user u on v.createdByUserId=u.mappedid \n  left join identifier chid on chid.mappedId=v.childId and chid.preferred \n  left join vaccine vac on v.vaccineid=vac.vaccineid \n  where exists (select childid from vaccination where childid=v.childid \n 		and vaccineId=v.vaccineid group by vaccineid having count(vaccinationRecordnum) > 1) \n  order by v.childid limit 0,10000);',NULL,35,'DUPLICATE_VACCINATION_RECORDS'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,36,'DUPLICATE_VACCINATION_RECORDS'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','select count(v.vaccinationRecordNum)\nfrom vaccination v \nwhere exists (select childid from vaccination where childid=v.childid and vaccineId=v.vaccineid group by vaccineid having count(vaccinationRecordnum) > 1)',NULL,37,'DUPLICATE_VACCINATION_RECORDS'),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','SELECT \'Program ID\', \'FirstName\',\'LastName\',\'Gender\',\'SecondaryContact\'  \nunion \nselect id.identifier, ch.firstName, ch.lastName, ch.gender, cs.number  from child ch \nleft join contactnumber cn on ch.mappedId=cn.mappedId and cn.numberType = \'primary\'\nleft join contactnumber cs on ch.mappedId=cs.mappedId and cs.numberType <> \'primary\'\nleft join identifier id on ch.mappedId=id.mappedId and id.preferred \nwhere cn.number is null or length(cn.number)=0;',NULL,38,'MISSING_PRIMARY_CONTACT'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,39,'MISSING_PRIMARY_CONTACT'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','select count(*)  from child ch \nleft join contactnumber cn on ch.mappedId=cn.mappedId and cn.numberType = \'primary\'\nleft join contactnumber cs on ch.mappedId=cs.mappedId and cs.numberType <> \'primary\'\nleft join identifier id on ch.mappedId=id.mappedId and id.preferred \nwhere cn.number is null or length(cn.number)=0;',NULL,40,'MISSING_PRIMARY_CONTACT'),(NULL,NULL,'DATA_INCONSISTENCY','DATA_QUERY','SELECT \'Program ID\', \'FirstName\',\'LastName\',\'Gender\',\'PrimaryContact\',\'SecondaryContact\' \nunion \nselect id.identifier, ch.firstName, ch.lastName, ch.gender, cn.number, cs.number \nfrom child ch \nleft join contactnumber cn on ch.mappedId=cn.mappedId and cn.numberType = \'primary\' \nleft join contactnumber cs on ch.mappedId=cs.mappedId and cs.numberType <> \'primary\' \nleft join identifier id on ch.mappedId=id.mappedId and id.preferred \nwhere ch.nic is null or length(ch.nic) < 12;',NULL,41,'MISSING_OR_INVALID_NIC'),(NULL,NULL,'DATA_INCONSISTENCY','DESCRIPTION','no desc',NULL,42,'MISSING_OR_INVALID_NIC'),(NULL,NULL,'DATA_INCONSISTENCY','ROW_COUNT_QUERY','select count(*) from child ch \nleft join contactnumber cn on ch.mappedId=cn.mappedId and cn.numberType = \'primary\' \nleft join contactnumber cs on ch.mappedId=cs.mappedId and cs.numberType <> \'primary\' \nleft join identifier id on ch.mappedId=id.mappedId and id.preferred \nwhere ch.nic is null or length(ch.nic) < 12;',NULL,43,'MISSING_OR_INVALID_NIC');
/*!40000 ALTER TABLE `variablesetting` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-31 20:57:21
