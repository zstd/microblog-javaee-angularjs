var app = angular.module('microblogApp', []);

app.factory('Feed', function($rootScope,$http) {
  var PAGE_SIZE = 5;

  var Feed = function Feed(query) {
    this.query = query || {};
    this.posts = [];
    this.lastTime = 0;
    this.moreToLoad = false;
  };

  Feed.prototype.loadPosts = function() {
    var feed = this;

    var query = angular.copy(this.query);
    var queryStr = "";
    for(key in query) {
    	queryStr += key + "=" + query[key];
    }
    $http.get(MicroblogApp.Config.contextPath + '/rest/posts?'+queryStr).
	    success(function(result, status, headers, config) {
	    	if (result.length > PAGE_SIZE) {
	            result.pop();
	            feed.moreToLoad = true;
	          } else {
	            feed.moreToLoad = false;
	          }
	          if (result.length) feed.lastTime = result[result.length - 1].postedTime;

	          Array.prototype.push.apply(feed.posts, result);
	          if(!$rootScope.$$phase) {
	        	  $rootScope.$apply();
	          }		          
	    }).
	    error(function(data, status, headers, config) {
	      alert("Failed to log blog posts!")
	    });    
  };

  Feed.prototype.refresh = function() {
    this.lastTime = new Date().getTime();
    this.posts.length = 0;
    this.loadPosts();
    this.moreToLoad = false;
  };
  
  var currentEditedPost = null; 
  
  Feed.prototype.currentPostEdited = function(id) {
     return this.currentEditedPost && this.currentEditedPost.id == id;
  }
  
  Feed.prototype.startPostEditing = function(id,message) {
     this.currentEditedPost = { id : id, message : message};
  }

  Feed.prototype.stopPostEditing = function() {
     this.currentEditedPost = null;
  }
  
  Feed.prototype.addPost = function(newPost) {
	  var feed = this;
	  $http.post('/blog/rest/posts',newPost).
	    success(function(result, status, headers, config) {
	    	feed.posts.unshift(result);
	        feed.newPost = '';
	        feed.stopPostEditing();
	        if(!$rootScope.$$phase) {
	        	  $rootScope.$apply();
	        }
	    }).
	    error(function(data, status, headers, config) {
	      console.log("Failed to save new post!")
	    });	     
  }

  return Feed;
});

