<%@page import="org.ird.unfepi.GlobalParams.DownloadableType"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.model.Model"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.EncounterType"%>
<%@page import="org.ird.unfepi.model.Encounter.DataEntrySource"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<script type="text/javascript">
var srfilters = new Array();

function addSearchFilter() {
	var parentdiv = $('#sfdiv');
	var id="filter"+srfilters.length;
	var cname_id  = id+"cname";
	var ftype_id = id+"ftype";
	var val_id = id+"val";
	var div_id = id+"div";
	
	var comboColumnName = $("<select></select>").attr("id", cname_id).attr("name", cname_id).attr("srflindex", srfilters.length);
	comboColumnName.append($("#columnnameslist > option").clone());
	comboColumnName.change(function() {
		srflindex = $(this).attr('srflindex');
	});
	
	var filterType = $("<select></select>").attr("id", ftype_id).attr("name", ftype_id);
	filterType.append('<option value="=">is</option>'+
			' <option value="!=">is not</option>'+
			' <option value="like">contains</option>'+
			' <option value="not like">doesnot contain</option>'+
			' <option value="between">is between</option>');
	
	var div = $("<div></div>").attr("id", div_id).attr("name", div_id).attr("srflindex", srfilters.length);
	div.append(comboColumnName);
	div.append(filterType);
	div.append('<input id="'+val_id+'" name="'+val_id+'" />');
	div.append(' <a class="clearDate" srflindex="'+srfilters.length+'" onclick="removeSerachFilter('+srfilters.length+');" >X</a>');
	
	parentdiv.append(div);

	var ids = {};
	ids["cname_id"] = cname_id;
	ids["ftype_id"] = ftype_id;
	ids["val_id"] = val_id;
	ids["div_id"] = div_id;
	srfilters.push(ids);
	/* comboColumnName.find('option').each(function(){
		if ($(this).val() == center+''){
			$(this).attr("selected","selected");
		}
	}); */
}

function sendSearchFilter() {
	var sf = "";
	var columns = "";
	for ( var i = 0; i < srfilters.length; i++) {
		if(srfilters[i] != -1){
			//alert(JSON.stringify(srfilters[i]));
			if($('#'+srfilters[i].val_id).val().trim() === ''){
				alert('empty value for search filter '+$('#'+srfilters[i].cname_id).val());
				return;
			}
			columns+=$('#'+srfilters[i].cname_id).val()+",";
			
			sf += 
				//IF NOT first filter append AND with it to allow for multiple filters
			(i==0?"":" AND ")+
				//Append filter cloumn and condition i.e. columnName = / columnName LIKE 
			$('#'+srfilters[i].cname_id).val()+
				" "+$('#'+srfilters[i].ftype_id).val();
			
			//Append filter value

			// IF filter is LIKE clause append '%%' for matching otherwise append wrap in ''
			if($('#'+srfilters[i].ftype_id).val().toLowerCase().indexOf('like') != -1){
				sf+=" '%"+$('#'+srfilters[i].val_id).val()+"%' ";
			}
			else if($('#'+srfilters[i].ftype_id).val().toLowerCase().indexOf('between') != -1){
				var start = $('#'+srfilters[i].val_id).val().substring(0, $('#'+srfilters[i].val_id).val().indexOf(" AND"));
				var end = $('#'+srfilters[i].val_id).val().substring($('#'+srfilters[i].val_id).val().indexOf("AND ")+4);
				sf+=" '"+start+"' AND '"+end+"' ";
			}
			// if user input yes/no for boolean fields
			else if($('#'+srfilters[i].val_id).val().toLowerCase() == "yes"
					|| $('#'+srfilters[i].val_id).val().toLowerCase() == "no"){
				var vl = $('#'+srfilters[i].val_id).val().toLowerCase().replace("yes", "true").replace("no", "false");
				sf+=" '"+vl+"' ";
			}
			else {
				sf+=" '"+$('#'+srfilters[i].val_id).val()+"' ";
			}
		}
	}
	
	// alert(sf);
	document.getElementById("searchFilter").value=sf;
	document.getElementById("columns").value=columns;
	goForSearch(document.searchfrm);
}

function removeSerachFilter(ind) {
	$('div[srflindex='+ind+']').remove();
	srfilters[ind] = -1;
}
</script>

<select id="columnnameslist" style="display: none;">
	<c:forEach items="${model.columnNames}" var="coln">
		<option>${coln}</option>
	</c:forEach>
</select>

<table width="100%">
	<tr>
		<td class="left">
			<div class="returnedDataDetails">
			<a onclick="$('#winhelp').window('open');" class="linkiconM iconhelp leftalign"></a><br><br>
			</div>
			<div id="sfdiv"></div>
		</td>
		<td rowspan="10" class="right"><span class="left">${model.searchFilterMessage}</span><td>
	</tr>
	<tr>
		<td class="left">
			<a onclick="sendSearchFilter();" class="anchorCustom">Search</a> 
			<a onclick="addSearchFilter();" class="anchorCustom">Add filter</a>
			
			<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.SEARCH_FILTER.FILTER_NAME()%>"></c:set>
			<input id="searchFilter" type="hidden" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}">

			<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.COLUMNS.FILTER_NAME()%>"></c:set>
			<input id="columns" type="hidden" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}">
		</td>
<%-- 	 	<td><a href="viewDownloadables.htm?action=search&<%=SearchFilter.NAME_PART.FILTER_NAME()%>=${model.dmpFileNameInit}&<%=SearchFilter.TYPE.FILTER_NAME()%>=<%=DownloadableType.DATA_DUMP%>" class="linkiconM iconcsv"></a></td>
 --%>	</tr>
	
</table>

<div id="winhelp" class="dvwform">
<table>
<tr><td>All Dates</td><td>yyyy-MM-dd<br>i.e. (2013-12-31)</td></tr>
<tr><td>ChildID</td><td>14 digits</td></tr>
<tr><td>Gender</td><td>Male | Female</td></tr>
<tr><td>IsEstimatedBirthdate, HasApprovedReminders,<br>ApprovedLottery , PCVGiven, OPVGiven</td><td>Yes | No</td></tr>
<tr><td>EPInumber</td><td>8 digits</td></tr>
<tr><td>VaccinationStatus</td><td>Vaccinated | Scheduled | Retro | Retro_date_missing</td></tr>
<tr><td>CodeStatus</td><td>Available | Consumed | Expired</td></tr>
<tr><td>VerficiationCode</td><td>6 digits</td></tr>
<tr><td>StorekeeperID</td><td>6 digits</td></tr>
<tr><td>Center</td><td>5 digits</td></tr>
<tr><td>ReminderStatus</td><td>NA | Scheduled | Sent | Missed | Failed | Logged</td></tr>
<tr><td>ResponseCount</td><td>any number</td></tr>
<tr><td>Using 'Between'</td><td>startRange AND endRange</td></tr>
<tr><td>Using 'Between' with date</td><td>yyyy-MM-dd AND yyyy-MM-dd<br>i.e. 2013-12-01 AND 2014-01-26</td></tr>
<tr><td>Using 'contains' with date</td><td>yyyy- | yyyy-MM- | yyyy-MM-dd<br>i.e. 2013- | 2013-01- | 2013-012-01</td></tr>
</table>

</div>
<script type="text/javascript"><!--
$('#winhelp').window({
	title: 'Help',
	width: 600,  
    height: 600,
    minimizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 
//--></script>
</form>