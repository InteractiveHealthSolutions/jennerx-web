<%@page import="org.ird.unfepi.GlobalParams.DailySummaryVaccineGivenTableVariables"%>
<%@page import="org.ird.unfepi.model.DailySummaryVaccineGiven"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
	<div class="datagridepi">
	<table >
	<thead>
            	<c:set var="totalvaccinated" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_VACCINE_GIVEN.getREPRESENTATION()%>" />
            	<c:set var="totalbcg" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_BCG.getREPRESENTATION()%>" />
            	<c:set var="totalp1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA1.getREPRESENTATION()%>" />
            	<c:set var="totalp2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA2.getREPRESENTATION()%>" />
            	<c:set var="totalp3" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA3.getREPRESENTATION()%>" />
            	<c:set var="totalm1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES1.getREPRESENTATION()%>" />
            	<c:set var="totalm2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES2.getREPRESENTATION()%>" />
        <tr>
<%
boolean editDailySummary=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_DAILY_SUMMARY, request);

if(editDailySummary){
%>
        	<th>edit</th>
<%
}
%> 
        	<th>---</th>
            <th>Summary Date</th>
			<th>Vaccination Center</th>
			<th>Vaccinator</th>
            <th>Total Visited/Vaccinated</th>
            <th>Total BCG</th> 
            <th>Total Penta1</th> 
            <th>Total Penta2</th> 
            <th>Total Penta3</th> 
            <th>Total Measles1</th> 
            <th>Total Measles2</th> 
        </tr>
    </thead>
    <tbody class="rows">
   <c:forEach items="${model.dailysummap}" var="dsmap">
   	 <tr>
<%
if(editDailySummary){
%>    	 
 			<c:choose>
            <c:when test="${not empty dsmap[totalvaccinated]}">
			<td><a href="editDailySummary.htm?dsId=${dsmap.dailysummary.serialNumber}" class="icon"><img alt="edit" src ="images/edit-icon.png" class="icon"></a></td>
 			</c:when>
            <c:otherwise>
            <td></td>
            </c:otherwise>
            </c:choose>
<%
}
%>
		  	<td><a  title="${dsmap.dailysummary.serialNumber}"  onclick="viewDailySummaryDetails(this.title);" >details</a></td>
            <td><fmt:formatDate value="${dsmap.dailysummary.summaryDate}" type="both" pattern="dd-MMM-yyyy hh:mm" /></td>
            <td><c:out value="${dsmap.dailysummary.vaccinationCenter.idMapper.identifiers[0].identifier}"></c:out></td>
            <td><c:out value="${dsmap.dailysummary.vaccinator.idMapper.identifiers[0].identifier}"></c:out></td>
            <c:choose>
            <c:when test="${not empty dsmap.dailysummary.totalEnrolled}">
            	<td><c:out value="${dsmap.dailysummary.totalVisited}"></c:out></td>
            	<td><c:out value="${dsmap.dailysummary.bcgVisited}"></c:out></td>
            	<td><c:out value="${dsmap.dailysummary.penta1Visited}"></c:out></td>
            	<td><c:out value="${dsmap.dailysummary.penta2Visited}" /></td>
            	<td><c:out value="${dsmap.dailysummary.penta3Visited}"></c:out></td>
            	<td><c:out value="${dsmap.dailysummary.measles1Visited}" /></td>
            	<td><c:out value="${dsmap.dailysummary.measles2Visited}"></c:out></td>
            </c:when>
            <c:otherwise>
	            <td><c:out value="${dsmap[totalvaccinated]}"></c:out></td>
	            <td><c:out value="${dsmap[totalbcg]}"></c:out></td>
	            <td><c:out value="${dsmap[totalp1]}"></c:out></td>
	            <td><c:out value="${dsmap[totalp2]}"></c:out></td>
	            <td><c:out value="${dsmap[totalp3]}"></c:out></td>
	            <td><c:out value="${dsmap[totalm1]}"></c:out></td>
	            <td><c:out value="${dsmap[totalm2]}"></c:out></td>
            </c:otherwise>
            </c:choose>
           
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


