<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

	<!-- In the Head of the Page being shared -->
	<!-- For Facebook -->
	<meta property="og:title" content="Wanderlust" />
	<meta property="og:type" content="article" />
	<meta property="og:image" content="" />
	<meta property="og:url" content="" />
	<meta property="og:description" content="" />



<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>View Adventure</title>
<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkHN_gdiuaWXmHeLB8Fpe_pBc840VRgIk&callback=map"
	type="text/javascript"></script>
<style>
.image {
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 5px;
	width: 150px;
}

.image:hover {
	opacity: 0.5;
	box-shadow: 0 0 2px 1px rgb(51, 51, 255);
}

table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 80%;
	border: 2px solid #909090;
}

td, .th {
	border: 2px solid #909090;
	text-align: left;
	padding: 5px;
}

tr {
	background-color: #dddddd;
}
</style>
</head>
<body>

	<c:if test="${ sessionScope.user == null }">
		<c:redirect url="/login"></c:redirect>
	</c:if>


	<jsp:include page="header.jsp"></jsp:include><br>

	<table align="center">
		<tr>
			<td><img src="/user/picture/${sessionScope.post.user.userId}"
				border="3" width="45" height="45" align="middle"
				style="border-radius: 80px; border-style: solid; border-color: #bbb;">
				${sessionScope.post.dateTimeString} <a target="_blank"
				href="/showPassport/${sessionScope.post.user.userId}">
					${sessionScope.post.user.username}</a> <c:if
					test="${sessionScope.post.location!=null}">
				was at
				<a target="_blank" href="/location/${sessionScope.post.location.id}">${sessionScope.post.location.locationName}</a>

				</c:if>
				<c:if test="${sessionScope.post.taggedPeople.size()>0}">
				with
				<c:forEach var="taggedUser"
						items="${sessionScope.post.taggedPeople}">
						<a target="_blank" href="/showPassport/${taggedUser.userId}">
							${taggedUser.username}</a>;
				</c:forEach>
				</c:if>
				<p>${sessionScope.post.description}
				<div class="subContainer">
					Categories:
					<c:forEach var="category" items="${sessionScope.post.categories}">
					${category.name};
				</c:forEach>
				</div>
				<div class="subContainer">
					Tags:
					<c:forEach var="tag" items="${sessionScope.post.tags}">
					${tag.tag_name};
				</c:forEach>
				</div>
				<div class="subContainer">
					Likes:
					<p id="likesCount">${sessionScope.post.peopleLiked.size()}</p>
					Dislikes:
					<p id="dislikesCount">${sessionScope.post.peopleDisliked.size()}</p>
				</div>
				</td>
		</tr>
		<tr>
	</table>

	<table align="center">
		<tr>
			<td><c:forEach var="multimediaFile"
					items="${sessionScope.post.multimedia}">
					<a target="_blank" href="/post/multimedia/${multimediaFile.id}">
						<img class="image" src="/post/multimedia/${multimediaFile.id}"
						style="width: 150px">
					</a>
				</c:forEach></td>
		<tr>
	</table>
	<c:if test="${sessionScope.post.video.url != null}">
		<video width="320" height="240" controls="controls"> <source
			src="<c:url value="/getVideo/${sessionScope.post.video.url}"/> "
			type="video/mp4"> Your browser does not support the video
		tag. </video>
	</c:if>


	<button id="shareBtn" class="btn btn-success clearfix" onclick="loadShare()">Share On Facebook</button>


	<script>

		function loadShare() {
            window.fbAsyncInit = function() {
                FB.init({
                    appId : '299678220535690',
                    cookie : true,
                    xfbml : true,
                    version : 'v2.2'
                });
                FB.getLoginStatus(function(response) {
                    statusChangeCallback(response);
                });
            };

            window.open('https://www.facebook.com/sharer/sharer.php?u=https://192.168.6.237:8080/post/'+${sessionScope.post.id},  'sharer', 'width=626,height=436');

        }


	</script>



	<c:if test="${sessionScope.post.location !=null}">
	<table align="center">
		<tr>
			<td>
				<div id="map" align="center" style="width: 1070px; height: 400px;"></div>
			</td>
		</tr>
	</table>		<input type="hidden" id="latitude"
			value="${sessionScope.post.location.latitude}" />
		<input type="hidden" id="longtitude"
			value="${sessionScope.post.location.longtitude}" />
	</c:if>

	<br>
	<br>
	<br>
	<textarea id="newCommentInputContent" rows="6" cols="79"></textarea>
	<br>
	<button style="background-color: purple" id="postCommentButton"
		onclick="postComment(${sessionScope.post.id})">Post</button>
	<br>
	<br>
	<table id="commentsTable" align="center">
		<c:forEach var="comment" items="${sessionScope.post.comments}">
			<tr>
				<td>
					<div class="container" width=70%>
						<img src="/user/picture/${comment.sentBy.userId}" border="3"
							width="45" height="45" align="middle"
							style="border-radius: 80px; border-style: solid; border-color: #bbb;">
						${comment.datetimeString} <a target="_blank"
							href="/showPassport/${comment.sentBy.userId}">
							${comment.sentBy.username}</a> <br>${comment.content} <br>
						<p id="likesCount/${comment.id}">Likes:
							${comment.peopleLiked.size()}</p>
						<p id="dislikesCount/${comment.id}">Dislikes:
							${comment.peopleDisliked.size()}</p>


						<div id="likeComment/dislikeComment">
							<c:set var="containsLiked" value="false" />
							<c:forEach var="personLiked" items="${comment.peopleLiked}">
								<c:if test="${personLiked eq sessionScope.user.userId}">
									<c:set var="containsLiked" value="true" />
								</c:if>
							</c:forEach>

							<c:if test="${containsLiked}">
								<button style="background-color: red"
									id="likeButton/${comment.id}"
									onclick="handleCommentLike(${comment.id})">Unlike</button>
							</c:if>
							<c:if test="${!containsLiked}">
								<button style="background-color: green"
									id="likeButton/${comment.id}"
									onclick="handleCommentLike(${comment.id})">Like</button>
							</c:if>

							<c:set var="containsDisliked" value="false" />
							<c:forEach var="personDisliked" items="${comment.peopleDisliked}">
								<c:if test="${personDisliked eq sessionScope.user.userId}">
									<c:set var="containsDisliked" value="true" />
								</c:if>
							</c:forEach>

							<c:if test="${containsDisliked}">
								<button style="background-color: red"
									id="dislikeButton/${comment.id}"
									onclick="handleCommentDislike(${comment.id})">Undislike</button>
							</c:if>
							<c:if test="${!containsDisliked}">
								<button style="background-color: green"
									id="dislikeButton/${comment.id}"
									onclick="handleCommentDislike(${comment.id})">Dislike</button>
							</c:if>
						</div>


					</div>
				</td>
			</tr>
		</c:forEach>
	</table>


	<div id="like/dislike">
		<c:set var="containsLiked" value="false" />
		<c:forEach var="personLiked" items="${sessionScope.post.peopleLiked}">
			<c:if test="${personLiked eq sessionScope.user.userId}">
				<c:set var="containsLiked" value="true" />
			</c:if>
		</c:forEach>

		<c:if test="${containsLiked}">
			<button style="background-color: red" id="likeButton"
				onclick="handleLike(${sessionScope.post.id})">Unlike</button>
		</c:if>
		<c:if test="${!containsLiked}">
			<button style="background-color: green" id="likeButton"
				onclick="handleLike(${sessionScope.post.id})">Like</button>
		</c:if>

		<c:set var="containsDisliked" value="false" />
		<c:forEach var="personDisliked"
			items="${sessionScope.post.peopleDisliked}">
			<c:if test="${personDisliked eq sessionScope.user.userId}">
				<c:set var="containsDisliked" value="true" />
			</c:if>
		</c:forEach>

		<c:if test="${containsDisliked}">
			<button style="background-color: red" id="dislikeButton"
				onclick="handleDislike(${sessionScope.post.id})">Undislike</button>
		</c:if>
		<c:if test="${!containsDisliked}">
			<button style="background-color: green" id="dislikeButton"
				onclick="handleDislike(${sessionScope.post.id})">Dislike</button>
		</c:if>
	</div>

	<jsp:include page="footer.jsp"></jsp:include>

	<script>
    function handleLike(postId){
        var button = document.getElementById("likeButton");
        var title = button.innerHTML;
        if(title == 'Like'){
            likePost(postId);
        }
        else{
            unlikePost(postId);
        }
    }

    function handleDislike(postId){
        var button = document.getElementById("dislikeButton");
        var title = button.innerHTML;
        if(title == "Dislike"){
            dislikePost(postId);
        }
        else{
            undislikePost(postId);
        }
    }

    function likePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var likeButton = document.getElementById("likeButton");
                likeButton.innerHTML = "Unlike";
                likeButton.style.background='red';
                var dislikeButton=document.getElementById("dislikeButton");
                dislikeButton.innerHTML="Dislike";
                dislikeButton.style.background="green";
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];

            }
            else if(this.readyState == 4 && this.status == 201){
                var likeButton1 = document.getElementById("likeButton");
                likeButton1.innerHTML = "Unlike";
                likeButton1.style.background='red';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];
            }
            else if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you cannot like this video!");
            }
        }
        request.open("post", "/like/"+postId, true);
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
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "/unlike/"+postId, true);
        request.send();
    }

    function dislikePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var dislikeButton = document.getElementById("dislikeButton");
                dislikeButton.innerHTML = "Undislike";
                dislikeButton.style.background='red';
                var likeButton=document.getElementById("likeButton");
                likeButton.innerHTML="Like";
                likeButton.style.background="green";
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];
            }
            else if(this.readyState == 4 && this.status == 201){
                var dislikeButton1 = document.getElementById("dislikeButton");
                dislikeButton1.innerHTML = "Undislike";
                dislikeButton1.style.background='red';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to dislike this video!");
            }
        }
        request.open("post", "/dislike/"+postId, true);
        request.send();
    }

    function undislikePost(postId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("dislikeButton");
                button.innerHTML = "Dislike";
                button.style.background='green';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount").innerHTML=counters[0];
                document.getElementById("dislikesCount").innerHTML=counters[1];
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("post", "/undislike/"+postId, true);
        request.send();
    }
