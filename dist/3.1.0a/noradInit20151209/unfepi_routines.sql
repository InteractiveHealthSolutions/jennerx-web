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
-- Dumping routines for database 'unfepi'
--
/*!50003 DROP FUNCTION IF EXISTS `GetAncestry` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GetAncestry`(GivenID INT) RETURNS varchar(1024) CHARSET latin1
    DETERMINISTIC
BEGIN
    DECLARE rv VARCHAR(1024);
    DECLARE cm CHAR(1);
    DECLARE ch INT;

    SET rv = '';
    SET cm = '';
    SET ch = GivenID;
    WHILE ch > 0 DO
        SELECT IFNULL(parentLocation,-1) INTO ch FROM
        (SELECT parentLocation FROM location WHERE locationId = ch) A;
        IF ch > 0 THEN
            SET rv = CONCAT(rv,cm,ch);
            SET cm = ',';
        END IF;
    END WHILE;
    RETURN rv;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `GetTree` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GetTree`(startLocationId INT) RETURNS varchar(1024) CHARSET latin1
    DETERMINISTIC
BEGIN

    DECLARE rv,q,queue,queue_children,queue_names VARCHAR(1024);
    DECLARE queue_length,pos INT;
    DECLARE LocID,front_loc VARCHAR(64);

    SET rv = '';

    SELECT locationId INTO LocID 
    FROM location 
    WHERE 
	CASE WHEN startLocationId IS NULL THEN locationId IS NULL ELSE locationId = startLocationId END ;

    IF ISNULL(LocID) THEN
        RETURN rv;
    END IF;

    SET queue = LocID;
    SET queue_length = 1;

    WHILE queue_length > 0 DO
        IF queue_length = 1 THEN
            SET front_loc = queue;
            SET queue = '';
        ELSE
            SET pos = LOCATE(',',queue);
            SET front_loc = LEFT(queue,pos - 1);
            SET q = SUBSTR(queue,pos + 1);
            SET queue = q;
        END IF;
        SET queue_length = queue_length - 1;
        SELECT IFNULL(qc,'') INTO queue_children
        FROM
        (
            SELECT GROUP_CONCAT(locationId) qc FROM location
            WHERE parentLocation = front_loc 
        ) A;
        SELECT IFNULL(qc,'') INTO queue_names
        FROM
        (
            SELECT GROUP_CONCAT(locationId) qc FROM location
            WHERE parentLocation = front_loc 
        ) A;
        IF LENGTH(queue_children) = 0 THEN
            IF LENGTH(queue) = 0 THEN
                SET queue_length = 0;
            END IF;
        ELSE
            IF LENGTH(rv) = 0 THEN
                SET rv = queue_names;
            ELSE
                SET rv = CONCAT(rv,',',queue_names);
            END IF;
            IF LENGTH(queue) = 0 THEN
                SET queue = queue_children;
            ELSE
                SET queue = CONCAT(queue,',',queue_children);
            END IF;
            SET queue_length = LENGTH(queue) - LENGTH(REPLACE(queue,',','')) + 1;
        END IF;
    END WHILE;

    RETURN rv;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ChildIncentiveRealizedCalculate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ChildIncentiveRealizedCalculate`(IN datefrom VARCHAR(20),IN dateto VARCHAR(20))
BEGIN

select a.armName, cl.amount AmountWon, 15 epCharges, cl.amount+15 amountDue, ch.nic CNIC, cn.number CellNumber, 
cl.incentiveDate IncentiveRealizedDate, cl.consumptionDate ConsumptionDate, cl.transactionDate TransactionDate, 
chid.identifier ProgramId, CONCAT(ch.firstName, ' ', IFNULL(ch.lastName,'')) ChildName, 
vcenid.identifier CenterId, vcen.fullName Center, cl.createdDate IncentiveRecordCreatedDate, 
cl.incentiveStatus IncentiveStatus, cl.childIncentiveId IncentiveRecordId, 
vc.name Vaccine, v.vaccinationDate VaccineDate, v.vaccinationStatus VaccineStatus, 
vtorid.identifier VaccinatorId, CONCAT(vtor.firstName, ' ',IFNULL(vtor.lastName, '')) Vaccinator 
from childincentive cl 
left join arm a on cl.armId = a.armId   
left join vaccination v on v.vaccinationRecordNum = cl.vaccinationRecordNum  
left join vaccine vc on v.vaccineId = vc.vaccineId 
left join vaccinator vtor on v.vaccinatorId = vtor.mappedId 
left join identifier vtorid on vtorid.mappedId = v.vaccinatorId and vtorid.preferred 
left join vaccinationcenter vcen on vcen.mappedId = v.vaccinationCenterId 
left join identifier vcenid on vcenid.mappedId = vcen.mappedId and vcenid.preferred  
left join child ch on ch.mappedId = v.childId 
left join identifier chid on chid.mappedId = ch.mappedId and chid.preferred  
left join contactnumber cn on cn.mappedId = ch.mappedId and cn.numberType='PRIMARY' and cn.telelineType = 'MOBILE' 
where ch.nic IS NOT NULL and length(ch.nic) > 10 and cn.number IS NOT NULL and length(cn.number) > 10  
and cl.hasWonIncentive and cl.incentiveStatus = 'AVAILABLE' and date(cl.createdDate) between datefrom and dateto;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DMP_MasterAll` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DMP_MasterAll`()
BEGIN 

DECLARE done INT DEFAULT FALSE;
DECLARE vi INT;
DECLARE bdg INT;
DECLARE vn VARCHAR(255);
DECLARE cVaccList CURSOR FOR (SELECT vaccineId, name, (SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
				WHEN gaptimeunit = 'week' THEN (7*value) 
				WHEN gaptimeunit = 'day' THEN (0*value) 
				WHEN gaptimeunit = 'year' THEN (365*value) 
				ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineId = vc.vaccineId) birthdategap 
		FROM vaccine vc 
		HAVING birthdategap IS NOT NULL ORDER BY vaccineId);
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

SET @vaccineInfoquery="";
SET @vaccineJoinQuery="";

OPEN cVaccList;

  read_loop: LOOP
    FETCH cVaccList INTO vi, vn, bdg;
    IF done THEN
      LEAVE read_loop;
    ELSE
		SET vn = UPPER(vn);
		SET @vaccineInfoquery = CONCAT(@vaccineInfoquery,"
 ",	vn,".vaccineName '",vn,"',
    ",vn,".vaccinationStatus '",vn,"VaccinationStatus',
	IF(DATEDIFF(ch.dateEnrolled, ",vn,".vaccinationDate) < 0, TRUE, FALSE) '",vn,"IsRetroData',
    ",vn,".vaccinationCenterPid '",vn,"CenterID',
    ",vn,".vaccinatorPid '",vn,"VaccinatorID',
    ",vn,".epiNumber '",vn,"EPINumber',
    ",vn,".vaccinationRecordNum '",vn,"VaccinationRecordNumber',
    ",vn,".vaccineId '",vn,"VaccineID',
    ",vn,".pcvGiven '",vn,"PCVGiven',
    ",vn,".polioVaccineGiven '",vn,"OPVGiven',
    ",vn,".vaccinationDuedate '",vn,"VaccinationDueDate',
    ",vn,".vaccinationDate '",vn,"VaccinationDate',
	",vn,".timelinessStatus '",vn,"Timeliness',
	",vn,".timelinessFactor '",vn,"TimelinessFactor',
   	",vn,".isFirstVaccination '",vn,"IsFirstVaccination',
	",vn,".vaccinationCreatedDate '",vn,"VaccinationDateCreated',
	",vn,".vaccinationDescription '",vn,"VaccinationAdditionalNote',
	",vn,".daysPastDueDate '",vn,"DaysPastDueDate',
    ",vn,".hasApprovedLottery '",vn,"ApprovedLottery',
    ",vn,".lotteryDate '",vn,"LotteryDate',
	",vn,".lotteryDataEntrySource '",vn,"LotteryDataEntrySource',
    ",vn,".amount '",vn,"Amount',
    ",vn,".verificationcode '",vn,"VerificationCode',
    ",vn,".codeStatus '",vn,"CodeStatus',
	",vn,".LotteryWonReminderStatus '",vn,"LotteryWonReminderStatus',
	",vn,".LotteryWonReminderDuedate '",vn,"LotteryWonReminderDuedate',
	",vn,".LotteryWonReminderSentDate '",vn,"LotteryWonReminderSentDate',
    ",vn,".transactionDate '",vn,"TransactionDate',
	",vn,".LotteryConsumedReminderStatus '",vn,"LotteryConsumedReminderStatus',
	",vn,".LotteryConsumedReminderDuedate '",vn,"LotteryConsumedReminderDuedate',
	",vn,".LotteryConsumedReminderSentDate '",vn,"LotteryConsumedReminderSentDate',
    ",vn,".consumptionDate '",vn,"ConsumptionDate',
    ",vn,".storekeeperPid '",vn,"StorekeeperID',
	",vn,".amount*0.4 '",vn,"VaccinatorIncentiveAmount',
	",vn,".amount*0.05 '",vn,"StorekeeperIncentiveAmount',
    ",vn,".lotteryDescription '",vn,"LotteryAdditionalNote',
	",vn,".VaccineReminder1Status '",vn,"VaccineReminder1Status',
	",vn,".VaccineReminder1Duedate '",vn,"VaccineReminder1DueDate',
	",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1SentDate',
    ",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1CreatedDate',
    ",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1CancelReason',
    ",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1Recipient',
    ",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1dayNumber',
	",vn,".VaccineReminder2Status '",vn,"VaccineReminder2Status',
	",vn,".VaccineReminder2Duedate '",vn,"VaccineReminder2DueDate',
	",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2SentDate',
    ",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2CreatedDate',
    ",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2CancelReason',
    ",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2Recipient',
    ",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2dayNumber',
	",vn,".VaccineReminder3Status '",vn,"VaccineReminder3Status',
	",vn,".VaccineReminder3Duedate '",vn,"VaccineReminder3DueDate',
	",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3SentDate',
    ",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3CreatedDate',
    ",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3CancelReason',
    ",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3Recipient',
    ",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3dayNumber',
	",vn,".responseCount  '",vn,"ResponseCount',
	",vn,".responseText '",vn,"ResponseText',");
	
		SET @vaccineJoinQuery = CONCAT(@vaccineJoinQuery," ","
	LEFT JOIN _tmptmasterallCsvVaccination 
	",vn," ON ch.mappedId=",vn,".childId AND ",vn,".vaccineId=",vi);
	
	END IF;
  END LOOP;

CLOSE cVaccList;

SET group_concat_max_len=20000;

DROP TABLE IF EXISTS _resphelper;

create table _resphelper (INDEX(mappedId), INDEX(eventId), INDEX(eventClass))
SELECT mappedId, eventId, eventClass, count(*) responseCount, group_concat('[', responseDate, ': ', REPLACE(responseBody,'"','`'), '] /') responseText FROM response resp WHERE eventId IS NOT NULL GROUP BY IF(eventClass LIKE '%.class',REPLACE(eventClass,'.class',''),eventClass),eventId ;

DROP TABLE IF EXISTS _encounterhelper;

CREATE TABLE _encounterhelper (INDEX(p1id), INDEX(createdByUserId), INDEX(element), INDEX(value))  
	SELECT e.*, er.element, er.value, er.groupName, er.orderAs, er.displayName 
	FROM encounter e JOIN encounterresults er USING(p1id, p2id,encounterId) 
	WHERE e.encounterType IN ('ENROLLMENT','FOLLOWUP','LOTTERY_GEN');

-- DROP TEMPORARY TABLE IF EXISTS
DROP TABLE IF EXISTS _tmptmasterallCsvVaccination;

-- CREATE TEMPORARY TABLE TO HOLD VACCINATIONS AND ALL OTHER STUFF ASSOCIATED WITH IT.. (To avoid derived tables)
-- Would be dropped in the end
CREATE TABLE _tmptmasterallCsvVaccination (INDEX(vaccinationrecordnum), INDEX(childId)) 
AS (SELECT v.childId, v.vaccinationrecordnum, v.vaccinationDuedate, v.vaccinationDate, v.vaccinationStatus, 
	v.timelinessStatus, v.timelinessFactor, IF(v.vaccinationDate IS NOT NULL, '', DATEDIFF(CURDATE(), v.vaccinationDuedate)) daysPastDueDate, 
	v.epiNumber, v.nextAssignedDate, v.createdDate 'vaccinationCreatedDate', v.description 'vaccinationDescription',
	ivcen.identifier 'vaccinationCenterPid', ivac.identifier 'vaccinatorPid', v.vaccineId, 
	vc.name 'vaccineName', v.hasApprovedLottery, v.polioVaccineGiven, v.pcvGiven, v.isFirstVaccination, 
    ev.dataEntrySource vaccinationDataEntrySource, 
	chl.amount, chl.code verificationCode, chl.codeStatus, chl.consumptionDate, chl.lotteryDate, 
	istk.identifier 'storekeeperPid', chl.transactionDate, chl.description 'lotteryDescription',
	el.dataEntrySource lotteryDataEntrySource, 
	vsms1.reminderStatus VaccineReminder1Status, vsms1.dueDate VaccineReminder1Duedate, vsms1.sentDate VaccineReminder1SentDate, 
	vsms1.createdDate VaccineReminder1CreatedDate, vsms1.smsCancelReason VaccineReminder1CancelReason, vsms1.text VaccineReminder1Text, 
	vsms1.referenceNumber VaccineReminder1ReferenceNumber, vsms1.recipient VaccineReminder1Recipient, vsms1.dayNumber VaccineReminder1dayNumber ,
	vsms1.reminderId VaccineReminder1ReminderId,  
	
	vsms2.reminderStatus VaccineReminder2Status, vsms2.dueDate VaccineReminder2Duedate, vsms2.sentDate VaccineReminder2SentDate, 
	vsms2.createdDate VaccineReminder2CreatedDate, vsms2.smsCancelReason VaccineReminder2CancelReason, vsms2.text VaccineReminder2Text, 
	vsms2.referenceNumber VaccineReminder2ReferenceNumber, vsms2.recipient VaccineReminder2Recipient, vsms2.dayNumber VaccineReminder2dayNumber ,
	vsms2.reminderId VaccineReminder2ReminderId,  
	
	vsms3.reminderStatus VaccineReminder3Status, vsms3.dueDate VaccineReminder3Duedate, vsms3.sentDate VaccineReminder3SentDate, 
	vsms3.createdDate VaccineReminder3CreatedDate, vsms3.smsCancelReason VaccineReminder3CancelReason, vsms3.text VaccineReminder3Text, 
	vsms3.referenceNumber VaccineReminder3ReferenceNumber, vsms3.recipient VaccineReminder3Recipient, vsms3.dayNumber VaccineReminder3dayNumber ,
	vsms3.reminderId VaccineReminder3ReminderId,  
	
	lwonsms.reminderStatus LotteryWonReminderStatus, lwonsms.dueDate LotteryWonReminderDuedate, lwonsms.sentDate LotteryWonReminderSentDate, 
	lwonsms.createdDate LotteryWonReminderCreatedDate, lwonsms.smsCancelReason LotteryWonReminderCancelReason, lwonsms.text LotteryWonReminderText, 
	lwonsms.referenceNumber LotteryWonReminderReferenceNumber, lwonsms.recipient LotteryWonReminderRecipient, lwonsms.dayNumber LotteryWonReminderdayNumber ,
	lwonsms.reminderId LotteryWonReminderReminderId, 
	
	lconsms.reminderStatus LotteryConsumedReminderStatus, lconsms.dueDate LotteryConsumedReminderDuedate, lconsms.sentDate LotteryConsumedReminderSentDate, 
	lconsms.createdDate LotteryConsumedReminderCreatedDate, lconsms.smsCancelReason LotteryConsumedReminderCancelReason, lconsms.text LotteryConsumedReminderText, 
	lconsms.referenceNumber LotteryConsumedReminderReferenceNumber, lconsms.recipient LotteryConsumedReminderRecipient, lconsms.dayNumber LotteryConsumedReminderdayNumber ,
	lconsms.reminderId LotteryConsumedReminderReminderId, 
	resp.responseCount, resp.responseText 
	FROM vaccination v 
	LEFT JOIN vaccine vc ON v.vaccineid=vc.vaccineId 
	LEFT JOIN idmapper idvcen ON v.vaccinationCenterId=idvcen.mappedId 
	LEFT JOIN identifier ivcen ON idvcen.mappedId = ivcen.mappedId AND ivcen.preferred = TRUE 
	LEFT JOIN idmapper idvac ON v.vaccinatorId = idvac.mappedId 
	LEFT JOIN identifier ivac ON idvac.mappedId = ivac.mappedId AND ivac.preferred = TRUE 
	LEFT JOIN childlottery chl ON v.vaccinationRecordNum = chl.vaccinationRecordNum 
	LEFT JOIN idmapper idstk ON chl.storekeeperId = idstk.mappedId 
	LEFT JOIN identifier istk ON idstk.mappedId = istk.mappedId AND istk.preferred = TRUE 
	LEFT JOIN remindersms vsms1 ON v.vaccinationRecordNum = vsms1.vaccinationRecordNum AND vsms1.reminderId=1
	LEFT JOIN remindersms vsms2 ON v.vaccinationRecordNum = vsms2.vaccinationRecordNum AND vsms1.reminderId=2
	LEFT JOIN remindersms vsms3 ON v.vaccinationRecordNum = vsms3.vaccinationRecordNum AND vsms1.reminderId=3
	LEFT JOIN remindersms lwonsms ON v.vaccinationRecordNum = lwonsms.vaccinationRecordNum AND lwonsms.reminderId=4
	LEFT JOIN remindersms lconsms ON v.vaccinationRecordNum = lconsms.vaccinationRecordNum AND lconsms.reminderId=5
	LEFT JOIN _resphelper resp ON resp.eventId = v.vaccinationRecordNum AND resp.eventClass LIKE '%Vaccination%' 
	LEFT JOIN _encounterhelper el ON el.p1id=v.childId AND el.createdByUserId = chl.createdByUserId 
			AND (  (el.element LIKE 'LOTTERY_%VACCINATION_RECORD_NUM' AND el.value=v.vaccinationRecordNum) 
			OR (el.element LIKE 'VACCINE_NAME_RECEIVED' AND el.value=vc.name))
	LEFT JOIN _encounterhelper ev ON ev.p1id=v.childId  
			AND (  (ev.element LIKE CONCAT('VACCINE_%',vc.name,'_',v.vaccineId,'%VACCINATION_STATUS') AND ev.value=v.vaccinationStatus) 
			OR (ev.element LIKE 'VACCINE_NAME_RECEIVED' AND ev.value=vc.name)) 

);

-- Drop master csv table of previous dump to create new table with updated data
DROP TABLE IF EXISTS dmp_master_all;

SET @generalInfoquery = CONCAT("
CREATE TABLE dmp_master_all 
SELECT 
    chid.identifier ChildId,
    chidloc.fullName ChildIdLocation, 
	choldid.identifier OLD_CHILD_ID, 
    enrv.epiNumber EnrollmentEPInumber,
    ch.mappedId MappedId,
    CONCAT(ch.firstName, ' ',IFNULL(ch.lastName, '')) ChildFullName,",
    -- ch.lastName 'ChildLastName',
    "CONCAT(ch.fatherFirstName, ' ', IFNULL(ch.fatherLastName,'')) FatherFullName,",
    -- ch.fatherLastName 'FatherLastName',
    "ch.gender Gender,
    ch.birthdate Birthdate,
    ch.estimatedBirthdate IsEstimatedBirthdate,
    ch.dateEnrolled DateEnrolled, 
    cp.number PrimaryContact,
    cs.number SecondaryContact, 
	ad.addHouseNumber HouseNumber, 
	ad.addStreet Street, 
	ad.addSector Sector, 
	ad.addColony Colony, 
	twn.fullName Town, 
	uc.fullName UC, 
	ad.addLandmark Landmark, 
	cty.fullName City,
    ch.status EnrollmentStatus,
    ch.terminationDate TerminationDate, 
    ch.terminationReason TerminationReason, 
    ch.createdDate DateChildCreated, 
    CONCAT(chcreator.firstName, ' ',IFNULL(chcreator.lastName, '')) ChildCreatorFullName,
    ch.lastEditedDate DateChildLastEdited, 
    CONCAT(cheditor.firstName, ' ',IFNULL(cheditor.lastName, '')) ChildEditorFullName,
    ch.description AdditionalNoteOnChild, 
    enrv.vaccinationCenterPid EnrollmentCenter,
    enrv.vaccinationRecordNum EnrollmentVaccinationRecordNumber,
    enrv.vaccineId EnrollmentVaccineID,
    enrv.vaccineName 'EnrollmentVaccineName',
	prf.hasApprovedReminders ApprovedReminders, 
    prf.datePreferenceChanged DateOfPreference, 
    prf.createdDate DatePreferenceCreated, 
    CONCAT(prfcreator.firstName, ' ',IFNULL(prfcreator.lastName, '')) PreferenceCreatorFullName,");

SET @generalInfoJoinQuery = CONCAT(" FROM child ch 
left join address ad on ch.mappedId = ad.mappedId 
left join location cty on ad.cityId = cty.locationId 
left join location twn on ad.addTown = twn.locationId 
left join location uc on ad.addUc = uc.locationId ",
-- GET one contact number for each type primary and secondary
"
LEFT JOIN contactnumber cp ON ch.mappedId=cp.mappedId AND cp.numberType='PRIMARY' AND cp.contactNumberId = (SELECT MAX(contactNumberId) FROM contactnumber WHERE mappedId=cp.mappedId AND numberType=cp.numberType) 
LEFT JOIN contactnumber cs ON ch.mappedId=cs.mappedId AND cs.numberType='SECONDARY' AND cs.contactNumberId = (SELECT MAX(contactNumberId) FROM contactnumber WHERE mappedId=cs.mappedId AND numberType=cs.numberType) 
LEFT JOIN identifier chid ON chid.mappedId = ch.mappedId AND chid.preferred = TRUE 
LEFT JOIN location chidloc ON chid.locationId = chidloc.locationId  
LEFT JOIN identifier choldid ON choldid.mappedId = ch.mappedId AND choldid.identifierId <> chid.identifierId 
LEFT JOIN user chcreator ON chcreator.mappedId = ch.createdByUserId   
LEFT JOIN user cheditor ON cheditor.mappedId = ch.lastEditedByUserId   
",
-- Get the latest preference of reminders
"LEFT JOIN lotterysms prf ON prf.mappedid=ch.mappedid AND prf.serialNumber = (SELECT serialNumber FROM lotterysms WHERE mappedid=ch.mappedid 
			ORDER BY datepreferencechanged DESC, createdDate DESC LIMIT 1) 
LEFT JOIN user prfcreator ON prfcreator.mappedId = prf.createdByUserId   
",
-- ENROLMENT  - enrollment vaccination would have child and vaccination, vaccineId same and records would have isFirstVaccination TRUE
"LEFT JOIN _tmptmasterallCsvVaccination  
	enrv ON ch.mappedId=enrv.childId AND ch.enrollmentVaccineId=enrv.vaccineId AND enrv.isFirstVaccination = true ");

-- Remove extra comma at the end of list
SET @vaccineInfoquery = substring(@vaccineInfoquery,1,length(@vaccineInfoquery)-1);

SET @masterquery = CONCAT(@generalInfoquery," ", @vaccineInfoquery, " ", @generalInfoJoinQuery, " ", @vaccineJoinQuery, " LIMIT 0,1000000000; ");

PREPARE stmt1 FROM @masterquery;
EXECUTE stmt1; 
DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DMP_MasterEPIData` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DMP_MasterEPIData`()
BEGIN 

DECLARE done INT DEFAULT FALSE;
DECLARE vi INT;
DECLARE bdg INT;
DECLARE vn VARCHAR(255);
DECLARE cVaccList CURSOR FOR (SELECT vaccineId, name, (SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
				WHEN gaptimeunit = 'week' THEN (7*value) 
				WHEN gaptimeunit = 'day' THEN (0*value) 
				WHEN gaptimeunit = 'year' THEN (365*value) 
				ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineId = vc.vaccineId) birthdategap 
		FROM vaccine vc 
		HAVING birthdategap IS NOT NULL ORDER BY vaccineId);
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

SET @vaccineInfoquery="";
SET @vaccineJoinQuery="";

OPEN cVaccList;

  read_loop: LOOP
    FETCH cVaccList INTO vi, vn, bdg;
    IF done THEN
      LEAVE read_loop;
    ELSE
		SET vn = UPPER(vn);
		SET @vaccineInfoquery = CONCAT(@vaccineInfoquery,"
 ",	vn,".vaccineName '",vn,"',
    ",vn,".vaccinationStatus '",vn,"VaccinationStatus',
	IF(DATEDIFF(ch.dateEnrolled, ",vn,".vaccinationDate) < 0, TRUE, FALSE) '",vn,"IsRetroData',
    ",vn,".vaccinationCenterPid '",vn,"CenterID',
    ",vn,".vaccinatorPid '",vn,"VaccinatorID',
    ",vn,".epiNumber '",vn,"EPINumber',
    ",vn,".vaccinationRecordNum '",vn,"VaccinationRecordNumber',
    ",vn,".vaccineId '",vn,"VaccineID',
    ",vn,".vaccinationDuedate '",vn,"VaccinationDueDate',
    ",vn,".vaccinationDate '",vn,"VaccinationDate',
	",vn,".timelinessStatus '",vn,"Timeliness',
	",vn,".timelinessFactor '",vn,"TimelinessFactor',
    ",vn,".hasApprovedLottery '",vn,"ApprovedLottery',
    ",vn,".incentiveDate '",vn,"IncentiveDate',
    ",vn,".amount '",vn,"Amount',
    ",vn,".incentiveStatus '",vn,"IncentiveStatus',
    ",vn,".transactionDate '",vn,"TransactionDate',
    ",vn,".consumptionDate '",vn,"ConsumptionDate',
	",vn,".VaccineReminder1Status '",vn,"VaccineReminder1Status',
	",vn,".VaccineReminder1Recipient '",vn,"VaccineReminder1Recipient',
	",vn,".VaccineReminder1Duedate '",vn,"VaccineReminder1DueDate',
	",vn,".VaccineReminder1Sentdate '",vn,"VaccineReminder1SentDate',
	",vn,".VaccineReminder2Status '",vn,"VaccineReminder2Status',
	",vn,".VaccineReminder2Recipient '",vn,"VaccineReminder2Recipient',
	",vn,".VaccineReminder2Duedate '",vn,"VaccineReminder2DueDate',
	",vn,".VaccineReminder2Sentdate '",vn,"VaccineReminder2SentDate',
	",vn,".VaccineReminder3Status '",vn,"VaccineReminder3Status',
	",vn,".VaccineReminder3Recipient '",vn,"VaccineReminder3Recipient',
	",vn,".VaccineReminder3Duedate '",vn,"VaccineReminder3DueDate',
	",vn,".VaccineReminder3Sentdate '",vn,"VaccineReminder3SentDate',	
	",vn,".responseCount  '",vn,"ResponseCount',
	",vn,".responseText '",vn,"ResponseText',");
	
		SET @vaccineJoinQuery = CONCAT(@vaccineJoinQuery," ","
	LEFT JOIN _tmptmasterepidataCsvVaccination 
	",vn," ON ch.mappedId=",vn,".childId AND ",vn,".vaccineId=",vi);
	
	END IF;
  END LOOP;

CLOSE cVaccList;

SET group_concat_max_len=20000;

DROP TABLE IF EXISTS _resphelper;

create table _resphelper (INDEX(mappedId), INDEX(eventId), INDEX(eventClass))
SELECT mappedId, eventId, eventClass, count(*) responseCount, group_concat('[', responseDate, ': ', REPLACE(responseBody,'"','`'), '] /') responseText FROM response resp WHERE eventId IS NOT NULL GROUP BY mappedid,eventId ;

-- DROP TEMPORARY TABLE IF EXISTS
DROP TABLE IF EXISTS _tmptmasterepidataCsvVaccination;

-- CREATE TEMPORARY TABLE TO HOLD VACCINATIONS AND ALL OTHER STUFF ASSOCIATED WITH IT.. (To avoid derived tables)
-- Would be dropped in the end
CREATE TABLE _tmptmasterepidataCsvVaccination (INDEX(vaccinationrecordnum), INDEX(childId), INDEX(vaccineId)) 
AS (SELECT v.childId, v.vaccinationrecordnum, v.vaccinationDuedate, v.vaccinationDate, v.vaccinationStatus, 
	v.timelinessStatus, v.timelinessFactor, IF(v.vaccinationDate IS NOT NULL, '', DATEDIFF(CURDATE(), v.vaccinationDuedate)) daysPastDueDate, 
	v.epiNumber, v.createdDate 'vaccinationCreatedDate',
	ivcen.identifier 'vaccinationCenterPid', ivac.identifier 'vaccinatorPid', v.vaccineId, 
	vc.name 'vaccineName', v.hasApprovedLottery, v.isFirstVaccination, chl.amount, chl.incentiveStatus, chl.consumptionDate, chl.incentiveDate, 
	chl.transactionDate,
	vsms1.reminderStatus VaccineReminder1Status, vsms1.dueDate VaccineReminder1Duedate, vsms1.sentDate VaccineReminder1SentDate, 
	vsms1.createdDate VaccineReminder1CreatedDate, vsms1.smsCancelReason VaccineReminder1CancelReason, vsms1.text VaccineReminder1Text, 
	vsms1.referenceNumber VaccineReminder1ReferenceNumber, vsms1.recipient VaccineReminder1Recipient, vsms1.dayNumber VaccineReminder1dayNumber ,
	vsms1.reminderId VaccineReminder1ReminderId,  
	
	vsms2.reminderStatus VaccineReminder2Status, vsms2.dueDate VaccineReminder2Duedate, vsms2.sentDate VaccineReminder2SentDate, 
	vsms2.createdDate VaccineReminder2CreatedDate, vsms2.smsCancelReason VaccineReminder2CancelReason, vsms2.text VaccineReminder2Text, 
	vsms2.referenceNumber VaccineReminder2ReferenceNumber, vsms2.recipient VaccineReminder2Recipient, vsms2.dayNumber VaccineReminder2dayNumber ,
	vsms2.reminderId VaccineReminder2ReminderId,  
	
	vsms3.reminderStatus VaccineReminder3Status, vsms3.dueDate VaccineReminder3Duedate, vsms3.sentDate VaccineReminder3SentDate, 
	vsms3.createdDate VaccineReminder3CreatedDate, vsms3.smsCancelReason VaccineReminder3CancelReason, vsms3.text VaccineReminder3Text, 
	vsms3.referenceNumber VaccineReminder3ReferenceNumber, vsms3.recipient VaccineReminder3Recipient, vsms3.dayNumber VaccineReminder3dayNumber ,
	vsms3.reminderId VaccineReminder3ReminderId,  
	
	incentsms.reminderStatus IncentiveReminderStatus, incentsms.dueDate IncentiveReminderDuedate, incentsms.sentDate IncentiveReminderSentDate, 
	incentsms.createdDate IncentiveReminderCreatedDate, incentsms.smsCancelReason IncentiveReminderCancelReason, incentsms.text IncentiveReminderText, 
	incentsms.referenceNumber IncentiveReminderReferenceNumber, incentsms.recipient IncentiveReminderRecipient, incentsms.dayNumber IncentiveReminderdayNumber ,
	incentsms.reminderId IncentiveReminderReminderId, 
	
	incentconsms.reminderStatus IncentiveConsumedReminderStatus, incentconsms.dueDate IncentiveConsumedReminderDuedate, incentconsms.sentDate IncentiveConsumedReminderSentDate, 
	incentconsms.createdDate IncentiveConsumedReminderCreatedDate, incentconsms.smsCancelReason IncentiveConsumedReminderCancelReason, incentconsms.text IncentiveConsumedReminderText, 
	incentconsms.referenceNumber IncentiveConsumedReminderReferenceNumber, incentconsms.recipient IncentiveConsumedReminderRecipient, incentconsms.dayNumber IncentiveConsumedReminderdayNumber ,
	incentconsms.reminderId IncentiveConsumedReminderReminderId, 
	resp.responseCount, resp.responseText 
	FROM vaccination v 
	LEFT JOIN vaccine vc ON v.vaccineid=vc.vaccineId 
	LEFT JOIN idmapper idvcen ON v.vaccinationCenterId=idvcen.mappedId 
	LEFT JOIN identifier ivcen ON idvcen.mappedId = ivcen.mappedId AND ivcen.preferred = TRUE 
	LEFT JOIN idmapper idvac ON v.vaccinatorId = idvac.mappedId 
	LEFT JOIN identifier ivac ON idvac.mappedId = ivac.mappedId AND ivac.preferred = TRUE 
	LEFT JOIN childincentive chl ON v.vaccinationRecordNum = chl.vaccinationRecordNum 
	LEFT JOIN remindersms vsms1 ON v.vaccinationRecordNum = vsms1.vaccinationRecordNum AND vsms1.reminderId=1
	LEFT JOIN remindersms vsms2 ON v.vaccinationRecordNum = vsms2.vaccinationRecordNum AND vsms1.reminderId=2
	LEFT JOIN remindersms vsms3 ON v.vaccinationRecordNum = vsms3.vaccinationRecordNum AND vsms1.reminderId=3
	LEFT JOIN remindersms incentsms ON v.vaccinationRecordNum = incentsms.vaccinationRecordNum AND incentsms.reminderId=4
	LEFT JOIN remindersms incentconsms ON v.vaccinationRecordNum = incentconsms.vaccinationRecordNum AND incentconsms.reminderId=5
	LEFT JOIN _resphelper resp ON resp.eventId = v.vaccinationRecordNum AND resp.eventClass LIKE '%Vaccination%' 
);

-- Drop master csv table of previous dump to create new table with updated data
DROP TABLE IF EXISTS dmp_master_epidata;

SET @generalInfoquery = CONCAT("
CREATE TABLE dmp_master_epidata 
SELECT 
    chid.identifier ChildId,
    enrv.epiNumber EnrollmentEPInumber,
    ch.mappedId MappedId,
    CONCAT(ch.firstName, ' ',IFNULL(ch.lastName, '')) ChildFullName,",
    -- ch.lastName 'ChildLastName',
    "CONCAT(ch.fatherFirstName, ' ', IFNULL(ch.fatherLastName,'')) FatherFullName,",
    -- ch.fatherLastName 'FatherLastName',
    "ch.gender Gender,
    ch.birthdate Birthdate,
    ch.estimatedBirthdate IsEstimatedBirthdate,
    ch.dateEnrolled DateEnrolled, 
    ch.nic CNIC,
    cp.number PrimaryContact,
    cs.number SecondaryContact, 
	ad.addHouseNumber HouseNumber, 
	ad.addStreet Street, 
	ad.addSector Sector, 
	ad.addColony Colony, 
	twn.fullName Town, 
	uc.fullName UC, 
	ad.addLandmark Landmark, 
	cty.fullName City,
    enrv.vaccinationCenterPid 'EnrollmentCenter',
    enrv.vaccinationRecordNum 'EnrollmentVaccinationRecordNumber',
    enrv.vaccineId 'EnrollmentVaccineID',
    enrv.vaccineName 'EnrollmentVaccineName',
	prf.hasApprovedReminders ApprovedReminders,");

SET @generalInfoJoinQuery = CONCAT(" FROM child ch 
left join address ad on ch.mappedId = ad.mappedId 
left join location cty on ad.cityId = cty.locationId 
left join location twn on ad.addTown = twn.locationId 
left join location uc on ad.addUc = uc.locationId ",
-- GET one contact number for each type primary and secondary
"
LEFT JOIN contactnumber cp ON ch.mappedId=cp.mappedId AND cp.numberType='PRIMARY' AND cp.contactNumberId = (SELECT MAX(contactNumberId) FROM contactnumber WHERE mappedId=cp.mappedId AND numberType=cp.numberType) 
LEFT JOIN contactnumber cs ON ch.mappedId=cs.mappedId AND cs.numberType='SECONDARY' AND cs.contactNumberId = (SELECT MAX(contactNumberId) FROM contactnumber WHERE mappedId=cs.mappedId AND numberType=cs.numberType) 
LEFT JOIN identifier chid ON chid.mappedId = ch.mappedId AND chid.preferred = TRUE 
LEFT JOIN identifier choldid ON choldid.mappedId = ch.mappedId AND choldid.identifierId <> chid.identifierId ",
-- Get the latest preference of reminders
"LEFT JOIN lotterysms prf ON prf.mappedid=ch.mappedid AND prf.serialNumber = (SELECT serialNumber FROM lotterysms WHERE mappedid=ch.mappedid 
			ORDER BY datepreferencechanged DESC, createdDate DESC LIMIT 1) ",
-- ENROLMENT  - enrollment vaccination would have child and vaccination, vaccineId same and records would have isFirstVaccination TRUE
"LEFT JOIN _tmptmasterepidataCsvVaccination  
	enrv ON ch.mappedId=enrv.childId AND ch.enrollmentVaccineId=enrv.vaccineId AND enrv.isFirstVaccination = true ");

-- Remove extra comma at the end of list
SET @vaccineInfoquery = substring(@vaccineInfoquery,1,length(@vaccineInfoquery)-1);

SET @masterquery = CONCAT(@generalInfoquery," ", @vaccineInfoquery, " ", @generalInfoJoinQuery, " ", @vaccineJoinQuery, " LIMIT 0,1000000000; ");

PREPARE stmt1 FROM @masterquery;
EXECUTE stmt1; 
DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SummaryEnrByCenterCohort` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SummaryEnrByCenterCohort`(IN centerIdFilterCommaSeparated VARCHAR(1000),
IN enrollmentDateFrom DATE, IN enrollmentDateTo DATE,
IN limitStart INT, IN limitEnd INT,IN sortName VARCHAR(1000),IN orderName VARCHAR(50))
BEGIN 

SET @filterQueryString = "";

IF centerIdFilterCommaSeparated IS NOT NULL AND TRIM(centerIdFilterCommaSeparated) <> "" 
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND ivc.identifier IN (",centerIdFilterCommaSeparated,") ");
END IF;

IF enrollmentDateFrom IS NOT NULL AND enrollmentDateTo IS NOT NULL  
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND c.dateEnrolled BETWEEN '", enrollmentDateFrom,"' AND '",enrollmentDateTo,"'");
END IF;

IF sortName IS NULL OR TRIM(sortName) = "" 
THEN 
	SET @sortQueryString = "";
ELSE 
	SET @sortQueryString = CONCAT(" ORDER BY ",sortName," ", orderName);
END IF;

SET @AllCentersQuery = concat(" select 'Total' centerProgramId, 
'All Centers' centerProgramName, 
totalEnrollments, 
bcgEnrollments, TRUNCATE(bcgEnrollments*100/totalEnrollments, 0) bcgEnrollmentPercent, 
p1Enrollments, TRUNCATE(p1Enrollments*100/totalEnrollments, 0) p1EnrollmentPercent, 
p2Enrollments, TRUNCATE(p2Enrollments*100/totalEnrollments, 0) p2EnrollmentPercent, 
p3Enrollments, TRUNCATE(p3Enrollments*100/totalEnrollments, 0) p3EnrollmentPercent, 
m1Enrollments, TRUNCATE(m1Enrollments*100/totalEnrollments, 0) m1EnrollmentPercent, 
m2Enrollments, TRUNCATE(m2Enrollments*100/totalEnrollments, 0) m2EnrollmentPercent 
from 
(
select 'Total' centerProgramId, 'All Centers' centerProgramName,
count(vaccinationRecordNum) totalEnrollments,
sum(case when  vaccineId=1 then 1 else 0 end) bcgEnrollments,
sum(case when  vaccineId=2 then 1 else 0 end) p1Enrollments,
sum(case when  vaccineId=3 then 1 else 0 end) p2Enrollments,
sum(case when  vaccineId=4 then 1 else 0 end) p3Enrollments,
sum(case when  vaccineId=5 then 1 else 0 end) m1Enrollments,
sum(case when  vaccineId=6 then 1 else 0 end) m2Enrollments 
from vaccinationcenter vcent 
left join idmapper idvc on vcent.mappedId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
left join vaccination v on ivc.mappedId=v.vaccinationCenterId 
left join child c on v.childid=c.mappedid 
where (c.enrollmentvaccineid = v.vaccineid OR v.isFirstVaccination = true) 
",@filterQueryString,"
) as e ");

SET @QueryString=concat("select ifnull(centerProgramId,'--- Not Found') centerProgramId, 
(case when centerProgramId is null then '--- Not Found' else centerProgramName end) centerProgramName, 
totalEnrollments, 
bcgEnrollments, TRUNCATE(bcgEnrollments*100/totalEnrollments, 0) bcgEnrollmentPercent, 
p1Enrollments, TRUNCATE(p1Enrollments*100/totalEnrollments, 0) p1EnrollmentPercent, 
p2Enrollments, TRUNCATE(p2Enrollments*100/totalEnrollments, 0) p2EnrollmentPercent, 
p3Enrollments, TRUNCATE(p3Enrollments*100/totalEnrollments, 0) p3EnrollmentPercent, 
m1Enrollments, TRUNCATE(m1Enrollments*100/totalEnrollments, 0) m1EnrollmentPercent, 
m2Enrollments, TRUNCATE(m2Enrollments*100/totalEnrollments, 0) m2EnrollmentPercent 
from 
(
select ivc.identifier centerProgramId, vcent.name centerProgramName,
count(vaccinationRecordNum) totalEnrollments,
sum(case when  vaccineId=1 then 1 else 0 end) bcgEnrollments,
sum(case when  vaccineId=2 then 1 else 0 end) p1Enrollments,
sum(case when  vaccineId=3 then 1 else 0 end) p2Enrollments,
sum(case when  vaccineId=4 then 1 else 0 end) p3Enrollments,
sum(case when  vaccineId=5 then 1 else 0 end) m1Enrollments,
sum(case when  vaccineId=6 then 1 else 0 end) m2Enrollments 
from vaccinationcenter vcent 
left join idmapper idvc on vcent.mappedId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
left join vaccination v on ivc.mappedId=v.vaccinationCenterId 
left join child c on v.childid=c.mappedid 
where (c.enrollmentvaccineid = v.vaccineid OR v.isFirstVaccination = true) ",
@filterQueryString,
" group by ivc.identifier ", @sortQueryString, " LIMIT ", limitStart, ",", limitEnd, 
" ) as e ", " UNION ",@AllCentersQuery); 

 -- select @QueryString, enrollmentDateFrom, enrollmentDateTo;
PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SummaryFollowupAgeAppropriate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SummaryFollowupAgeAppropriate`(IN centerIdFilterCommaSeparated VARCHAR(1000),
IN enrollmentDateFrom DATE, IN enrollmentDateTo DATE,
IN limitStart INT, IN limitEnd INT,IN sortName VARCHAR(1000),IN orderName VARCHAR(50))
BEGIN 

SET @filterQueryString = "";

IF centerIdFilterCommaSeparated IS NOT NULL AND TRIM(centerIdFilterCommaSeparated) <> "" 
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND ivc.identifier IN (",centerIdFilterCommaSeparated,") ");
END IF;

IF enrollmentDateFrom IS NOT NULL AND enrollmentDateTo IS NOT NULL  
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND c.dateEnrolled BETWEEN '", enrollmentDateFrom,"' AND '",enrollmentDateTo,"'");
END IF;


IF sortName IS NULL OR TRIM(sortName) = "" 
THEN 
    SET @sortQueryString = "";
ELSE 
    SET @sortQueryString = CONCAT(" ORDER BY ",sortName," ", orderName);
END IF;

SET @AllTotalQuery = concat(" SELECT 'ID' vaccineId, 'Total' cohort , inn.cohorttotal, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.due END) bcgdue,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.due END) penta1due,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.due END) penta2due,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.due END) penta3due,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.due END) measles1due,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.due END) measles2due, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.done END) bcgdone,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.done END) penta1done,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.done END) penta2done,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.done END) penta3done,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.done END) measles1done,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.done END) measles2done  
FROM (select vc.vaccineId , count(*) cohorttotal, 
count(DISTINCT env.vaccinationRecordNum) due,
-- sum(case when vc.vaccineId > env.vaccineId then 1 else 0 end) due, 
sum(case when v.vaccinationStatus in ('VACCINATED') then 1 else 0 end) done 
FROM child c 
CROSS JOIN vaccine vc 
LEFT JOIN vaccination env ON c.mappedId = env.childId AND env.vaccineId=c.enrollmentVaccineId AND vc.vaccineId > env.vaccineId AND isFirstVaccination 
LEFT JOIN vaccination v ON c.mappedId = v.childId AND vc.vaccineId=v.vaccineId AND v.vaccinationRecordNum <> env.vaccinationRecordNum 
			-- 	AND v.vaccinationRecordNum = (SELECT MAX(vaccinationRecordNum) FROM vaccination WHERE c.mappedId = childId AND vaccineId=vc.vaccineId)  
left join idmapper idvc on env.vaccinationCenterId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
",
@filterQueryString,
" 
AND DATEDIFF(CURDATE(), c.birthdate) > IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
     WHEN gaptimeunit = 'week' THEN (7*value) 
     WHEN gaptimeunit = 'day' THEN (0*value) 
     WHEN gaptimeunit = 'year' THEN (365*value) 
     ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=vc.vaccineId), 9999) 
GROUP BY vc.vaccineid) inn 
");

SET @QueryString=concat("SELECT vc.vaccineId vaccineId, vc.name cohort , inn.cohorttotal, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.due END) bcgdue,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.due END) penta1due,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.due END) penta2due,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.due END) penta3due,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.due END) measles1due,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.due END) measles2due, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.done END) bcgdone,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.done END) penta1done,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.done END) penta2done,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.done END) penta3done,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.done END) measles1done,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.done END) measles2done  
FROM vaccine vc 
LEFT JOIN (select vc.vaccineId , env.vaccineId envaccineid, count(*) cohorttotal, count(DISTINCT c.mappedId) due, 
sum(case when v.vaccinationStatus in ('VACCINATED') then 1 else 0 end) done 
FROM child c 
CROSS JOIN vaccine vc 
LEFT JOIN vaccination env ON c.mappedId = env.childId AND env.vaccineId=c.enrollmentVaccineId AND vc.vaccineId > env.vaccineId AND isFirstVaccination 
LEFT JOIN vaccination v ON c.mappedId = v.childId AND vc.vaccineId=v.vaccineId AND v.vaccinationRecordNum <> env.vaccinationRecordNum 
			-- 	AND v.vaccinationRecordNum = (SELECT MAX(vaccinationRecordNum) FROM vaccination WHERE c.mappedId = childId AND vaccineId=vc.vaccineId)  
left join idmapper idvc on env.vaccinationCenterId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
",
@filterQueryString,
" 
AND DATEDIFF(CURDATE(), c.birthdate) > IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
     WHEN gaptimeunit = 'week' THEN (7*value) 
     WHEN gaptimeunit = 'day' THEN (0*value) 
     WHEN gaptimeunit = 'year' THEN (365*value) 
     ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=vc.vaccineId), 9999) 
GROUP BY env.vaccineId, vc.vaccineid) inn ON vc.vaccineId = inn.envaccineid AND vc.vaccineId < inn.vaccineid
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
GROUP BY vc.vaccineId ", @sortQueryString, " UNION ", @AllTotalQuery); 

PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SummaryFollowupAgeAppropriateWRetro` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SummaryFollowupAgeAppropriateWRetro`(IN centerIdFilterCommaSeparated VARCHAR(1000),
IN enrollmentDateFrom DATE, IN enrollmentDateTo DATE,
IN limitStart INT, IN limitEnd INT,IN sortName VARCHAR(1000),IN orderName VARCHAR(50))
BEGIN 

SET @filterQueryString = "";

IF centerIdFilterCommaSeparated IS NOT NULL AND TRIM(centerIdFilterCommaSeparated) <> "" 
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND ivc.identifier IN (",centerIdFilterCommaSeparated,") ");
END IF;

IF enrollmentDateFrom IS NOT NULL AND enrollmentDateTo IS NOT NULL  
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND c.dateEnrolled BETWEEN '", enrollmentDateFrom,"' AND '",enrollmentDateTo,"'");
END IF;


IF sortName IS NULL OR TRIM(sortName) = "" 
THEN 
    SET @sortQueryString = "";
ELSE 
    SET @sortQueryString = CONCAT(" ORDER BY ",sortName," ", orderName);
END IF;

SET @AllTotalQuery = concat(" SELECT 'ID' vaccineId, 'Total' cohort , inn.cohorttotal, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.due END) bcgdue,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.due END) penta1due,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.due END) penta2due,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.due END) penta3due,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.due END) measles1due,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.due END) measles2due, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.done END) bcgdone,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.done END) penta1done,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.done END) penta2done,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.done END) penta3done,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.done END) measles1done,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.done END) measles2done  
FROM (select vc.vaccineId , count(*) cohorttotal, 
count(DISTINCT env.vaccinationRecordNum) due,
-- sum(case when vc.vaccineId > env.vaccineId then 1 else 0 end) due, 
sum(case when v.vaccinationStatus in ('VACCINATED') then 1 else 0 end) done 
FROM child c 
CROSS JOIN vaccine vc 
LEFT JOIN vaccination env ON c.mappedId = env.childId AND env.vaccineId=c.enrollmentVaccineId AND vc.vaccineId > env.vaccineId AND isFirstVaccination 
LEFT JOIN vaccination v ON c.mappedId = v.childId AND vc.vaccineId=v.vaccineId AND v.vaccinationRecordNum <> env.vaccinationRecordNum 
			-- 	AND v.vaccinationRecordNum = (SELECT MAX(vaccinationRecordNum) FROM vaccination WHERE c.mappedId = childId AND vaccineId=vc.vaccineId)  
left join idmapper idvc on env.vaccinationCenterId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
",
@filterQueryString,
" 
AND DATEDIFF(CURDATE(), c.birthdate) > IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
     WHEN gaptimeunit = 'week' THEN (7*value) 
     WHEN gaptimeunit = 'day' THEN (0*value) 
     WHEN gaptimeunit = 'year' THEN (365*value) 
     ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=vc.vaccineId), 9999) 
GROUP BY vc.vaccineid) inn 
");

SET @QueryString=concat("SELECT vc.vaccineId vaccineId, vc.name cohort , inn.cohorttotal, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.due END) bcgdue,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.due END) penta1due,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.due END) penta2due,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.due END) penta3due,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.due END) measles1due,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.due END) measles2due, 
MAX(CASE WHEN inn.vaccineId=1 THEN inn.done END) bcgdone,  
MAX(CASE WHEN inn.vaccineId=2 THEN inn.done END) penta1done,  
MAX(CASE WHEN inn.vaccineId=3 THEN inn.done END) penta2done,  
MAX(CASE WHEN inn.vaccineId=4 THEN inn.done END) penta3done,  
MAX(CASE WHEN inn.vaccineId=5 THEN inn.done END) measles1done,  
MAX(CASE WHEN inn.vaccineId=6 THEN inn.done END) measles2done  
FROM vaccine vc 
LEFT JOIN (select vc.vaccineId , env.vaccineId envaccineid, count(*) cohorttotal, count(DISTINCT c.mappedId) due, 
sum(case when v.vaccinationStatus in ('VACCINATED') then 1 else 0 end) done 
FROM child c 
CROSS JOIN vaccine vc 
LEFT JOIN vaccination env ON c.mappedId = env.childId AND env.vaccineId=c.enrollmentVaccineId AND isFirstVaccination 
LEFT JOIN vaccination v ON c.mappedId = v.childId AND vc.vaccineId=v.vaccineId  AND v.vaccinationRecordNum <> env.vaccinationRecordNum 
			-- 	AND v.vaccinationRecordNum = (SELECT MAX(vaccinationRecordNum) FROM vaccination WHERE c.mappedId = childId AND vaccineId=vc.vaccineId)  
left join idmapper idvc on env.vaccinationCenterId=idvc.mappedId 
left join identifier ivc on idvc.mappedId=ivc.mappedId AND ivc.preferred = TRUE 
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
",
@filterQueryString,
" 
AND DATEDIFF(CURDATE(), c.birthdate) > IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) 
     WHEN gaptimeunit = 'week' THEN (7*value) 
     WHEN gaptimeunit = 'day' THEN (0*value) 
     WHEN gaptimeunit = 'year' THEN (365*value) 
     ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=vc.vaccineId), 9999) 
GROUP BY env.vaccineId, vc.vaccineid) inn ON vc.vaccineId = inn.envaccineid 
WHERE vc.vaccineId IN (1,2,3,4,5,6) 
GROUP BY vc.vaccineId ", @sortQueryString, " UNION ", @AllTotalQuery); 

PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SummaryImmunizationByCenter` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SummaryImmunizationByCenter`(IN centerIdFilterCommaSeparated VARCHAR(1000),
IN vaccineDateFrom DATE, IN vaccineDateTo DATE,
IN limitStart INT, IN limitEnd INT,IN sortName VARCHAR(1000),IN orderName VARCHAR(50))
BEGIN 

SET @filterQueryString = "";

IF centerIdFilterCommaSeparated IS NOT NULL AND TRIM(centerIdFilterCommaSeparated) <> "" 
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND id.identifier IN (",centerIdFilterCommaSeparated,") ");
END IF;

IF vaccineDateFrom IS NOT NULL AND vaccineDateTo IS NOT NULL  
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND vc.vaccinationDate BETWEEN '", vaccineDateFrom,"' AND '",vaccineDateTo,"'");
END IF;


IF sortName IS NULL OR TRIM(sortName) = "" 
THEN 
    SET @sortQueryString = "";
ELSE 
    SET @sortQueryString = CONCAT(" ORDER BY ",sortName," ", orderName);
END IF;


SET @QueryString=concat("select id.identifier, name, 
count(*) total, 
sum(if(vc.vaccineId=1,1,0)) bcg,
sum(if(vc.vaccineId=2,1,0)) p1,
sum(if(vc.vaccineId=3,1,0)) p2,
sum(if(vc.vaccineId=4,1,0)) p3,
sum(if(vc.vaccineId=5,1,0)) m1,
sum(if(vc.vaccineId=6,1,0)) m2,
sum(if(vc.vaccineId=7,1,0)) opv0,
sum(if(vc.vaccineId=8,1,0)) opv1,
sum(if(vc.vaccineId=9,1,0)) opv2,
sum(if(vc.vaccineId=10,1,0)) opv3,
sum(if(vc.vaccineId=11,1,0)) pcv1,
sum(if(vc.vaccineId=12,1,0)) pcv2,
sum(if(vc.vaccineId=13,1,0)) pcv3
from vaccinationcenter v 
left join identifier id on v.mappedId=id.mappedId and id.preferred 
left join vaccination vc on vc.vaccinationCenterId=v.mappedId 
WHERE vc.vaccineId is not null  
",
@filterQueryString,
"
group by v.name ", @sortQueryString, " LIMIT ", limitStart, ",", limitEnd); 

PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SummaryImmunizationByVaccinator` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SummaryImmunizationByVaccinator`(IN centerIdFilterCommaSeparated VARCHAR(1000),
IN vaccineDateFrom DATE, IN vaccineDateTo DATE,
IN limitStart INT, IN limitEnd INT,IN sortName VARCHAR(1000),IN orderName VARCHAR(50))
BEGIN 

SET @filterQueryString = "";

IF centerIdFilterCommaSeparated IS NOT NULL AND TRIM(centerIdFilterCommaSeparated) <> "" 
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND ivcen.identifier IN (",centerIdFilterCommaSeparated,") ");
END IF;

IF vaccineDateFrom IS NOT NULL AND vaccineDateTo IS NOT NULL  
THEN 
	SET @filterQueryString = CONCAT(@filterQueryString," AND vc.vaccinationDate BETWEEN '", vaccineDateFrom,"' AND '",vaccineDateTo,"'");
END IF;


IF sortName IS NULL OR TRIM(sortName) = "" 
THEN 
    SET @sortQueryString = "";
ELSE 
    SET @sortQueryString = CONCAT(" ORDER BY ",sortName," ", orderName);
END IF;


SET @QueryString=concat("select id.identifier, concat(firstName, ' ', if(lastName IS NULL,'',lastName)) name, IF(epAccountNumber='000000000000','No','Yes') incentives,
count(*) total, 
sum(if(vc.vaccineId=1,1,0)) bcg,
sum(if(vc.vaccineId=2,1,0)) p1,
sum(if(vc.vaccineId=3,1,0)) p2,
sum(if(vc.vaccineId=4,1,0)) p3,
sum(if(vc.vaccineId=5,1,0)) m1,
sum(if(vc.vaccineId=6,1,0)) m2,
sum(if(vc.vaccineId=7,1,0)) opv0,
sum(if(vc.vaccineId=8,1,0)) opv1,
sum(if(vc.vaccineId=9,1,0)) opv2,
sum(if(vc.vaccineId=10,1,0)) opv3,
sum(if(vc.vaccineId=11,1,0)) pcv1,
sum(if(vc.vaccineId=12,1,0)) pcv2,
sum(if(vc.vaccineId=13,1,0)) pcv3
from vaccinator v left join identifier id on v.mappedId=id.mappedId and id.preferred 
left join vaccination vc on vc.vaccinatorId=v.mappedId 
left join vaccinationcenter vcen on vcen.mappedId=v.vaccinationCenterId  
left join identifier ivcen on vcen.mappedId=ivcen.mappedId 
WHERE vc.vaccineId is not null  
",
@filterQueryString,
"
group by v.mappedId ", @sortQueryString, " LIMIT ", limitStart, ",", limitEnd); 

PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
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

-- Dump completed on 2015-12-09 10:19:54
