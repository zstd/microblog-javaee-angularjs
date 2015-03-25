package com.example.zstd.microblog.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation for logout functionality.
 */
@WebServlet(urlPatterns = {"logout"})
public class LogoutServlet extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		// New location to be redirected
	    String site = request.getContextPath();
	    response.setStatus(response.SC_MOVED_TEMPORARILY);
	    response.setHeader("Location", site);    	    
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);		
	}

}
