<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral"%>
<div class="dvwform centerdivh">
<table>
			<tr>
<!-- 			<td colspan="16"><span class="formheadingS">Project Summary</span> -->
			<%-- <a class="linkiconS iconcsv" href="exportdata?extype=<%=SystemPermissions.DOWNLOAD_SUMMARY_PROJECT_CSV%>"></a> --%></td>
			</tr>
				<tr>
				<th>Total Enrollments</th>
				<th>Total Events</th>
				<th>Average Enrollment per week</th>
				<th>Average Events per week</th>
				
			</tr>
			<tr>
				<td>${model.totalEnrollments}</td>
				<td>${model.successfulEvents}</td>
				<td><fmt:formatNumber maxFractionDigits="1" value="${model.avgEnrollmentsPerWeek}" /></td>
				<td><fmt:formatNumber maxFractionDigits="1" value="${model.avgSuccessfulEventsPerWeek}" /></td>
			</tr>
		</table>
</div>
<br>
<script type="text/javascript">
<!--
var dgSummaryEnrollmentByCenter = "dgSummaryEnrollmentByCenterTable";
var dgSummaryFupAgeAppropriate = "dgSummaryFollowupAgeAppropriate";
var dgSummaryFupAgeAppropriateWRetro = "dgSummaryFollowupAgeAppropriateWRetro";
var dgSummaryFupByVaccinator = "dgSummaryFupByVaccinatorTable";
var dgSummaryFupByCenter = "dgSummaryFupByCenterTable";


var paramMap = new Object();
paramMap['<%=DWRParamsGeneral.vaccinationCenter.name()%>'] = "";

DWRVaccinationCenterService.getAllVaccinationCenters({async: true, callback: function (data) {
	//alert(JSON.stringify(data));
	for ( var i = 0; i < data.length; i++) {
		//alert("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
		$("#vaccinationCentercombo").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
	}
	$("#vaccinationCentercombo").multiselect('refresh');
}});

