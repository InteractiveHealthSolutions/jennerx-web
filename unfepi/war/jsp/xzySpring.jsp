<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral"%>

<script type="text/javascript">


$( document ).ready(function() {
	
	
	var paramMap = new Object();
	paramMap['abc'] = 1;
	paramMap['def'] = 2;
	
// 	$.get( "homepage.htm" , function( data ) {
// 		console.log(data);		
// 	});
	
	$('#healthprogramtable').datagrid({
		
		url: "homepage.htm",
		columns:[[
		          { field:'programId', title:'Program Id', sortable: true},
		          { field:'name', title:'Program Name', sortable: true},
				  { field:'vaccinationcalendarId', title:'Calendar Id'}
		]]
	});
	
});
</script>

<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="Enrollment by Center">	
		<table id="healthprogramtable" class="easyui-datagrid" ></table>
	</div>
</div>
