<!-- start navigation -->
<div id="nav_div">
      <nav><ul class="sf-menu" id="nav">
<li class="current"><a href="mainpage.htm">Home</a></li>
			<li><a href="children.htm">Children</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistchildren.jsp" %>
				</ul>
			</li>
<%-- 			<li><a href="vaccination.htm">Vaccines</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistvaccines.jsp" %>
				</ul>
			</li>
 --%>			<li><a href="reminders.htm">Reminders</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistreminders.jsp" %>
				</ul>
			</li>
			<li><a href="#">Responses</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistresponses.jsp" %>
				</ul>
			</li>
			<li><a href="#">Reporting</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistreporting.jsp" %>
				</ul>
			</li>
			<li><a href="#">Admin</a>
				<ul>
					<%@ include file="/WEB-INF/layout/linklistadminpanel.jsp" %>
				</ul>
			</li>
			<li><a href="#">Contact Us</a></li>
        </ul>
</nav>
</div>
<!-- end navigation -->