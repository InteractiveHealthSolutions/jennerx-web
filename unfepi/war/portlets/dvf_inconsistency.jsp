<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.GlobalParams.VariableSettingType"%>
<%@page import="org.ird.unfepi.GlobalParams"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DGInconsistenciesFieldNames"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<script type="text/javascript">
<!--
var dgChooseRecipientId = "inconsistenciesGrid";

var paramMap = new Object();
paramMap['<%=DWRParamsGeneral.vaccinationCenter.name()%>'] = "";

window.onload = function () 
{
	// DATAGRID FOR CHOOSING RECIPIENTS
	$('#'+dgChooseRecipientId).datagrid({
		title:'Inconsistencies',
		iconCls:'icon-database-error', 
		width:900,
		height:350,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		collapsible:false, 
		checkOnSelect: false,
		queryParams: paramMap,
		sortName: '<%=DGInconsistenciesFieldNames.rowCount%>', 
		sortOrder: 'desc', 
		idField:'<%=DGInconsistenciesFieldNames.uniqueName%>', 
		url: DWRAdminTaskService.getSystemInconsistencies,
		frozenColumns:[[
		    {title:'download', field:'type',
		    	formatter: function(value,row,index){
		    		try{
		    			if('<%=UserSessionUtils.hasActiveUserPermission(SystemPermissions.DOWNLOAD_DATA_INCONSISTENCY_CSV,request)%>'){
				    		return "<a href='exportdata?extype=<%=SystemPermissions.DOWNLOAD_DATA_INCONSISTENCY_CSV%>&"+'<%=DGInconsistenciesFieldNames.uniqueName%>'+"="+row['<%=DGInconsistenciesFieldNames.uniqueName%>']+"' class='linkiconS iconcsv'></a>";
			    		}
		    			else{
		    				return "n/a";
		    			}
		    		}
		    		catch (e) {
						alert('Unable to authenticate download');
					}
		    		
		    	}
		    },         
	        {title:'Type',field:'<%=DGInconsistenciesFieldNames.uniqueName%>', width: 300,}
		]], 
		columns:[[
			{field:'<%=DGInconsistenciesFieldNames.rowCount%>',title:'# of Rows', width: 100,},
			{field:'<%=DGInconsistenciesFieldNames.description%>',title:'Description', width: 300,}
		]],
		pagination:false,
		rownumbers:true,
	});
};

function getParamMap(k) {
    return paramMap[k];
}
//-->
</script>
<table class = "datadisplayform">
	<tr>
	<td>
		<table id="inconsistenciesGrid" class="easyui-datagrid"></table>
	</td></tr>
</table>
