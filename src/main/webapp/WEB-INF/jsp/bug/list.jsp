<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.bug.list.page.title"/></h1>
    <div>
        <a href="/scrumsapientia/bug/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.bug.link"/></a>
    </div>
    <div id="bug-list" class="page-content">
        <c:choose>
            <c:when test="${empty bugs}">
                <p><spring:message code="label.bug.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ bugs}" var="bug">
                    <div class="well well-small">
                        <a href="/scrumsapientia/bug/${bug.id}"><c:out value="${bug.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>