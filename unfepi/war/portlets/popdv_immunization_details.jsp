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
<tr><td>Mother First Name	</td><td>${entry.motherFirstName}	</td></tr>
<tr><td>Mother Last Name	</td><td>${entry.motherLastName}	</td></tr>
<tr><td>Gender	</td><td>${entry.Gender}	</td></tr>
<tr><td>Birthdate	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.Birthdate}"/>	</td></tr>
<tr><td>Is Estimated Birthdate	</td><td>${entry.IsEstimatedBirthdate}	</td></tr>
<tr><td>Date Enrolled	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.DateEnrolled}"/>	</td></tr>
<tr><td>Primary Contact	</td><td>${entry.PrimaryContact}	</td></tr>
<tr><td>Secondary Contact	</td><td>${entry.SecondaryContact}	</td></tr>
<%-- <tr><td>Enrollment EPI number	</td><td>${entry.EnrollmentEPInumber}	</td></tr> --%>
<tr><td>Enrollment Vaccination Record Number	</td><td>${entry.EnrollmentVaccinationRecordNumber}	</td></tr>
<tr><td>Enrollment Vaccine ID	</td><td>${entry.EnrollmentVaccineID}	</td></tr>
<tr><td>Enrollment Vaccine	</td><td>${entry.EnrollmentVaccine}	</td></tr>
<tr><td>Enrollment Center	</td><td>${entry.EnrollmentCenter}	</td></tr>
<%-- <tr><td>Has Approved VaccineReminders	</td><td>${entry.hasApprovedVaccineReminders}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">BCG</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.BCGVaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.BCGEPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.BCGVaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.BCGVaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.BCGApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGWinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.BCGAmount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.BCGIncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGTransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.BCGCenter}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.BCGVaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.BCGVaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.BCGVaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.BCGVaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.BCGVaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.BCGVaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.BCGVaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.BCGResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.BCGResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">Penta1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PENTA1VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PENTA1EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PENTA1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PENTA1VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PENTA1ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PENTA1Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PENTA1IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PENTA1Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PENTA1VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PENTA1VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PENTA1VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PENTA1VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PENTA1VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PENTA1VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA1VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PENTA1ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PENTA1ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">Penta2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PENTA2VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PENTA2EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PENTA2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PENTA2VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PENTA2ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PENTA2Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PENTA2IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PENTA2Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PENTA2VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PENTA2VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PENTA2VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PENTA2VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PENTA2VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PENTA2VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA2VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PENTA2ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PENTA2ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">Penta3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PENTA3VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PENTA3EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PENTA3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PENTA3VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PENTA3ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PENTA3Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PENTA3IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PENTA3Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PENTA3VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PENTA3VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PENTA3VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PENTA3VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PENTA3VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PENTA3VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PENTA3VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PENTA3ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PENTA3ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">Measles1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.MEASLES1VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.MEASLES1EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.MEASLES1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.MEASLES1VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.MEASLES1ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.MEASLES1Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.MEASLES1IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.MEASLES1Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.MEASLES1VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.MEASLES1VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.MEASLES1VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.MEASLES1VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.MEASLES1VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.MEASLES1VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES1VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.MEASLES1ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.MEASLES1ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">Mealses2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.MEASLES2VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.MEASLES2EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.MEASLES2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.MEASLES2VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.MEASLES2ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.MEASLES2Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.MEASLES2IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.MEASLES2Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.MEASLES2VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.MEASLES2VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.MEASLES2VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.MEASLES2VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.MEASLES2VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.MEASLES2VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.MEASLES2VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.MEASLES2ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.MEASLES2ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">OPV0</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV0VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.OPV0EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV0VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV0VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.OPV0ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.OPV0Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.OPV0IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.OPV0Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.OPV0VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.OPV0VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.OPV0VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.OPV0VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.OPV0VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.OPV0VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV0VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.OPV0ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.OPV0ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">OPV1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV1VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.OPV1EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV1VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccinationDate}"/>	</td></tr>
<%-- <tr><td>	</td><td>${entry.OPV1ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.OPV1Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.OPV1IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.OPV1Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.OPV1VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.OPV1VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.OPV1VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.OPV1VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.OPV1VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.OPV1VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV1VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.OPV1ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.OPV1ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">OPV2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV2VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.OPV2EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV2VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.OPV2ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.OPV2Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.OPV2IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.OPV2Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.OPV2VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.OPV2VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.OPV2VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.OPV2VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.OPV2VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.OPV2VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV2VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.OPV2ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.OPV2ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">OPV3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.OPV3VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.OPV3EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.OPV3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.OPV3VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.OPV3ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.OPV3Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.OPV3IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.OPV3Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.OPV3VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.OPV3VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.OPV3VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.OPV3VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.OPV3VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.OPV3VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.OPV3VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.OPV3ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.OPV3ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">PCV1</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV1VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PCV1EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV1VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV1VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PCV1ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PCV1Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PCV1IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PCV1Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PCV1VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PCV1VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PCV1VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PCV1VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PCV1VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PCV1VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV1VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PCV1ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PCV1ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">PCV2</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV2VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PCV2EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV2VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV2VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PCV2ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PCV2Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PCV2IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PCV2Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PCV2VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PCV2VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PCV2VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PCV2VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PCV2VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PCV2VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV2VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PCV2ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PCV2ResponseText}	</td></tr> --%>
<tr><td colspan="10" class="headerrow">PCV3</td></tr>
<tr><td>Vaccination Status	</td><td>${entry.PCV3VaccinationStatus}	</td></tr>
<%-- <tr><td>EPI Number	</td><td>${entry.PCV3EPINumber}	</td></tr> --%>
<tr><td>Vaccination Record Number	</td><td>${entry.PCV3VaccinationRecordNumber}	</td></tr>
<tr><td>Vaccine ID	</td><td>${entry.PCV3VaccineID}	</td></tr>
<tr><td>Vaccination Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccinationDueDate}"/>	</td></tr>
<tr><td>Vaccination Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccinationDate}"/>	</td></tr>
<%-- <tr><td>Approved Incentive	</td><td>${entry.PCV3ApprovedLottery}	</td></tr> --%>
<%-- <tr><td>Winning Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3WinningDate}"/>	</td></tr> --%>
<%-- <tr><td>Amount	</td><td>${entry.PCV3Amount}	</td></tr> --%>
<%-- <tr><td>Incentive Status	</td><td>${entry.PCV3IncentiveStatus}	</td></tr> --%>
<%-- <tr><td>Transaction Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3TransactionDate}"/>	</td></tr> --%>
<%-- <tr><td>Consumption Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3ConsumptionDate}"/>	</td></tr> --%>
<tr><td>Center	</td><td>${entry.PCV3Center}	</td></tr>
<%-- <tr><td>Vaccine Reminder1 Status	</td><td>${entry.PCV3VaccineReminder1Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Recipient	</td><td>${entry.PCV3VaccineReminder1Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder1DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder1 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder1SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Status	</td><td>${entry.PCV3VaccineReminder2Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Recipient	</td><td>${entry.PCV3VaccineReminder2Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder2DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder2 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder2SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Recipient	</td><td>${entry.PCV3VaccineReminder3Recipient}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Status	</td><td>${entry.PCV3VaccineReminder3Status}	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Due Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder3DueDate}"/>	</td></tr> --%>
<%-- <tr><td>Vaccine Reminder3 Sent Date	</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${entry.PCV3VaccineReminder3SentDate}"/>	</td></tr> --%>
<%-- <tr><td>Response Count	</td><td>${entry.PCV3ResponseCount}	</td></tr> --%>
<%-- <tr><td>Response Text	</td><td style="width: 50%">${entry.PCV3ResponseText}	</td></tr> --%>

<%-- 	</c:forEach>
 --%></c:forEach>
</table>
</div>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
