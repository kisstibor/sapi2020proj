<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<meta charset="UTF-8">
    <title>Bugs</title>
</head>
<body>
    <h1>Bugs page</h1>
    <div>
        <a href="/bug/add" id="add-button" class="btn btn-primary">Add</a>
    </div>
    <div id="bug-list" class="page-content">
        <c:choose>
            <c:when test="${empty bugs}">
                <p>No bugs added</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${bugs}" var="bug">
                    <div class="well well-small">
                        <a href="/bug/${bug.id}"><c:out value="${bug.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>