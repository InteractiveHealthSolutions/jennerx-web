
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.service.exception.UserServiceException"%><input type="hidden" id="page_nav_id" value="_li5">

<%@page import="org.springframework.beans.support.PagedListHolder"%>

<%@ include file="/jsp/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
            <div class="function-tag">Analyze Reminders and Responses</div>
            <br><br>
<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_ANALYZER,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perms){ 
%> 
<script type="text/javascript"><!--
function viewNewPage(obj){
	document.getElementById("action").value="display";
	var pgNum=obj.title;
	document.getElementById("pagedir").value=pgNum;
	document.getElementById("searchfrm").submit();
}
function clearDate(){
document.getElementById("date1").value="";
document.getElementById("date2").value="";

}
function subFrm(){
	if(document.getElementById("date1").value == null || document.getElementById("date1").value == ''){
		if(confirm("You have not specified any starting date to search for record. This may slow down query and may bring huge amount of data. Do you still want to continue ?")){
			document.getElementById("searchfrm").submit();
		}
	}
}
//-->
</script>
<form id="searchfrm" name="searchfrm" action="analyzeReminder.htm" method="post">
<div style="size:900px; overflow: scroll" >

    <table class="searchTable">
           <tr>
                <td>Select arm</td>
                <td>     
                <input type="hidden" id="armNameVal" value="${lastSearchArmName}"/> 
                <select id="armName" name="armName">
                	<option></option>
                	<c:forEach  items="${model.arm}" var="arm">
                		<option>${arm.armName}</option>
                	</c:forEach>               
                </select>
                <script><!--
                    sel = document.getElementById("armName");
                    val=document.getElementById("armNameVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //-->
                </script>
                </td>
           </tr>
            <tr>
                <td>Enter child id or name </td>
                <td><input type="text" id="childId" name="childId" value="${lastSearchChildId}"/></td>
            </tr>
            <tr>
                <td>Enter vaccination due date </td>
                <td>
                	<input id="date1" name="date1" onclick='scwShow(this,event);' readonly="readonly" value="${dt1Search}"/>
                    <input onclick="scwShow(scwID('date1'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                    <span class="searchCalendarTag">:  Start</span>
					<br>                
					<input id="date2" name="date2" onclick='scwShow(this,event);' readonly="readonly" value="${dt2Search}"/>
                    <input onclick="scwShow(scwID('date2'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                    <span class="searchCalendarTag">:  End<br></span>
		  			<a style=" width: 2cm;text-decoration: underline;cursor: pointer;font-style: italic ;font-size: small;" onclick="clearDate();">ClearDate</a>					
                </td>
               </tr>

            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td><input type="submit" value="Search" title="search" onclick="subFrm();"/> </td>
            </tr>
       
    </table>
    <input name="searchlog" type="hidden" value="${searchlog}">
    <div style="color: blue;font-size: medium;font-style: italic">${searchlog}</div>
    <div style="color: red;font-size: small;font-style: italic">${message}</div>
     <input type="hidden" id="action" value="search" name="action" >
            <input type="hidden" id="pagedir" name="pagedir" >
                <table>
            	<tr>
            <% Object o= request.getSession().getAttribute("pagedList");
            	int currpagenum=((PagedListHolder)o).getPage();
            	int total=((PagedListHolder)o).getPageCount();
            	String pagesText=(currpagenum+1)+" of "+total;
            	int totalrecords=((PagedListHolder)o).getSource().size();
            %>            	
            	<td width="40%" style="font-size: small;color: purple;">Total~Rows:(<%=totalrecords%>)<br>Page:(<%=pagesText%>)</td>
            	<td width="60%" style="padding: "><a title="<%=currpagenum-1 %>" onclick="viewNewPage(this);"/>Prev</a> , <c:forEach begin="0" end="<%= total-1 %>" var="i"><a href="#"title="<c:out value="${i}" />" onclick="viewNewPage(this);">${i+1}</a> ,</c:forEach>
            	<a title="<%=currpagenum+1 %>" onclick="viewNewPage(this);"/>Next</a>
            	</td>
            	</tr>
            	</table>              
            	<%@ include file="/portlets/analyzeRemindergrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to use analyzer. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarReporting.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>