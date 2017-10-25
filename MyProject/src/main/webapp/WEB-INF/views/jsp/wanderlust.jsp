<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="w3-container">
<div class="w3-panel w3-teal">
    <h6 class="w3-opacity">Welcome, ${sessionScope.user.username}</h6>
</div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
