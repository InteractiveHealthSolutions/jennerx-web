SELECT 'Vaccinator ID', 'Date Registered', 
'First Name','Last Name',
'Gender','Birthdate','Is Birthdate Estimated','Qualification',
'Center','NIC','EP Wallet Number',
'Contact Numbers','Addresses',
'Additional Note'
UNION
SELECT vtorid.identifier ID, vtor.dateRegistered, 
vtor.firstName , vtor.lastName , 
vtor.gender , vtor.birthdate , vtor.estimatedBirthdate, vtor.qualification, 
vid.identifier ClosestCenter, vtor.nic, vtor.epAccountNumber, 
(select group_concat('--',numberType,':',number) from contactnumber where mappedid=vtor.mappedId) contactnumbers, 
(select group_concat('H.No:',IFNULL(a.addHouseNumber,'NIL'),' Street:',IFNULL(a.addStreet,'NIL'),
' Sector:',IFNULL(a.addSector,'NIL'),' Colony:',IFNULL(a.addColony,'NIL'),' Town:',IFNULL(a.addtown,'NIL'),
' UC:',IFNULL(a.addUc,'NIL'),' LMARK:',IFNULL(a.addLandmark,'NIL'),
' CityID:',CAST(IFNULL(a.cityId,'') AS char(2))) from address a where a.mappedid=vtor.mappedId) addresses, 
vtor.description
FROM vaccinator vtor 
LEFT JOIN idmapper vidm ON vtor.vaccinationCenterId=vidm.mappedId 
LEFT JOIN identifier vid ON vidm.mappedId=vid.mappedId AND vid.preferred=TRUE 
LEFT JOIN idmapper vtoridm ON vtor.mappedId=vtoridm.mappedId 
LEFT JOIN identifier vtorid ON vtorid.mappedId=vtoridm.mappedId AND vtorid.preferred=TRUE 

