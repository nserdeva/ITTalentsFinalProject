<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Wanderlust</title>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkHN_gdiuaWXmHeLB8Fpe_pBc840VRgIk&callback=map"
			type="text/javascript"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>

	<c:forEach var="post" items="${sessionScope.user.posts}">
	<div class="w3-container w3-pink w3-border" style="width: 75%">
		<div id="USER_INFO" class="w3-cell-row w3-pink w3-border " style="width: 75%">
			<img src="/settings/getAvatar" class="w3-circle" height="50px" width="auto">
				${post.dateTime}
		</div>
		<c:if test="${ not empty post.location}">
			<div id="LOCATION_INFO" class="w3-cell-row w3-teal w3-border" style="width: 75%">${sessionScope.user.username} was at
				${post.location.locationName}
		</div>
		</c:if>
		<c:if test="${post.taggedPeople.size()>0}">
			<div id="TAGGED_PEOPLE" class="w3-cell-row w3-border w3-teal" style="width: 75%">
				with
				<c:forEach var="taggedUser" items="${post.taggedPeople}">
					${taggedUser.username}
				</c:forEach>
			</div>
		</c:if>
		<div id="mapp" class="w3-cell-row w3-border " style="width: 75%">
			<div id="map" style="float: left; width: 500px; height: 400px;"></div>
			<br> <br> <br>
		</div>
		<c:if test="${post.categories.size()>0}">
			<div id="CATEGORIES" class="w3-cell-row w3-border w3-pink" style="width: 75%">
				<p>categories:</p>
				<c:forEach var="category" items="${post.categories}">
					${category.name}
				</c:forEach>
			</div>
		</c:if>
		<div id="PICTURES" class="w3-cell-row w3-teal w3-border" style="width: 75%">
			<div class="w3-row">
				<c:forEach var="multimedia" items="${post.multimedia}">
					<div class="w3-half">
						<img src="/getMultimedia/${multimedia.url}" style="width:50%" class="w3-margin-bottom">
					</div>
				</c:forEach>
			</div>
		</div>
		<c:if test="${post.video!=null}">
			<div id="VIDEO" class="w3-cell-row w3-pink w3-border" style="width: 75%">
				<video width="320" height="240" controls="controls">
					<source src="<c:url value="/getVideo/${post.video.url}"/> " type="video/mp4">
					Your browser does not support the video tag.
				</video>
			</div>
		</c:if>
		<div id="COMMENTS" class="w3-cell-row w3-teal w3-border" style="width: 75%">
			<c:forEach var="comment" items="${post.comments}">
				<div class="w3-border" style="width: 75%">
					<img src="${comment.sentBy.profilePic.url}" width=25px;>
						${comment.datetime}
					<p>${comment.sentBy.username}:</p>
					<p>${comment.content}</p>
					<p>likes: ${comment.likesCount} dislikes:
							${comment.dislikesCount}</p>
				</div>
			</c:forEach>
		</div>
	</div>
		<br><br>
	</c:forEach>


	<jsp:include page="footer.jsp"></jsp:include>

	<script type="text/javascript">
        var locations = [
            ['Musagenica', 42.661723, 23.360285, 4],
            ['Stud. grad', 42.653884, 23.345965, 5],
            ['Geo Milev', 42.680135, 23.356985, 3],
            ['Lozenec', 42.674448, 23.309441, 2],
            ['Center', 42.697180, 23.316997, 1]
        ];
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 12,
            center: new google.maps.LatLng(42.661723, 23.360285),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });
        var infowindow = new google.maps.InfoWindow();
        var marker, i;
        for (i = 0; i < locations.length; i++) {
            marker = new google.maps.Marker({
                position: new google.maps.LatLng(locations[i][1], locations[i][2]),
                map: map
            });
            google.maps.event.addListener(marker, 'click', (function(marker, i) {
                return function() {
                    infowindow.setContent(locations[i][0]);
                    infowindow.open(map, marker);
                }
            })(marker, i));
        }
	</script>


</body>
</html>