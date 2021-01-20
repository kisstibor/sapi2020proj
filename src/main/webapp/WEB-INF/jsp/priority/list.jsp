<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.priority.list.page.title"/></h1>
    <div>
        <a href="/scrumsapientia/priority/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.priority.link"/></a>
    </div>
    <div id="priority-list" class="page-content">
        <c:choose>
            <c:when test="${empty priorities}">
                <p><spring:message code="label.priority.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ priorities}" var="story">
                    <div class="well well-small">
                        <a href="/scrumsapientia/priority/${priority.id}"><c:out value="${priority.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>