<%@ page import="com.example.model.DBManagement.UserDao"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-blue-grey.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
html,body,h1,h2,h3,h4,h5 {font-family: "Open Sans", sans-serif}
</style>
<body class="w3-theme-l5">



<meta http-equiv="Content-Type" content="text/plain; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<style>
body, h1, h2, h3, h4, h5 {
	font-family: "Raleway", sans-serif
}
</style>
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
<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAHRkWRORCE1pttqPPUWLt0dF6tpO2LUow&callback=map"
	type="text/javascript"></script>

</head>
<body class="w3-light-grey">

	<c:if test="${ sessionScope.user == null }">
		<c:redirect url="/login"></c:redirect>
	</c:if>

	<jsp:include page="header.jsp"></jsp:include><br>




<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

  <!-- The Grid -->
  <div class="w3-row-padding">
  
    <!-- Left Column -->
    <div class="w3-third">
    
      <div class="w3-white w3-text-grey w3-card-4">
        <div class="w3-display-container">
          <img src="/user/picture/${sessionScope.selectedUser.userId}" style="width:100%" alt="Avatar">
          <div class="w3-display-bottomleft w3-container w3-text-black">
          </div>
        </div>
        <div class="w3-container">
          <p><i class="fa fa-briefcase fa-fw w3-margin-right w3-large w3-text-teal"></i>${sessionScope.selectedUser.username}</p>
			<p id="selectedUserFollowers">Followers: ${sessionScope.selectedUser.followers.size()}</p>
			<p id="selectedUserFollowing">Following: ${sessionScope.selectedUser.following.size()}</p> 
			<c:if test="${isMyPassport=='false' }">
					<c:if test="${thisFollowsSelected!='false' }">
						<div id="follow/unfollow">
							<br><button style="background-color: blue" id="followButton"
								onclick="handleFollow(${sessionScope.selectedUser.userId})">Unfollow</button>
						</div>
					</c:if>

					<c:if test="${thisFollowsSelected=='false'}">
						<div id="follow/unfollow">
							<br><button style="background-color: pink" id="followButton"
								onclick="handleFollow(${sessionScope.selectedUser.userId})">Follow</button>
						</div>
					</c:if>
				</c:if> 
			<hr>     
        </div>
      </div>

    <!-- End Left Column -->
    </div>

    <!-- Right Column -->
    <div class="w3-twothird">
    
      <div class="w3-container w3-card w3-white w3-margin-bottom">
        <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Passport</h2>
        <div class="w3-container">
          <h5 class="w3-opacity"><b>About: </b></h5>
          <p>${sessionScope.selectedUser.description}</p>
          <hr>
        </div>
          <hr>
        </div>    
      </div>
    <!-- End Right Column -->
    </div>
    
  <!-- End Grid -->
  </div>
  
  <!-- End Page Container -->
	
				
				
	<br>
	<table align="center">
		<tr>
			<td>
				<div id="map" align="center" style="width: 1070px; height: 400px;"></div>
			</td>
		</tr>
	</table>
	<br>
	<br>

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
              button.style.background='blue';
              var counters=JSON.parse(request.responseText);
              document.getElementById("selectedUserFollowers").innerHTML="Followers: " + counters[0];
              document.getElementById("selectedUserFollowing").innerHTML="Following: " + counters[1];
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
              button.style.background='pink';
              var counters=JSON.parse(request.responseText);
              document.getElementById("selectedUserFollowers").innerHTML="Followers: " + counters[0];
              document.getElementById("selectedUserFollowing").innerHTML="Following: " + counters[1];
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





	<!-- w3-content defines a container for fixed size centered content, 
and is wrapped around the whole page content, except for the footer in this example -->
	<div class="w3-content" style="max-width: 1400px">

		<!-- Grid -->
		<div class="w3-row">

			<!-- Blog entries -->
			<div class="w3-col l8 s12" style="float: none; margin: 0 auto;">


				<c:forEach var="post" items="${sessionScope.selectedUser.posts}">
					<!-- Blog entry -->
					<div class="w3-card-4 w3-margin w3-white">
						<div style="text-align: center;">
							<img src="/post/getMainPic/${post.id}" style="max-width:100%; margin: 0 auto;">
						</div>

						<div class="w3-container">
							<h3>
								<b> ${post.user.username} <c:if
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