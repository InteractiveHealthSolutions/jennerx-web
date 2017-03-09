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
</script>

<div id="tt" class="easyui-tabs" plain="true" border="false">
<hr>
<div title="Daily Report">
	<%@ include file="report_daily.jsp"%>
</div>
<div title="Summary per Area">
	<%@ include file="report_summary_per_area.jsp"%>
</div>
<div title="Summary per Round">
	<%@ include file="report_summary_per_round.jsp"%>
</div>
</div>


