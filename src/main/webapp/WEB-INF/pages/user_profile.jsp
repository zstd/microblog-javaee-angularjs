<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<jsp:include page="../elements/page_head.jsp" />
<body>
  <div class="container">
      <jsp:include page="../elements/header.jsp" />
      <div class="feed" ng-controller="UserFeedCtrl">
          <div class="span8">
              <h2>Profile of user '{{username}}'</h2>
              <div class="profile_info">
          <span class="profile_foto">
              <img src="{{userdetails.photoUrl}}" alt="{{username}} avatar">
          </span>
          <span class="profile_data">
              <div class="descr_title">Nickname: </div>
              <div class="descr_value">{{userdetails.nickname}}</div>
              <div class="descr_title">About:</div>
              <div class="descr_value">{{userdetails.description}}</div>
          </span>
              </div>
              <ul class="nav nav-tabs user_toolbar">
                  <li ng-class="{active: feedName == 'posts'}"><a href ng-click="switchFeeds('posts')">Posts</a></li>
                  <li ng-class="{active: feedName == 'following'}"><a href ng-click="switchFeeds('following')">Followed by '{{username}}'</a></li>
                  <li ng-class="{active: feedName == 'followers'}"><a href ng-click="switchFeeds('followers')">Followers of '{{username}}'</a></li>
              </ul>

              <ng-include src="'${pageContext.request.contextPath}/static/partials/follow.html'"></ng-include>
              <ng-include src="'${pageContext.request.contextPath}/static/partials/feed.html'"></ng-include>
          </div>
      </div>
  </div>

  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/user.js"></script>
</body>
</html>