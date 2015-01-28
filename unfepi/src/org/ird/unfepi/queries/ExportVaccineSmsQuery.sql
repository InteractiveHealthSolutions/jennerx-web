SELECT 'Vaccination Record Number', 'Child ID', 'Vaccine ID', 'Vaccine',
'Reminder ID', 'Reminder', 'Recipient', 'Day Number', 'Due Date', 'Sent Date', 
'Reminder Status', 'Sms Cancel Reason', 'Sms Cancel Reason Other', 'Hours Difference', 'Text', 'Description' 
UNION
SELECT v.vaccinationRecordNum, cid.identifier, v.vaccineId, vc.name, 
rs.reminderId, r.reminderName, rs.recipient, rs.dayNumber, rs.dueDate, rs.sentDate, 
rs.reminderStatus, rs.smsCancelReason, rs.smsCancelReasonOther, rs.hoursDifference, rs.text, rs.description 
FROM remindersms rs 
LEFT JOIN reminder r ON rs.reminderId=r.reminderId 
LEFT JOIN vaccination v ON rs.vaccinationRecordNum=v.vaccinationRecordNum 
LEFT JOIN child ch ON v.childId=ch.mappedId 
LEFT JOIN idmapper cidm ON v.childId=cidm.mappedId 
LEFT JOIN identifier cid ON cid.mappedId=cidm.mappedId AND cid.preferred=TRUE 
LEFT JOIN vaccine vc ON v.vaccineId=vc.vaccineId 
WHERE r.reminderType = 'NEXT_VACCINATION_REMINDER' 