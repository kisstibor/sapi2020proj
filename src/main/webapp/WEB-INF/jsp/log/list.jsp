<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.log.list.page.title"/></h1>
    <div>
        <a href="/log/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.log.link"/></a>
    </div>
    <div id="log-list" class="page-content">
        <c:choose>
            <c:when test="${empty logs}">
                <p><spring:message code="label.log.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ logs}" var="log">
                    <div class="well well-small">
                        <a href="/log/${log.id}"><c:out value="${log.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>