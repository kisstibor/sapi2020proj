<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.task.list.page.title"/></h1>
    <div>
        <a href="/task/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.task.link"/></a>
    </div>
    <div id="task-list" class="page-content">
        <c:choose>
            <c:when test="${empty tasks}">
                <p><spring:message code="label.task.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ tasks}" var="task">
                    <div class="well well-small">
                        <a href="/task/${task.id}"><c:out value="${task.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>