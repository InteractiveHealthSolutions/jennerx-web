<%@page import="org.ird.unfepi.model.VaccinationCenter.CenterType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--
window.onbeforeunload = function (e) {
	  var e = e || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
	};

var globalDTf="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>";
var globalDOf="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>";

function convertToDate(stringDate) {
	try{
		var datec=Date.parseExact(stringDate,globalDTf);
		if(datec == null){
			datec=Date.parseExact(stringDate,globalDOf);
		}
		
		return datec;
	}
	catch (e) {
		return null;
	}
}

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
<span id="pageHeadingTextDiv" style="color: #FF7A7A">Vaccination Center <span style="font-size: medium;">(New)</span></span>

<p id="vaccinationCenterCountDisplayp" style="color: maroon;width: 100%;text-align: right;padding: 0; margin: 0"></p>
<table class = "mobileForms" style="outline-color : #FF7A7A">
		
		<%@ include file="plt_global_errors.jsp" %>
		
    <tr>
		<td>Date Registered<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.vaccinationCenter.dateRegistered">
        <input id="dateRegistered" name="vaccinationCenter.dateRegistered" value="${status.value}" class="calendarbox" readonly="readonly"/>
		<script>
			$(function() {
			    $('#dateRegistered').datepicker({
			    	duration: '',
			        constrainInput: false,
			        maxDate: '+0d',
			        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
			     });
			});
		</script>
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
		<td>Center Type :<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinationCenter.centerType">
			<input type="hidden" value="${status.value}" id="centerTypeVal"/>
			<select id="centerType" name="vaccinationCenter.centerType">
				<c:forEach items="<%=CenterType.values()%>" var="centerType_value">
					<option>${centerType_value}</option>
				</c:forEach>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
              sel = document.getElementById("centerType");
              val=document.getElementById("centerTypeVal").value;
              makeTextSelectedInDD(sel,val);
              //-->
             </script> 
		</td>
	</tr>
    <tr>
        <td>Center Name(unique and identifiable): <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.vaccinationCenter.name">
             <input type="text" id="name" name="vaccinationCenter.name" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Center Full Name : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.vaccinationCenter.fullName">
             <input type="text" id="fullName" name="vaccinationCenter.fullName" maxlength="50" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td colspan="2" class="separator-heading">VACCINE DAYS</td>
    </tr>
    <tr>
    	<td colspan="2">
	    <c:forEach items="${command.vaccineDayMapList}" var="vdm" varStatus="vdmlistvarsta">
	    <table class="previousDataDisplay">
	    	<tr>
	    		<td>${vdm['vaccine'].name}<%-- :${vdmlistvarsta.index} --%>
	    		<%-- <input type="hidden" name="command.vaccineDayMapList[${vdmlistvarsta.index}]['vaccine'].name"> --%>
    			</td>
	    		<td>
	    		<spring:bind path="command.calendarDays[${vdmlistvarsta.index}]">
    			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
	    		<br>
	    		</spring:bind>
	    		<c:forEach items="${command.calendarDays}" var="calday" varStatus="caldayvarsta">
	    			<spring:bind path="command.vaccineDayMapList[${vdmlistvarsta.index}]['daylist'][${caldayvarsta.index}]">
	    				<c:choose>
	    				<c:when test="${not empty status.value}">
		    				<input type="checkbox" name="${status.expression}" value="${calday.dayFullName}" checked="checked">${calday.dayFullName}<%-- :${caldayvarsta.index} --%>
	    				</c:when>
	    				<c:otherwise>
		    				<input type="checkbox" name="${status.expression}" value="${calday.dayFullName}" >${calday.dayFullName}<%-- :${caldayvarsta.index} --%>
	    				</c:otherwise>
	    				</c:choose>
	    				<br>
	    			</spring:bind>
	    		</c:forEach> 
	    		</td>
	    	</tr>
	    </table>
	    </c:forEach>
    	</td>
    </tr>
	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.vaccinationCenter.description">
			<textarea name="description" maxlength="255"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
    <tr>
        <td>
        <input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();" align="right" style="width: 300px;">
        </td>
        <td>
        </td>
    </tr>
</table>
</form>
