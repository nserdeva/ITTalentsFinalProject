<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 23.10.2017 г.
  Time: 14:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<style>
body{
	margin: 0;
	padding: 0;
	background: url(img/wanderlost.jpg);
	background-size: cover;
	font-family: sans-serif;
}
</style>
<title>Ooops!</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<h2>Ooops! You got wanderLOST!</h2>
	<br>
	<h3>Reason: ${ sessionScope.errorMessage}</h3>
    <br>
    <h2>Please, be more careful next time.</h2>


<br><br><br><br><br><br><br><br><br><br><br>
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>