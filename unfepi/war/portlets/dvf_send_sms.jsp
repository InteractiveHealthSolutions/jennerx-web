<%@page import="org.ird.unfepi.constants.WebGlobals.DGUserSmsFieldNames"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.PagerParams"%>
<%@page import="org.ird.unfepi.model.Child"%>
<script type="text/javascript">
<!--
var dgSelectedRecipientListId = "dgRecipientList";
var dgChooseRecipientId = "test";

var dgChoosenData = new Object();
dgChoosenData['rows']=[];

var paramMap = new Object();
paramMap['<%=DWRParamsGeneral.entityRole.name()%>'] = "";

window.onload = function () 
{
	// LOAD VACCINATION CENTERS
	$('#cmbVaccinationCenter').combobox({  
	    valueField:'id',  
	    textField:'text' ,
	    required:true,  
	    editable: false,
	    disabled: true,
	    mode: 'remote',
	    url: DWRVaccinationCenterService.getAllVaccinationCenters
	}); 
	
	// DATAGRID FOR CHOOSING RECIPIENTS
	$('#'+dgChooseRecipientId).datagrid({
		title:'Recipients',
		iconCls:'icon-reminder', 
		width:700,
		height:350,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		collapsible:false, 
		checkOnSelect: false,
		queryParams: paramMap,
		sortName: '<%=DGUserSmsFieldNames.programId%>', 
		sortOrder: 'desc', 
		idField:'<%=DGUserSmsFieldNames.contactNumberId%>', 
		url: DWREntityService.getContactNumberList,
		frozenColumns:[[
	              {field:'ck',checkbox:true},
	              {title:'Program Id',field:'<%=DGUserSmsFieldNames.programId%>',width:120,sortable:true}
		]], 
		columns:[[
			{field:'<%=DGUserSmsFieldNames.number%>',title:'Number',width:120},
			{field:'<%=DGUserSmsFieldNames.numberType%>',title:'Number Type',width:120},
			{field:'<%=DGUserSmsFieldNames.roleName%>',title:'Role',width:120} 
		]],
		pagination:true,
		rownumbers:true,
		pageSize: '<%=WebGlobals.PAGER_PAGE_SIZE%>',
		toolbar: '#tbChooseRecipientList'
	});
	
	var p = $('#'+dgChooseRecipientId).datagrid('getPager');
	$(p).pagination({showPageList: false}); 
	
	// DATAGRID FOR SELECTED RECIPIENTS
	$('#'+dgSelectedRecipientListId).datagrid({
		title:'Specify Recipients',
		iconCls:'icon-reminder', 
		width:450,
		height:350,
		nowrap: true, 
		autoRowHeight: false,
		noheader: true,
		showHeader: true,
		checkOnSelect: false,
		collapsible:false, 
		idField:'<%=DGUserSmsFieldNames.contactNumberId%>', 
		loader: loadRecipientListData, 
		frozenColumns:[[
	              {field:'ck',checkbox:true,width:25},
		]], 
		columns:[[
			{field:'<%=DGUserSmsFieldNames.number%>',title:'Number',width:90},
			{field:'<%=DGUserSmsFieldNames.roleName%>',title:'Role',width:80,
				styler: function(value,row,index){
					return 'font-size:xx-small;';
				}
			},
			{field:'<%=DGUserSmsFieldNames.programId%>',title:'Program Id',width:90},
		]],
		toolbar: '#tbRecipientList',
		rownumbers:true
	});
};

function getParamMap(k) {
    return paramMap[k];
}

function loadGridData(){
	var entRole = $('#cmbRoleName').combobox('getValue');
	if(entRole == ''){
		alert('Role must be specified');
		return;
	}
	paramMap['<%=DWRParamsGeneral.entityRole.name()%>'] = entRole;
	paramMap['<%=DWRParamsGeneral.vaccinationCenter.name()%>'] = $('#cmbVaccinationCenter').combobox('getValue');

	$('#'+dgChooseRecipientId).datagrid('load'  ,paramMap) ;
}


function addDataToDatagrid(data) {
	var duplicateNums = "";
	for ( var i = 0; i < data.length; i++) {
		if(findInRecipientList(data[i].<%=DGUserSmsFieldNames.number%>)){
			duplicateNums += data[i].<%=DGUserSmsFieldNames.number%>+"; ";
		}
		else{
			dgChoosenData['rows'] = dgChoosenData['rows'].concat(data[i]);
		}
	}
	
	if(duplicateNums != ""){
		alert('Following number already exists or duplicated, hence not added to list :'+ duplicateNums);
	}
}
function loadRecipientListData(){
	try{
	$('#'+dgSelectedRecipientListId).datagrid('loadData'  ,dgChoosenData);
	}catch (e) {
		alert('exception:'+e);
	}
	return false;
}

function validateCellNum(cellnum){
	var reg = /^03([0-9]{9,9})$/;
	 if(reg.test(cellnum) == false) {
	      return false;
	 }
	 return true;
}

