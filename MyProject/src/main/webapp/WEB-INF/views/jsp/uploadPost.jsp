<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Wanderlust - Register</title>
    <link rel="stylesheet" href="../../../static/css/style.css">
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<form name="newPost" class="w3-container">
    <div id="global">
        <form name="images" action="/uploadPost/uploadImg" method="post" enctype="multipart/form-data">
                <p>
                    <label>Images: </label>
                    <input type="file" name="images" accept="image/*" multiple="multiple"/>
                </p>
                <p>
                    <input type="reset" value="Reset">
                    <input type="submit" value="Add Product">
                </p>
        </form><br>
        <c:forEach var="image" items="${sessionScope.images}">
            <img src="/getImage/${image.originalFileName}"/>
        </c:forEach>
    </div>
    <input type="submit" value="Share experience!">
</form>


<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>