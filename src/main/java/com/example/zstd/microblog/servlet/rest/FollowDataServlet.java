package com.example.zstd.microblog.servlet.rest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for providing data about following-followers via REST.
 */
public class FollowDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//private FollowDataService followDataService = new FollowDataService();
	
    public FollowDataServlet() {
        super();        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		/*
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
		
		response.getWriter().println(toJsonString(followData));
		*/
	}


    /*
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
	*/

}
