<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="microblogApp">
<jsp:include page="../elements/page_head.jsp" />
<body>
  <div class="container">
      <jsp:include page="../elements/header.jsp" />
        Welcome to the party!
  </div>

  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/global.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/welcome.js"></script>
</body>
</html>