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
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `roleId` smallint(6) NOT NULL,
  `permissionId` smallint(6) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`),
  KEY `FKBD40D53895E72B9E` (`permissionId`),
  KEY `FKBD40D538C344B2EC` (`roleId`),
  CONSTRAINT `FKBD40D53895E72B9E` FOREIGN KEY (`permissionId`) REFERENCES `permission` (`permissionId`),
  CONSTRAINT `FKBD40D538C344B2EC` FOREIGN KEY (`roleId`) REFERENCES `role` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (5,171),(6,171),(7,171),(8,171),(12,171),(5,172),(6,172),(7,172),(8,172),(12,172),(5,173),(6,173),(7,173),(8,173),(12,173),(5,174),(6,174),(7,174),(8,174),(12,174),(5,175),(5,176),(6,176),(5,177),(5,178),(6,178),(7,178),(8,178),(5,179),(5,180),(6,180),(7,180),(5,181),(6,181),(7,181),(5,182),(6,182),(7,182),(5,183),(5,184),(6,184),(7,184),(5,185),(6,185),(7,185),(5,186),(6,186),(7,186),(5,187),(6,187),(7,187),(8,187),(12,187),(5,188),(6,188),(7,188),(5,189),(6,189),(7,189),(5,190),(6,190),(7,190),(5,191),(6,191),(7,191),(5,192),(5,193),(6,193),(7,193),(8,193),(12,193),(13,193),(5,194),(6,194),(7,194),(8,194),(12,194),(13,194),(5,195),(6,195),(7,195),(8,195),(12,195),(13,195),(5,196),(6,196),(7,196),(8,196),(12,196),(13,196),(5,197),(6,197),(7,197),(8,197),(12,197),(13,197),(5,198),(6,198),(7,198),(8,198),(12,198),(13,198),(5,199),(6,199),(7,199),(8,199),(12,199),(13,199),(5,200),(5,201),(6,201),(5,202),(6,202),(7,202),(8,202),(12,202),(13,202),(5,203),(6,203),(5,204),(6,204),(7,204),(5,205),(6,205),(7,205),(5,206),(6,206),(7,206),(5,207),(6,207),(5,208),(6,208),(5,209),(6,209),(7,209),(5,210),(6,210),(7,210),(5,211),(6,211),(7,211),(13,211),(5,212),(6,212),(7,212),(8,212),(12,212),(13,212),(5,213),(6,213),(7,213),(5,214),(6,214),(7,214),(5,215),(6,215),(7,215),(5,216),(5,217),(5,218),(6,218),(7,218),(5,219),(6,219),(7,219),(5,220),(6,220),(7,220),(13,220),(5,221),(6,221),(7,221),(13,221),(5,222),(6,222),(7,222),(5,223),(6,223),(7,223),(5,224),(6,224),(7,224),(5,225),(6,225),(7,225),(5,226),(6,226),(7,226),(5,227),(6,227),(7,227),(5,228),(6,228),(7,228),(5,229),(6,229),(5,230),(6,230),(7,230),(5,231),(6,231),(7,231),(8,231),(9,231),(12,231),(13,231),(5,232),(6,232),(7,232),(8,232),(9,232),(12,232),(13,232),(5,233),(6,233),(7,233),(8,233),(9,233),(12,233),(13,233),(5,234),(6,234),(7,234),(9,234),(5,235),(6,235),(7,235),(8,235),(9,235),(12,235),(13,235),(5,236),(6,236),(7,236),(8,236),(9,236),(12,236),(13,236),(5,237),(6,237),(7,237),(8,237),(9,237),(12,237),(13,237),(14,237),(5,238),(6,238),(7,238),(8,238),(9,238),(13,238),(5,239),(6,239),(7,239),(8,239),(9,239),(12,239),(13,239),(5,240),(6,240),(7,240),(9,240),(5,241),(6,241),(7,241),(9,241),(5,242),(6,242),(7,242),(8,242),(9,242),(13,242),(5,243),(6,243),(9,243),(5,244),(6,244),(7,244),(9,244),(5,245),(6,245),(7,245),(9,245),(5,246),(6,246),(7,246),(9,246),(5,247),(6,247),(7,247),(9,247),(5,248),(6,248),(7,248),(9,248),(5,249),(6,249),(7,249),(9,249),(13,249),(5,250),(6,250),(7,250),(8,250),(9,250),(12,250),(5,251),(6,251),(7,251),(8,251),(9,251),(12,251),(13,251),(5,252),(6,252),(7,252),(8,252),(9,252),(12,252),(13,252),(5,253),(6,253),(7,253),(9,253),(5,254),(6,254),(7,254),(8,254),(9,254),(12,254),(13,254),(5,255),(6,255),(7,255),(8,255),(9,255),(12,255),(13,255),(5,256),(6,256),(7,256),(8,256),(9,256),(12,256),(13,256),(5,257),(6,257),(7,257),(8,257),(9,257),(12,257),(13,257),(5,258),(6,258),(7,258),(9,258),(5,259),(6,259),(7,259),(9,259),(5,260),(6,260),(7,260),(9,260),(5,261),(6,261),(7,261),(9,261),(13,261),(5,262),(6,262),(7,262),(9,262),(13,262),(5,263),(6,263),(7,263),(8,263),(9,263),(12,263),(13,263),(5,264),(6,264),(7,264),(9,264),(5,265),(6,265),(7,265),(9,265),(5,266),(6,266),(7,266),(9,266),(5,267),(6,267),(5,268),(6,268),(7,268),(8,268),(12,268),(5,269),(6,269),(7,269),(8,269),(12,269),(5,270),(6,270),(5,271),(6,271),(5,272),(6,272),(7,272),(5,273),(6,273),(7,273),(5,274),(6,274),(7,274),(5,275),(6,275),(7,275),(5,276),(6,276),(7,276),(13,276),(5,277),(6,277),(7,277),(13,277),(5,278),(6,278),(7,278),(5,279),(6,279),(7,279),(5,280),(6,280),(7,280),(5,281),(6,281),(7,281),(5,282),(6,282),(7,282),(13,282),(5,283),(6,283),(7,283),(13,283),(5,284),(6,284),(7,284),(5,285),(6,285),(7,285),(5,286),(6,286),(7,286),(5,287),(6,287),(7,287),(5,288),(6,288),(7,288),(5,289),(6,289),(7,289),(5,290),(6,290),(7,290),(5,291),(6,291),(7,291),(5,292),(6,292),(7,292),(5,293),(6,293),(7,293),(5,294),(6,294),(7,294),(5,295),(6,295),(7,295),(5,296),(6,296),(7,296),(5,297),(6,297),(7,297),(5,298),(6,298),(7,298),(5,299),(6,299),(7,299),(5,300),(6,300),(7,300),(5,301),(6,301),(7,301),(5,302),(6,302),(7,302),(5,303),(6,303),(7,303),(5,304),(6,304),(7,304),(5,305),(6,305),(7,305),(5,306),(6,306),(7,306),(5,307),(6,307),(7,307),(5,308),(6,308),(7,308),(5,309),(6,309),(7,309),(13,309),(5,310),(6,310),(7,310),(13,310),(5,311),(13,311),(14,311),(5,312),(7,312),(13,312),(5,313),(5,314),(5,315),(5,316),(5,317),(5,318),(5,319),(5,320),(5,321),(5,322),(5,323),(5,324),(5,325),(5,326),(5,327),(5,328);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09 10:19:29
