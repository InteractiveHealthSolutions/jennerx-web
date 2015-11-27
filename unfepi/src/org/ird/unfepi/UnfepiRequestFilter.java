package org.ird.unfepi;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class UnfepiRequestFilter /*implements Filter*/ extends OncePerRequestFilter{
	public static boolean ALLOW_WEBAPP_ACCESS = true;

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//System.out.println("Start filtering");

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null 
				&& !request.getRequestURI().toLowerCase().contains("login.htm") 
				&& !request.getRequestURI().toLowerCase().contains(".js")
				&& !request.getRequestURI().toLowerCase().contains(".css")){
			String url = request.getContextPath() + "/login.htm";
			response.sendRedirect(url);
			return;
		}
		
		if(!ALLOW_WEBAPP_ACCESS) {
			filterChain.doFilter(request, response);
		}
		else {
			if(request.getRequestURI().toLowerCase().endsWith("denyit.htm")){
				System.out.println("redirecting");
				getServletContext().getRequestDispatcher("/jsp/deniedWebAvailability.jsp").forward(request, response);
				
				//response.sendRedirect("webServicesUnavaliable.jsp");
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
		
		//System.out.println("Done Filtering");
	}
}
