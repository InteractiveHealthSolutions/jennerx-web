SELECT 'Storekeeper ID', 'Date Registered', 
'First Name','Last Name',
'Store Name','Gender','Birthdate','Is Birthdate Estimated','Qualification',
'Closest Center','NIC','EP Wallet Number',
'Contact Numbers','Addresses',
'Additional Note'
UNION
SELECT stkid.identifier ID, stk.dateRegistered, 
stk.firstName , stk.lastName , 
stk.storeName , stk.gender , stk.birthdate , stk.estimatedBirthdate, stk.qualification, 
vid.identifier ClosestCenter, stk.nic, stk.epAccountNumber, 
(select group_concat('--',numberType,':',number) from contactnumber where mappedid=stk.mappedId) contactnumbers, 
(select group_concat('AddressLine1:',IFNULL(a.address1,'NIL'),' Town:',IFNULL(a.town,'NIL'),
' UC:',IFNULL(a.uc,'NIL'),' LMARK:',IFNULL(a.landmark,'NIL'),
' CityID:',CAST(IFNULL(a.cityId,'') AS char(2))) from address a where a.mappedid=stk.mappedId) addresses, 
stk.description
FROM storekeeper stk 
LEFT JOIN idmapper vidm ON stk.closestVaccinationCenterId=vidm.mappedId 
LEFT JOIN identifier vid ON vidm.mappedId=vid.mappedId AND vid.preferred=TRUE 
LEFT JOIN idmapper stkidm ON stk.mappedId=stkidm.mappedId 
LEFT JOIN identifier stkid ON stkidm.mappedId=stkid.mappedId AND stkid.preferred=TRUE 

