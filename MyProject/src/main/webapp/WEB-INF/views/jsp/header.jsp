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
        <a href="/explore" class="w3-bar-item w3-button w3-hover-teal w3-right">Explore</a>
        <a href="/uploadPost" class="w3-bar-item w3-button w3-hover-teal w3-right">Share experience</a>
        <a href="/newsfeed" class="w3-bar-item w3-button w3-hover-teal w3-right">Get inspiration</a>
        <a href="/myPassport" class="w3-bar-item w3-button w3-hover-teal w3-right">My Passport</a>
        <a href="/wanderlust" class="w3-bar-item w3-button w3-hover-teal w3-right">World of Wanderlust</a>
    </div>
</div>
</body>
</html>