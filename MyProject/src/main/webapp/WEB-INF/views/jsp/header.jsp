<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>Wanderlust</title>
</head>
<body>
<div class="w3-container w3-right-align">
    <div class="w3-bar w3-border w3-light-grey">
        <div class="w3-dropdown-hover">
            <button class="w3-button">${sessionScope.user.username}</button>
            <div class="w3-dropdown-content w3-bar-block w3-card-4">
                <a href="/logout" class="w3-bar-item w3-button">Logout</a>
                <a href="/settings" class="w3-bar-item w3-button">Settings</a>
            </div>
        </div>
        <a href="/uploadPost" class="w3-bar-item w3-button w3-hover-teal w3-right">Share experience</a>
        <a href="/newsfeed" class="w3-bar-item w3-button w3-hover-teal w3-right">Get inspiration</a>
        <a href="/myPassport" class="w3-bar-item w3-button w3-hover-teal w3-right">My Passport</a>
        <a href="/wanderlust" class="w3-bar-item w3-button w3-hover-teal w3-right">World of Wanderlust</a>
    </div>
</div>
	
	
	<style>
input[type=text] {
	width: 130px;
	height: 30px;
	box-sizing: border-box;
	border: 2px solid #ccc;
	border-radius: 4px;
	font-size: 16px;
	background-color: white;
	background-image: url('../img/search_icon.png');
	background-position: 10px 2px;
	background-repeat: no-repeat;
	padding: 12px 20px 12px 40px;
	-webkit-transition: width 0.4s ease-in-out;
	transition: width 0.4s ease-in-out;
}

input[type=text]:focus {
	width: 39%;
}
</style>

	<form id="searchForm" method="post" action="/search">
	<div id="show_categories" >Categories:
<input type="checkbox" name="natureCheckBox" class="chb" value="false">Nature
		<input type="checkbox" onclick="changeCheckBoxesValues()"  name="seaCheckBox" class="chb" value="false">Sea
		<input type="checkbox" onclick="changeCheckBoxesValues()" name="mountainsCheckBox" class="chb"
			value="false">Mountains <input type="checkbox"
			name="dessertCheckBox" onclick="changeCheckBoxesValues()" class="chb" value="false">Desert <input
			type="checkbox" onclick="changeCheckBoxesValues()" name="landmarkCheckBox" class="chb" value="false">Landmark
		<input type="checkbox" onclick="changeCheckBoxesValues()" name="resortCheckBox" class="chb" value="false">Resort
		<input type="checkbox" onclick="changeCheckBoxesValues()" name="cityCheckBox" class="chb" value="false">City
</div>
		<input type="text" id="searchFormDataTxt" name="searchFormDataTxt" placeholder="Search..">
			<button id = "exploreButton" style = "visibility: hidden;" onclick="changeCheckBoxesValues()">explore</button>		
	</form>

<script>
    // prevent the enter key from submitting the form if input_id has focus to prevent accidental submission
    document.getElementById('searchFormDataTxt').addEventListener('keypress', function(event) {
        if (event.keyCode == 13) {
            event.preventDefault();
        }
    });
</script>
	
 <div class="dropdown">
  <button class="dropbtn">Browse</button>
  <div class="plainText" id="browse_adventures">Search in: Adventures</div> 
    <div class="plainText" id="browse_destinations">Search in: Destinations</div> 
      <div class="plainText" id="browse_adventurers">Search in: Adventurers</div>   
  <div class="dropdown-content">
  <button text-color="black" class="browseOptions" onclick="searchInAdventures()">Adventures</button>
    <button class="browseOptions" onclick="searchInDestinations()">Destinations</button>
    <button class="browseOptions" onclick="searchInAdventurers()">Adventurers</button>
 
  </div>
</div>



<script>
		function searchInAdventures() {
			  document.getElementById("exploreButton").style.visibility = 'visible';
			  document.getElementById('show_categories').style.display= 'block' ;
			  document.getElementById('browse_adventures').style.display= 'block' ;
			  document.getElementById('browse_destinations').style.display= 'none' ;
			  document.getElementById('browse_adventurers').style.display= 'none' ;
			  document.getElementById('searchForm').action = '/searchAdventures';
		}
		
		function searchInDestinations() {
			  document.getElementById("exploreButton").style.visibility = 'visible';
			  document.getElementById('show_categories').style.display= 'none' ;
			  document.getElementById('browse_destinations').style.display= 'block' ;
			  document.getElementById('browse_adventures').style.display= 'none' ;
			  document.getElementById('browse_adventurers').style.display= 'none' ;
			  document.getElementById('searchForm').action = '/searchDestinations';

		}
		
		function searchInAdventurers() {
			  document.getElementById("exploreButton").style.visibility = 'visible';
			  document.getElementById('show_categories').style.display= 'none' ;
			  document.getElementById('browse_adventures').style.display= 'none' ;
			  document.getElementById('browse_destinations').style.display= 'none' ;
			  document.getElementById('browse_adventurers').style.display= 'block' ;
			  document.getElementById('searchForm').action = '/searchAdventurers';

		}
	</script>

<style>
.plainText{
}
.browseOptions{
border-radius: 0px;
border: none;
text-color: black;


}

#show_categories{
 display: none
}

#browse_adventures{
 display: none
}

#browse_destinations{
 display: none
}

#browse_adventurers{
 display: none
}

.dropbtn {
	border-radius: 8px;
    background-color: #4CAF50;
    font-size: 16px;
    border: none;
    cursor: pointer;
    width: 130px;
    height: 34px;
}

.dropdown {
    position: relative;
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
}

.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
}

.dropdown-content a:hover {background-color: #9fdfbf}

.dropdown:hover .dropdown-content {
    display: block;
}

.dropdown:hover .dropbtn {
    background-color: #3e8e41;
}
</style>





<style>
button
{
border: none;
outline: none;
height: 34px;
width: 223px;
color: #fff;
font-size: 16px;
background: #00cc66;	
cursor: pointer;
border-radius: 10px;
}
button:hover
{
	background: #66ff99;
	color: #262626;
}

</style>



<script>
		function changeCheckBoxesValues() {
			var chb = document.getElementsByClassName('chb');

			if (chb[0].checked) {
				chb[0].value = "true";
			}
			if (chb[1].checked) {
				chb[1].value = "true";
			}

			if (chb[2].checked) {
				chb[2].value = "true";
			}

			if (chb[3].checked) {
				chb[3].value = "true";
			}

			if (chb[4].checked) {
				chb[4].value = "true";
			}

			if (chb[5].checked) {
				chb[5].value = "true";
			}

			if (chb[6].checked) {
				chb[6].value = "true";
			}
		}
	</script>

</body>
</html>