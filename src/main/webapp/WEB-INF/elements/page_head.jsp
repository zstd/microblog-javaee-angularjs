<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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