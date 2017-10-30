<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>${sessionScope.location.locationName}</title>

<style>
.image{
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 5px;
    width: 150px;
}

.image:hover {
    opacity: 0.5;
    box-shadow: 0 0 2px 1px rgb(51, 51, 255);
}
</style>

</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 80%;
            border: 2px solid #909090;
    
}

td, .th {
        border: 2px solid #909090;
    text-align: left;
    padding: 5px;
}

tr {
    background-color: #dddddd;
    
}
</style>

<table align="center">
<tr >
    <td > 
    <h3>${sessionScope.location.locationName}</h3><br>
 ${sessionScope.location.description}<br><br>
</td>
<td >
     <img src="/location/getMainPic/${sessionScope.location.id}" height="300" align="middle" border-radius: 8px;>
</td>
  </tr>
  <tr>
</table>
<table align="center">
<tr>
 <td > 
 	<c:forEach var="picture" items="${sessionScope.location.pictures}">				
    <a target="_blank" href="/location/picture/${picture.id}"> <img class="image" src="/location/picture/${picture.id}" alt="Paris" style="width:150px"></a>
	</c:forEach>
   </td>
  <tr>
</table>

	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>