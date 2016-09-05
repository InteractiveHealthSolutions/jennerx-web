
<%@include file="/WEB-INF/template/include.jsp"%>

	<div class="searchpalette">
	<table>
		<tr>
		<td>Health Program
			<select id="healthProgramId">
				<option></option>
				<c:forEach items="${healthPrograms}" var="hprogram">
					<option value="${hprogram.programId}" <c:if test="${model.programId == hprogram.programId}">selected="selected"</c:if>> ${hprogram.name}</option>
				</c:forEach>
			</select></td>
		<td><a onclick="searchData()" class="searchButton"></a></td>
		</tr>	
	</table>
	
	</div>
	<script type="text/javascript">
		function searchData(){
			window.location="viewHealthProgramRounds.htm?programId="+$('#healthProgramId').val();
		}
	</script>
	
	<c:if test="${not empty model.centerPrograms}">
	<div class="dvwform">
		<table>
		<tr><td colspan="5"><a onclick="addNewRound()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="float: right;">New Round</a></td></tr>
		<tr><td colspan="5"><b>Sites:</b>
		<c:forEach var="cp" items="${model.centerPrograms}" varStatus="status">
		${cp.vaccinationCenter.name}  
<%-- 		<c:if test="${!status.last}">,</c:if> --%>
		  ${!status.last ? ',' : ''}
		</c:forEach>
		</td></tr>		
		<script type="text/javascript">
		
// 		$(function(){
// 			console.log('${health_prog_active}');
// 		});
		
		function addNewRound(){
			
// 			var hp_active = '${health_prog_active}' ;
			
// 			if(hp_active == false || hp_active == 'false'){
// 				alert('health program is inactive');
// 				return false;
// 			}
// 			else if(hp_active == true || hp_active == 'true'){
// 				window.location = "addprogramround.htm?center="+$('#vaccinationCenterId option:selected').text()+"&program="+$('#healthProgramId option:selected').text()+"&centerId="+$('#vaccinationCenterId').val()+"&programId="+$('#healthProgramId').val();
// 				centerId="+$('#vaccinationCenterId').val()+"&
				window.location = "addprogramround.htm?programId="+$('#healthProgramId').val();
				
// 			}		
		}
		</script>
<!-- 		<tr><td colspan="5" class="headerrow-light left">Rounds</td></tr> -->
		<tr>
		<th>Name</th>
		<th>Start Date</th>
		<th>End Date</th>
		<th>Is Active</th>
		<th>edit</th>
		</tr>
		<c:if test="${not empty model.rounds}">
		<c:forEach var="round" items="${model.rounds}">
		<tr>
		<td>${round.name}</td>
		<td><fmt:formatDate value="${round.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
		<td><fmt:formatDate value="${round.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
		<td>${round.isActive}</td>
		<td><a href="editprogramround.htm?roundIdd=${round.roundId}" class="icon"><img alt="edit" src="images/edit-icon.png" class="icon"></a></td>
		</tr>
		</c:forEach>
		</c:if>	
		</table>
			
	</div>
	</c:if>
