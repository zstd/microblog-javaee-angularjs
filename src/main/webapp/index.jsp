<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<head>
  <title>Microblogging</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/angular.js"></script>
    <script type="text/javascript">
        var MicroblogApp = {};
        MicroblogApp.Config = {
            contextPath : '<%=request.getContextPath()%>'
        }
    </script>
</head>
<body>
  <div class="container">
    <ng-include src="'${pageContext.request.contextPath}/static/partials/header.html'"></ng-include>
    <div class="welcome" ng-controller="WelcomeCtrl">
      <div class="span8">
        <form id="login-form" class="well" ng-show="showMode == 'LOGIN'"
              action="${pageContext.request.contextPath}/login" method="POST">
          <h3>Enter existing user credentials</h3>
          <div>
            <label>Username</label><input type="text" name="j_username" />
          </div>
          <div>  
            <label>Password</label><input type="password" name="j_password" />  
          </div>
          <div class="control-buttons">
            <button class="btn btn-primary" >Log in</button>
            or
            <a ng-href="" ng-click="setShowMode('REGISTER')"/>Register</a>
          </div>
        </form>
        <form id="registration-form" class="well" ng-show="showMode == 'REGISTER'"
              action="${pageContext.request.contextPath}/register" method="POST">
          <h3>Enter new user's details</h3>
          <div>
            <label>Username</label><input type="text" name="username" />
          </div>
          <div>
            <label>Nickname</label><input type="text" name="nickname" />
          </div>
          <div>
            <label>Password</label><input type="password" name="password" />
          </div>
          <div>
            <label>Password check</label><input type="password" name="passwordCheck" />
          </div>
          <div>
              <label>Photo URL</label><input type="text" name="photoUrl" />
          </div>
          <div>
             <label>Description</label><textarea rows="4" cols="60"  class="input-xxlarge" name="description"></textarea>  
          </div>                    
          <div class="control-buttons">
            <button class="btn btn-primary">Register</button>
            or
            <a ng-href="" ng-click="setShowMode('LOGIN')"/>Login</a>
          </div>
        </form>
        
      </div>
    </div>
  </div>

  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/welcome.js"></script>
</body>
</html>