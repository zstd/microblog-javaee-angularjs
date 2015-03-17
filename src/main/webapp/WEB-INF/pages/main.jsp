<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<head>
  <title>Microblogging</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/angular.js"></script>
</head>
<body>
  <div class="container">
    <ng-include src="'${pageContext.request.contextPath}/static/partials/header.html'"></ng-include>
        Welcome to the party!
  </div>

  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/welcome.js"></script>
</body>
</html>