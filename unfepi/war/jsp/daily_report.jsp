<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<style type="text/css">

.dailyreport{
	border-collapse: collapse;
}
.dailyreport td{
	text-align: center;
}
.dailyreport td ,.dailyreport tr, .dailyreport th{
 	border: 1px dotted gray;
/*  	#BB4C07 */
	padding: 3px;
}
.datagrid-wrap{
	border: 1px dotted gray;
}
.dailyreport th{
	font-weight: normal;
}
.dailyreport .td_bold,.dailyreport th{
/* 	border: 1px outset #d8d8d8; */
	background-color: #E5E5E5;
/* 	#fdf0df */
/* 	font-weight: bold; */
}
.datagrid-body{
overflow: hidden;
}
#dailyreport1{
 	width: 100%;  
}
#dailyreport2{
  	width: 50%;   
}
#dailyreport3{
	margin-right: 100px;
/*   	width: 50%;    */
}
.datagrid-header-row .datagrid-cell  {
	background-color: #E5E5E5;
}
.search_filter{
	text-align: center;
	color: gray;
	
}
.filter{
	border-collapse: separate;
	border-spacing: 0px;
}
</style>
<script type="text/javascript">

function dateDifference(firstDate, secondDate) {
	var startDay = new Date(firstDate);
	var endDay = new Date(secondDate);
	var millisecondsPerDay = 1000 * 60 * 60 * 24;
	var millisBetween = startDay.getTime() - endDay.getTime();
	var days = millisBetween / millisecondsPerDay;
	return Math.floor(days);
}

