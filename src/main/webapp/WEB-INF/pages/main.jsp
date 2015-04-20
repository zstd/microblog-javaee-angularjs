<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<jsp:include page="../elements/page_head.jsp" />
<body>
  <div class="container">
      <jsp:include page="../elements/header.jsp" />
      <div class="feed" ng-controller="FeedCtrl">
          <div class="span8">
              <p ng-hide="currentUser" class="muted">
                  Log in to post a message
              </p>
              <form class="well" ng-show="currentUser">
                  <textarea class="input-xxlarge" placeholder="Tell us something?" row="4" cols="80" ng-model="newPost"></textarea>
                  <div>
                      <button class="btn btn-primary" ng-click="submit(newPost)">Post</button>
                  </div>
              </form>
              <ul class="nav nav-tabs user_toolbar">
                  <li ng-class="{active: feedName == 'discover'}"><a href ng-click="switchFeeds('discover')">Discover post</a></li>
                  <li ng-class="{active: feedName == 'followers'}"><a href ng-click="switchFeeds('followers')">My followers posts</a></li>
                  <li ng-class="{active: feedName == 'all'}"><a href ng-click="switchFeeds('all')">All Posts</a></li>
              </ul>
              <ng-include src="'${pageContext.request.contextPath}/static/partials/feed.html'"></ng-include>
          </div>
      </div>
  </div>

  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/index.js"></script>
</body>
</html>