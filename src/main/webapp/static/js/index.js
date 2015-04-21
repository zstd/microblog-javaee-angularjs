app.controller('FeedCtrl', function($scope,$rootScope, $http, Feed,FollowData) {

  //var PAGE_SIZE = 5;

  var allFeed = new Feed();
  allFeed.refresh();
  
  var followingFeed = new Feed({
	  'following' : true
  }); 
  followingFeed.refresh();
  
  var discoverFeed = new Feed({
	  'discover' : true
  }); 
  discoverFeed.refresh();
  var scope = $scope;
  var rootScope = $rootScope;
  // initialize after current user has loaded
  $rootScope.$watch("currentUser",function(newValue,oldValue){ 
	  if(rootScope.currentUser) {
		  scope.feed = allFeed;
		  var followData = new FollowData(rootScope.currentUser.username);
		  followData.loadFollowData();
		  scope.followData = followData;
		  scope.switchFeeds('discover');
	  }	  
  });
  
   
  $scope.switchFeeds = function(feedName) {
	    console.log("switchFeeds1 " + feedName);
	    $scope.feedName = feedName;
	    if (feedName == 'all') {
	      $scope.feed = allFeed;
	    } else if (feedName == 'discover') {
	      $scope.feed = discoverFeed;
	    } else if (feedName == 'followers') {
	      $scope.feed = followingFeed;
	    } else {
	      $scope.feed = null;
	    }
  };
  
  $scope.submit = function(newPost) {
	    
	  $http.post(MicroblogApp.Config.contextPath + '/rest/posts',newPost).
	    success(function(result, status, headers, config) {
	    	$scope.newPost = '';
	    	allFeed.refresh();
	    	followingFeed.refresh();
	    	discoverFeed.refresh();
	        if(!$scope.$$phase) {
	        	  $scope.$apply();
	        }
	    }).
	    error(function(data, status, headers, config) {
	      console.log("Failed to save new post!")
	    });
  }

});