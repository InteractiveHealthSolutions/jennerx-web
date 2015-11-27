<!-- NAVIGATION -->
<div id="nav_container">
<ul id="nav_menu" class="bar_menu">
	<li><a href="mainpage.htm">Home</a></li>
			<li><a>Children</a>
				<ul>
					<%@ include file="linklistchildren.jsp" %>
				</ul>
			</li>
			<li><a href="viewVaccinatorIncentives.htm">Incentives</a>
				<%-- <ul>
					<%@ include file="linklistincentives.jsp" %>
				</ul> --%>
			</li>
			<li><a href="s_home_reminder.htm">Communication</a>
				<ul>
					<%@ include file="linklistcommunication.jsp" %>
				</ul>
			</li>
			</li>
			<li><a href="viewDownloadables.htm">Data</a>
				<%-- <ul>
					<%@ include file="linklistreporting.jsp" %>
				</ul> --%>
			</li>
			<li><a href="#">Admin</a>
				<ul>
					<%@ include file="linklistadminpanel.jsp" %>
				</ul>
			</li>
			<!-- <li><a href="#">About Us</a></li> -->
</ul>
</div>
<!-- /NAVIGATION -->