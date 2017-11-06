<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 18.10.2017 г.
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>Wanderlust - Start exploring now!</title>
	<link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
<c:if test="${ sessionScope.user != null }">
	<c:redirect url="/myPassport"></c:redirect>
</c:if>

	<div class="loginBox">
		<img src="img/user_icon.png" class="user">
		<h2>Welcome, traveller!</h2>
		<form action="/login" method="post">
			<p>
				<font color="white"> username</font>
			</p>
			<c:if test="${isValidData!='false'}">
				<input type="text" placeholder="enter username" name="user">
			</c:if>

			<c:if test="${isValidData=='false'}">
				<input type="text" placeholder="username or password incorrect"
					name="user">
			</c:if>
			<p>
				<font color="white">password</font>
			</p>
			<input type="password" placeholder="••••••••••••••" name="pass">
			<input type="submit" value="Sign in"> 
			<font color = "white" >New to the travelling world? </font>
			<a href="<c:url value="/register" /> "><u> Join now! </u></a>
		</form>
	</div>
</body>
</html>