</script>
	<script type="text/javascript">

    var latitudeString = document.getElementById("latitude").value;
    var longtitudeString = document.getElementById("longtitude").value;

    var latitude = parseFloat(latitudeString);
    var longtitude = parseFloat(longtitudeString);


    var locations = [
        ['', latitude, longtitude, 4],
    ];

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12,
        center: new google.maps.LatLng(latitude, longtitude),
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
	<script>
    function postComment(postId){
    	   var textAreaInput = document.getElementById("newCommentInputContent").value;
    	   document.getElementById("newCommentInputContent").value = "";
    	    var request = new XMLHttpRequest();
    	      request.onreadystatechange = function() {
    	          //when response is received 
    	          if (this.readyState == 4 && this.status == 200) {
    	             var comment = JSON.parse(request.responseText); 
    	             var commentContent = comment.content;
    	             var commentDatetime = comment.datetimeString;
    	            var commentId = comment.id;
    	            //now that everything is set:    
    	            var table = document.getElementById("commentsTable");
    	            var row = table.insertRow(0);
    	             var cell = row.insertCell(0);
    	             var newInnerHtml = "<div class='container' width=70%> <img src='/user/picture/${sessionScope.user.userId}' border='3' width='45' height='45' align='middle'style='border-radius: 80px; border-style: solid; border-color: #bbb;'>"+commentDatetime+" <a target='_blank' href='/showPassport/${sessionScope.user.userId}'>${sessionScope.user.username}</a><br>"+ commentContent +"<br><p id='likesCount/"+ commentId + "'>Likes: 0</p><p id='dislikesCount/"+ commentId + "'>Dislikes: 0</p> <button style='background-color: green' id='likeButton/" + commentId +"' onclick='handleCommentLike("+ commentId + ")'>Like</button><button style='background-color: green' id='dislikeButton/" + commentId +"'onclick='handleCommentDislike("+ commentId + ")'>Dislike</button></div>";
    	    	             cell.innerHTML = newInnerHtml;
    	          }
    	          else
    	          if (this.readyState == 4 && this.status == 401) {
    	              alert("Sorry, you must log in to comment on this post.");
    	          }
    	      }
    	      request.open("POST", "/postComment/"+postId + "/" + textAreaInput, true);
    	      request.send();
    	  }
    </script>
	<script>
    function handleCommentLike(commentId){
        var button = document.getElementById("likeButton/"+commentId);
        var title = button.innerHTML;
        if(title == 'Like'){
            likeComment(commentId);
        }
        else{
            unlikeComment(commentId);
        }
    }

    function handleCommentDislike(commentId){
        var button = document.getElementById("dislikeButton/"+commentId);
        var title = button.innerHTML;
        if(title == "Dislike"){
            dislikeComment(commentId);
        }
        else{
            undislikeComment(commentId);
        }
    }

    function likeComment(commentId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var likeButton = document.getElementById("likeButton/"+commentId);
                likeButton.innerHTML = "Unlike";
                likeButton.style.background='red';
                var dislikeButton=document.getElementById("dislikeButton/"+commentId);
                dislikeButton.innerHTML="Dislike";
                dislikeButton.style.background="green";
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];    
            }
            else if(this.readyState == 4 && this.status == 201){
                var likeButton1 = document.getElementById("likeButton/"+commentId);
                likeButton1.innerHTML = "Unlike";
                likeButton1.style.background='red';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];    
            }
            else if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you cannot like this video!");
            }
        }
        request.open("POST", "/likeComment/"+commentId, true);
        request.send();
    }

    function unlikeComment(commentId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("likeButton/"+commentId);
                button.innerHTML = "Like";
                button.style.background='green';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];    
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("POST", "/unlikeComment/"+commentId, true);
        request.send();
    }

    function dislikeComment(commentId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var dislikeButton = document.getElementById("dislikeButton/"+commentId);
                dislikeButton.innerHTML = "Undislike";
                dislikeButton.style.background='red';
                var likeButton=document.getElementById("likeButton/"+commentId);
                likeButton.innerHTML="Like";
                likeButton.style.background="green";
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];    
            }
            else if(this.readyState == 4 && this.status == 201){
                var dislikeButton1 = document.getElementById("dislikeButton/"+commentId);
                dislikeButton1.innerHTML = "Undislike";
                dislikeButton1.style.background='red';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];    
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to dislike this video!");
            }
        }
        request.open("post", "/dislikeComment/"+commentId, true);
        request.send();
    }

    function undislikeComment(commentId) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("dislikeButton/"+commentId);
                button.innerHTML = "Dislike";
                button.style.background='green';
                var counters=JSON.parse(request.responseText);
                document.getElementById("likesCount/"+commentId).innerHTML= "Likes: " + counters[0];
                document.getElementById("dislikesCount/"+commentId).innerHTML= "Dislikes: " + counters[1];            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        request.open("POST", "/undislikeComment/"+commentId, true);
        request.send();
    }
</script>
</body>
</html>