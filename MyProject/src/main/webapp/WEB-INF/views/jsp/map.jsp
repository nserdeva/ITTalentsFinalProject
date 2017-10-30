<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 30.10.2017 г.
  Time: 0:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkHN_gdiuaWXmHeLB8Fpe_pBc840VRgIk&callback=map"
        type="text/javascript"></script>
</head>

<div id="map" style="float: left; width: 500px; height: 400px;"></div>

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
