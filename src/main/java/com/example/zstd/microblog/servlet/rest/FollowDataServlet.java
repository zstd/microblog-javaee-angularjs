package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.service.FollowDataService;
import com.example.zstd.microblog.service.ServiceLocator;
import com.example.zstd.microblog.utils.SomeUtils;
import com.example.zstd.microblog.utils.StringUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servlet for providing data about following-followers via REST.
 */
@WebServlet(urlPatterns = {"/rest/followdata/*"})
public class FollowDataServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UsersServlet.class.getName());

    private FollowDataService followDataService =
            ServiceLocator.getInstance().getService(FollowDataService.class);
	
    public FollowDataServlet() {
        super();        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doGet");
		String following = request.getParameter("following");
		String follower = request.getParameter("follower");
		List<FollowData> followData = Collections.emptyList();
		if(!StringUtils.isNullOrEmpty(follower)) {
			followData = followDataService.getFollowerData(follower);
		} else if(!StringUtils.isNullOrEmpty(following)) {
			followData = followDataService.getFollowingData(following);
		} else {
			System.err.println("No input for follow data provided");
		} 
		
		response.getWriter().print(toJsonString(followData));
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getSimpleName() + " doPost");
		Map<String,String> map = extractParams(request);
		FollowData added = followDataService.addFollowerData(map.get("follower"),map.get("following"));
		response.getWriter().println(toJsonString(added));
	}
	
	private Map<String,String> extractParams(HttpServletRequest request) throws IOException {
		String payload = SomeUtils.extractPayloadAsString(request);
		Gson gson = new GsonBuilder().create();
		Map<String,String> map=new HashMap<String,String>();
		map=(Map<String,String>) gson.fromJson(payload, map.getClass());
		return map;
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(this.getClass().getSimpleName() + " doDelete " + request.getRequestURI());
		followDataService.deleteFollowerData(SomeUtils.getFirst("follower", request.getParameterMap()),
				SomeUtils.getFirst("following", request.getParameterMap()));
	}
	
	private JsonObject toJsonObject(FollowData obj) {
		JsonObject message = new JsonObject();
		message.addProperty("follower",obj.getFollower());
		message.addProperty("following",obj.getFollowing());
		message.addProperty("id",obj.getId());
		return message;
	}
	
	private String toJsonString(FollowData obj) {		
		JsonObject message = toJsonObject(obj);
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(message);
        return resultStr;
	}
	
	private String toJsonString(List<FollowData> data) {
		
		JsonArray result = new JsonArray();
		
		for(FollowData obj : data) {
			JsonObject message = toJsonObject(obj);
			result.add(message);
		}
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(result);
        return resultStr;
	}


}
