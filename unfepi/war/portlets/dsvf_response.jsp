<%@page import="org.ird.unfepi.web.controller.ViewResponsesController.TabRole"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.GlobalParams"%>
<%@page import="com.mysql.jdbc.StringUtils"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>


<script type="text/javascript">
<!--
function expandD(className, responseId){
	var trId=responseId+"r";
	var tblId = responseId+"tbl";
	anchorContrl = $("#"+responseId+"anc");
	act=anchorContrl.attr('value');

	if(act=="+"){
		document.getElementById(trId).style.display="table-row";
		//document.getElementById(divId).style.display="table";

		anchorContrl.val("-");
		
		try{
			DWRCommunicationService.getCommunicationNotes(responseId, className, 
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

<c:set var="roleSearchFieldName" value="<%=SearchFilter.ROLE_NAME.FILTER_NAME()%>"></c:set>
<c:set var="tabRole" value="${model[roleSearchFieldName]}"></c:set>
	
<div id="tt" class="easyui-tabs" plain="true" border="false">
	<div title="<%= TabRole.CHILD.TITLE()%>" >
		<%String tabrole = (String)pageContext.getAttribute("tabRole");
		if(!StringUtils.isEmptyOrWhitespaceOnly(tabrole)
				&& tabrole.equalsIgnoreCase(TabRole.CHILD.TITLE())){
		%>
			<%@ include file="dg_child_response.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabRole.VACCINATOR.TITLE()%>" >
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabrole)
				&& tabrole.equalsIgnoreCase(TabRole.VACCINATOR.TITLE())){
		%>
			<%@ include file="dg_vaccinator_response.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabRole.STOREKEEPER.TITLE()%>">
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabrole)
				&& tabrole.equalsIgnoreCase(TabRole.STOREKEEPER.TITLE())){
		%>
			<%@ include file="dg_storekeeper_response.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabRole.OTHER.TITLE()%>">
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabrole)
				&& tabrole.equalsIgnoreCase(TabRole.OTHER.TITLE())){
		%>
			<%@ include file="dg_other_response.jsp" %>
		<%
		}
		%>
	</div>
	<div title="<%= TabRole.CALLS.TITLE()%>">
		<%if(!StringUtils.isEmptyOrWhitespaceOnly(tabrole)
				&& tabrole.equalsIgnoreCase(TabRole.CALLS.TITLE())){
		%>
			<%@ include file="dg_call_response.jsp" %>
		<%
		}
		%>
	</div>
</div>

<script type="text/javascript">
<!--
var loaded = false;
$(document).ready(function() {
	// alert('${tabRole}');
	$('#tt').tabs({
		onSelect: function(title,index){
			if(loaded && title != '${tabRole}'){window.location='${javax.servlet.redirect.request_uri}?rolename='+title;}
			loaded = true;
		}
	});
	
	$('#tt').tabs('select','${tabRole}');
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

<div id="winreply">
<%@ include file="/portlets/plt_reply.jsp"%></div>
<script type="text/javascript"><!--
$('#winreply').window({
	title: 'Reply',
	width: 600,  
    height: 600,
    minimizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true,
}); 

function openReplyWindow(recipientMappedId, probeCls, probeId, recipient) {
	openRWindow(recipientMappedId, probeCls, probeId, recipient, $('#winreply'));
}
//--></script>
