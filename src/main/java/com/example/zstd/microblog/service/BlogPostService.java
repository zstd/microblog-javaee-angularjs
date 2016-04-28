package com.example.zstd.microblog.service;

import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.BlogPostRepo;
import com.example.zstd.microblog.repository.FollowDataRepo;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.utils.StringUtils;
import com.example.zstd.microblog.utils.ValidationUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class BlogPostService {
	
	private static final int DISCOVER_LIST_SIZE = 10;
    public static final String MENTION_PREFIX = "@";
	public static final String TOPIC_PREFIX = "#";

	private static final Logger LOG = Logger.getLogger(BlogPostService.class.getName());
	
	private BlogPostRepo blogPostRepo() {
        return ServiceLocator.getInstance().getService(BlogPostRepo.class);
    }

	private UserRepo userRepo() {
		return ServiceLocator.getInstance().getService(UserRepo.class);
	}

	private FollowDataRepo followDataRepo() {
		return ServiceLocator.getInstance().getService(FollowDataRepo.class);
	}
	
	public BlogPost save(String username,String message) {
		ValidationUtils.checkArgument(
				StringUtils.isNullOrEmpty(username), new IllegalArgumentException("No username"));
		ValidationUtils.checkArgument(
                StringUtils.isNullOrEmpty(message), new IllegalArgumentException("No message"));
		ValidationUtils.checkArgument(
				StringUtils.exceedsLength(message, BlogPost.MAX_LENGTH), new IllegalArgumentException("Message too long"));
		
		String[] tokens = message.split(" ");
		
		List<String> mentions = extractMentions(tokens);
		List<String> topics = extractTopics(tokens);
		
		BlogPost toSave = new BlogPost(username,message,
				StringUtils.join(",", topics.toArray(new String[0])),
				StringUtils.join(",", mentions.toArray(new String[0])));

        return blogPostRepo().save(toSave);
    }

	public List<BlogPost> getListForTopic(String topic) {
		List<BlogPost> posts = getList();
		return posts.stream()
				.filter(post -> post.getTopics() != null && Arrays.asList(post.getTopics().split(",")).contains(topic))
				.collect(Collectors.toList());
	}
	
	public List<BlogPost> getListForUser(String user) {
		List<BlogPost> posts = getList();
		return posts.stream()
				.filter(p -> p.getCreator().equals(user))
				.collect(Collectors.toList());
	}
	
	public List<BlogPost> getListOfOtherUser(String user) {
		List<BlogPost> posts = getList();
		return posts.stream()
				.filter(p -> !p.getCreator().equals(user))
				.collect(Collectors.toList());
	}
	
	public List<BlogPost> getList() {
		List<BlogPost> posts = blogPostRepo().listAll();
        return posts;
    }

	private List<String> extractTopics(String[] tokens) {
		return Arrays.stream(tokens)
				.filter(t -> t.startsWith(TOPIC_PREFIX))
				.map(t -> t.replaceAll(TOPIC_PREFIX,""))
				.collect(Collectors.toList());
	}
	
	private List<String> extractMentions(String[] tokens) {
        return Arrays.stream(tokens)
                .filter(t -> t.startsWith(MENTION_PREFIX))
                .map(t -> t.replaceAll(MENTION_PREFIX,""))
                .filter(this::isValidUsername)
                .collect(Collectors.toList());
	}

	private boolean isValidUsername(String username) {
        return !userRepo().findByField(User.DB_FIELD_USERNAME, username).isEmpty();
    }

	public List<BlogPost> discoverMessagesForUser(String name) {
		List<BlogPost> data = getListOfOtherUser(name);
		Map<String, Long> followingData = userRepo().getUserFollowingData();
        Comparator<BlogPost> comparator = BlogPost.createPriorityComparator(followingData);
		return data.stream()
				.sorted(comparator)
				.limit(DISCOVER_LIST_SIZE)
				.collect(Collectors.toList());
	}

	public List<BlogPost> getMessagesOfUserFollowers(String name) {
		List<String> followers = getFollowersNames(name);
		LOG.info(name + " followers " + followers);
		List<BlogPost> allPosts = getList();
		return allPosts.stream()
				.filter(p -> followers.contains(p.getCreator()))
				.collect(Collectors.toList());
	}

	private List<String> getFollowersNames(String name) {
		List<FollowData> followData = followDataRepo().findByField(FollowData.DB_FIELD_FOLLOWING, name);
        return followData.stream()
				.map(FollowData::getFollower)
				.collect(Collectors.toList());

	}
	
	

}
