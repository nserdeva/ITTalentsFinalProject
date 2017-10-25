<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Wanderlust - Register</title>
<link rel="stylesheet" href="../../../static/css/style.css">
</head>
<body>
	<div class="registerBox">
		<h2>Register</h2>
		<form method="post" action="register">
			<div class="tooltip">
				usename <span class="tooltiptext"><font size="2">at
						least 5 characters long, a-Z, 0-9, '-', '_' allowed</font></span>
			</div>
			<input type="text" name="user" placeholder="enter username">
			<p>e-mail</p>
			<input type="text" name="email" placeholder="enter a valid e-mail address">
			<div class="tooltip">
				password <span class="tooltiptext"><font size="2">at
						least 6 characters long, must contain one uppercase, one lowercase
						and one non-alphabetic character</font></span>
			</div>
			<input type="password" name="pass" placeholder="enter password">
			<input type="password" name="pass2" placeholder="retype password">
			<input type="submit"  value="Register">
		</form>
	</div>
</body>
</html>