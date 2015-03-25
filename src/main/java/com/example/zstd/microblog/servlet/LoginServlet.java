package com.example.zstd.microblog.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for login functionality.
 */
@WebServlet(urlPatterns = {"login"})
public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" doGet ");
        request.getSession().invalidate();
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		request.login(username,password);
		response.sendRedirect("/app/main");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);		
	}

}
