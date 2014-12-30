<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

	<div class="dvwform">
	<table >
	<thead>
        <tr>
<%
boolean editvaccdata=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_VACCINATORS_DATA, request);
boolean nicPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_VACCINATOR_NIC, request);
boolean epWalletPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_VACCINATOR_EP_WALLET_NUMBER, request);

if(editvaccdata){
%>
        	<th></th>
<%
}
%> 
            <th>ID</th>
			<th>Name</th>
            <th>Age</th>
            <th>Gender</th> 
   			<th>Center</th>
<%
if(nicPerm){
%>           
            <th>NIC</th>
<%
}
if(epWalletPerm){
%>   
            <th>EP Wallet</th>
<%
}
%>
            <th></th>
			<th>Contacts</th>
            <th>Date Registered</th> 
            <!-- <th>Qualification</th> -->
        </tr>
    </thead>
   <tbody>
   <c:forEach items="${model.datalist}" var="map">
   	 <tr>
<%
if(editvaccdata){
%>    	 
			<td><a href="editvaccinator.htm?programId=${map.vaccinator.idMapper.identifiers[0].identifier}" class="linkiconS iconedit"></a></td>
<%
}
%>
		  	<td><a onclick="viewVaccinatorDetails(this.text);" class="anchorCustom">${map.vaccinator.idMapper.identifiers[0].identifier}</a></td>
            <td><c:out value="${map.vaccinator.firstName} ${vaccinator.lastName}"></c:out></td>
            <td><fmt:formatNumber value="${map.vaccinator.age/52}" maxFractionDigits="0"></fmt:formatNumber></td>
            <td class="lowercase"><c:out value="${map.vaccinator.gender}"></c:out></td>
            <td><c:out value="${map.vaccinator.vaccinationCenter.idMapper.identifiers[0].identifier}"></c:out></td>
<%
if(nicPerm){
%>           
            <td><c:out value="${map.vaccinator.nic}"></c:out></td>
<%
}
if(epWalletPerm){
%>   
            <td><c:out value="${map.vaccinator.epAccountNumber}"></c:out></td>
<%
}
%>
            <td><a href="addcontactNumber.htm?programId=${map.vaccinator.idMapper.identifiers[0].identifier}" class="icon"><img alt="contact" src ="images/contact.png" class="icon"></a></td>
			<td style="padding: 0;margin: 0">
            <div style="width:140px; overflow: visible ;display: inline-block;padding: 0;margin: 0;">
            <c:forEach items="${map.contacts}" var="cont">
            <a href="editcontactNumber.htm?coNum=${cont.contactNumberId}" class="linkiconXS iconedit"></a> ${fn:substring(cont.numberType,0,3)}- ${cont.number}
            <br>
            </c:forEach>
            </div>
            </td>
            <td><fmt:formatDate value="${map.vaccinator.dateRegistered}" pattern="dd-MMM-yyyy" /></td>
            <%-- <td><c:out value="${map.vaccinator.qualification}" /></td> --%>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


