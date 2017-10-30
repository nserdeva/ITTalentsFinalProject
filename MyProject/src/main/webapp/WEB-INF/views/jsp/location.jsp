<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>${sessionScope.location.locationName}</title>
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
    <h3>${sessionScope.location.locationName}</h3><br>
 ${sessionScope.location.description}<br><br>
</td>
<td >
     <img src="/location/getMainPic/${sessionScope.location.id}" height="300" align="middle" border-radius: 8px;>
</td>
  </tr>
  <tr>
</table>

<table align="center">
<tr>
 <td > 
 	<c:forEach var="picture" items="${sessionScope.location.pictures}">				
    <a target="_blank" href="/location/picture/${picture.id}"> <img class="image" src="/location/picture/${picture.id}" alt="Paris" style="width:150px"></a>
	</c:forEach>
   </td>
  <tr>
</table>

<<<<<<< HEAD
=======

    <div id="googleMap" style="width:100%;height:400px;"></div>

    <script>
        function myMap() {
            var mapProp= {
                center:new google.maps.LatLng(21.508742,-0.120850),
                zoom:5,
            };
            var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
        }
    </script>

>>>>>>> 9037f8ed29442fcba5bf43e4f4b9dd11038dd0dc
			<div id="map" style="width:400px;height:400px"></div>
			<input type = "hidden" id = "latitude" id = "latitude" value="${sessionScope.location.latitude}" />
			<input type = "hidden" id = "longtitude" id="longtitude" value="${sessionScope.location.longtitude}" />
			ai stiga we
			${sessionScope.location.latitude}
		    ${sessionScope.location.longtitude}<br>
	<jsp:include page="footer.jsp"></jsp:include>
	
	
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


</body>
</html>