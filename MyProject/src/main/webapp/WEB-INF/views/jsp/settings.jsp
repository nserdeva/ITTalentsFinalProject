<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 26.10.2017 г.
  Time: 17:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>Title</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>

<c:if test="${ sessionScope.user == null }">
    <c:redirect url="/login"></c:redirect>
</c:if>

	<jsp:include page="header.jsp"></jsp:include>

	<div class="w3-container">

        <button onclick="showAvatar()" class="w3-btn w3-black">Avatar</button>
        <img src="/settings/getAvatar" class="w3-circle" height="10%" width="auto">
        <form id="avatarChange" class="w3-hide w3-red" method="post" action="/settings/changeAvatar"  enctype="multipart/form-data">
            Change avatar:
            <input type="file" name="avatar" accept="image/*">
            <input type="submit" value="Change avatar"/>
        </form><br>

        <button onclick="showDescription()" class="w3-btn w3-black">Description</button>
        <form id="descriptionChange" class="w3-hide w3-red" method="post" action="/settings/changeDescription">
			Change description: 
			<input type="text" name="descriptionTxt" value="${sessionScope.user.description}">
		    <input type="submit" value="Change description"/>
		</form><br>

        <button onclick="showEmail()" class="w3-btn w3-black">Email</button>
        <form id="emailChange" name="email" class="w3-hide w3-red" method="post" action="/settings/changeEmail"  >
            Change email:
            <input type="text"  name="email" placeholder="${sessionScope.user.email}" />
            <input type="submit" value="Change email"/>
        </form><br>

        <button onclick="showPassword()" class="w3-btn w3-black">Password</button>
        <form id="passwordChange" name="password" class="w3-hide w3-red" method="post" action="/settings/changePassword"  >
            Change password:
            <input type="password"  name="oldPassword" placeholder="Old password" /><br>
            <input type="password"  name="newPassword" placeholder="New password"  /><br>
            <input type="password"  name="confirmPassword" placeholder="Confirm new password" /><br>
            <input type="submit" value="Change password"/>
        </form><br>
    </div>

	<script>
		function showEmail() {
			var x = document.getElementById("emailChange");
			if (x.className.indexOf("w3-show") == -1) {
				x.className += " w3-show";
			} else {
				x.className = x.className.replace(" w3-show", "");
			}
		}
		function showDescription() {
            var x = document.getElementById("descriptionChange");
            if (x.className.indexOf("w3-show") == -1) {
                x.className += " w3-show";
            } else {
                x.className = x.className.replace(" w3-show", "");
            }
        }
        function showAvatar() {
            var x = document.getElementById("avatarChange");
            if (x.className.indexOf("w3-show") == -1) {
                x.className += " w3-show";
            } else {
                x.className = x.className.replace(" w3-show", "");
            }
        }
        function showPassword() {
            var x = document.getElementById("passwordChange");
            if (x.className.indexOf("w3-show") == -1) {
                x.className += " w3-show";
            } else {
                x.className = x.className.replace(" w3-show", "");
            }
        }
	</script>

	<jsp:include page="footer.jsp"></jsp:include>


</body>
</html>