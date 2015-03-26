<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="header" ng-controller="LoginCtrl">
    <div class="clearfix">
        <h1 class="pull-left"><a href="${pageContext.request.contextPath}/app/main">Microblogging</a></h1>
        <div class="welcome pull-right" ng-show="userLoaded">
            <span ng-show="currentUser">Welcome, @{{currentUser.username}}!
                <a href="${pageContext.request.contextPath}/logout">Logout</a></span>
        </div>
    </div>
</div>
