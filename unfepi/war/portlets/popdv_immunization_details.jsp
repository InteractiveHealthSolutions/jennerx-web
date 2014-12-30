<%@page import="java.util.Locale"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<%@ include file="/WEB-INF/template/include.jsp"%>

<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
<div class="dvwform">
<table>
<c:forEach var="entry" items="${model.datalist}">
<%-- 	<c:forEach var="entry" items="${initem}">
 --%>	
<tr><td>Child Id</td><td>${entry.ChildId}	</td></tr>
<tr><td>Mapped Id	</td><td>${entry.MappedId}	</td></tr>
<tr><td>Child First Name	</td><td>${entry.ChildFirstName}	</td></tr>
<tr><td>Child Last Name	</td><td>${entry.ChildLastName}	</td></tr>
<tr><td>Father First Name	</td><td>${entry.FatherFirstName}	</td></tr>
<tr><td>Father Last Name	</td><td>${entry.FatherLastName}	</td></tr>
<tr><td>Gender	</td><td>${entry.Gender}	</td></tr>
<tr><td>Birthdate	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Birthdate}"/>	</td></tr>
<tr><td>Is Estimated Birthdate	</td><td>${entry.IsEstimatedBirthdate}	</td></tr>
<tr><td>Date Enrolled	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.DateEnrolled}"/>	</td></tr>
<tr><td>Primary Contact	</td><td>${entry.PrimaryContact}	</td></tr>
<tr><td>Secondary Contact	</td><td>${entry.SecondaryContact}	</td></tr>
<tr><td>Enrollment EPI number	</td><td>${entry.EnrollmentEPInumber}	</td></tr>
<tr><td>Enrollment Vaccination Record Number	</td><td>${entry.EnrollmentVaccinationRecordNumber}	</td></tr>
<tr><td>Enrollment Vaccine ID	</td><td>${entry.EnrollmentVaccineID}	</td></tr>
<tr><td>Enrollment Vaccine	</td><td>${entry.EnrollmentVaccine}	</td></tr>
<tr><td>Enrollment Center	</td><td>${entry.EnrollmentCenter}	</td></tr>
<tr><td>Has Approved Reminders	</td><td>${entry.hasApprovedReminders}	</td></tr>
<tr><td colspan="10" class="headerrow">BCG</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.BCGVaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.BCGEPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.BCGVaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.BCGVaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.BCGPCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.BCGOPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.BCGApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGWinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.BCGAmount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.BCGVerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.BCGCodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGTransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.BCGStorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.BCGCenter}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.BCGReminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.BCGReminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.BCGReminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGReminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.BCGResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.BCGResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">Penta1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.Penta1VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.Penta1EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.Penta1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.Penta1VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.Penta1PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.Penta1OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.Penta1ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.Penta1Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.Penta1VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.Penta1CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.Penta1StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.Penta1Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.Penta1Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.Penta1Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.Penta1Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta1Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.Penta1ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.Penta1ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">Penta2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.Penta2VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.Penta2EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.Penta2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.Penta2VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.Penta2PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.Penta2OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.Penta2ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.Penta2Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.Penta2VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.Penta2CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.Penta2StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.Penta2Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.Penta2Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.Penta2Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.Penta2Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta2Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.Penta2ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.Penta2ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">Penta3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.Penta3VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.Penta3EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.Penta3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.Penta3VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.Penta3PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.Penta3OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.Penta3ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.Penta3Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.Penta3VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.Penta3CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.Penta3StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.Penta3Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.Penta3Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.Penta3Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.Penta3Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Penta3Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.Penta3ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.Penta3ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">Measles1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.Measles1VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.Measles1EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.Measles1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.Measles1VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.Measles1PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.Measles1OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.Measles1ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.Measles1Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.Measles1VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.Measles1CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.Measles1StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.Measles1Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.Measles1Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.Measles1Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.Measles1Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles1Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.Measles1ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.Measles1ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">Mealses2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.Measles2VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.Measles2EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.Measles2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.Measles2VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.Measles2PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.Measles2OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.Measles2ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.Measles2Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.Measles2VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.Measles2CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.Measles2StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.Measles2Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.Measles2Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.Measles2Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.Measles2Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Measles2Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.Measles2ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.Measles2ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">OPV0</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV0VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.OPV0EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV0VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV0VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.OPV0PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.OPV0OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.OPV0ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.OPV0Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.OPV0VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.OPV0CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.OPV0StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.OPV0Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.OPV0Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.OPV0Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.OPV0Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.OPV0ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.OPV0ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">OPV1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV1VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.OPV1EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV1VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.OPV1PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.OPV1OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.OPV1ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.OPV1Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.OPV1VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.OPV1CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.OPV1StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.OPV1Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.OPV1Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.OPV1Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.OPV1Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.OPV1ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.OPV1ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">OPV2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV2VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.OPV2EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV2VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.OPV2PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.OPV2OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.OPV2ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.OPV2Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.OPV2VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.OPV2CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.OPV2StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.OPV2Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.OPV2Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.OPV2Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.OPV2Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.OPV2ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.OPV2ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">OPV3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV3VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.OPV3EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV3VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.OPV3PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.OPV3OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.OPV3ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.OPV3Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.OPV3VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.OPV3CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.OPV3StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.OPV3Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.OPV3Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.OPV3Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.OPV3Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.OPV3ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.OPV3ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">PCV1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV1VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.PCV1EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV1VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.PCV1PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.PCV1OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.PCV1ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.PCV1Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.PCV1VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.PCV1CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.PCV1StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.PCV1Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.PCV1Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.PCV1Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.PCV1Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.PCV1ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.PCV1ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">PCV2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV2VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.PCV2EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV2VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.PCV2PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.PCV2OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.PCV2ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.PCV2Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.PCV2VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.PCV2CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.PCV2StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.PCV2Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.PCV2Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.PCV2Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.PCV2Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.PCV2ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.PCV2ResponseText}	</td></tr>
<tr><td colspan="10" class="headerrow">PCV3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV3VaccinationStatus}	</td></tr>
<tr><td>EPI Number	</td><td>${entry.PCV3EPINumber}	</td></tr>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV3VaccineID}	</td></tr>
<tr><td>PCV Given	</td><td>${entry.PCV3PCVGiven}	</td></tr>
<tr><td>OPV Given	</td><td>${entry.PCV3OPVGiven}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccinationDate}"/>	</td></tr>
<tr><td>Approved Lottery	</td><td>${entry.PCV3ApprovedLottery}	</td></tr>
<tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3WinningDate}"/>	</td></tr>
<tr><td>Amount	</td><td>${entry.PCV3Amount}	</td></tr>
<tr><td>Verification Code	</td><td>${entry.PCV3VerificationCode}	</td></tr>
<tr><td>Code Status	</td><td>${entry.PCV3CodeStatus}	</td></tr>
<tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3TransactionDate}"/>	</td></tr>
<tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3ConsumptionDate}"/>	</td></tr>
<tr><td>Storekeeper ID	</td><td>${entry.PCV3StorekeeperID}	</td></tr>
<tr><td>Center	</td><td>${entry.PCV3Center}	</td></tr>
<tr><td>Reminder1 Status	</td><td>${entry.PCV3Reminder1Status}	</td></tr>
<tr><td>Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder1DueDate}"/>	</td></tr>
<tr><td>Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder1SentDate}"/>	</td></tr>
<tr><td>Reminder2 Status	</td><td>${entry.PCV3Reminder2Status}	</td></tr>
<tr><td>Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder2DueDate}"/>	</td></tr>
<tr><td>Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder2SentDate}"/>	</td></tr>
<tr><td>Reminder3 Status	</td><td>${entry.PCV3Reminder3Status}	</td></tr>
<tr><td>Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder3DueDate}"/>	</td></tr>
<tr><td>Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3Reminder3SentDate}"/>	</td></tr>
<tr><td>Response Count	</td><td>${entry.PCV3ResponseCount}	</td></tr>
<tr><td>Response Text	</td><td style="width: 50%">${entry.PCV3ResponseText}	</td></tr>

<%-- 	</c:forEach>
 --%></c:forEach>
</table>
</div>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
