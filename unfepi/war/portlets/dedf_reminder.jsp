<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript"><!--
/*function editRt(){
	var remtxt=document.getElementById("reminderText");
	var txtbx=document.getElementById("val");
	for (i=0; i<remtxt.options.length; i++) {
		if (remtxt.options[i].selected) {
	        txtbx.value=remtxt.options[i].value;
	        document.getElementById("optIndex").value=i;
	        document.getElementById("action").value="edit";
	    }
	}
}*/
function deleteRt(){
	if(confirm("Are you sure to delete the text selected?")){
	var remtxt=document.getElementById("remText");
	for (i=0; i<remtxt.options.length; i++) {
		if (remtxt.options[i].selected) {
			try{
		        remtxt.remove(i,null);//stndrd
		        }catch (e) {
		        	remtxt.remove(i);//ie only
				};
				i--;
	    }
	}
	}
}
function editRt(){
	var remtxt=document.getElementById("remText");
	for (i=0; i<remtxt.options.length; i++) {
		if (remtxt.options[i].selected) {
			document.getElementById("val").value=remtxt.options[i].text;

			try{
		        remtxt.remove(i,null);//stndrd
		        }catch (e) {
		        	remtxt.remove(i);//ie only
				};
			break;
	    }
	}
}
function subAction(){
	var remtxt=document.getElementById("remText");
	var txtbxval=document.getElementById("val").value;
	if(trim(txtbxval)!=""){
		var opt = document.createElement("option");
        
        opt.text = txtbxval;
        opt.value = txtbxval;
        // Add an Option object to Drop Down/List Box
        try{
        document.getElementById("remText").options.add(opt,null);//stndrd
        }catch (e) {
        	document.getElementById("remText").options.add(opt);//ie only
		}
        document.getElementById("val").value="";
	}
}
function SubmitForm(){
	var remtxt=document.getElementById("remText");
	if(remtxt.options.length<1){
		alert("Atleast one Reminder Text should be associated with the reminder");
		return;
	}
	
	for(var count=0; count < remtxt.options.length; count++) {
		if (!remtxt.options[count].selected) {
			remtxt.options[count].selected = true;
		}
	}
	
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}

function trim(s)
{
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}

//-->
</script>
<form method="post" id="frm" name="frm" >
<table class="denform-h">
		<tr>
		<td>Reminder Name : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
        <td>
        <spring:bind path="command.remindername">${status.value}</spring:bind>
        </td>
		</tr>
		<tr>
		<td>Description : </td>
        <td>
        <spring:bind path="command.description">
        <textarea name="description" >${status.value}</textarea>
        <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
        </spring:bind>
        </td>
		</tr>
	<tr>
	<td colspan="2">	
		<div class="headingS">Notations to insert child specific data in Text</div>
		<div class="columnHeadingDiv">
		<br><div>[[Child.FirstName]]</div> for First name part of Current Child
		<br><div>[[Child.MiddleName]]</div> for Middle name part of Current Child
		<br><div>[[Child.LastName]]</div> for Last name part of Current Child
		<br><div>[[Vaccination.Day]]</div> for inserting the week day when vaccination due date was scheduled
		<br><div>[[Vaccination.Date]]</div> for inserting vaccination due date
		<br><div>[[VerificationCode.Amount]]</div> for inserting vaccination due date
		<br><div>[[VerificationCode.Code]]</div> for inserting vaccination due date
		</div>
		
	</td>
	</tr>
	<tr>
	<td colspan="2">Reminder Texts</td>
	</tr>
	<tr>
	<td>
		<select id="remText" name="remText" multiple="multiple" class="listMedium">
			<c:forEach items="${reminderText}" var="remt">
				<option title="${remt}" >${remt}</option>
			</c:forEach>
		</select>
	</td>
	<td>
		<a class="anchorCustom" onclick="deleteRt();">delete selected text</a>
		<br>
		<a class="anchorCustom" onclick="editRt();">edit selected text</a>
		<br>
		<br><textarea id="val" maxlength="300"></textarea>
		<a class="anchorCustom" onclick="subAction();">Add</a>
		</td>
		</tr>
		<tr>
		<td>
        	<input type="button"  id="submitBtn" value="Submit" title="submit" onclick="SubmitForm();">
		</td>
		<td></td>
		</tr>
</table>
</form>