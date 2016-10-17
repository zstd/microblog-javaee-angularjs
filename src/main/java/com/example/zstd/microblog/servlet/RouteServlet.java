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
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Servlet simply moving/routing to some view.
 */
@WebServlet(urlPatterns = {"/app/route"})
public class RouteServlet extends HttpServlet{
	
	private static final Logger LOG = Logger.getLogger(RegistrationServlet.class.getName());

    static final String PARAM_USER = "user";
    static final String PARAM_TOPIC = "topic";

    static final String ROUTE_USER = "/WEB-INF/pages/user_profile.jsp?user=";
    static final String ROUTE_TOPIC = "/WEB-INF/pages/topic.jsp?topic=";

    private static final Map<String,String> ROUTES = ImmutableMap.of(
            PARAM_USER, ROUTE_USER,
            PARAM_TOPIC, ROUTE_TOPIC
    );

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doGet: {}" + request.getParameterMap());
        Optional<String> routeKey = ROUTES.keySet().stream()
                .filter(r -> request.getParameter(r) != null)
                .findFirst();
        if(routeKey.isPresent()) {
            request.getRequestDispatcher(ROUTES.get(routeKey.get()) + request.getParameter(routeKey.get()))
                    .forward(request, response);
        } else {
            response.setStatus(ServletUtils.NOT_FOUND);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);		
	}

}
