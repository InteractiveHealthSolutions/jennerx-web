<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
	<table>
		<tr>
			<td colspan="2"><input class="easyui-searchbox" data-options="prompt:'Enter Child ID',searcher:doSearch" value="${param.childId}" /> 
			<script>
				function doSearch(value) {
					window.location = "childDashboard.htm?childId=" + value;
				}
			</script></td>
			<td colspan="2"><a href="addchild.htm" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="float: right;">Enrollment</a></td>
		</tr>
	</table>
</div>
<c:if test="${not empty model.datalist}">
	<div class="dvwform">
		<table>
			<tr>
				<td><span class="error-message">${model.errorMessage}</span> 
				<span class="success-message">${param.editOrUpdateMessage}</span></td>
				<td colspan="100"><a href="followupVaccination.htm?child_id=${model.datalist.child.mappedId}" class="easyui-linkbutton" data-options="iconCls:'icon-save'"style="float: right;"> Follow up </a></td>
			</tr>
			<tr>
				<td colspan="3" class="headerrow">
					<%
						boolean editchilddata = UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_CHILDREN_DATA,request);
							if (UserSessionUtils.getActiveUser(request).isDefaultAdministrator()) {
					%>
						(${model.datalist.child.mappedId}) 
					<%
						}
					%> 
					<a onclick="viewChildDetails(this.text);" class="anchorCustom headingS">${model.datalist.child.idMapper.identifiers[0].identifier}</a>
					: ${model.datalist.child.firstName} ${model.datalist.child.lastName} (${model.datalist.child.gender}) 
					<%
						if (editchilddata) {
					%>
					<a href="editchild.htm?programId=${model.datalist.child.idMapper.identifiers[0].identifier}" class="linkiconS iconedit" style="background-color: white;"></a> 
					<%
					 	}
					 %>
				</td>
			</tr>
			<tr>
				<td colspan="2">Mother Name : ${model.datalist.child.motherFirstName}</td>
				<td>Village : 
					<c:forEach items="${model.datalist.address}" var="add"> 
					${add.address1} <%-- UC:${add.uc}, Town: ${add.town} --%><br>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td colspan="2">Birthdate : <fmt:formatDate value="${model.datalist.child.birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" /> 
				<c:if test="${model.datalist.child.estimatedBirthdate}">(approx)</c:if></td>
				<td>Location : 
					<c:forEach items="${model.datalist.address}" var="add">
 						${add.address2} <br>
					</c:forEach>
				</td>
		</table>
		<table class="right">
			<tr>
				<td colspan="20" class="headerrow-light left">Vaccination History</td>
			</tr>
			<tr>
				<th class="center">Vaccine</th>
				<th class="center">Vaccination Date</th>
				<th class="center">Vaccination Duedate</th>
				<th class="center">Vaccination Status</th>
				<th class="center">Site Id</th>
				<th class="center">Round Id</th>
				<th class="center">Vaccinator</th>
				<th class="center">Vaccination Record Num</th>
			</tr>
			<c:forEach items="${model.datalist.vaccinations}" var="vacc">
				<c:set var="enrvaccclass" value=""></c:set>
				<c:if
					test="${vacc.vaccine.vaccineId == model.datalist.child.enrollmentVaccineId}">
					<c:set var="enrvaccclass" value="datahighlight"></c:set>
				</c:if>
				<tr class="enrvaccclass">
					<td class="${enrvaccclass} left">${vacc.vaccine.name}</td>
					<td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" /></td>
					<td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" /></td>
					<td class="${enrvaccclass} right lowercase">${vacc.vaccinationStatus}</td>
					<td class="${enrvaccclass}">${vacc.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
					<td class="${enrvaccclass}">${vacc.roundId}</td>
					<td class="${enrvaccclass}">${vacc.vaccinator.idMapper.identifiers[0].identifier}</td>
					<td class="${enrvaccclass}">${vacc.vaccinationRecordNum}</td>
				</tr>
			</c:forEach>
		</table>
</c:if>