app.factory('FollowData', function($rootScope,$http) {
	var FollowData = function FollowData(username) {
	    this.username = username;
		this.followersOfCurrentUser = [];
	    this.followedByCurrentUser = [];
	    this.followData = [];
	    this.feedName = null;
	    
	};
	
	FollowData.prototype.loadFollowData = function() {
		this.loadFollowedByCurrentUser();
		this.loadFollowersOfCurrentUser();
	};
	
	FollowData.prototype.isShowFollowButton = function(checkedUser) {
	    for(var i = 0; i < this.followedByCurrentUser.length; i++) {
	          if(this.followedByCurrentUser[i].username == checkedUser) {
	              return false;
	          }
	      }
	      return true;
    }

	FollowData.prototype.submitUnfollowAction = function(followUser) {
		var followData = this;
	      console.log('submitUnfollowAction: for user ' + followUser);
	      $http.delete(MicroblogApp.Config.contextPath + '/rest/follow-data?follower='+this.username + "&following="+followUser).
			  success(function(result, status, headers, config) {
				  removeFromArray(followData.followedByCurrentUser,followUser);
		          //$scope.switchFeeds($scope.feedName);
		          if(!$rootScope.$$phase) {
		        	  $rootScope.$apply();
		          }
			  }).
			  error(function(data, status, headers, config) {
				  alert("An error occurred deleting follow data");
			  });                  
	  }  

	FollowData.prototype.submitFollowAction = function(followUser) {
		var followData = this;
	      console.log('submitFollowAction: for user ' + followUser);
	      $http.post(MicroblogApp.Config.contextPath +'/rest/follow-data',{follower : this.username, following : followUser}).
			  success(function(result, status, headers, config) {
				  followData.followedByCurrentUser.push({recordId : result.id, username: result.following });
		          //$scope.switchFeeds($scope.feedName);
		          if(!$rootScope.$$phase) {
		        	  $rootScope.$apply();
		          }
			  }).
			  error(function(data, status, headers, config) {
				  alert("An error occurred adding follow data");
			  });
	  }
	  

	FollowData.prototype.isShowUnfollowButton = function(checkedUser) {
	      for(var i = 0; i < this.followedByCurrentUser.length; i++) {
	          if(this.followedByCurrentUser[i].username == checkedUser) {
	              return true;
	          }
	      }
	      return false;
	  }
	
	FollowData.prototype.isShowUnfollowButton = function(checkedUser) {
	      for(var i = 0; i < this.followedByCurrentUser.length; i++) {
	          if(this.followedByCurrentUser[i].username == checkedUser) {
	              return true;
	          }
	      }
	      return false;
	  }
	
	FollowData.prototype.switchFeeds = function(feedName) {
	    console.log("switchFeeds " + feedName);
	    this.feedName = feedName;
	    if (feedName == 'following') {
	      this.followData = this.followedByCurrentUser;
	    } else if (feedName == 'followers') {
	    	this.followData = this.followersOfCurrentUser;
	    } else {
	    	this.followData.feed = null;
	    }
	  };
	
	function removeFromArray(arr,username) {
		console.log('removeFromArray ' + arr);
	    var index = null;
		for(var i = 0; i < arr.length; i++) {
	    	if(arr[i].username == username) {
	    		index = i;
	    		break;	    		
	    	}
	    }
		if(index != null) {
			arr.splice(index, 1);
		}
		console.log('removeFromArray result' + arr);
	    return arr;
	  }
	
	FollowData.prototype.loadFollowersOfCurrentUser = function() {
		var followData = this;  
		$http.get(MicroblogApp.Config.contextPath +'/rest/follow-data?following='+this.username).
		    success(function(resultIn, status, headers, config) {
		    	  //console.log('load d from server ' + resultIn);
		          var followingUsers = [];
		          for(var i = 0; i < resultIn.length; i++) {
		              followingUsers.push({recordId : resultIn[i].id, username: resultIn[i].follower });
		          }
		          console.log('filtered followers ' + followingUsers);
		          followData.followersOfCurrentUser = followingUsers;		                    
		    }).
		    error(function(data, status, headers, config) {
		      console.log("Failed to get following of current user!")
		    });	      
	  }

	FollowData.prototype.loadFollowedByCurrentUser = function() {
		  var followData = this;  
		  $http.get(MicroblogApp.Config.contextPath +'/rest/follow-data?follower='+this.username).
		    success(function(resultIn, status, headers, config) {
		    	// console.log('loaded from server ' + resultIn);
		          var followingUsers = [];
		          for(var i = 0; i < resultIn.length; i++) {
		              followingUsers.push({recordId : resultIn[i].id, username: resultIn[i].following });
		          }
		          console.log('filtered followed ' + followingUsers);
		          followData.followedByCurrentUser = followingUsers;		          
		    }).
		    error(function(data, status, headers, config) {
		      console.log("Failed to get follower of current user!")
		    });          
	  }
	
	return FollowData;
});

app.directive('dpdMessageFor', function() {
  return function(scope, element, attrs) {
    var post = scope.$eval(attrs.dpdMessageFor);
    var message = post.message;
    var mentions = post.mentions;
    
    if (mentions) {
      mentions.forEach(function(m) {
        message = message.replace('@' + m, '<a href="/blog/app/user?user=' + m + '">@' + m + '</a>');
      });
    }

    var topics = post.topic; 
    if (topics) {
      topics.forEach(function(m) {
        message = message.replace('#' + m, '<a class="topic" ' +
                                            'href="'+ MicroblogApp.Config.contextPath+ '/app/route?topic=' + m +
                                            '">#' + m + '</a>');
      });
    }

    element.html(message);
  };
});


app.controller('LoginCtrl', function($scope, $rootScope,$http) {
  $rootScope.userLoaded = false;
  
  function getCurrentUser() {
	  $http.get(MicroblogApp.Config.contextPath+'/rest/users?action=current').
	    success(function(data, status, headers, config) {
	    	//console.log('loaded ' + data);
	    	if(data && data.username) {
	    		$rootScope.currentUser = data;
		        $rootScope.userLoaded = true;
		        if(!$scope.$$phase) {
		        	$scope.$apply();
		        }
	    	}	    	
	    }).
	    error(function(data, status, headers, config) {
	      console.log("Failed to log current user!")
	    });	
  }
  getCurrentUser();

});
