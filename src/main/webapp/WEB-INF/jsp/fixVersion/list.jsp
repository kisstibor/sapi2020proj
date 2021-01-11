<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.fixVersion.list.page.title"/></h1>
    <div>
        <a href="/fixVersion/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.fixVersion.link"/></a>
    </div>
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty fixVersions}">
                <p><spring:message code="label.fixVersion.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ fixVersions}" var="fixVersion">
                    <div class="well well-small">
                        <a href="/fixVersion/${fixVersion.id}"><c:out value="${fixVersion.name}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>