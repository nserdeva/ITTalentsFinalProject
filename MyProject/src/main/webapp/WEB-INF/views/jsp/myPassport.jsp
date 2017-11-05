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
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAvyBZwme7_jzLC85kY0OCv_5SXfFuc-qw&callback=map" type="text/javascript"></script>
</head>
<body>
<c:if test="${ sessionScope.user == null }">
	<c:redirect url="/login"></c:redirect>
</c:if>

	<jsp:include page="header.jsp"></jsp:include>
	<c:set var="i" value="0" scope="page" />
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
						<img src="/getMultimedia/${multimedia.id}" style="width:50%" class="w3-margin-bottom">
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
		<div id="like/dislike" >
			<button style="background-color: green" id="likeButton " onclick="handleLike(${post.id})" >Like</button>
			<button style="background-color: green" id="dislikeButton " onclick="handleDislike(${post.id})" >Dislike</button>
		</div>
	</div>
		<br><br>
		<c:set var="i" value="${i + 1}" scope="page"/>
	</c:forEach>
	<jsp:include page="footer.jsp"></jsp:include>

	<script type="text/javascript">

            var locations = [
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
                google.maps.event.addListener(marker, 'click', (function (marker, i) {
                    return function () {
                        infowindow.setContent(locations[i][0]);
                        infowindow.open(map, marker);
                    }
                })(marker, i));
            }
	</script>

<script>
    function handleLike(postId){
        var button = document.getElementById("likeButton");
        var title = button.innerHTML;
        if(title == "Like"){
            likePost(postId);
        }
        else{
            unlikePost(postId);
        }
    }

    function handleDislike(postId) {
        var button=document.getElementById("dislikeButton");
        var title=button.innerHTML;
        if(title=='Dislike'){
            dislikePost(postId);
		}
		else{
            unDislikePost(postId);
		}
    }

    function likePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("likeButton");
                button.innerHTML = "Unlike";
                button.style.background='red';
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "like/"+postId, true);
        request.send();
    }

    function unlikePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("likeButton");
                button.innerHTML = "Like";
                button.style.background='green';
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "unlike/"+postId, true);
        request.send();
    }

    function dislikePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("dislikeButton");
                button.innerHTML = "Undislike";
                button.style.background='red';
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "dislike/"+postId, true);
        request.send();
    }

    function unDislikePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("dislikeButton");
                button.innerHTML = "Dislike";
                button.style.background='green';
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "undislike/"+postId, true);
        request.send();
    }





</script>

</body>
</html>