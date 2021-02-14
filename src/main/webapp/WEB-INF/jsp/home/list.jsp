<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.scrum.page.title"/></h1>
    
    
    <div id="story-list" class="page-content">
    <h2><spring:message code="label.story.list.page.title"/></h2>
        <c:choose>
            <c:when test="${empty stories}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ stories}" var="story">
                    <div class="well well-small">
                        <a href="/scrumsapientia/story/${story.id}"><c:out value="${story.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    
    <div id="task-list" class="page-content">
    <h2><spring:message code="label.task.list.page.title"/></h2>
        <c:choose>
            <c:when test="${empty tasks}">
                <p><spring:message code="label.task.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ tasks}" var="task">
                    <div class="well well-small">
                        <a href="/scrumsapientia/task/${task.id}"><c:out value="${task.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>