<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Wanderlust - Register</title>
    <link rel="stylesheet" href="../../../static/css/style.css">
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<form:form commandName="newPost">
    <form:input path="description" placeholder="description"></form:input>
    <form:input path="locationName" placeholder="location name"></form:input>
    <form:input path="taggedPeople" placeholder="tagged people"></form:input>
    <form:input path="categories" placeholder="categories"></form:input>
    <div id="global">
        <form:form commandName="img" action="uploadImg" method="post" enctype="multipart/form-data">
            <fieldset>
               Add images
                <p>
                    <label>Images: </label>
                    <input type="file" name="images" multiple="multiple"/>
                </p>
                <p id="buttons">
                    <input id="reset" type="reset" tabindex="4">
                    <input id="submit" type="submit" tabindex="5" value="Add image">
                </p>
            </fieldset>
        </form:form>
    </div>
    <input type="submit" value="Share experience!">
</form:form>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>