$( document ).ready(function() {
	
	$('#'+dgSummaryEnrollmentByCenter).datagrid({
		width:980,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		sortName: "totalEnrollments",
		sortOrder: "DESC", 
		selectOnCheck: false,
		singleSelect: true,
		queryParams: paramMap,
		url: DWRReportService.getSummaryEnrollmentByCenter,
		pagination: true,
		pageSize: '<%=WebGlobals.PAGER_PAGE_SIZE%>',
		rowStyler: function(index,row){
			if (row.centerProgramId.toLowerCase() == 'total'){
				return 'background-color:#F3E2A9;color:gray;font-weight: bolder'; // return inline style
				// the function can return predefined css class and inline style
				// return {class:'r1', style:{'color:#fff'}};	
			}
		},
		toolbar: '#toolbarEnrByCenter',
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
	
	// AGE APPROPRIATE 
	try{
	$('#'+dgSummaryFupAgeAppropriate).datagrid({
		width:980,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		selectOnCheck: false,
		singleSelect: true,
		queryParams: paramMap,
		url: DWRReportService.getSummaryFollowupAgeAppropriate,
		pagination: true,
		pageSize: '<%=WebGlobals.PAGER_PAGE_SIZE%>',
		rowStyler: function(index,row){
			if (row.cohort.toLowerCase() == 'total'){
				return 'background-color:#F3E2A9;color:gray;font-weight: bolder'; // return inline style
				// the function can return predefined css class and inline style
				// return {class:'r1', style:{'color:#fff'}};	
			}
		},
		toolbar: '#toolbarFupAgeAppropriate',
		columns:[ 
		          [{title:'Cohort',colspan:2},
		           {title:'Penta1',colspan:2},
		           {title:'Penta2',colspan:2},
		           {title:'Penta3',colspan:2},
		           {title:'Measles1',colspan:2},
		           {title:'Measles2',colspan:2}], 
		          [
					{field:'cohort', title: 'Vaccine', width: 100},
					{field:'cohorttotal', title: 'n', width: 80, align: 'right'},
					{field:'penta1due', title: 'Due', width: 75,   align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							return value;
						}
					},
					{field:'penta1done', title: '%V', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta1due);
							return isNaN(prc)?"":prc;
						}
					},
					{field:'penta2due', title: 'Due', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							return value;
						}
					},
					{field:'penta2done', title: '%V', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta2due);
							return isNaN(prc)?"":prc;
						}
					},
					{field:'penta3due', title: 'Due', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							return value;
						}
					},
					{field:'penta3done', title: '%V', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta3due);
							return isNaN(prc)?"":prc;
						}
					},
					{field:'measles1due', title: 'Due', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							return value;
						}
					},
					{field:'measles1done', title: '%V', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.measles1due);
							return isNaN(prc)?"":prc;
						}
					},
					{field:'measles2due', title: 'Due', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							return value;
						}
					},
					{field:'measles2done', title: '%V', width: 75,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.measles2due);
							return isNaN(prc)?"":prc;
						}
					},]
		      ],
		});
	}catch(e){alert(e);}
	
	// AGE APPROPRIATE WITH RETRO
	try{
	$('#'+dgSummaryFupAgeAppropriateWRetro).datagrid({
		width:980,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		selectOnCheck: false,
		singleSelect: true,
		queryParams: paramMap,
		url: DWRReportService.getSummaryFollowupAgeAppropriateWRetro,
		pagination: true,
		pageSize: '<%=WebGlobals.PAGER_PAGE_SIZE%>',
		rowStyler: function(index,row){
			if (row.cohort.toLowerCase() == 'total'){
				return 'background-color:#F3E2A9;color:gray;font-weight: bolder'; // return inline style
				// the function can return predefined css class and inline style
				// return {class:'r1', style:{'color:#fff'}};	
			}
		},
		toolbar: '#toolbarFupAgeAppropriateWRetro',
		columns:[ 
		          [{title:'Cohort',colspan:2},
		           {title:'BCG',colspan:2},
		           {title:'Penta1',colspan:2},
		           {title:'Penta2',colspan:2},
		           {title:'Penta3',colspan:2},
		           {title:'Measles1',colspan:2},
		           {title:'Measles2',colspan:2}], 
		          [
					{field:'cohort', title: 'Vaccine', width: 100},
					{field:'cohorttotal', title: 'n', width: 65, align: 'right'},
					{field:'bcgdue', title: 'Due', width: 65,   align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 1){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'bcgdone', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.bcgdue);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 1){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},
					{field:'penta1due', title: 'Due', width: 65,   align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 2){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'penta1done', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta1due);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 2){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},
					{field:'penta2due', title: 'Due', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 3){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'penta2done', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta2due);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 3){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},
					{field:'penta3due', title: 'Due', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 4){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'penta3done', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.penta3due);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 4){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},
					{field:'measles1due', title: 'Due', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 5){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'measles1done', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.measles1due);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 5){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},
					{field:'measles2due', title: 'Due', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							if(row.vaccineId > 6){
								return "<span style='color:gray'>"+value+"</span>";
							}
							return value;
						}
					},
					{field:'measles2done', title: '%V', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							if(hideColumnContent(value,row,index, this)){
								return "";
							}
							
							var prc = Math.round(100*value/row.measles2due);
							var v = isNaN(prc)?"":prc;
							
							if(row.vaccineId > 6){
								return "<span style='color:gray'>"+v+"</span>";
							}
							
							return v;
						}
					},]
		      ],
		});
	
	}catch(e){alert(e);}
	
	// SUMMARY FUP BY VACCINATOR
	try{
	$('#'+dgSummaryFupByVaccinator).datagrid({
		width:980,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		selectOnCheck: false,
		singleSelect: true,
		queryParams: paramMap,
		url: DWRReportService.getSummaryFupByVaccinator,
		pagination: true,
		pageSize:20,
		rowStyler: function(index,row){
			if (row.identifier.toLowerCase() == 'total'){
				return 'background-color:#F3E2A9;color:gray;font-weight: bolder'; // return inline style
				// the function can return predefined css class and inline style
				// return {class:'r1', style:{'color:#fff'}};	
			}
		},
		toolbar: '#toolbarFupByVaccinator',
		columns:[[
					{field:'identifier', title: 'ID', width: 60},
					{field:'name', title: 'Name', width: 130},
					{field:'incentives', title: 'Incentives', width: 65},
					{field:'total', title: 'Total', width: 65, align: 'right'},
					
					{field:'bcg', title: 'BCG(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p1', title: 'Penta1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p2', title: 'Penta2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p3', title: 'Penta3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'m1', title: 'Measles1(%)', width: 80,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'m2', title: 'Measles2(%)', width: 80,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv0', title: 'OPV0(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv1', title: 'OPV1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv2', title: 'OPV2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv3', title: 'OPV3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv1', title: 'PCV1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv2', title: 'PCV2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv3', title: 'PCV3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					}

			]]
		});
	}catch(e){alert(e);}
	
	// SUMMARY FUP BY CENTER
	try{
	$('#'+dgSummaryFupByCenter).datagrid({
		width:980,
		nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		selectOnCheck: false,
		singleSelect: true,
		queryParams: paramMap,
		url: DWRReportService.getSummaryFupByCenter,
		pagination: true,
		pageSize:20,
		rowStyler: function(index,row){
			if (row.identifier.toLowerCase() == 'total'){
				return 'background-color:#F3E2A9;color:gray;font-weight: bolder'; // return inline style
				// the function can return predefined css class and inline style
				// return {class:'r1', style:{'color:#fff'}};	
			}
		},
		toolbar: '#toolbarFupByCenter',
		columns:[[
					{field:'identifier', title: 'ID', width: 60},
					{field:'name', title: 'Name', width: 130},
					{field:'total', title: 'Total', width: 65, align: 'right'},
					
					{field:'bcg', title: 'BCG(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p1', title: 'Penta1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p2', title: 'Penta2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'p3', title: 'Penta3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'m1', title: 'Measles1(%)', width: 80,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'m2', title: 'Measles2(%)', width: 80,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv0', title: 'OPV0(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv1', title: 'OPV1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv2', title: 'OPV2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'opv3', title: 'OPV3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv1', title: 'PCV1(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv2', title: 'PCV2(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					},
					{field:'pcv3', title: 'PCV3(%)', width: 65,    align: 'right', 
						formatter: function(value,row,index){
							return getInPercent(value,row.total);
						}
					}

			]]
		});
	}catch(e){alert(e);}

	// INIT CENTER FILTER
	$("#vaccinationCentercombo").multiselect({height: 120, noneSelectedText: 'Select center',
		selectedText: '# centers selected', 
		close: function(){
			refreshDatagrids();
		},
	}); 
	
});

function getInPercent(value,total) {
	var prc = Math.round(100*value/total);
	var v = isNaN(prc)?"":prc;
	return v;
}

function hideColumnContent(value,row,index, column) {
	if(value==null || column.field.toLowerCase().startsWith(row.cohort.toLowerCase())){
		return true;
	}
	
	return false;
}

function refreshDatagrids() {
	try{
		var centerparam = $('#vaccinationCentercombo').val()!=null?$('#vaccinationCentercombo').val().join(','):"";
		paramMap['<%=DWRParamsGeneral.vaccinationCenter.name()%>'] = centerparam;
		paramMap['<%=DWRParamsGeneral.date1from.name()%>'] = $('#filterDatefrom').val();
		paramMap['<%=DWRParamsGeneral.date1to.name()%>'] = $('#filterDateto').val();
		
		$('#'+dgSummaryEnrollmentByCenter).datagrid('load', paramMap);
		$('#'+dgSummaryFupAgeAppropriate).datagrid('load', paramMap);
		$('#'+dgSummaryFupAgeAppropriateWRetro).datagrid('load', paramMap);
		$('#'+dgSummaryFupByVaccinator).datagrid('load', paramMap);
		$('#'+dgSummaryFupByCenter).datagrid('load', paramMap);
	}catch(e){alert("ERROR:refreshDatagrids:"+e);}
}
function filterDateChanged() {
	if(convertToDate($('#filterDatefrom').val()) != null 
			&& convertToDate($('#filterDateto').val()) != null){
		refreshDatagrids();
	}
}

function addPercentage(value, percentage) {
	if(value != null && value != ""){
		return value+
		"<span style='padding-left: 10px; color: gray;'>("+percentage+")</span>";		
	}
	
	return "";
}

//-->
</script>
<form>
<div class="searchpalette right">
<select id="vaccinationCentercombo" name="vaccinationCentercombo" multiple="multiple"></select>
Date 
        <input id="filterDatefrom" name="filterDatefrom" class="calendarbox"  readonly="readonly" value=""/> 
		<input id="filterDateto" name="filterDateto" class="calendarbox"  readonly="readonly" value=""/>
		<a class="clearDate" onclick="clearFilterDate();">X</a>
		<script>
		function clearFilterDate(){	
			$('input[id^="filterDate"]').val("");
			refreshDatagrids();
		}
		
		$(function() {
			$('input[id^="filterDate"]').datepicker({
		    	duration: '',
		        constrainInput: false,
		        maxDate: "+0d",
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		        onSelect: filterDateChanged
		     });
		});
		</script>
</div>
<script type="text/javascript">
function exportCSV(csvType){
	var centerparam = $('#vaccinationCentercombo').val()!=null?$('#vaccinationCentercombo').val().join(','):"";
	window.location="exportdata?extype="+csvType+
			"&<%=DWRParamsGeneral.vaccinationCenter.name()%>="+centerparam+
			"&<%=DWRParamsGeneral.date1from.name()%>="+$('#filterDatefrom').val()+
			"&<%=DWRParamsGeneral.date1to.name()%>="+$('#filterDateto').val();
}

function exportTableToCSV($table, filename) {
    var $rows = $table.find('tr:has(td)'),

    // Temporary delimiter characters unlikely to be typed by keyboard
    // This is to avoid accidentally splitting the actual contents
    tmpColDelim = String.fromCharCode(11), // vertical tab character
    tmpRowDelim = String.fromCharCode(0), // null character

    // actual delimiter characters for CSV format
    colDelim = '","',
    rowDelim = '"\r\n"',

    // Grab text from table into CSV formatted string
    csv = '"' + $rows.map(function (i, row) {
        var $row = $(row),
            $cols = $row.find('td');

        return $cols.map(function (j, col) {
            var $col = $(col),
                text = $col.text();

            return text.replace('"', '""'); // escape double quotes

        }).get().join(tmpColDelim);

    }).get().join(tmpRowDelim)
        .split(tmpRowDelim).join(rowDelim)
        .split(tmpColDelim).join(colDelim) + '"',

    // Data URI
    csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

    $(this).attr({
        'download': filename,
        'href': csvData,
        'target': '_blank'
    });
}

// This must be a hyperlink
$(".export").on('click', function (event) {
    // CSV
    exportTableToCSV.apply(this, [$('#dvData>table'), 'export.csv']);

    // IF CSV, don't do event.preventDefault() or return false
    // We actually need this to be a typical hyperlink
});
</script>
<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="Enrollment by Center">
		<div class="headingRuleUp" id="toolbarEnrByCenter" style="background-color: transparent;" >
			<!-- <select id="vaccinationCentercombo1" multiple="multiple"></select> -->
			<span class="formheadingM">Summary Enrollment by Vaccination Center </span>
			<a class="linkiconS iconcsv" onclick="exportCSV('<%=SystemPermissions.DOWNLOAD_SUMMARY_ENROLLMENT_BY_CENTER_CSV%>');"></a>
		</div>
		<table id="dgSummaryEnrollmentByCenterTable" class="easyui-datagrid" ></table>
	</div>
	<div title="Age Appropriate Coverage">
		<div class="headingRuleUp" id="toolbarFupAgeAppropriate" style="background-color: transparent;" >
		<!-- <select id="vaccinationCentercombo2" multiple="multiple"></select> -->
			<span class="formheadingM">Age-appropriate Immunization Completion Rates by Vaccine Cohort </span>
			<a class="linkiconS iconcsv" onclick="exportCSV('<%=SystemPermissions.DOWNLOAD_SUMMARY_IMUNIZATION_AGE_APROPRIATE_CSV%>');"></a>
		</div>
		<table id="dgSummaryFollowupAgeAppropriate" class="easyui-datagrid">
			<tr>
				<th colspan="2">Cohort</th>
				<th colspan="2">Penta1</th>
				<th colspan="2">Penta2</th>
				<th colspan="2">Penta3</th>
				<th colspan="2">Measles1</th>
				<th colspan="2">Measles2</th>
			</tr>
		</table>
	</div>
	<div title="Age Appropriate Coverage With Retro">
		<div class="headingRuleUp" id="toolbarFupAgeAppropriateWRetro" style="background-color: transparent;" >
		<!-- <select id="vaccinationCentercombo3" multiple="multiple"></select> -->
			<span class="formheadingM">Age-appropriate Immunization Completion Rates by Vaccine Cohort (With Retro)</span>
			<a class="linkiconS iconcsv" onclick="exportCSV('<%=SystemPermissions.DOWNLOAD_SUMMARY_IMUNIZATION_AGE_APROPRIATE_WITH_RETRO_CSV%>');"></a>
		</div>
		<table id="dgSummaryFollowupAgeAppropriateWRetro" class="easyui-datagrid">
			<tr>
				<th colspan="2">Cohort</th>
				<th colspan="2">BCG</th>
				<th colspan="2">Penta1</th>
				<th colspan="2">Penta2</th>
				<th colspan="2">Penta3</th>
				<th colspan="2">Measles1</th>
				<th colspan="2">Measles2</th>
			</tr>
			<!-- <tr>
				<th field="cohort" width="80">Vaccine</th>
				<th field="cohorttotal" width="80">n</th>
				<th field="penta1due">Due</th>
				<th field="penta1done">%V</th>
				<th field="penta2due">Due</th>
				<th field="penta2done">%V</th>
				<th field="penta3due">Due</th>
				<th field="penta3done">%V</th>
				<th field="measles1due">Due</th>
				<th field="measles1done">%V</th>
				<th field="measles2due">Due</th>
				<th field="measles2done">%V</th>
			</tr> -->
		</table>
	</div>
	<div title="Immunizations by Vaccinator">
		<div class="headingRuleUp" id="toolbarFupByVaccinator" style="background-color: transparent;" >
			<span class="formheadingM">Summary Immunization by Vaccinator </span>
			<a class="linkiconS iconcsv" onclick="exportCSV('<%=SystemPermissions.DOWNLOAD_SUMMARY_IMMUNIZATION_BY_VACCINATOR_CSV%>');"></a>
		</div>
		<table id="dgSummaryFupByVaccinatorTable" class="easyui-datagrid" ></table>
	</div>
		<div title="Immunizations by Center">
		<div class="headingRuleUp" id="toolbarFupByCenter" style="background-color: transparent;" >
			<span class="formheadingM">Summary Immunization by Center </span>
			<a class="linkiconS iconcsv" onclick="exportCSV('<%=SystemPermissions.DOWNLOAD_SUMMARY_IMMUNIZATION_BY_CENTER_CSV%>');"></a>
		</div>
		<table id="dgSummaryFupByCenterTable" class="easyui-datagrid" ></table>
	</div>
</div>
</form>