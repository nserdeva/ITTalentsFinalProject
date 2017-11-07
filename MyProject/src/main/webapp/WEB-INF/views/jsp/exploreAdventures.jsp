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
	margin-bottom: 0px;
	margin-left: 150px;
	padding: 0;
	background-color: #b3e6cc;
	border: 2px solid #ddd;
}

.container {
	position: center;
	height: 160px;
	align: center;
	width: 70%;
	margin-bottom: 25px;
	padding: 1em;
	background-color: #eee;
	border: 3px solid #ddd;
}

.floatedbox {
	float: left;
	width: 125px;
	height: 125px;
	margin-right: 1em;
	padding: 0 10px;
	background-color: #fff;
	border: 3px solid #bbb;
	border-radius: 8px;
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

<!-- w3-content defines a container for fixed size centered content, 
and is wrapped around the whole page content, except for the footer in this example -->
	<div class="w3-content" style="max-width: 1400px">

		<!-- Grid -->
		<div class="w3-row">

			<!-- Blog entries -->
			<div class="w3-col l8 s12" style="float: none; margin: 0 auto;">


				<c:forEach var="post" items="${sessionScope.browsedAdventures}">
					<!-- Blog entry -->
					<div class="w3-card-4 w3-margin w3-white">
						<div style="text-align: center;">
							<img src="/post/getMainPic/${post.id}" style="max-width:100%; margin: 0 auto;">
						</div>

						<div class="w3-container">
							<h3>
								<b> 
								
								<a target="_blank" href="/showPassport/${post.user.userId}">
												${post.user.username}</a>;
								
								<c:if
										test="${post.location!=null}">
 was at 
 <a target="_blank" href="/location/${post.location.id}">${post.location.locationName}</a>

									</c:if> <c:if test="${post.taggedPeople.size()>0}">
with 	
<c:forEach var="taggedUser" items="${post.taggedPeople}">
											<a target="_blank" href="/showPassport/${taggedUser.userId}">
												${taggedUser.username}</a>;
				</c:forEach>
									</c:if></b>
							</h3>
							<h5>
								<span class="w3-opacity">${post.dateTimeString}</span>

								<c:if test="${post.categories.size()>0}">
									<br>Categories:
							<c:forEach var="category" items="${post.categories}">
							${category.name};
							</c:forEach>
								</c:if>
								<c:if test="${post.tags.size()>0}">
									<br>Tags:
				<c:forEach var="tag" items="${post.tags}">${tag.tag_name};</c:forEach>
								</c:if>
							</h5>
						</div>
						<div class="w3-container">
							<p>${post.description}</p>
							<div class="w3-row">
								<div class="w3-col m8 s12">
									<p>
										<button class="w3-button w3-padding-large w3-white w3-border">
											<b><a target="_blank" href="/post/${post.id}">
													EXPLORE MORE »</a> </b>
										</button>
									</p>
								</div>
								<div class="w3-col m4 w3-hide-small">
									<p>
										<span class="w3-padding-large w3-right"><b>Likes:  </b>
											<span class="w3-tag">${post.peopleLiked.size()}</span></span>
									</p>
									<p>
										<span class="w3-padding-large w3-right"><b>Dislikes:
												 </b> <span class="w3-tag">${post.peopleDisliked.size()}</span></span>
									</p>
								</div>
							</div>
						</div>
					</div>
					<hr>
				</c:forEach>


				<!-- END BLOG ENTRIES -->
			</div>


			<!-- END GRID -->
		</div>
		<br>

		<!-- END w3-content -->
	</div>



	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>