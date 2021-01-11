<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.label.list.page.title"/></title>
</head>
<body>
    <h1><spring:message code="label.label.list.page.title"/></h1>
    <div>
        <a href="/label/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.label.link"/></a>
    </div>
    <div id="label-list" class="page-content">
        <c:choose>
            <c:when test="${empty labels}">
                <p><spring:message code="label.label.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${labels}" var="label">
                    <div class="well well-small">
                    	<c:out value="${label.title}"/>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>