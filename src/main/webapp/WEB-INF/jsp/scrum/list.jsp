<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.scrum.list.page.title"/></h1>
    
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty teams}">
                <p><spring:message code="label.scrumteam.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${teams}" var="team">
                    <div class="well well-small">
                        <a href="/scrum/${team.id}">
                        	<c:out value="${team.title}"/>
                        </a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    
    <a href="/scrum/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.scrum.link"/></a>
    
</body>
</html>