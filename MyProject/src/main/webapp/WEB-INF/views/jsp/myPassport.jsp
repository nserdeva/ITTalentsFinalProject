<%@ page import="model.UserDao"%>
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
	<jsp:include page="header.jsp"></jsp:include>


	<c:forEach var="post" items="${sessionScope.user.posts}">
		<div id="POST" class="w3-container w3-border" style="height: 500px">
			<div id="UPPER_POST" class="w3-container w3-border">
				<div id="LEFT_PICTURE_USER"
					class="w3-cell-row w3-cell w3-border w3-pink">
					<div id="USER_INFO" class="w3-cell-row w3-border ">
						<img src="${sessionScope.user.profilePic.url}" width=50px;>
						${post.dateTime}
						<p>${sessionScope.user.username}wasat
							${post.location.locationName} with</p>
						<c:forEach var="taggedUser" items="${post.taggedPeople}">
							${taggedUser.username};
						</c:forEach>
						<p>categories:</p>
						<c:forEach var="category" items="${post.categories}">
							${category.name};
						</c:forEach>
					</div>
					<div id="PICTURES" class="w3-cell-row w3-border">
						<p>${post.description}</p>
					</div>
				</div>
				<div id="RIGHT_MAP_VIDEO"
					class="w3-cell-row w3-cell w3-border w3-teal">
					<div id="MAP" class="w3-cell-row w3-border ">
						<p>sdlfksjdlkfsdlfs</p>
						<br> <br> <br>
					</div>
					<div id="VIDEO" class="w3-cell-row w3-border">
						<p>sdlfksjdlkfsdlfs</p>
						<br> <br> <br>
					</div>
				</div>
			</div>
			<div id="COMMENTS_LOWER" class="w3-container w3-teal w3-border">
				<c:forEach var="comment" items="${post.comments}">
					<img src="${comment.sentBy.profilePic.url}" width=25px;> 
						${comment.datetime}
							<p>${comment.sentBy.username}:</p>
					<p>${comment.content}</p>
					<p>likes: ${comment.likesCount} dislikes:
						${comment.dislikesCount}</p>
				</c:forEach>
			</div>
		</div>
	</c:forEach>


	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>