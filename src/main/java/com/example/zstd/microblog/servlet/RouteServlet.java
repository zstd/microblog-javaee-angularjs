package com.example.zstd.microblog.servlet;

import com.example.zstd.microblog.utils.ServletUtils;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servlet simply moving/routing to some view.
 */
@WebServlet(urlPatterns = {"/app/route"})
public class RouteServlet extends HttpServlet{
	
	private static final Logger LOG = Logger.getLogger(RegistrationServlet.class.getName());

    private static final Map<String,String> ROUTES = ImmutableMap.of(
            "user","/WEB-INF/pages/user_profile.jsp?user=",
            "topic","/WEB-INF/pages/topic.jsp?topic="
    );

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doGet: " + request.getParameterMap());
        for (String key : ROUTES.keySet()) {
            String value = request.getParameter(key);
            if(value != null) {
                request.getRequestDispatcher(ROUTES.get(key) + value).forward(request, response);
                return;
            }
        }
        response.setStatus(ServletUtils.NOT_FOUND);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);		
	}

}
