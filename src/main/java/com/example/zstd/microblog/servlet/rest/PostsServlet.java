package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.service.BlogPostService;
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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = {"/rest/posts/*"})
public class PostsServlet extends HttpServlet {
	
	private static final Long ONE_SECOND_MILLIS = TimeUnit.SECONDS.toMillis(1);
	private static final Long ONE_MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1);
	private static final Long ONE_HOUR_MILLIS = TimeUnit.HOURS.toMillis(1);
	private static final Long ONE_DAY_MILLIS = TimeUnit.DAYS.toMillis(1);
	
	private static final String DEFAULT_FORMAT = "dd/MM/yyyy hh:mm";

    private static final Logger LOG = Logger.getLogger(UsersServlet.class.getName());
	
	private BlogPostService postService = new BlogPostService();
	
	
       
    public PostsServlet() {
        super();        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getSimpleName() + " doGet " + request.getRequestURI());		

        List<BlogPost> posts = Collections.EMPTY_LIST;
		if(!StringUtils.isNullOrEmpty(request.getParameter("creatorName"))) {
			doGetAllUserMessages(request,response);			
		} else if(!StringUtils.isNullOrEmpty(request.getParameter("topic"))) {
			doGetAllTopicMessages(request,response);			
		} else if(!StringUtils.isNullOrEmpty(request.getParameter("discover"))) {
			doGetDiscoverMessages(request,response);
		} else if(!StringUtils.isNullOrEmpty(request.getParameter("following"))) {
			doGetFollowingMessages(request,response);
		} else {
			posts = postService.getList();
			Collections.sort(posts, new BlogPostComparator());
			response.getWriter().println(toJsonString(posts));
		}
		
	}

	private void doGetFollowingMessages(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("doGetFollowingMessages");
		List<BlogPost> posts = postService.getMessagesOfUserFollowers(request.getUserPrincipal().getName());
		response.getWriter().println(toJsonString(posts));		
	}

	private void doGetDiscoverMessages(HttpServletRequest request,HttpServletResponse response) throws IOException {
        LOG.info("doGetDiscoverMessages");
		List<BlogPost> posts = postService.discoverMessagesForUser(request.getUserPrincipal().getName());
		response.getWriter().println(toJsonString(posts));		
	}

	private void doGetAllTopicMessages(HttpServletRequest request,HttpServletResponse response) throws IOException {
		LOG.info("doGetAllTopicMessages");
        List<BlogPost> posts = postService.getListForTopic(request.getParameter("topic"));
		Collections.sort(posts, new BlogPostComparator());
		response.getWriter().println(toJsonString(posts));		
	}

	private void doGetAllUserMessages(HttpServletRequest request,HttpServletResponse response) throws IOException {
        LOG.info("doGetAllUserMessages");
		List<BlogPost> posts = postService.getListForUser(request.getParameter("creatorName"));
		Collections.sort(posts, new BlogPostComparator());
		response.getWriter().println(toJsonString(posts));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info(this.getClass().getSimpleName() + " doPost " + request.getRequestURI());
		String payload = SomeUtils.extractPayloadAsString(request);
		BlogPost created = postService.save(request.getUserPrincipal().getName(), payload);
		response.getWriter().println(toJsonString(created));
	}
	
	private static String toJsonString(BlogPost data) {		
		Gson gson = new GsonBuilder().create();
		JsonObject message = toJsonObject(data);			
		String resultStr = gson.toJson(message);
		return resultStr;
	}
	
	private static JsonObject toJsonObject(BlogPost obj) {		
		Gson gson = new GsonBuilder().create();
		JsonObject message = new JsonObject();
		message.addProperty("message",obj.getMessage());
		message.addProperty("creatorName",obj.getCreator());
		message.addProperty("postedTimeRaw",obj.getCreated().getTime());
		message.addProperty("postedTime",getPrettyTime(new Date(),obj.getCreated()));
		message.addProperty("id",obj.getId());
		if(!StringUtils.isNullOrEmpty(obj.getTopics())) {
			message.add("topic", gson.toJsonTree(obj.getTopics().split(",")));
		} else {
			message.add("topic", new JsonArray());
		}

		if(!StringUtils.isNullOrEmpty(obj.getMentions())) {
			message.add("mentions", gson.toJsonTree(obj.getMentions().split(",")));
		} else {
			message.add("mentions", new JsonArray());
		}
		return message;
	}
	
	private static String toJsonString(List<BlogPost> data) {
		
		JsonArray result = new JsonArray();
		Gson gson = new GsonBuilder().create();
		for(BlogPost obj : data) {
			JsonObject message = toJsonObject(obj);			
			result.add(message);
		}		
		String resultStr = gson.toJson(result);
		return resultStr;
	}

	static String getPrettyTime(Date currentDate,Date created) {
		Long createdTimeMillis = created.getTime();
		Long currentTimeMillis = currentDate.getTime();
		Long delta = currentTimeMillis - createdTimeMillis;
		if(delta < ONE_MINUTE_MILLIS) {
			return "today, " +delta/ONE_SECOND_MILLIS + " second(s) ago"; 
		} else if(delta < ONE_HOUR_MILLIS) {
			return "today, " +delta/ONE_MINUTE_MILLIS + " minute(s) ago";
		} else if(delta < ONE_DAY_MILLIS) {
			return "today, " + delta/ONE_HOUR_MILLIS + " hour(s) ago";
		} else {
			return new SimpleDateFormat(DEFAULT_FORMAT).format(created);
		}		
	}
	
	/* Simple tests are here */
	/*
	public static void main(String args[]) throws ParseException {
		//testPublishFormatting();
		//testJsonFormattingFormatting();	
	}
	
	private static final void testJsonFormattingFormatting() throws ParseException {
		BlogPost post = new BlogPost(1L,"bob","message of @bob in #topic1 is here","topic1,","bob,alice",new Date());
		toJsonString(Arrays.asList(post));
	}
	*/
	private class BlogPostComparator implements Comparator<BlogPost> {

		@Override
		public int compare(BlogPost o1, BlogPost o2) {
			return o2.getCreated().compareTo(o1.getCreated());
		}
		
	}

}
