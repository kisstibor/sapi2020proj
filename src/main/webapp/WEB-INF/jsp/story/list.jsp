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
                	<c:choose>
			            <c:when test="${story.getDueDateAsDateTime().isBeforeNow()}">
			                <div style="background-color: #ff000075; display:flow-root;" class="well well-small">
			            </c:when>
			            <c:otherwise>
			                <div style="background-color: aquamarine; display:flow-root;" class="well well-small">
			            </c:otherwise>
			        </c:choose>
                    	<div style="float: left;">
                        <a  href="/story/${story.id}"><h4><c:out value="${story.title}"/></h4></a>
                        <p><c:out value="${story.status.getDisplayName()}"/></p>
                        </div><div style="float: right;">
                        <p>Comments: <c:out value="${story.comments.size()}"/></p>
                        <p><c:out value="${story.dueDate}"/></p>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>