function findInRecipientList(numberAdded) {
	var allrows = $('#'+dgSelectedRecipientListId).datagrid('getRows');
	
	for ( var i = 0; i < allrows.length; i++) {
		if (allrows[i].number === numberAdded) {
			return true;
		}
	}
	return false;
}
function addCellNumberToRecipientList(txtbox){
	var numberAdded = txtbox.value;
	if(!validateCellNum(numberAdded)){
		alert('Invalid Number. Only numbers of the format 03354444555 are acceptable');
		return;
	}
	var index = $('#'+dgSelectedRecipientListId).datagrid('getRowIndex', numberAdded);
	if(index != -1){
		alert('Given number already exists in the list');
		return;
	}
	
	
	if (findInRecipientList(numberAdded)) {
		alert('Given number already exists in the list');
		return;
	}

	$('#' + dgSelectedRecipientListId).datagrid('appendRow', {
		<%=DGUserSmsFieldNames.contactNumberId%> : numberAdded,
		<%=DGUserSmsFieldNames.number%> : numberAdded,
		<%=DGUserSmsFieldNames.roleName%> : 'MANUAL'
	});
	
	txtbox.value="";
}
function deleteCellNum() {
	var selected = $('#' + dgSelectedRecipientListId).datagrid('getChecked');
	for ( var i = 0; i < selected.length; i++) {
		var index = $('#' + dgSelectedRecipientListId).datagrid('getRowIndex', selected[i].<%=DGUserSmsFieldNames.contactNumberId%>);
		$('#' + dgSelectedRecipientListId).datagrid('deleteRow', index);
	}
}

function showSendMsgTemplate() {
	document.getElementById("sendMsgTemplate").style.display = "block";
	document.getElementById("chooseRecipientsTemplate").style.display = "none";

	$('#' + dgSelectedRecipientListId).datagrid('reload');
}

function showChooseRecipientTemplate() {
	document.getElementById("sendMsgTemplate").style.display = "none";
	document.getElementById("chooseRecipientsTemplate").style.display = "block";
}

function clearDiv() {
	document.getElementById("duplicateNums").innerHTML = "";
}

function sendSMSs() {
	var msg = document.getElementById("txtMsg").value;
	var hrs = document.getElementById("txbvalidityPeriod").value;
	var descr = document.getElementById("txtDescr").value;
	if (hrs > 23 || hrs < 0) {
		alert('Please enter validity period between 0 to 23');
		return;
	}
	
	if (trim(msg) != '') {
		var maplist = $('#' + dgSelectedRecipientListId).datagrid('getRows');
		if (maplist.length != 0) {
			DWRCommunicationService.queueSms(maplist, msg, descr, hrs, processSendMsgResultReturned);
		} 
		else {
			alert('No recipients specified');
		}
	} else {
		document.getElementById("txtMsg").value = "";
		alert('Please enter an appropriate message text');
	}
}
var processSendMsgResultReturned = function(result) {
	if (result == null) {
		window.location = 'login.htm?logmessage=Session expired. Please login again';
		return;
	}
	document.getElementById("txtMsg").value = "";
	document.getElementById("txtDescr").value = "";
	showError(result);
};

function clearmsgDiv() {
	document.getElementById("divSendMsgResult").innerHTML = "";
	document.getElementById("divError").style.display = "none";
}

function showError(errormsg) {
	document.getElementById("divError").style.display = "table";
	document.getElementById("divSendMsgResult").innerHTML = errormsg;
}
//-->
</script>
<div class="denform">
<div id="sendMsgTemplate">
<div id="divError" style=" display: none;">
<div id="divSendMsgResult" style="color: red; display: inline-block;"></div>
<a href="#" onclick="clearmsgDiv();">Clear</a>
</div>
<span style="font-size: xx-small; float: right;">Validity : <input id="txbvalidityPeriod" type="text" value="8" maxlength="2" title="Discard sms if not sent within given hours from now" style="width: 30px">(hrs)
</span>
<textarea id="txtMsg" rows="5" title="Sms text to send" placeholder="Enter sms text here" maxlength="1000" style="width: 100%; resize: none"></textarea> 
<table id="dgRecipientList" class="easyui-datagrid"></table>
<div id="tbRecipientList">

<input id="ipManualRecipientNumber" style="width:100px;" maxlength="11" placeholder="03354444555">
<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addCellNumberToRecipientList(document.getElementById('ipManualRecipientNumber'));">Add</a>
<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteCellNum();"></a>  
<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showChooseRecipientTemplate();">Search</a>  
<a class="easyui-linkbutton" iconCls="icon-save" onclick="sendSMSs();" style="float: right;">Send</a>

</div>
<textarea id="txtDescr" rows="3" style="width: 100%; resize: none" maxlength="255" title="Additional note" placeholder="Additional note for sms record"></textarea> 

</div>
<div id="chooseRecipientsTemplate" style="display: none;">

<div id="tbChooseRecipientList">
<div style="display: inline-block;float: left;">
<select id="cmbRoleName" class="easyui-combobox" name="cmbRoleName" style="width: 150px" 
	data-options="required:true,
				editable: false,
				panelWidth:140,
				panelHeight:100,
				onSelect: function(rec){ 
           			if(rec.value.toLowerCase().indexOf('child') != -1){
           				$('#cmbVaccinationCenter').combobox({disabled:false});
           			}
           			else{
           				$('#cmbVaccinationCenter').combobox({disabled:true});
           			}
       			}">  
    <option value="">Select role (required)</option>  
    <option>Vaccinator</option>  
    <option>Storekeeper</option>  
    <option>Child</option>  
</select>
<input id="cmbVaccinationCenter" value="" style="width: 150px"> 
<a class="easyui-linkbutton" iconCls="icon-search" plain="true" 
	onclick="loadGridData();">Query</a>
</div>
<div class="dialog-tool-separator" style="float: left;"></div>
<div style="display: inline-block;float: right;">
<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="
		var selected = $('#'+dgChooseRecipientId).datagrid('getChecked');
		if(selected.length > 0){
		addDataToDatagrid(selected);
		showSendMsgTemplate();
		$('#'+dgChooseRecipientId).datagrid('uncheckAll');
		}
		else{
			alert('No recipient selected');
		}">Select</a> 
<a class="easyui-linkbutton" iconCls="icon-back" plain="true" onclick="showSendMsgTemplate();">Cancel and Back</a>
</div>  
</div>
		<table id="test" class="easyui-datagrid"></table>
</div>
</div>