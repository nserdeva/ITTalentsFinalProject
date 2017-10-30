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

<form  class="w3-container">
    <div id="global">
        <form name="images" action="/uploadPost/uploadImg" method="post" enctype="multipart/form-data">

                <p>
                    <input type="reset" value="Reset">
                    <input type="submit" value="Add Product">
                </p>
        </form><br>
        <c:forEach var="image" items="${sessionScope.images}">
            <img src="/getImage/${image.originalFileName}"/>
        </c:forEach>
    </div>
</form>


<form name="newPost" class="w3-container" action="/uploadPost" method="post" enctype="multipart/form-data">
    Description: <textarea  name="description" rows="4" cols="50"></textarea> <br>
    Location: <input type="text" name="location"><br>
    Tag people: <input type="text" name="taggedPeople"><br>
    Add tags: <input type="text" name="tags"><br>
    Add categories:
    <div class="w3-dropdown-hover">
        <button class="w3-button w3-black">Categories</button>
        <select id="categoriesDropdown" class="w3-dropdown-content w3-bar-block w3-border" onchange="addCategory()">
            <c:forEach var="category" items="${applicationScope.categories}">
                <option value="${category.key}"> ${category.key} </option>
            </c:forEach>
        </select>
    </div>
    <input type="text" name="categories" id="chosenCat" value="" style="width: 800px" readonly>
    <br>
    Upload image: <input type="file" name="image1" accept="image/*"/><br>
    Upload image: <input type="file" name="image2" accept="image/*"/><br>
    Upload image: <input type="file" name="image3" accept="image/*"/><br>
    Upload video: <input type="file" name="video" accept="video/*"><br>
    <input type="submit" value="Share experience!">

</form>

<script type="text/javascript">
    function addCategory() {
        var chosenCatInput=document.getElementById("chosenCat");
        var chosenCatString=chosenCatInput.value;
        var categoriesDropdown = document.getElementById("categoriesDropdown");
        var selectedValue = categoriesDropdown.options[categoriesDropdown.selectedIndex].value;
        if(chosenCatString.indexOf(selectedValue) === -1){
            var chosenCategories=chosenCatString.concat(" "+selectedValue);
            chosenCatInput.setAttribute("value",chosenCategories);
            alert(chosenCategories);
        }else{
            alert("You already added that category!");
        }
    }
</script>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>