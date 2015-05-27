<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<jsp:include page="../elements/page_head.jsp"/>
<body>
<div class="container">
    <jsp:include page="../elements/header.jsp"/>
    <div class="feed" ng-controller="TopicCtrl">
        <div class="span8">
            <h2>Topic {{topicName}}'s feed</h2>
            <ng-include src="'${pageContext.request.contextPath}/static/partials/feed.html'"></ng-include>
        </div>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/topic.js"></script>
</body>
</html>