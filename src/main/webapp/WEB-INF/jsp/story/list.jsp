<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.story.list.page.title"/></h1>
    <div>
        <a href="/story/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
    </div>
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty stories}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ stories}" var="story">
                    <div class="well well-small">
                        <a href="/story/${story.id}"><c:out value="${story.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    
    <hr>
    
    <h1><spring:message code="label.story.list.page.scrumofscrums.title"/></h1>
    
    TODO<br>
    List:
    <!-- 
    <div id="team-list" class="page-content">
        <c:choose>
            <c:when test="${empty teams}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${teams}" var="team">
                    <div class="well well-small">
                        <a href="/story/${team.id}"><c:out value="${team.name}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
     -->
    <div>
        <a href="/story/add-team" id="add-team-button" class="btn btn-primary"><spring:message code="label.add.story.link.team"/></a>
    </div>
    
</body>
</html>