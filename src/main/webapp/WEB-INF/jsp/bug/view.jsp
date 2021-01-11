<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>Bug</title>
</head>
<body>
	<div id="bug-id" class="hidden">${bug.id}</div>
    <h1>Bug <c:out value="${bug.id}"/></h1>
    <div class="well page-content">
        <h2 id="bug-title"><c:out value="${bug.title}"/></h2>
        <div>
            <p><c:out value="${bug.description}"/></p>
        </div>
        <div class="action-buttons">
         	<a href="/bug/update/${bug.id}" class="btn btn-primary">Update</a>
            <a href="/bug/delete/${bug.id}" class="btn btn-primary">Delete</a>
        </div>
    </div>
</body>
</html>