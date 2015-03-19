package com.example.zstd.microblog.servlet.rest;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for working with users via REST.
 */
@WebServlet(urlPatterns = {"/rest/users/*"})
public class UsersServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UsersServlet.class.getName());
	
	private static final String DEFAULT_PHOTO_URL = "/blog/static/img/default.jpg";
	
	private static final long serialVersionUID = 1L;
	
	//private UserRepo userRepo = new JdbcUserRepo();
       
    public UsersServlet() {
        super();        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.fine("doGet: " + request.getRequestURI());

        /*
		BlogUser blogUser = null;
		String name = null;
		if(request.getRequestURI().endsWith("/current")) {
			name = request.getUserPrincipal().getName();
		} else if(request.getRequestURI().endsWith("/user-info")) {
			name = request.getParameter("user");
		} else {
			throw new ServletException("Illegal 'get' format");
		}
		try {
			List<BlogUser> list = userRepo.findByField(BlogUser.DB_FIELD_USERNAME, name);
			if(!list.isEmpty()) {
				blogUser = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(blogUser == null) {
			throw new ServletException("Failed to load user");
		}
		response.getWriter().println(toJsonString(blogUser));*/
        response.getWriter().println("not now");

	}
	/*
	private String toJsonString(BlogUser blogUser) {
		JsonObject result = new JsonObject();
		result.addProperty("username",blogUser.getUsername());
		result.addProperty("nickname",blogUser.getNickname());
		result.addProperty("description",blogUser.getDescription());
		String photoUrl = DEFAULT_PHOTO_URL;
		if(!StringUtils.isNullOrEmpty(blogUser.getPhotoUrl())) {
			photoUrl = blogUser.getPhotoUrl();
		}
		result.addProperty("photoUrl",photoUrl);
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(result);
        return resultStr;
	}
	
	private String toJsonString(List<BlogUser> blogUsers) {
		JsonArray resultArray = new JsonArray();
		
		for(BlogUser blogUser : blogUsers) {
			JsonObject result = new JsonObject();
			result.addProperty("username",blogUser.getUsername());
			result.addProperty("nickname",blogUser.getNickname());
			result.addProperty("description",blogUser.getDescription());
			result.addProperty("photoUrl",blogUser.getPhotoUrl());
			resultArray.add(result);
		}
		
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(resultArray);
        return resultStr;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		response.getWriter().println("doGet");
	}
	
	*/

}
