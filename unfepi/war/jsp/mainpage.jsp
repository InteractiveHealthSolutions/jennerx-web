<%@ include file="/WEB-INF/template/header.jsp" %>
<input type="hidden" id="page_nav_id" value="_li1">

<%-- <link rel="stylesheet" href="galleria/themes/classic/galleria.classic.css">
<script type="text/javascript" src="galleria/galleria-1.2.9.min.js?v=${version_css_js}"></script>
<script type="text/javascript" src="galleria/themes/classic/galleria.classic.min.js"></script> --%>
<%--     <div id="tbs" class="easyui-tabs" data-options="plain:true, border:false">
    <div title="Gallery">
    <%@ include file="gallery.jsp" %>
    </div>
    <div title="Program Overview">
    
    </div>
    </div> --%>
    <%@ include file="summaries.jsp" %>
<%-- <script type="text/javascript">
<!--
$(window).load(function() {
	$('#tbs').tabs('select',<%=request.getParameter("tabIndex")%>);
});
//-->
</script> --%>

<%@ include file="/WEB-INF/template/footer.jsp" %>