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
<style type="text/css" media="screen">


.subContainer {
position: right;
align: right;
height: 30px;
width: 80%;
margin-bottom:0px;
margin-left: 150px;
padding:0;
background-color: #b3e6cc;
border:2px solid #ddd;
}
.container {
position: center;
height: 160px;
align: center;
width: 70%;
margin-bottom:25px;
padding:1em;
background-color:#eee;
border:3px solid #ddd;
}
.floatedbox {
float:left;
width:125px;
height:125px;
margin-right:1em;
padding:0 10px;
background-color:#fff;
border:3px solid #bbb;
}

.clearfix:after {
content: ".";
display: block;
height: 0;
clear: both;
visibility: hidden;
}
</style>
</head>
<body>
<c:if test="${ sessionScope.user == null }">
	<c:redirect url="/login"></c:redirect>
</c:if>

	<jsp:include page="header.jsp"></jsp:include><br>

	<c:forEach var="location" items="${sessionScope.browsedLocations}">				
     <div class="container" width=70% >
<div class="floatedbox">
<p><img src="/location/getMainPic/${location.id}" border="3" width="120" height="120" align="middle"></p>
</div>
<h3>
<img src="img/location_tag.png" width="30" height="30"> 
<a target="_blank" href="/location/${location.id}">${location.locationName}</a></h3>
<p>${location.shortDescription} 
<div class="subContainer"> 
</div>
</p>

</div>
	</c:forEach>



	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>