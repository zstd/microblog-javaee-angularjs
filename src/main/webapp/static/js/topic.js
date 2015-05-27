app.controller('TopicCtrl', function($scope, Feed) {

  var query = location.search;
  var topicName = query.split('?topic=')[1];
  if (!topicName) {
    location.href = "/";
  }

  $scope.topicName = topicName;

  var feed = new Feed({
    topic: topicName
  });
  feed.refresh();

  $scope.feed = feed;
});