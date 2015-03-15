package com.example.zstd.microblog.model;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * Model class representing one post at blog.
 */
public class BlogPost {
	
	public static final int MAX_LENGTH = 140;
	
	public static final String DB_FIELD_ID = "id";
	public static final String DB_FIELD_CREATOR = "creator";
	public static final String DB_FIELD_MESSAGE = "message";
	public static final String DB_FIELD_TOPICS = "topics";
	public static final String DB_FIELD_MENTIONS = "mentions";
	public static final String DB_FIELD_CREATED = "created";
	
	public static final String[] DB_FIELDS = new String[]{
						BlogPost.DB_FIELD_ID,BlogPost.DB_FIELD_CREATOR,BlogPost.DB_FIELD_MESSAGE,
						BlogPost.DB_FIELD_TOPICS,BlogPost.DB_FIELD_MENTIONS,BlogPost.DB_FIELD_CREATED};
	public BlogPost() {}
	
	private Long id;
	private String creator;
	private String message;
	private String topics;
	private String mentions;
	private Date created;
	
	public BlogPost(BlogPost original) {
		super();
		this.id = original.getId();
		this.message = original.getMessage();
		this.creator = original.getCreator();
		this.topics = original.getTopics();
		this.mentions = original.getMentions();
		this.created = original.getCreated();
	}
	
	public BlogPost(Long id, String creator, String message, String topics,
			String mentions, Date created) {
		super();
		this.id = id;
		this.creator = creator;
		this.message = message;
		this.topics = topics;
		this.mentions = mentions;
		this.created = created;
	}

	public BlogPost(String creator, String message, String topics,
			String mentions) {
		super();
		this.creator = creator;
		this.message = message;
		this.topics = topics;
		this.mentions = mentions;
		this.created = new Date();
	}
	
	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public String getMentions() {
		return mentions;
	}

	public void setMentions(String mentions) {
		this.mentions = mentions;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * Returns the number of times the message was republished.
	 * @return - republish count.
	 */
	public int getRepublishCount() {
		String temp = this.message;
		int oldLength = temp.length();
		temp = temp.replaceAll("RE:", "");
		int newLength = temp.length();
		return (oldLength - newLength)/"RE:".length();
	}

	@Override
	public String toString() {
		return "BlogPost [id=" + id + ", creator=" + creator + ", message="
				+ message + ", topics=" + topics + ", mentions=" + mentions
				+ ", created=" + created + "]";
	}
	
	/**
	 * Creates comparator for sorting blon messages by rating.
	 * The rating is based on number of followers and number of replies.
	 * @param followingData - map with followers count for each user
	 * @return - new Comparator
	 */
	public static Comparator<BlogPost> createPriorityComparator(final Map<String, Long> followingData) {
		
		return new Comparator<BlogPost>() {
			
			private Double calcPriority(BlogPost blogPost) {
				Double result = Math.log(2 + followingData.get(blogPost.getCreator()));
				result += Math.log(2 + blogPost.getRepublishCount());
				return result;
			}
			
			@Override
			public int compare(BlogPost o1, BlogPost o2) {
				Double pr1 = calcPriority(o1),pr2 = calcPriority(o2);
				Double res = pr1 - pr2;
				return res.intValue();
			}
		};
	}
}
