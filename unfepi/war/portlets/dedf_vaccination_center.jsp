
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.model.VaccinationCenter.CenterType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--

function subfrm(){
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<!-- DONOT REMOVE -->
<input type="hidden" value="${editvaccinedays}"/>
<table class="denform-h">

	<tr>
		<td>Area</td>
        <td> <input name="centerLocation" id="cc" class="easyui-combotree" style="width:250px;"/>
			<script type="text/javascript">
				$( document ).ready(function() {
				$('#cc').combotree({
				    required: true,
				    loader: treeDataLoaderLocations
				    });
				    
				});
			
			function treeDataLoaderLocations(parentId){
				//alert(JSON.stringify(parentId));
				DWREntityService.getLocationHierarchy({"parentId": (isNaN(parentId)?"":parentId)}, 
						{callback: function(result) {
							$('#cc').combotree('clear');
							$('#cc').combotree('loadData' ,result);
							$('#cc').combotree('setValue', '${command.vaccinationCenter.idMapper.identifiers[0].location.locationId}');
						}, async: false, timeout: 5000});
			}
			</script>
    	</td>
	</tr>
    <tr>
		<td>Date Registered<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.vaccinationCenter.dateRegistered">
        <input id="dateRegistered" name="vaccinationCenter.dateRegistered" value="${status.value}" maxDate="+0d" class="calendarbox"/>
      	<br><span class="error-message"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
    	</td>
	</tr>
    <tr>
    	<td>Project ID (Program Id) : </td>
        <td><div id="idContainerDiv" class="hltext1">${command.vaccinationCenter.idMapper.identifiers[0].identifier}</div>
        </td>
    </tr>

    <tr>
        <td>Site Name(unique and identifiable): <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.vaccinationCenter.name">
             <input type="text" id="name" name="vaccinationCenter.name" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Site Full Name : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.vaccinationCenter.fullName">
             <input type="text" id="fullName" name="vaccinationCenter.fullName" maxlength="50" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
<c:if test="${not empty editvaccinedays && editvaccinedays == true}">
    <tr>
        <td colspan="2" class="separator-heading">VACCINE DAYS</td>
    </tr>
    <tr>
    	<td colspan="2">
	    <table class="previousDataDisplay">
	    <c:forEach items="${command.vaccineDayMapList}" var="vdm" varStatus="vdmlistvarsta">
	    	<tr>
	    		<td colspan="12" class="headerrow">
	    		<input type="checkbox" 
	    			onclick='var checkBoxes = $(".${vdm["vaccine"].name} input");checkBoxes.attr("checked", !checkBoxes.attr("checked"));'> ${vdm['vaccine'].name}<%-- :${vdmlistvarsta.index} --%>
	    		<%-- <input type="hidden" name="command.vaccineDayMapList[${vdmlistvarsta.index}]['vaccine'].name"> --%>
    			</td>
    		</tr>
    		<tr class="${vdm['vaccine'].name}">
	    		<td class="columnHeadingDiv">
	    		<c:forEach items="${command.calendarDays}" var="calday" varStatus="caldayvarsta">
	    		<div class="columnHeadingDiv">
	    			<spring:bind path="command.vaccineDayMapList[${vdmlistvarsta.index}]['daylist'][${caldayvarsta.index}]">
	    				<c:choose>
	    				<c:when test="${not empty status.value}">
		    				<input type="checkbox" name="${status.expression}" value="${calday.dayFullName}" checked="checked">${calday.dayFullName}<%-- :${caldayvarsta.index} --%>
	    				</c:when>
	    				<c:otherwise>
		    				<input type="checkbox" name="${status.expression}" value="${calday.dayFullName}" >${calday.dayFullName}<%-- :${caldayvarsta.index} --%>
	    				</c:otherwise>
	    				</c:choose>
	    			</spring:bind>
	    		</div>
    			<c:if test="${caldayvarsta.index %2 ==0}"><br></c:if>
	    		</c:forEach> 
	    		</td>
	    	</tr>
	    </c:forEach>
   	    </table>
    	</td>
    </tr>
</c:if>
	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.vaccinationCenter.description">
			<textarea name="${status.expression}" maxlength="255">${status.value}</textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<% ServiceContext sc = (ServiceContext)request.getAttribute("sc");
 	//must close session here; or do it in finally of respective controller else can create memory leak
 	if(sc!=null)sc.closeSession();%>
    <tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>
