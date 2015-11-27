SELECT i.identifier childid, 
CONCAT(IFNULL(ch.firstname,''),' ',IFNULL(ch.middlename,''),' ',IFNULL(ch.lastname,'')) nameofchild, 
CONCAT(IFNULL(ch.fatherFirstname,''),' ',IFNULL(ch.fatherMiddlename,''),' ',IFNULL(ch.fatherLastname,'')) fathernameofchild, 
ch.enrollmentVaccineId, 
chvc.name enrollmentVaccine, 
(select group_concat('--',numberType,':',number) from contactnumber where mappedid=vtn.childid) contactnumbersofchild, 
(select group_concat('H.No:',IFNULL(a.addHouseNumber,'NIL'),' Street:',IFNULL(a.addStreet,'NIL'),
' Sector:',IFNULL(a.addSector,'NIL'),' Colony:',IFNULL(a.addColony,'NIL'),' Town:',IFNULL(a.addtown,'NIL'),
' UC:',IFNULL(a.addUc,'NIL'),' LMARK:',IFNULL(a.addLandmark,'NIL'),
' CityID:',CAST(IFNULL(a.cityId,'') AS char(2))) from address a where a.mappedid=vtn.childid) addressofchild, 
vtn.vaccineid, 
vc.name, 
(case when ch.enrollmentVaccineId=vtn.vaccineId AND vtn.vaccinationStatus='VACCINATED' AND vtn.isFirstVaccination
		then 'ENROLLMENT' 
		when ch.enrollmentVaccineId <> vtn.vaccineId AND NOT vtn.isFirstVaccination
		then 'FOLLOWUP' Else 'NOT_FOUND' end) as 'EncounterOrFormType' , 
vtn.vaccinationRecordNum,
vtn.vaccinationDuedate, 
vtn.vaccinationDate, 
vctor.identifier AS VaccinatorProgramID, 
(chl.amount*vincentp.commission) AS VaccinatorIncentive,  
chl.lotteryDate, 
chl.hasWonLottery, 
chl.code, 
chl.codeStatus, 
chl.consumptionDate,  
chl.transactionDate, 
chl.amount, 
stk.identifier storekeeperid  
FROM childlottery chl 
left join vaccination vtn on chl.vaccinationRecordNum = vtn.vaccinationRecordNum 
left join idmapper id on vtn.childid=id.mappedid 
LEFT JOIN identifier i ON i.mappedId=id.mappedId AND i.preferred=TRUE 
left join vaccine vc on vtn.vaccineid=vc.vaccineid 
left join idmapper stkidm on chl.storekeeperId=stkidm.mappedid 
LEFT JOIN identifier stk ON stkidm.mappedId=stk.mappedId AND stk.preferred=TRUE 
left join idmapper vctoridm on vtn.vaccinatorId=vctoridm.mappedid 
LEFT JOIN identifier vctor ON vctoridm.mappedId=vctor.mappedId AND vctor.preferred=TRUE 
left join child ch on vtn.childid=ch.mappedid 
left join (select * from vaccinatorincentiveparams) vincentp on vincentp.commission is not null 
left join vaccine chvc on ch.enrollmentVaccineId=chvc.vaccineId 