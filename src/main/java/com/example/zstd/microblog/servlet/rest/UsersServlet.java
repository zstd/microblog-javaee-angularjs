package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.example.zstd.microblog.utils.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Servlet for working with users via REST.
 */
@WebServlet(urlPatterns = {"/rest/users/*"})
public class UsersServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UsersServlet.class.getName());
	
	private static final String DEFAULT_PHOTO_URL = "/blog/static/img/default.jpg";

    public static final String ACTION_PARAM = "action";
    public static final String UNDEFINED_ACTION_ERROR = "Undefined action";
	
	private UserRepo userRepo = ServiceLocator.getInstance().getService(UserRepo.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.fine("doGet: " + request.getRequestURI());

        Action action = Action.parseAction(request.getParameter(ACTION_PARAM));
        switch (action) {
            case CURRENT:
                doCurrentUserAction(request,response);
                break;
            case USER_INFO:
                doUserInfoAction(request, response);
                break;
            case NONE:
            default:
                doUndefinedAction(request, response);
        }
	}

    private void returnUserAsJson(String name,HttpServletResponse response) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name),"Empty user name provided!");
        User blogUser = null;
        List<User> list = userRepo.findByField(User.DB_FIELD_USERNAME, name);
        if(!list.isEmpty()) {
            blogUser = list.get(0);
        }
        if(blogUser != null) {
            response.getWriter().println(toJsonString(blogUser));
        } else {
            LOG.log(Level.SEVERE,"Failed to load user");
            throw new RuntimeException("Failed to load user");
        }

    }

    private void doUndefinedAction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getWriter().print(UNDEFINED_ACTION_ERROR);
    }

    private void doUserInfoAction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        returnUserAsJson(request.getParameter("user"),response);
    }

    private void doCurrentUserAction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String currentUserName =
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        Preconditions.checkState(currentUserName != null,
                "Failed to get current user name, is request authenticated?");
        returnUserAsJson(request.getUserPrincipal().getName(), response);
    }

	private String toJsonString(User blogUser) {
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
	
	private String toJsonString(List<User> blogUsers) {
		JsonArray resultArray = new JsonArray();
		
		for(User blogUser : blogUsers) {
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
		LOG.info("doPost");
		response.getWriter().println("doGet");
	}

    public static enum Action {
        CURRENT,
        USER_INFO,
        NONE;

        public static final Action parseAction(String actionString) {
            actionString = Strings.nullToEmpty(actionString);
            try {
                return Action.valueOf(actionString.toUpperCase());
            } catch(IllegalArgumentException e) {
                LOG.log(Level.WARNING,String.format("Failed to parse Action from '%s'",actionString));
                return NONE;
            }
        }
    }

}
