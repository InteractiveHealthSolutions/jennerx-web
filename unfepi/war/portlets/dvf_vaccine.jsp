<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<div class="dvwform">
	<table style="width: 100%">
	
	<thead>
	<tr>
	<tr><td colspan="8"><a onclick="addNewVaccine()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="float: right;">New Vaccine</a></td></tr>
	</tr>
	<script>
	function addNewVaccine(){
			window.location = "addVaccine.htm";
	}
	</script>
        <tr>
        	<th></th>
            <th>Name</th>
            <th>Short Name</th>
            <th>Full Name</th>
            <th>min Grace<br>Period Days</th>
            <th>max Grace<br>Period Days</th>
            <th>Description</th>
            <th>vaccine_entity</th>
        </tr>
    </thead>
    <tbody>
    	<c:forEach items="${model.vaccinesList}" var="vaccine" >
    	<tr>
    	<td><a href="editVaccine.htm?editRecord=${vaccine['name']}" class="linkiconS iconedit"></a></td>
    	<td>${vaccine['name']}</td>
    	<td>${vaccine['shortName']}</td>
    	<td>${vaccine['fullName']}</td>
    	<td>${vaccine['minGracePeriodDays']}</td>
    	<td>${vaccine['maxGracePeriodDays']}</td>
    	<td>${vaccine['description']}</td>
    	<td>${fn:toLowerCase(vaccine['vaccine_entity'])}</td>
    	</tr>
    	</c:forEach>
    </tbody>
</table>
</div>
