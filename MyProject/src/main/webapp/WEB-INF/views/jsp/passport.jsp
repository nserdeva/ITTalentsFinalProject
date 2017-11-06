<%@ page import="com.example.model.DBManagement.UserDao"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/plain; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>${sessionScope.selectedUser.username}</title>
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
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAHRkWRORCE1pttqPPUWLt0dF6tpO2LUow&callback=map" type="text/javascript"></script>

</head>
<body>

<c:if test="${ sessionScope.user == null }">
	<c:redirect url="/login"></c:redirect>
</c:if>

	<jsp:include page="header.jsp"></jsp:include><br>


	<div id="map" style="float: left; width: 500px; height: 400px;"></div>


	<table align="center">
		<tr>
			<td><img
				src="/user/picture/${sessionScope.selectedUser.userId}" border="3"
				width="200" height="200" align="middle"
				style="border-radius: 80px; border-style: solid; border-color: #bbb;">
				${sessionScope.selectedUser.username}
				<c:if test="${isMyPassport=='false' }">
					<c:if test="${thisFollowsSelected!='false' }">
						<div id="follow/unfollow">
							<button style="background-color: blue" id="followButton"
								onclick="handleFollow(${sessionScope.selectedUser.userId})">Unfollow</button>
						</div>
					</c:if>

					<c:if test="${thisFollowsSelected=='false'}">
						<div id="follow/unfollow">
							<button style="background-color: pink" id="followButton"
								onclick="handleFollow(${sessionScope.selectedUser.userId})">Follow</button>
						</div>
					</c:if>
				</c:if>
				Followers:
				<p id="selectedUserFollowers">
					${sessionScope.selectedUser.followers.size()}</p> <br>
				Following:
				${sessionScope.selectedUser.following.size()} <br>
				${sessionScope.selectedUser.description}</td>
		</tr>
	</table>
	<br>
	<br>
	<br>
	<c:forEach var="post" items="${sessionScope.selectedUser.posts}">
		<div class="container" width=70%>
			<div class="floatedbox">
				<p>
					<img src="/post/getMainPic/${post.id}" border="3" width="120"
						height="120" align="middle"
						style="border-radius: 8px; border-style: solid; border-color: #bbb;">
				</p>
			</div>
			<h3>

				${post.dateTimeString} ${post.user.username}
				<c:if test="${post.location!=null}">
 was at 
 <a target="_blank" href="/location/${post.location.id}">${post.location.locationName}</a>

				</c:if>
			</h3>
			<c:if test="${post.taggedPeople.size()>0}">
with 	
<c:forEach var="taggedUser" items="${post.taggedPeople}">
	<a target="_blank" href="/showPassport/${taggedUser.userId}">
			${taggedUser.username}</a>;
				</c:forEach>
			</c:if>
			<p>${post.description}
				<a target="_blank" href="/post/${post.id}"> explore more...</a>
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
			<div class="subContainer">
				Likes:  ${post.peopleLiked.size()}
				Dislikes: ${post.peopleDisliked.size()}</div>
			</p>

		</div>
		<br>
		<br>
		<br>
		<br>
		<br>
	</c:forEach>




	<script>

//follow/unfollow handling

function handleFollow(userId) {
      var button=document.getElementById("followButton");
      var title=button.innerHTML;
      if(title=='Follow'){
          followUser(userId);
		}
		else{
          unfollowUser(userId);
		}
  }

  function followUser(userId) {
      var request = new XMLHttpRequest();
      request.onreadystatechange = function() {
          //when response is received
          if (this.readyState == 4 && this.status == 200) {
              var button = document.getElementById("followButton");
              button.innerHTML = "Unfollow";
              document.getElementById("selectedUserFollowers").innerHTML = ${sessionScope.selectedUser.followers.size()}+1;
              button.style.background='blue';
          }
          else
          if (this.readyState == 4 && this.status == 401) {
              alert("Sorry, you must log in to like this video!");
          }
      }
      request.open("POST", "/follow/"+userId, true);
      request.send();
  }

  function unfollowUser(userId) {
      var request = new XMLHttpRequest();
      request.onreadystatechange = function() {
          //when response is received
          if (this.readyState == 4 && this.status == 200) {
              var button = document.getElementById("followButton");
              button.innerHTML = "Follow";
              document.getElementById("selectedUserFollowers").innerHTML = ${sessionScope.selectedUser.followers.size()};
              button.style.background='pink';
          }
          else
          if (this.readyState == 4 && this.status == 401) {
              alert("Sorry, you must log in to like this video!");
          }
      }
      request.open("POST", "/unfollow/"+userId, true);
      request.send();
  }



</script>

<script type="text/javascript">
    var locations = [];
    getVisitedLocations();

    function getVisitedLocations() {
        var request= new XMLHttpRequest();
        request.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var visitedLocations=JSON.parse(request.responseText);
                for(i=0; i<visitedLocations.length;i++){
                    var currentLocation=visitedLocations[i];
                    var latitudeString =currentLocation.latitude;
                    var longtitudeString = currentLocation.longtitude;

                    var latitude = parseFloat(latitudeString);
                    var longtitude = parseFloat(longtitudeString);
                    var loc={};
                    loc[0]='';
                    loc[1]=latitude;
                    loc[2]=longtitude;
                    loc[3]=i;
                    locations.push(loc);
                }
            }
        };
        request.open("GET","/getVisitedPlaces/"+${sessionScope.selectedUser.userId},false);
        request.send();
    }

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 2,
        center: new google.maps.LatLng(42.6388078, 23.1838613),
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


	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>