SELECT 'ProgramID','Name','FatherName','EnrollmentVaccine','ContactNumbers',
'Addresses','Vaccine','VaccinationRecordNum','VaccinationDuedate','VaccinationDate','VaccinatorID',	
'CenterID','IncentiveDate','IncentiveStatus','ConsumptionDate','TransactionDate','Amount'
UNION
SELECT i.identifier programId, 
CONCAT(IFNULL(ch.firstname,''),' ',IFNULL(ch.middlename,''),' ',IFNULL(ch.lastname,'')) name, 
CONCAT(IFNULL(ch.fatherFirstname,''),' ',IFNULL(ch.fatherMiddlename,''),' ',IFNULL(ch.fatherLastname,'')) fatherName, 
chvc.name enrollmentVaccine, 
(select group_concat('--',numberType,':',number) from contactnumber where mappedid=vtn.childid) contactnumbers, 
(select group_concat('AddressLine1:',IFNULL(a.address1,'NIL'),' Town:',IFNULL(a.town,'NIL'),
' UC:',IFNULL(a.uc,'NIL'),' LMARK:',IFNULL(a.landmark,'NIL'),
' CityID:',CAST(IFNULL(a.cityId,'') AS char(2))) from address a where a.mappedid=vtn.childid) addresses, 
vc.name vaccineName, 
vtn.vaccinationRecordNum,
vtn.vaccinationDuedate, 
vtn.vaccinationDate, 
vctor.identifier AS vaccinatorId, 
vcen.identifier centerId,
chl.incentiveDate, 
chl.incentiveStatus, 
chl.consumptionDate,  
chl.transactionDate, 
chl.amount 
FROM childincentive chl 
left join vaccination vtn on chl.vaccinationRecordNum = vtn.vaccinationRecordNum 
left join idmapper id on vtn.childid=id.mappedid 
LEFT JOIN identifier i ON i.mappedId=id.mappedId AND i.preferred=TRUE 
left join vaccine vc on vtn.vaccineid=vc.vaccineid 
LEFT JOIN identifier vctor ON vtn.vaccinatorId=vctor.mappedId AND vctor.preferred=TRUE 
LEFT JOIN identifier vcen ON vcen.mappedId=vtn.vaccinationCenterId AND vcen.preferred=TRUE 
left join child ch on vtn.childid=ch.mappedid 
left join vaccine chvc on ch.enrollmentVaccineId=chvc.vaccineId 