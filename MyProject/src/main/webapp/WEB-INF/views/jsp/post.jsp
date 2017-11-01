<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>View Adventure</title>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkHN_gdiuaWXmHeLB8Fpe_pBc840VRgIk&callback=map"
			type="text/javascript"></script>

<style>
.image{
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 5px;
    width: 150px;
}

.image:hover {
    opacity: 0.5;
    box-shadow: 0 0 2px 1px rgb(51, 51, 255);
}
</style>

</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

<style>
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
<table align="center">
<tr >
<td >
	<img src="/user/picture/${post.user.userId}" border="3" width="45"
					height="45" align="middle"
					style="border-radius: 80px; border-style: solid; border-color: #bbb;">
				${post.dateTime}
				<a target="_blank" href="/showPassport/${post.user.userId}">	
				${post.user.username}</a>;
				
				<c:if test="${post.location!=null}">
 was at 
 <a target="_blank" href="/location/${post.location.id}">${post.location.locationName}</a>

				</c:if>
			<c:if test="${post.taggedPeople.size()>0}">
with 	
<c:forEach var="taggedUser" items="${post.taggedPeople}">			
	<a target="_blank" href="/showPassport/${taggedUser.userId}">	
				${taggedUser.username}</a>; 
	</c:forEach>
			</c:if>
			<p>${post.description}			
			<div class="subContainer">
				Categories:
				<c:forEach var="category" items="${post.categories}">
${category.name};
</c:forEach>
			</div>
			<div class="subContainer">
				Tags:
				<c:forEach var="tag" items="${post.tags}">
${tag.tag_name};
</c:forEach>
			</div>
			<div class="subContainer">Likes: ${post.likesCount} Dislikes:
				${post.dislikesCount}</div>
			</p>

		</div></td>
  </tr>
  <tr>
</table>

<table align="center">
<tr>
 <td > 
 	<c:forEach var="multimediaFile" items="${sessionScope.post.multimedia}">				
    <a target="_blank" href="/post/multimedia/${multimediaFile.id}"> <img class="image" src="/post/multimedia/${multimediaFile.id}" alt="Paris" style="width:150px"></a>
	</c:forEach>
	
				    <a target="_blank" href="/getVideo/${sessionScope.post.video.url}"> <video controls="controls" class="image"> 
				    <source src="<c:url value="/getVideo/${sessionScope.post.video.url}"/> " type="video/mp4" style="width:150px">>
				     </a>
				
   </td>
  <tr>
</table>

<video width="320" height="240" controls="controls">
					<source src="<c:url value="/getVideo/${sessionScope.post.video.url}"/> " type="video/mp4">
					Your browser does not support the video tag.
				</video>
    <div id="googleMap" style="width:100%;height:400px;"></div>


			<div id="map" style="width:400px;height:400px"></div>
			<input type = "hidden" id = "latitude" id = "latitude" value="${sessionScope.location.latitude}" />
			<input type = "hidden" id = "longtitude" id="longtitude" value="${sessionScope.location.longtitude}" />
			ai stiga we
			${sessionScope.location.latitude}
		    ${sessionScope.location.longtitude}<br>
	
	
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


<div id="like/dislike" >
			<button style="background-color: green" id="likeButton"  value="like" onclick="handleLike(${sessionScope.post.id})" >Like</button>
			<button  style="background-color: red" id="dislikeButton " onclick="handleDislike(${sessionScope.post.id})" >Dislike</button>
		</div>


<script>
    function handleLike(postId){
    	alert(document.getElementById("likeButton").value);
        if( document.getElementById("likeButton").value == "like"){
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
        alert(postId);
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            alert('i am in function');

            //when response is received
            if (this.readyState == 4 && this.status == 200) {
                var button = document.getElementById("likeButton");
                button.innerHTML = "Unlike";
                button.style.background='red';
                alert('iii made it to the right if');
            }
            else
            if (this.readyState == 4 && this.status == 401) {
                alert("Sorry, you must log in to like this video!");
            }
        }
        alert('iii am sending the request');
        request.open("POST", "/like/"+postId, true);
        request.send();
        alert('iii made it');

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

	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>