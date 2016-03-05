<%@page import="org.ird.unfepi.GlobalParams.DailySummaryVaccineGivenTableVariables"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<link rel="stylesheet" type="text/css" href="css/style.css?v=<%=WebGlobals.VERSION_CSS_JS %>">
<%@ include file="/WEB-INF/layout/include.jsp"%>
<div class="headerwindow"></div>
<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
	<table  class="datalistreporting">
          
      <tbody >
	<tr>
      <th colspan="2" style="font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;">Daily Summary Details</th>
	</tr>
	 <tr>
	        <th>Vaccination center</th><td><c:out value="${model.vcenter.idMapper.identifiers[0].identifier} : ${model.vcenter.name}"></c:out></td>
	 </tr>  
	 <tr>          
	        <th>Vaccinator</th><td><c:out value="${model.vaccinator.idMapper.identifiers[0].identifier} : ${model.vaccinator.firstName}"></c:out></td>
	 </tr>
     <tr>
            <th>Summary date</th> <td><c:out value="${model.dsumar.summaryDate}"></c:out></td>
     </tr>
<c:choose>
<c:when test="${not empty model.dsumar.totalEnrolled}">
     <tr>
            <th>BCG visited</th> <td><c:out value="${model.dsumar.bcgVisited}"></c:out></td>
     </tr>
     <tr>
            <th>BCG enrolled total</th> <td><c:out value="${model.dsumar.bcgEnrolledTotal}"></c:out></td>
     </tr>
     <tr>
            <th>Penta1 visited</th> <td><c:out value="${model.dsumar.penta1Visited}"></c:out></td>
     </tr>
     <tr>            
	        <th>Penta1 enrolled total</th><td><c:out value="${model.dsumar.penta1EnrolledTotal}"></c:out></td>
	 </tr>
     <tr>
            <th>Penta1 followed up</th> <td><c:out value="${model.dsumar.penta1Followuped}"></c:out></td>
     </tr>
     <tr>
            <th>Penta2 visited</th> <td><c:out value="${model.dsumar.penta2Visited}"></c:out></td>
     </tr>
     <tr>
            <th>Penta2 followed up</th> <td><c:out value="${model.dsumar.penta2Followuped}"></c:out></td>
     </tr>
	 <tr>
            <th>Penta3 visited</th> <td><c:out value="${model.dsumar.penta3Visited}"></c:out></td>
     </tr>
     <tr>
            <th>Penta3 followed up</th> <td><c:out value="${model.dsumar.penta3Followuped}"></c:out></td>
     </tr>
     <tr>
            <th>Measles1 visited</th> <td><c:out value="${model.dsumar.measles1Visited}"></c:out></td>
     </tr>
     <tr>
            <th>Measles1 followed up</th> <td><c:out value="${model.dsumar.measles1Followuped}"></c:out></td>
     </tr>
     <tr>
            <th>Measles2 visited</th> <td><c:out value="${model.dsumar.measles2Visited}"></c:out></td>
     </tr>
     <tr>
            <th>Measles2 followed up</th> <td><c:out value="${model.dsumar.measles2Followuped}"></c:out></td>
     </tr>
     <tr>
            <th>OPV Visits</th> <td><c:out value="${model.dsumar.opvGivenTotal}"></c:out></td>
     </tr>
     <tr>
            <th>TT Visits</th> <td><c:out value="${model.dsumar.ttGivenTotal}"></c:out></td>
     </tr>
     <tr>
            <th>BCG enrolled with lottery</th> <td><c:out value="${model.dsumar.bcgEnrolledWithLottery}"></c:out></td>
     </tr>
     <tr>
            <th>BCG enrolled with reminder</th> <td><c:out value="${model.dsumar.bcgEnrolledWithReminder}"></c:out></td>
     </tr>
     <tr>
            <th>Penta1 enrolled with lottery</th> <td><c:out value="${model.dsumar.penta1EnrolledWithLottery}"></c:out></td>
     </tr>
     <tr>
            <th>Penta1 enrolled with reminder</th> <td><c:out value="${model.dsumar.penta1EnrolledWithReminder}"></c:out></td>
     </tr>
     <tr>
            <th>Total enrolled</th> <td><c:out value="${model.dsumar.totalEnrolled}"></c:out></td>
     </tr>
     <tr>
            <th>Total followed up</th> <td><c:out value="${model.dsumar.totalFollowuped}"></c:out></td>
     </tr>
     <tr>
            <th>Total visited</th> <td><c:out value="${model.dsumar.totalVisited}"></c:out></td>
     </tr>
