<%@page import="org.apache.naming.resources.FileDirContext"%>
<%@page import="java.io.File"%>

<%@page import="org.ird.unfepi.service.exception.UserServiceException" %>
<%@page import="org.ird.unfepi.constants.SystemPermissions" %>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<div class="dvwform centerdivh" style="width: 600px">
<div class="formheadingWRule">Gallery</div>
<link rel="stylesheet" href="galleria/themes/classic/galleria.classic.css">
<script type="text/javascript" src="galleria/galleria-1.2.9.min.js?v=${version_css_js}"></script>
<script type="text/javascript" src="galleria/themes/classic/galleria.classic.min.js"></script>
<div id="galleria">
        <%
        String file = application.getRealPath("/gallery/thumbs");

        File f = new File(file);
        String [] fileNames = f.list();
        for(String s : fileNames){
        %>
        <a href="gallery/unfepi<%=s%>">
        	<img src="gallery/thumbs/<%=s%>" 
        	data-title="UNFEPI" 
        	data-description="UNFEPI - Get every child fortified" 
        	data-big="gallery/unfepi<%=s%>">
        </a>
        <% } %>
</div>
    
<script>
     /*  Load the classic theme
     Galleria.loadTheme('galleria/themes/classic/galleria.classic.min.js'); */

     $(window).load(function() {
     	 Galleria.configure({
          	debug: false,
              transition: 'fade',
              showInfo: false,
              wait: true,
              height: 500,
              overlayBackground: '#ff00ff'
          });
          
     	// Initialize Galleria
Galleria.run('#galleria');
   	});
    

 </script>

<%@ include file="/WEB-INF/template/footer.jsp" %>