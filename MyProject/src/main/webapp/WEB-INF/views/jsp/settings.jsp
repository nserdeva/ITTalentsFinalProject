<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	<jsp:include page="header.jsp"></jsp:include>

	<div class="w3-container">
        <button onclick="showDescription()" class="w3-btn w3-black">Description</button>
        <form id="description" class="w3-hide w3-red" method="post" action="/settings/changeDescription">
			Change description: 
			<input type="text" id="descriptionChange" name="descriptionTxt" placeholder="${sessionScope.user.description}">
		    <input type="submit" value="Change description"/>
		</form>
		<br> <br>

        <button onclick="showEmail()" class="w3-btn w3-black">Email</button>
        <form:form id="emailChange" modelAttribute="email" class="w3-hide w3-red" method="post" action="/settings/changeEmail"  >
            Change email:
            <input type="text"  name="email" placeholder="${sessionScope.user.email}" />
            <input type="submit" value="Change email"/>
        </form:form>
        <br>
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
			var x = document.getElementById("description");
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