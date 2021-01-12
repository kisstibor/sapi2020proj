<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Epic list</title>
</head>
<body>
    <h1><spring:message code="label.epic.list.page.title"/></h1>
    <div>
        <a href="/epic/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.epic.link"/></a>
    </div>
    <div id="epic-list" class="page-content">
        <c:choose>
            <c:when test="${empty epics}">
                <p><spring:message code="label.epic.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ epics}" var="epic">
                    <div class="well well-small">
                        <a href="/epic/${epic.id}"><c:out value="${epic.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>