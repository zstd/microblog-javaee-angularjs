package com.example.zstd.microblog.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet of main application page
 */
@WebServlet(urlPatterns = {"/app/main"})
public class MainAppServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(MainAppServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info(" doGet ");
        request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);		
	}

}
