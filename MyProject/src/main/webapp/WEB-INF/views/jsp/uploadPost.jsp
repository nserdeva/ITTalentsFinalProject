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
    <form:input path="description"></form:input>
    <form:input path="locationName"></form:input>
    <form:input path="taggedPeople"></form:input>
    <form:input path="categories"></form:input>
    <input type="submit" value="Share experience!">
</form:form>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>