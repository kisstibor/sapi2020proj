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
    
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty stories}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${stories}" var="story">
                    <div class="well well-small">
                        <a href="/story/${story.id}">
                        <b>
                        <c:out value="${story.title}"/>
                        </b>
                        <c:if test="${not empty story.progress}">
                        	<br>
                        	<em style="color: green;"><c:out value="${story.progressBarDone}"/></em>
                        	<em style="color: gray;"><c:out value="${story.progressBarNotDone}"/></em>
                        	<c:out value=" ${story.progress}%"/>	
                        </c:if>
                        </a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    
    <a href="/story/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
</body>
</html>