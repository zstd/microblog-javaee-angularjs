app.controller('UserFeedCtrl', function($scope, $http, Feed,FollowData) {

  var query = location.search;
  var username = query.split('?user=')[1];
  if (!username) {
    location.href = "/";
  }

  $scope.username = username;

  var query = {username: username};

  function updateUserDetails(userDetails) {
    if(userDetails) {
      $scope.userdetails = userDetails;
      if(!$scope.$$phase) {
    	  $scope.$apply();
      }
    }    
  }

  function removeFromArray(arr) {
    var what, a = arguments, L = a.length, ax;
    while (L > 1 && arr.length) {
        what = a[--L];
        while ((ax= arr.indexOf(what)) !== -1) {
            arr.splice(ax, 1);
        }
    }
    return arr;
  }
  
  $http.get(MicroblogApp.Config.contextPath + '/rest/users/user-info?user='+username).
	  success(function(result, status, headers, config) {
	  	console.log('loaded user-info ' + result);
	  	updateUserDetails(result);
	  }).
	  error(function(data, status, headers, config) {
	    console.log("Failed to log user-info for user " + userinfo)
	  });

  var feed = new Feed({
    creatorName: username
  });
  feed.refresh();
  
  var followData = new FollowData(username);
  followData.loadFollowData();

  $scope.switchFeeds = function(feedName) {
    console.log("switchFeeds " + feedName);
    $scope.feedName = feedName;
    if (feedName == 'posts') {
        $scope.feed = feed;       
      } else {
    	  $scope.feed = null;
      }
    $scope.followData.switchFeeds(feedName);    
  };

  $scope.followData = followData;
  $scope.feed = feed;
  $scope.switchFeeds('posts');
  
  
});