$( document ).ready(function() {
	
	$( "#search_cl" ).datepicker({
		minDate:'0d',
		maxDate:'0d',
		dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
	});
	
	$("#search_rd").change(function(){
		var id = $("#search_rd").val();
		var max = dateDifference(convertToDate($('#rd_ed_'+id).val()), new Date())+1;
 		var min = dateDifference(convertToDate($('#rd_sd_'+id).val()), new Date())+1;		
 		$("#search_cl").datepicker("option", "maxDate", max+'d');
		$("#search_cl").datepicker("option", "minDate", min+'d');
 		
	});
		
	if($("#search_hp").val().length == 0 ){
		$("#search_rd, #search_st").val("");
		$("#search_rd option, #search_st option").prop('disabled', true);
	}
	
	$("#search_hp").change(function(){
		
		$("#search_rd, #search_st").val("");
		$("#search_rd option, #search_st option").prop('disabled', true);
		
		if($("#search_hp").val().length != 0 ){
			$.get( "search/rounds/"+$('#search_hp').val()+".htm" , function( data ) {
				var rounds = $.parseJSON(data);
				$.each(rounds, function(index, value){
					$("#search_rd option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#rd_'+value).prop('disabled', false);
						 }
					});
				});
			});
			
			$.get( "search/sites/"+$('#search_hp').val()+".htm" , function( data ) {
				var sites = $.parseJSON(data);
				$.each(sites, function(index, value){
					$("#search_st option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#st_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});
	
});

function dailyreport(){
	$("#dailyreport1 , #dailyreport2 , #dailyreport3").empty();
	var healthProgramId = $("#search_hp").val();
	var roundId = $("#search_rd").val();
	var siteId = $("#search_st").val();
	var date = $("#search_cl").val();
	var parameters = "?program="+healthProgramId+"&round="+roundId+"&site="+siteId+"&date="+date;
// 	console.log(parameters);
	$.get( "dailyreportperdose.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);	
		$("#calendername").text(reportdata.calender);
		$.each(reportdata.row, function(rowindex, row){
			if(row.name === 'vaccine'){
				$("#dailyreport1").append("<tr id='tr1_"+row.name+"'><td class='td_bold'></td></tr>");
			} else if(row.name !== 'total'){
				$("#dailyreport1").append("<tr id='tr1_"+row.name+"'><td class='td_bold'>"+row.name+" M</td></tr>");
			} else if(row.name === 'total'){
				$("#dailyreport1").append("<tr id='tr1_"+row.name+"' class='td_total'><td class='td_bold'>"+row.name+"</td></tr>");
			}
			$.each(reportdata.data, function(index, element){				
				if(row.name === 'vaccine')	{$("#tr1_"+row.name).append("<th>"+element.vaccine+"</th>");}
				if(row.name === '0-11')		{$("#tr1_"+row.name).append("<td>"+element['0-11']+"</td>");}
				if(row.name === '12-23')	{$("#tr1_"+row.name).append("<td>"+element['12-23']+"</td>");}
				if(row.name === '24-59')	{$("#tr1_"+row.name).append("<td>"+element['24-59']+"</td>");}
				if(row.name === 'total')	{$("#tr1_"+row.name).append("<td>"+element.total+"</td>");}
			});	
		});
	});	
	
	$.get( "dailyreportalldose.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);		
		$.each(reportdata.row, function(rowindex, row){
			if(row.name === 'vaccine'){
				$("#dailyreport2").append("<tr id='tr2_"+row.name+"'><td class='td_bold'></td></tr>");
			} else if(row.name !== 'total'){
				$("#dailyreport2").append("<tr id='tr2_"+row.name+"'><td class='td_bold'>"+row.name+" M</td></tr>");
			} else if(row.name === 'total'){
				$("#dailyreport2").append("<tr id='tr2_"+row.name+"' class='td_total'><td class='td_bold'>"+row.name+"</td></tr>");
			}
			$.each(reportdata.data, function(index, element){				
				if(row.name === 'vaccine')	{$("#tr2_"+row.name).append("<th>"+element.vaccine+"</th>");}
				if(row.name === '0-11')		{$("#tr2_"+row.name).append("<td>"+element['0-11']+"</td>");}
				if(row.name === '12-23')	{$("#tr2_"+row.name).append("<td>"+element['12-23']+"</td>");}
				if(row.name === '24-59')	{$("#tr2_"+row.name).append("<td>"+element['24-59']+"</td>");}
				if(row.name === 'total')	{$("#tr2_"+row.name).append("<td>"+element.total+"</td>");}
			});	
		});
	});	
	
	$.get( "dailyreportcounts.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);	
		$.each(reportdata.data, function(index, element){	
// 			console.log(element);
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Total seen today </td><td style='width:50px;'>"+element.total+"</td></tr>");
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Enrolled today </td><td style='width:50px;'>"+element.ENROLLMENT+"</td></tr>");
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Followed-up today </td><td style='width:50px;'>"+element.FOLLOWUP+"</td></tr>");
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Eligible for a vaccine today </td><td style='width:50px;'>"+element.ELIGIBLE+"</td></tr>");
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Eligible with vaccinated </td><td style='width:50px;'>"+element.VACCINATED+"</td></tr>");
			$("#dailyreport3").append("<tr id='tr3_"+index+"'><td  style='background-color: #E5E5E5; text-align: left;'>Eligible seen contraindications </td><td style='width:50px;'>"+element.CONTRAINDICATIONS+"</td></tr>");
			
		});
	});
}


function filterDailyReport(){
	
	if($("#search_hp").val().length == 0 || $("#search_rd").val().length == 0 
		|| $("#search_st").val().length == 0 || $("#search_cl").val().length == 0){
		alert("select all filters ...");
		return;
	}
	dailyreport();
}

</script>

<div id="tt" class="easyui-tabs" plain="true" border="false">
	<hr>
	<div title="Daily Report">	
	
	<table class="filter">
	<tr>
	<td class="search_filter">Health Program</td>
	<td class="search_filter">Round</td>
	<td class="search_filter">Site</td>
	<td class="search_filter">Date</td>
	</tr>
	<tr><td>
	<select id="search_hp" name="search_healthprogram" bind-value="">
		   	<option></option>
			<c:forEach items="${model.healthProgramList}" var="hp">
				<option id="hp_${hp.programId}" value="${hp.programId}">${hp.name}</option>
			</c:forEach>
		</select></td>
	<td><select id="search_rd" name="search_round" bind-value="">
		   	<option></option>
			<c:forEach items="${model.roundList}" var="rd">
				<option id="rd_${rd.roundId}" value="${rd.roundId}" disabled>${rd.name}</option>
			</c:forEach>
		</select>
		<c:forEach items="${model.roundList}" var="rd">
			<input id="rd_sd_${rd.roundId}" type="hidden" value="<fmt:formatDate value="${rd.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"/>
			<input id="rd_ed_${rd.roundId}" type="hidden" value="<fmt:formatDate value="${rd.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"/>

		</c:forEach></td>
		<td>
		<select id="search_st" name="search_site" bind-value="">
		   	<option></option>
			<c:forEach items="${model.siteList}" var="st">
				<option id="st_${st.mappedId}" value="${st.mappedId}" disabled >${st.name}</option>
			</c:forEach>
		</select></td>
		<td><input id="search_cl" name="search_date"  placeholder='dd-MM-yyyy'/></td>
		<td><button onclick="filterDailyReport();" > search </button></td>
	</tr>
	</table>
	<h1 id="calendername" style="float: right; padding-right: 100px; color: #ED8117"></h1>
		<table id="dailyreport1" class="dailyreport" ></table>
		<br><br>
		<table id="dailyreport2" class="dailyreport" style=" float: left;" ></table>
		
		<table id="dailyreport3" class="dailyreport" style=" float: right;"></table>
	</div>

<script type="text/javascript">
$( document ).ready(function() {
		
	$('#search_ar_spa').change(function(){
		
		$("#search_hp_spa, #search_rd_spa").val("");
		$("#search_hp_spa option, #search_rd_spa option").prop('disabled', true);
		
		if($("#search_ar_spa").val().length != 0 ){
			$.get( "search/healthprograms/"+$('#search_ar_spa').val()+".htm" , function( data ) {
				var healthprogram = $.parseJSON(data);
				$.each(healthprogram, function(index, value){
					$("#search_hp_spa option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#hp_spa_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});
	
	$('#search_hp_spa').change(function(){
		
		$("#search_rd_spa").val("");
		$("#search_rd_spa option").prop('disabled', true);
		
		if($("#search_hp_spa").val().length != 0 ){
			$.get( "search/rounds/"+$('#search_hp_spa').val()+".htm" , function( data ) {
				var round = $.parseJSON(data);
				$.each(round, function(index, value){
					$("#search_rd_spa option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#rd_spa_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});

});
	
	
function filterReport2(){
	var healthProgramId = $("#search_hp_spa").val();
	var roundId = $("#search_rd_spa").val();
	var locationId = $("#search_ar_spa").val();
	var parameters = "?program="+healthProgramId+"&round="+roundId+"&area="+locationId;
// 	console.log(parameters);
	$.get( "summaryPerArea.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);	
		$("#calendername2").text(reportdata.calender);
		var columns = reportdata.columns;
		var tablewidth = reportdata.tablewidth;
 		$('#summaryperareatable').datagrid({
 			
 			width: tablewidth,
 			singleSelect:true,
//  			fit: true,
 			fitColumns: false,
 			nowrap: true, 
 			autoRowHeight: false,
 			showHeader: true,
 			pagination: true,
 			pageSize: '10',
 			url: "summaryPerArea.htm"+parameters ,
 			columns : columns
 			
		});
 		
		$('#summaryPerAreaMinMaxAgetable').datagrid({
 			
			width: 205,
 			singleSelect:true,
 			nowrap: true, 
 			autoRowHeight: false,
 			showHeader: true,
//  			pagination: true,
 			pageSize: '10',
 			url: "summaryPerAreaMinMaxAge.htm"+parameters ,
 			columns : [[
						{field : 'shortName', title : 'vaccine',width : 100}, 
						{field : 'minAge', title : 'minAge', width : 50}, 
						{field : 'maxAge', title : 'maxAge', width : 50},
// 						{field : '', title : 'result', width : 75}
 			          ]]
		});
 		
//  		$('#summaryperareatable').datagrid('loadData', reportdata);
	});

}
</script>
	<div title="Summary per Area">
	<table class="filter">
	<tr>
	<td class="search_filter">Area</td>
	<td class="search_filter">Health Program</td>
	<td class="search_filter">Round</td>
	</tr>
	<tr><td>
	<select id="search_ar_spa" name="search_area" bind-value="">
		<option></option>
		<c:forEach items="${model.locationList}" var="ar">
			<option id="ar_spa_${ar.locationId}" value="${ar.locationId}">${ar.name}</option>
		</c:forEach>
	</select></td><td>
	<select id="search_hp_spa" name="search_healthprogram" bind-value="">
		<option></option>
		<c:forEach items="${model.healthProgramList}" var="hp">
			<option id="hp_spa_${hp.programId}" value="${hp.programId}" disabled>${hp.name}</option>
		</c:forEach>
	</select></td><td>
	<select id="search_rd_spa" name="search_round" bind-value="">
		<option></option>
		<c:forEach items="${model.roundList}" var="rd">
			<option id="rd_spa_${rd.roundId}" value="${rd.roundId}" disabled>${rd.name}</option>
		</c:forEach>
	</select></td><td>
	<button onclick="filterReport2();"> search </button></td>
	</tr>
	</table><br>
	<h1 id="calendername2" style="float: right; padding-right: 100px; color: #ED8117"></h1>
		<table id="summaryperareatable" class="easyui-datagrid" ></table>
		<br><br>
		<table id="summaryPerAreaMinMaxAgetable" class="easyui-datagrid" ></table>
	</div>
	
</div>
