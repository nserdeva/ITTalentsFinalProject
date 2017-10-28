<%@ page import="com.example.model.DBManagement.UserDao"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Wanderlust</title>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include><br>

	<style>
input[type=text] {
	width: 130px;
	height: 30px;
	box-sizing: border-box;
	border: 2px solid #ccc;
	border-radius: 4px;
	font-size: 16px;
	background-color: white;
	background-image: url('../img/search_icon.png');
	background-position: 10px 2px;
	background-repeat: no-repeat;
	padding: 12px 20px 12px 40px;
	-webkit-transition: width 0.4s ease-in-out;
	transition: width 0.4s ease-in-out;
}

input[type=text]:focus {
	width: 39%;
}
</style>

	<style>


table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 80%;
            border: 1px solid #909090;
    
}

td, .th {
        border: 1px solid #909090;
    text-align: left;
    padding: 5px;
}

tr {
    background-color: #dddddd;
    
}
</style>

	<form method="post" action="/search">
		<input type="text" name="searchFormDataTxt" placeholder="Search..">
		<input type="checkbox" name="natureCheckBox" class="chb" value="false">Nature
		<input type="checkbox" name="seaCheckBox" class="chb" value="false">Sea
		<input type="checkbox" name="mountainsCheckBox" class="chb"
			value="false">Mountains <input type="checkbox"
			name="dessertCheckBox" class="chb" value="false">Dessert <input
			type="checkbox" name="landmarkCheckBox" class="chb" value="false">Landmark
		<input type="checkbox" name="resortCheckBox" class="chb" value="false">Resort
		<input type="checkbox" name="cityCheckBox" class="chb" value="false">City

			<button  onclick="changeCheckBoxesValues()">explore</button>
		
	</form>


<style>
button
{
border: none;
outline: none;
height: 34px;
width: 223px;
color: #fff;
font-size: 16px;
background: #00cc66;	
cursor: pointer;
border-radius: 10px;
}
button:hover
{
	background: #66ff99;
	color: #262626;
}

</style>


	<script>
		function changeCheckBoxesValues() {
			var chb = document.getElementsByClassName('chb');

			if (chb[0].checked) {
				chb[0].value = "true";
			}
			if (chb[1].checked) {
				chb[1].value = "true";
			}

			if (chb[2].checked) {
				chb[2].value = "true";
			}

			if (chb[3].checked) {
				chb[3].value = "true";
			}

			if (chb[4].checked) {
				chb[4].value = "true";
			}

			if (chb[5].checked) {
				chb[5].value = "true";
			}

			if (chb[6].checked) {
				chb[6].value = "true";
			}
		}
	</script>





<br><br>
<table align="center">
	<c:forEach var="location" items="${sessionScope.user.browsedLocations}">
	


				
<tr >
    <td  width="10%"><img src="" width="100" height="100" align="middle">
    
</td>
<td ><a target="_blank" href="/location">${location.locationName}</a><br><br>${location.description}</td>
  </tr>
  <tr>
	</c:forEach>
</table>




	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>