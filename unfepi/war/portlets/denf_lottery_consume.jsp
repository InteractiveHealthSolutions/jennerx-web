<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<script type="text/javascript">
<!--
function subfrm() {
	var childId=document.getElementById("childId").value;

	var amount=document.getElementById("amount").value;
	var verificationCode=document.getElementById("verificationCode").value;
	var storekeeperId=document.getElementById("storekeeperId").value;
	var dateTransacted=convertToDate(document.getElementById("dateTransacted").value);
	
	var reg = /^[0-9]+$/;
	
	if(reg.test(childId)!=true){
		alert('Malformed ID: Specify a valid and 14 digit ProgramId of child');
		return;
	}
	
	if(reg.test(amount)!=true || amount.length < 3){
		alert('Invalid amount');
		return;
	}
	
	if(reg.test(verificationCode)!=true || verificationCode.length < 6){
		alert('Invalid verification code');
		return;
	}
	
	if(storekeeperId == ''){
		alert('Storekeeper not specified');
		return;
	}
	
	if(dateTransacted == '' || dateTransacted == null){
		alert('No transaction date specified');
		return;
	}
		
	if(confirm("Are you sure you want to submit data ?")){
		DWRChildService.consumeLottery(childId, amount, verificationCode, storekeeperId, dateTransacted,  {
			async: false,
			callback: function (res2) {
				showMsg(res2);
		}});
	}
}

function showMsg(msg) {
	document.getElementById("messageDiv").style.display = "table";
	document.getElementById("divResultMsg").innerHTML = msg;
}

//-->
</script>
<form method="post" id="frm" name="frm" >
<table class="denform-h">
<tr>
	<td colspan="2">
	<div id="messageDiv" style="width: 100%; display: none;">
	<div id="divResultMsg" style="color: red;display: inline-block;"></div>
	<span class="closeButton" onclick="document.getElementById('messageDiv').style.display='none';">X</span>
	</div></td>
</tr>
    <tr>
    	<td>Child ID (Program Id) <span class="mendatory-field">*</span></td>
        <td>
           	<input id="childId" name="childId" value="" maxlength="14">
        </td>
    </tr>
	<tr>
		<td>Amount</td>
		<td><input id="amount" name="amount" maxlength="3" type="text"/></td>
	</tr>
	<tr>
		<td>Verification Code</td>
		<td><input id="verificationCode" name="verificationCode" maxlength="6" type="text"/></td>
	</tr>
	<tr>
		<td>Storekeeper <span class="mendatory-field">*</span></td>
		<td>
            <select id="storekeeperId" name="storekeeperId">
                <option></option>
                <c:forEach items="${storekeepers}" var="stk"> 
                <option value="${stk.mappedId}">${stk.idMapper.identifiers[0].identifier} : ${stk.firstName} ${stk.lastName}</option>
            	</c:forEach> 
            </select>
		</td>
	</tr>
	<tr>
		<td>Date Transacted<span class="mendatory-field">*</span></td>
        <td>
        <input id="dateTransacted" name="dateTransacted" value=""  maxDate="+0d" class="calendarbox" />
    	</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"/></td>
	</tr>
</table>
</form>