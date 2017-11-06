<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Wanderlust - Register</title>
    <link rel="stylesheet" href="<c:url value="/css/awesomplete.css"/>" type="text/css"/>
    <script src="<c:url value="/js/awesomplete.js" />" type ="text/javascript" ></script>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">

    <style type="text/css">
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 100%;
        }
        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        #description {
            font-family: Roboto;
            font-size: 15px;
            font-weight: 300;
        }

        #infowindow-content .title {
            font-weight: bold;
        }

        #infowindow-content {
            display: none;
        }

        #map #infowindow-content {
            display: inline;
        }

        .pac-card {
            margin: 10px 10px 0 0;
            border-radius: 2px 0 0 2px;
            box-sizing: border-box;
            -moz-box-sizing: border-box;
            outline: none;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
            background-color: #fff;
            font-family: Roboto;
        }

        #pac-container {
            padding-bottom: 12px;
            margin-right: 12px;
        }

        .pac-controls {
            display: inline-block;
            padding: 5px 11px;
        }

        .pac-controls label {
            font-family: Roboto;
            font-size: 13px;
            font-weight: 300;
        }

        #pac-input {
            background-color: #fff;
            font-family: Roboto;
            font-size: 15px;
            font-weight: 300;
            margin-left: 12px;
            padding: 0 11px 0 13px;
            text-overflow: ellipsis;
            width: 400px;
        }

        #pac-input:focus {
            border-color: #4d90fe;
        }

        #title {
            color: #fff;
            background-color: #4d90fe;
            font-size: 25px;
            font-weight: 500;
            padding: 6px 12px;
        }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkHN_gdiuaWXmHeLB8Fpe_pBc840VRgIk&libraries=places&callback=init"
            async defer></script>
</head>
<body>
<c:if test="${ sessionScope.user == null }">
    <c:redirect url="/login"></c:redirect>
</c:if>

<jsp:include page="header.jsp"></jsp:include>

<div style="margin:auto; width: 50%">
<form name="newPost" class="w3-container" action="/uploadPost" method="post" enctype="multipart/form-data">
    Description: <br>
    <textarea  name="description" rows="4" cols="50"></textarea> <br>
    Location:
    <input name="locationName" id="locationInput"/>
    <input name="latitude" type="hidden" value="" id="latitude">
    <input name="longtitude" type="hidden" value="" id="longtitude">

    <br><br>
    Tag people:
    <input name="taggedPeople" data-list="${applicationScope.usernames}" data-multiple-taggedPeople />
    <h6>Tags should be separated by ","</h6>
    <br><br>
    Add hashtags:
    <input name="tags" data-list="${applicationScope.tags}" data-multiple-tags />
    <h6>Tags should be separated by ","</h6>
    <br> <br>
    Add categories:
    <input name="categories" data-list="${applicationScope.categories.keySet()}" data-multiple-categories />
    <h6>Categories should be separated by ","</h6>
    <br>

    Upload image: <input type="file" name="image1" accept="image/*"/><br>
    Upload image: <input type="file" name="image2" accept="image/*"/><br>
    Upload image: <input type="file" name="image3" accept="image/*"/><br>
    Upload video: <input type="file" name="video" accept="video/*"><br>
    <input type="submit" value="Share experience!">

</form>
</div>

<script type="text/javascript">
    new Awesomplete('input[data-multiple-taggedPeople]', {
        filter: function(text, input) {
            return Awesomplete.FILTER_CONTAINS(text, input.match(/[^,]*$/)[0]);
        },

        item: function(text, input) {
            return Awesomplete.ITEM(text, input.match(/[^,]*$/)[0]);
        },

        replace: function(text) {
            var before = this.input.value.match(/^.+,\s*|/)[0];
            this.input.value = before + text + ", ";
        }
    });

    new Awesomplete('input[data-multiple-tags]', {
        filter: function(text, input) {
            return Awesomplete.FILTER_CONTAINS(text, input.match(/[^,]*$/)[0]);
        },

        item: function(text, input) {
            return Awesomplete.ITEM(text, input.match(/[^,]*$/)[0]);
        },

        replace: function(text) {
            var before = this.input.value.match(/^.+,\s*|/)[0];
            this.input.value = before + text + ", ";
        }
    });

    new Awesomplete('input[data-multiple-categories]', {
        filter: function(text, input) {
            return Awesomplete.FILTER_CONTAINS(text, input.match(/[^,]*$/)[0]);
        },

        item: function(text, input) {
            return Awesomplete.ITEM(text, input.match(/[^,]*$/)[0]);
        },

        replace: function(text) {
            var before = this.input.value.match(/^.+,\s*|/)[0];
            this.input.value = before + text + ", ";
        }
    });

</script>


<script>
    function init() {
        var input = document.getElementById('locationInput');
        var autocomplete = new google.maps.places.Autocomplete(input);

        autocomplete.addListener('place_changed', function() {
            var place = autocomplete.getPlace();
            var inputLat=autocomplete.getPlace().geometry.location.lat();
            var inputLng=autocomplete.getPlace().geometry.location.lng();
            document.getElementById("latitude").value=inputLat;
            document.getElementById("longtitude").value=inputLng;
        })
    }


</script>


<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>