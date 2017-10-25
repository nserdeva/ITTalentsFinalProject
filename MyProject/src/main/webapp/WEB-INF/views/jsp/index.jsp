<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 23.10.2017 г.
  Time: 14:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Wanderlust</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>

<c:if test="${ sessionScope.user == null }">
    <c:redirect url="login.jsp"></c:redirect>
</c:if>
<jsp:include page="header.jsp"></jsp:include>

<div class="w3-content w3-display-container">
    <img class="mySlides" src="https://www.w3schools.com/w3css/img_fjords.jpg" style="width:100%">
    <img class="mySlides" src="https://www.w3schools.com/w3css/img_lights.jpg" style="width:100%">
    <img class="mySlides" src="https://www.w3schools.com/w3css/img_mountains.jpg" style="width:100%">
    <img class="mySlides" src="https://www.w3schools.com/w3css/img_forest.jpg" style="width:100%">

    <button class="w3-button w3-black w3-display-left" onclick="plusDivs(-1)">&#10094;</button>
    <button class="w3-button w3-black w3-display-right" onclick="plusDivs(1)">&#10095;</button>
</div>

<script>
    var slideIndex = 1;
    showDivs(slideIndex);

    function plusDivs(n) {
        showDivs(slideIndex += n);
    }

    function showDivs(n) {
        var i;
        var x = document.getElementsByClassName("mySlides");
        if (n > x.length) {slideIndex = 1}
        if (n < 1) {slideIndex = x.length}
        for (i = 0; i < x.length; i++) {
            x[i].style.display = "none";
        }
        x[slideIndex-1].style.display = "block";
    }
</script>

<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
