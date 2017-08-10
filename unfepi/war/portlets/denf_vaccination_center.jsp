
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.model.VaccinationCenter.CenterType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--
function subfrm(){
	var vacccenter=document.getElementById("cityId").value;
	if(vacccenter==null ||vacccenter==''){
		alert('Please select the City vaccination Center is located in');
		return;
	}
	
/* 	var programId;
	var asgnsno = document.getElementById("vaccinationCenterIdAssigned").value;
	var reg = /^[0-9]+/;
	if(reg.test(asgnsno)!=true || asgnsno.length < 3){
		alert('Please specify a valid numeric and 3 digits serial number part of programId assigned to vaccination center');
		return;
	}

	programId = document.getElementById("autogenIdPart").value+""+document.getElementById("vaccinationCenterIdAssigned").value;
				
	DWRVaccinationCenterService.isIdExists(programId,{
		async: false,
		callback: function (res2) {
			if(res2){
				alert('Cannot submit registration for id. Check if another center have been submitted with same id');
				return;
			}
			
			doconfirmunload = false;
			if(confirm("Are you sure you want to submit data ?")){
				submitThisForm();
			}
	}}); */
	
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
		<td>Sous-prefecture / City<span class="mendatory-field">*</span></td>
		<td>
            <select id="cityId" name="cityId" onchange="treeDataLoaderLocations(this.value);" bind-value="${cityIdselected}">
                  <option></option>
            </select>
    <script><!--
        $(document).ready(function() {
        	 DWREntityService.getLocationListByLevel(["1","2"],null,{
                 async: false,
                 callback: function (resl) {
                         $('#cityId').empty().append('<option></option>');
                         for ( var i = 0; i < resl.length; i++) {
                                 $('#cityId').append('<option value="'+resl[i].locationId+'">'+resl[i].fullname+'</option>');
                         }
                         $('#cityId').val($('#cityId').attr('bind-value'));
            }});
        });
    //--></script>

		</td>
	</tr>
	<tr>
		<td>Site Location<span class="mendatory-field">*</span></td>
        <td>
       <input name="centerLocation" id="cc" class="easyui-combotree" style="width:250px;"/>
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

	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.vaccinationCenter.description">
			<textarea name="${status.expression}" maxlength="255"></textarea>
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
