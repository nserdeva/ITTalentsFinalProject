<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Wanderlust - Register</title>
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/awesomplete.css"/>" type="text/css"/>
    <script src="<c:url value="/js/awesomplete.js" />" type ="text/javascript" ></script>

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>


<form name="newPost" class="w3-container" action="/uploadPost" method="post" enctype="multipart/form-data">
    Description: <textarea  name="description" rows="4" cols="50"></textarea> <br>
    Location:
    <input name="location" data-list="${applicationScope.locations}"/>
    <br><br>
    Tag people:
    <input name="taggedPeople" data-list="${applicationScope.usernames}" data-multiple-taggedPeople />
    <br><br>
    Add hashtags:
    <input name="tags" data-list="${applicationScope.tags}" data-multiple-tags />
    <br> <br>
    Add categories:
    <input name="categories" data-list="${applicationScope.categories.keySet()}" data-multiple-categories />
    <br>

    Upload image: <input type="file" name="image1" accept="image/*"/><br>
    Upload image: <input type="file" name="image2" accept="image/*"/><br>
    Upload image: <input type="file" name="image3" accept="image/*"/><br>
    Upload video: <input type="file" name="video" accept="video/*"><br>
    <input type="submit" value="Share experience!">

</form>

<script type="text/javascript">

    function addCategory() {
        var chosenCatInput = document.getElementById("chosenCat");
        var chosenCatString = chosenCatInput.value;
        var categoriesDropdown = document.getElementById("categoriesDropdown");
        var selectedValue = categoriesDropdown.options[categoriesDropdown.selectedIndex].value;
        if (chosenCatString.indexOf(selectedValue) === -1) {
            var chosenCategories = chosenCatString.concat(" " + selectedValue);
            chosenCatInput.setAttribute("value", chosenCategories);
            alert(chosenCategories);
        } else {
            alert("You already added that category!");
        }
    };


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

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>