<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 26.10.2017 г.
  Time: 17:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div class="w3-container">
    <button onclick="myFunction()" class="w3-btn w3-black">Toggle hide and show</button>

    <p>Paragraph 1.</p>
    <p id="Demo" class="w3-hide w3-red">
        



    </p>

    <button onclick="myFunction2()" class="w3-btn w3-black">Toggle hide and show</button>
    <p id="Demo2" class="w3-hide w3-red">Paragraph 4.</p>

</div>

<script>
    function myFunction() {
        var x = document.getElementById("Demo");
        if (x.className.indexOf("w3-show") == -1) {
            x.className += " w3-show";
        } else {
            x.className = x.className.replace(" w3-show", "");
        }
    }
    function myFunction2() {
        var x = document.getElementById("Demo2");
        if (x.className.indexOf("w3-show") == -1) {
            x.className += " w3-show";
        } else {
            x.className = x.className.replace(" w3-show", "");
        }
    }
</script>


<jsp:include page="footer.jsp"></jsp:include>


</body>
</html>
