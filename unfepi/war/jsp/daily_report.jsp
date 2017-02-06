<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<style type="text/css">
.dailyreport td ,tr, th{
	border: 1px solid gray;
	padding: 3px;
}
.td_bold, th{
	border: 2px outset #d8d8d8;
	background-color: #d8d8d8;
	font-weight: bold;
}
.td_total{
	background-color: #F6F6F6;
}
table{
	border: 1px solid gray;
	border-collapse: collapse;
}
td{
	text-align: center;
}

#dailyreport1{
	width: 100%;
}
#dailyreport2{
	width: 50%;
}
</style>
<script type="text/javascript">

$( document ).ready(function() {
	
// 	$.get( "summaryPerArea.htm" , function( data ) {
// 		console.log(data);
// 	});
	

	dailyreport1();
	dailyreport2();
	
	if($("#search_hp").val().length == 0 ){
		$("#search_rd").val("");
		$("#search_rd option").prop('disabled', true);
		
		$("#search_st").val("");
		$("#search_st option").prop('disabled', true);
	}
	
	$("#search_hp").change(function(){
		$("#search_rd").val("");
		$("#search_rd option").prop('disabled', true);
		
		$("#search_st").val("");
		$("#search_st option").prop('disabled', true);
		
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

function dailyreport1(){
	
	$("#dailyreport1").empty();
	var healthProgramId = $("#search_hp").val();
	var roundId = $("#search_rd").val();
	var siteId = $("#search_st").val();
	
	var parameters = "?program="+healthProgramId+"&round="+roundId+"&site="+siteId;
// 	console.log(parameters);
	
	$.get( "dailyreportperdose.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);		
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
// 				if(row.name === '60')		{$("#tr1_"+row.name).append("<td>"+element['60']+"</td>");}				
				if(row.name === 'total')	{$("#tr1_"+row.name).append("<td>"+element.total+"</td>");}
			});	
		});
	});	
}
function dailyreport2(){
	$("#dailyreport2").empty();
	var healthProgramId = $("#search_hp").val();
	var roundId = $("#search_rd").val();
	var siteId = $("#search_st").val();
	
	var parameters = "?program="+healthProgramId+"&round="+roundId+"&site="+siteId;
	
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
}

function filterReport(){
	dailyreport1();
	dailyreport2();
}

</script>

<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="Daily Report">	
		<br>
		<select id="search_hp" name="search_healthprogram" bind-value="">
		   	<option></option>
			<c:forEach items="${model.healthProgramList}" var="hp">
				<option id="hp_${hp.programId}" value="${hp.programId}">${hp.name}</option>
			</c:forEach>
		</select>
		<select id="search_rd" name="search_round" bind-value="">
		   	<option></option>
			<c:forEach items="${model.roundList}" var="rd">
				<option id="rd_${rd.roundId}" value="${rd.roundId}" disabled>${rd.name}</option>
			</c:forEach>
		</select>
		<select id="search_st" name="search_site" bind-value="">
		   	<option></option>
			<c:forEach items="${model.siteList}" var="st">
				<option id="st_${st.mappedId}" value="${st.mappedId}" disabled >${st.name}</option>
			</c:forEach>
		</select>
		
		<button onclick="filterReport();"> search </button>
		<br><br>
		<hr><br>
		<table id="dailyreport1" class="dailyreport" ></table>
		<br><br>
		<table id="dailyreport2" class="dailyreport" ></table>
	</div>

<script type="text/javascript">
$( document ).ready(function() {
	
	$('#summaryperareatable').datagrid({
		
		
		title:'DataGrid - DetailView',
	   
	    
	    nowrap: true, 
		autoRowHeight: false,
		showHeader: true,
		noheader: true,
		border: false,
		collapsible:false, 
		pagination: true,
		pageSize: '<%=WebGlobals.PAGER_PAGE_SIZE%>',

		method: "get",
		url: "summaryPerArea.htm",
		
	
	columns : [ [

			{
				field : 'day',
				title : 'day',
				sortable : true,
				width : 50,
				formatter : function(value, row, index) {
					return row.day;
				}
			}, {
				field : 'site',
				title : 'site',
				sortable : true,
				width : 100,
				formatter : function(value, row, index) {
					return row.site;
				}
			}, {
				field : 'opv',
				title : 'opv',
				width : 50,
				formatter : function(value, row, index) {
					return row.opv;
				}
			}, {
				field : 'bcg',
				title : 'bcg',
				width : 50,
				formatter : function(value, row, index) {
					return row.bcg;
				}
			}, {
				field : 'penta',
				title : 'penta',
				width : 50,
				formatter : function(value, row, index) {
					return row.penta;
				}
			}, {
				field : 'pcv',
				title : 'pcv',
				width : 50,
				formatter : function(value, row, index) {
					return row.pcv;
				}
			}, {
				field : 'measles',
				title : 'measles',
				width : 50,
				formatter : function(value, row, index) {
					return row.measles;
				}
			}, {
				field : 'yellowFever',
				title : 'yellow<br>Fever',
				width : 50,
				formatter : function(value, row, index) {
					return row.yellowFever;
				}
			} ] ]

		});

	});
</script>
	<div title="Summary per Area">
		<table id="summaryperareatable" class="easyui-datagrid" ></table>
	</div>
</div>