</c:when>
<c:otherwise>
				<c:set var="totalvaccinated" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_VACCINE_GIVEN.getREPRESENTATION()%>" />
            	<c:set var="totalbcg" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_BCG.getREPRESENTATION()%>" />
            	<c:set var="totalp1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA1.getREPRESENTATION()%>" />
            	<c:set var="totalp2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA2.getREPRESENTATION()%>" />
            	<c:set var="totalp3" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA3.getREPRESENTATION()%>" />
            	<c:set var="totalm1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES1.getREPRESENTATION()%>" />
            	<c:set var="totalm2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES2.getREPRESENTATION()%>" />
            	<c:set var="totalopv0" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV0.getREPRESENTATION()%>" />
            	<c:set var="totalopv1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV1.getREPRESENTATION()%>" />
            	<c:set var="totalopv2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV2.getREPRESENTATION()%>" />
            	<c:set var="totalopv3" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV3.getREPRESENTATION()%>" />
            	<c:set var="totaltt1" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT1.getREPRESENTATION()%>" />
            	<c:set var="totaltt2" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT2.getREPRESENTATION()%>" />
            	<c:set var="totaltt3" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT3.getREPRESENTATION()%>" />
            	<c:set var="totaltt4" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT4.getREPRESENTATION()%>" />
            	<c:set var="totaltt5" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT5.getREPRESENTATION()%>" />
     <tr>
            <th>Total BCG</th> <td><c:out value="${model[totalbcg]}"></c:out></td>
     </tr>
     <tr>
            <th>Total Penta1</th> <td><c:out value="${model[totalp1]}"></c:out></td>
     </tr>
     <tr>
            <th>Total Penta2</th> <td><c:out value="${model[totalp2]}"></c:out></td>
     </tr>
     <tr>
            <th>Total Penta3</th> <td><c:out value="${model[totalp3]}"></c:out></td>
     </tr>
     <tr>
            <th>Total Measles1</th> <td><c:out value="${model[totalm1]}"></c:out></td>
     </tr>
     <tr>
            <th>Total Measles2</th> <td><c:out value="${model[totalm2]}"></c:out></td>
     </tr>
          <tr>
            <th>Total OPV0</th> <td><c:out value="${model[totalopv0]}"></c:out></td>
     </tr>
          <tr>
            <th>Total OPV1</th> <td><c:out value="${model[totalopv1]}"></c:out></td>
     </tr>
          <tr>
            <th>Total OPV2</th> <td><c:out value="${model[totalopv2]}"></c:out></td>
     </tr>
          <tr>
            <th>Total OPV3</th> <td><c:out value="${model[totalopv3]}"></c:out></td>
     </tr>
          <tr>
            <th>Total TT1</th> <td><c:out value="${model[totaltt1]}"></c:out></td>
     </tr>
          <tr>
            <th>Total TT2</th> <td><c:out value="${model[totaltt2]}"></c:out></td>
     </tr>
          <tr>
            <th>Total TT3</th> <td><c:out value="${model[totaltt3]}"></c:out></td>
     </tr>
          <tr>
            <th>Total TT4</th> <td><c:out value="${model[totaltt4]}"></c:out></td>
     </tr>
          <tr>
            <th>Total TT5</th> <td><c:out value="${model[totaltt5]}"></c:out></td>
     </tr>
     <tr>
            <th>Total vaccinated</th> <td><c:out value="${model[totalvaccinated]}"></c:out></td>
     </tr>
</c:otherwise>
</c:choose>
<%
boolean permcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CREATEDBY_INFO.name());
boolean permed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_EDITEDBY_INFO.name());
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERID.name());
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERNAME.name());

	if(permcr){ 
	%>
	<tr>
	            <th>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.dsumar.createdByUserId.username},<%}%><%if(permuname){%>${model.dsumar.createdByUserId.firstName}<%}%></td>
	</tr>
	<%
	}
	%>
	<tr>
	            <th>Created on</th><td><c:out value="${model.dsumar.createdDate}" /></td>
	</tr>
	<%
	if(permed){ 
	%>
	<tr>
	            <th>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.dsumar.lastEditedByUserId.username},<%}%><%if(permuname){%>${model.dsumar.lastEditedByUserId.firstName}<%}%></td>
	</tr>
	<%
	}
	%>
	<tr>
	            <th>Last updated</th><td><c:out value="${model.dsumar.lastEditedDate}" /></td>    
	</tr>
</tbody>


</table>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
