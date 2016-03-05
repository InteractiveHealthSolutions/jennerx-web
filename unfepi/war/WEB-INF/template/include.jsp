<%-- <%@ page session="false"%> --%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ taglib prefix="pg" uri="/WEB-INF/tld/pager-taglib.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="springform" uri="/WEB-INF/tld/spring-form.tld"%>
<%@ taglib prefix="spring" uri="/WEB-INF/tld/spring.tld"%>

<script type="text/javascript">
<!--

$(document).ready(function() {  
	$('.dvwform td').filter(function(){
		  return $(this).text().trim() === 'true';
	}).each(function () {
	   $(this).html('yes');
	});
	
	$('.dvwform td').filter(function(){
		  return $(this).text().trim() === 'false';
	}).each(function () {
	   $(this).html('no');
	});
});

function showHide(obj) {
	var uid = $(obj).attr('hideshow');
	var tx = $(obj).text();
//	alert(uid+"-id:tx-"+tx);
	if(tx.indexOf('more') != -1){
		$('.'+uid).show();
		$('a[hideshow="'+uid+'"]').text('<< less');
	}
	else {
		$('.'+uid).hide();
		$('a[hideshow="'+uid+'"]').text('more >>')
	}
}
//-->
</script>