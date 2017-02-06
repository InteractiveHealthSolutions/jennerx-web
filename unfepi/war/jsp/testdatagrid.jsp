<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral"%>

<script type="text/javascript">

var dgSummaryEnrollmentByCenter = "dgSummaryEnrollmentByCenterTable";

$( document ).ready(function() {
	
	
	var paramMap = new Object();
	paramMap['abc'] = 1;
	paramMap['def'] = 2;
	
	console.log(paramMap);
	DWRReportService.getSummaryEnrollmentByCenter(paramMap, function(data) {
		console.log(data.rows);
	});	
	
	
	$('#'+dgSummaryEnrollmentByCenter).datagrid({
		
		url: DWRReportService.getSummaryEnrollmentByCenter,		
		
		columns:[[
			{field:'centerProgramName', title: 'Center Name', width: 170, sortable: true,
				formatter: function(value,row,index){
					return row.centerProgramName;
				}
			},
			{field:'totalEnrollments', title: 'Total', width: 80, align: 'right', sortable: true,
				formatter: function(value,row,index){
					return row.totalEnrollments;
				}
			},
			{field:'bcgEnrollments', title: 'BCG  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.bcgEnrollments, row.bcgEnrollmentPercent);
 				}
			},
			{field:'p1Enrollments', title: 'Penta-1  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.p1Enrollments, row.p1EnrollmentPercent);
				}
			},
			{field:'p2Enrollments', title: 'Penta-2  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.p2Enrollments, row.p2EnrollmentPercent);
				}
			},
			{field:'p3Enrollments', title: 'Penta-3  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.p3Enrollments, row.p3EnrollmentPercent);
				}
			},
			{field:'m1Enrollments', title: 'Measles-1  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.m1Enrollments, row.m1EnrollmentPercent);
				}
			},
			{field:'m2Enrollments', title: 'Measles-2  (%)', width: 115, align: 'right', 
				formatter: function(value,row,index){
					return addPercentage(row.m2Enrollments, row.m2EnrollmentPercent);
				}
			},
			
		]],
		});
	
});

function getInPercent(value,total) {
	var prc = Math.round(100*value/total);
	var v = isNaN(prc)?"":prc;
	return v;
}

function addPercentage(value, percentage) {
	if(value != null && value != ""){
		return value+
		"<span style='padding-left: 10px; color: gray;'>("+percentage+")</span>";		
	}
	
	return "";
}

</script>

<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="Enrollment by Center">	
		<table id="dgSummaryEnrollmentByCenterTable" class="easyui-datagrid" ></table>
	</div>
</div>
