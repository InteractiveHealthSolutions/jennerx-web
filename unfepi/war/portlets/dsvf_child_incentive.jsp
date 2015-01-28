<%@page import="com.mysql.jdbc.StringUtils"%>
<%@page import="org.ird.unfepi.web.controller.ViewChildIncentivesController.TabIncentive"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.context.Context"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<script type="text/javascript">
<!--
function expandD(className, eventId){
	var trId=eventId+"r";
	var tblId = eventId+"tbl";
	anchorContrl = $("#"+eventId+"anc");
	act=anchorContrl.attr('value');

	if(act=="+"){
		document.getElementById(trId).style.display="table-row";
		//document.getElementById(divId).style.display="table";

		anchorContrl.val("-");
		
		try{
			DWRCommunicationService.getCommunicationNotes(eventId, className, 
				function(data) {
				//alert(JSON.stringify(data));
					if(data.SUCCESS){
						$('#'+tblId).find("tr").remove();

						var r = $('<tr class="datahighlight"></tr>');
						r.append($('<td> ').append('Phone operator'));
						r.append($('<td> ').append('Datetime'));
						r.append($('<td> ').append('Who called'));
						r.append($('<td> ').append('Caller details'));
						r.append($('<td> ').append('Reason for call (code)'));
						r.append($('<td> ').append('Reason for call'));
						r.append($('<td> ').append('What was told (code)'));
						r.append($('<td> ').append('What was told'));
						r.append($('<td> ').append('Action type'));
						r.append($('<td> ').append('Additional note'));
						
						$('#'+tblId).append(r);
						for ( var i = 0; i < data.notes.length; i++) {
							var note = data.notes[i];
							var row = $('<tr></tr>');
							row.append($('<td> ').append(note.receiver));
							row.append($('<td> ').append(note.eventDate));
							row.append($('<td> ').append(note.subject));
							row.append($('<td> ').append(note.source));
							row.append($('<td> ').append(note.communicationEventType));
							row.append($('<td> ').append(note.problemGroup));
							row.append($('<td> ').append(note.problem));
							row.append($('<td> ').append(note.solutionGroup));
							row.append($('<td> ').append(note.solution));
							row.append($('<td> ').append(note.description));
							
							$('#'+tblId).append(row);
						}
					}
					else {
						$('#'+tblId).append("<div>"+data.ERROR+"</div>");
					}

				});
				}
				catch (e) {
					alert(e);
				}
	}
	else{
		document.getElementById(trId).style.display="none";
		//document.getElementById(divId).style.display="none";
		
		anchorContrl.val("+");
	}
}
//-->
</script>
<div class="error" style="float: right;">${model.dataLocationMessage}</div>
<br>
<c:set var="typeSearchFieldName" value="<%=SearchFilter.TYPE.FILTER_NAME()%>"></c:set>
<c:set var="tabType" value="${model[typeSearchFieldName]}"></c:set>
<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="<%= TabIncentive.WON.TITLE()%>" >
		<%String tabtype = (String)pageContext.getAttribute("tabType");
		if(!StringUtils.isEmptyOrWhitespaceOnly(tabtype)
				&& tabtype.equalsIgnoreCase(TabIncentive.WON.TITLE())){
		%>
			<%@ include file="dg_child_incentive.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabIncentive.LOST.TITLE()%>" >
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabtype)
				&& tabtype.equalsIgnoreCase(TabIncentive.LOST.TITLE())){
		%>
			<%@ include file="dg_child_incentive.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabIncentive.ALL.TITLE()%>">
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabtype)
				&& tabtype.equalsIgnoreCase(TabIncentive.ALL.TITLE())){
		%>
			<%@ include file="dg_child_incentive.jsp" %>
		<%
		}
		%>
	</div>
</div>

<script type="text/javascript">
<!--
var loaded = false;
$(document).ready(function() {
	// alert('${tabType}');
	$('#tt').tabs({
		onSelect: function(title,index){
			if(loaded && title != '${tabType}'){window.location='${javax.servlet.redirect.request_uri}?type='+title;}
			loaded = true;
		}
	});
	
	$('#tt').tabs('select','${tabType}');
});
//-->
</script> 

<div id="winnote">
<%@ include file="/portlets/plt_add_note.jsp"%></div>
<script type="text/javascript"><!--
$('#winnote').window({
	title: 'Note',
	width: 600,  
    height: 600,
    minimizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 

function openNoteWindow(probeCls, probeId) {
	openWindow(probeCls, probeId, $('#winnote'));
}
//--></script>