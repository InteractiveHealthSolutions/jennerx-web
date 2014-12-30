DROP TEMPORARY TABLE IF EXISTS tt;

CREATE TEMPORARY TABLE tt (INDEX(p1id), INDEX(createdByUserId), INDEX(element), INDEX(value))  
SELECT e.*, er.element, er.value, er.groupName, er.orderAs, er.displayName FROM encounter e JOIN encounterresults er ON e.encounterId=er.encounterId 
										AND e.p1id=er.p1id AND e.p2id=er.p2id AND encounterType IN ('ENROLLMENT','FOLLOWUP','LOTTERY_GEN');

SELECT v.childid MappedId, chid.identifier, vc.VaccineId, vc.name VaccineName, v.hasApprovedLottery ApprovedLottery, 
chlot.lotteryDate LotteryDate, hasWonLottery HasWonLottery, 
chlot.code VerificationCode, chlot.codeStatus CodeStatus, chlot.amount Amount, 
stid.identifier StorekeeperId, chlot.consumptionDate ConsumptionDate,chlot.transactionDate TransactionDate, 
chlot.vaccinationRecordNum VaccinationRecordNumber , e.*  
FROM childlottery chlot 
LEFT JOIN vaccination v ON chlot.vaccinationRecordNum = v.vaccinationRecordNum 
LEFT JOIN user uc ON chlot.createdByUserId = uc.mappedid 
LEFT JOIN user ul ON chlot.lastEditedByUserId = ul.mappedid 
LEFT JOIN idmapper chidm ON v.childId = chidm.mappedId 
LEFT JOIN identifier chid ON chid.mappedId=chidm.mappedId AND chid.preferred=TRUE
LEFT JOIN idmapper stidm ON chlot.storekeeperId = stidm.mappedId 
LEFT JOIN identifier stid ON stid.mappedId=stidm.mappedId AND stid.preferred=TRUE 
LEFT JOIN vaccine vc ON v.vaccineId = vc.vaccineId 
LEFT JOIN tt e ON e.p1id=v.childId AND e.createdByUserId = chlot.createdByUserId 
		AND (  (e.element LIKE 'LOTTERY_%VACCINATION_RECORD_NUM' AND e.value=v.vaccinationRecordNum) 
			OR (e.element LIKE 'VACCINE_NAME_RECEIVED' AND e.value=vc.name))
-- LEFT JOIN (SELECT * FROM encounter e JOIN encounterresults er ON e.encounterId=er.encounterId 
-- 										AND e.p1id=er.p1id AND e.p2id=er.p2id AND er.element=CONCAT(1,1) )
-- ON e.p1id=v.childId AND e.createdByUserId = chlot.createdByUserId 
-- 	AND ABS(TIMESTAMPDIFF(SECOND,e.dateEncounterEnd, chlot.createdDate)) BETWEEN 0 AND 20    
 
order by childid desc 

DROP TEMPORARY TABLE tt;

-- select ABS(TIMESTAMPDIFF(MINUTE,dateenrolled,createddate)),CONCAT(1,1) from child;
