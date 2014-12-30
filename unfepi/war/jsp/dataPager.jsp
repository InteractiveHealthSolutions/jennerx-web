<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.GlobalParams.ServiceType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<div class="epipager">
<span class="returnedDataDetails">Total rows: ${model.totalRows}</span> -- 

<pg:pager items="${model.totalRows}"
	url = "${javax.servlet.forward.request_uri}"
	index="<%=WebGlobals.DEFAULT_PAGING_INDEX%>"
    maxPageItems="<%=WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS%>"
    maxIndexPages="<%=WebGlobals.DEFAULT_PAGING_MAX_INDEX_PAGES%>"
    isOffset="<%=true%>"
    export="offset,currentPageNumber=pageNumber"    
 	scope="request" >
 
<%-- keep track of preference --%>
  <pg:param name="style"/>
  <pg:param name="position"/>
  <pg:param name="index"/>
  <pg:param name="maxPageItems"/>
  <pg:param name="maxIndexPages"/>

<%-- Filter Params --%>

	<c:set var="filterPrefix" value="<%=SearchFilter.FILTER_PREFIX()%>"></c:set>

	<c:forEach items="${model}" var="modelattribute">
		<c:if test="${fn:startsWith(fn:toLowerCase(modelattribute.key), fn:toLowerCase(filterPrefix))}">
			<pg:param name="${modelattribute.key}" value="${modelattribute.value}"/>
		</c:if>
	</c:forEach>
  
<%-- save pager offset during form changes --%>
<%--   <input type="hidden" name="pager.offset" value="<%= offset %>"> --%>

  <%-- warn if offset is not a multiple of maxPageItems --%>
  <%
  String style = WebGlobals.DEFAULT_PAGING_STYLE;
  if (offset.intValue()
						% WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS != 0
						&& ("alltheweb".equals(style) || "lycos".equals(style))) {%>
    <p>Warning: The current page offset is not a multiple of Max. Page Items.
    <br>Please
    <pg:first><a href="<%=pageUrl%>">return to the first page</a></pg:first>
    if any displayed range numbers appear incorrect.</p>
  <%}%>

  <%-- the pg:pager items attribute must be set to the total number of
       items for index before items to work properly --%>
  <%if ("top".equals(WebGlobals.DEFAULT_PAGING_POSITION)
						|| "both".equals(WebGlobals.DEFAULT_PAGING_POSITION)) {%>
    <!-- <br> COMMENTED BY MAIMOONA-->
    <pg:index>
      <%if ("texticon".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/texticon.jsp" flush="true"/>
      <%} else if ("jsptags".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/jsptags.jsp" flush="true"/>
      <%} else if ("google".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/google.jsp" flush="true"/>
      <%} else if ("altavista".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/altavista.jsp" flush="true"/>
      <%} else if ("lycos".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/lycos.jsp" flush="true"/>
      <%} else if ("yahoo".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/yahoo.jsp" flush="true"/>
      <%} else if ("alltheweb".equals(style)) {%>
  	<jsp:include page="/WEB-INF/paging/alltheweb.jsp" flush="true"/>
      <%} else {%>
  	<jsp:include page="/WEB-INF/paging/simple.jsp" flush="true"/>
      <%}%>
    </pg:index>
  <%}%>
</pg:pager>
 
  <%--
    Since the data source is static, it's easy to offset and limit the
    loop for efficiency. If the data set size is not known or cannot
    be easily offset, the pager tag library can count the items and display
    the correct subset for you.

    The following is an example using an enumeration data source of
    unknown size. The pg:pager items and isOffset attributes must
    not be set for this example:

    <% while (enumeration.hasMoreElements()) { %>
	<pg:item><%= enumeration.nextElement() %><br></pg:item>
    <% } %>

   --%>
</div>