<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Wanderlust - Register</title>
<link href="<c:url value="/css/style.css"/>" rel="stylesheet"
	type="text/css">
</head>
<body>

	<c:if test="${ sessionScope.user != null }">
		<c:redirect url="/myPassport"></c:redirect>
	</c:if>

	<div class="registerBox">
		<h2>Register</h2>
		<form method="post" action="/register">
			<div class="tooltip">
				<p>username</p>
				<span class="tooltiptext"><font size="2">at least 5
						characters long, a-Z, 0-9, '-', '_' allowed</font></span>
			</div>


			<c:if test="${isValidUsername!='false'}">
				<input type="text" placeholder="enter username" name="user">
			</c:if>

			<c:if test="${isValidUsername=='false'}">
				<input type="text" style="border:2px solid #ff0000" placeholder="invalid username" name="user">
			</c:if>

			<p>e-mail</p>

			<c:if test="${isValidEmail!='false'}">
				<input type="text" name="email"
					placeholder="enter a valid e-mail address">
			</c:if>

			<c:if test="${isValidEmail=='false'}">
				<input type="text" style="border:2px solid #ff0000" name="email" placeholder="invalid e-mail address">
			</c:if>

			<div class="tooltip">
				<p>password
				<p>
					<span class="tooltiptext"><font size="2">at least 6
							characters long, must contain one uppercase, one lowercase and
							one non-alphabetic character</font></span>
			</div>

			<c:if test="${isValidPassword!='false'}">
				<input type="password" name="pass" placeholder="enter password">
			</c:if>
			
			<c:if test="${isValidPassword=='false'}">
			<input type="password" style="border:2px solid #ff0000" name="pass" placeholder="invalid password">
			</c:if>

			<c:if test="${doPasswordsMatch!='false'}">
				<input type="password" name="pass2" placeholder="retype password">
			</c:if>

			<c:if test="${doPasswordsMatch=='false'}">
				<input type="password"  style="border:2px solid #ff0000" name="pass2" placeholder="passwords do not match">
			</c:if>


			<input type="submit" value="Register">
			<font color = "white" >Already part of the travelling world? </font>
			<a href="<c:url value="/login" /> "><u> Get back on track! </u></a>
		</form>
	</div>
</body>
</html>