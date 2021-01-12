<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.story.point.list.page.title"/></h1>
    <div id="story-list" class="page-content">
        <p>Total story point in this sprint: <strong id="total-point"><c:out value="${total}"/></strong></p>
        <p>Average story points: <strong id="average-point"><c:out value="${average}"/></strong></p>
    </div>
</body>
</html>