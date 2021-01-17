<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.user.list.page.title"/></h1>
    <div>
        <a href="/user/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.user.link"/></a>
    </div>
    <div id="user-list" class="page-content">
        <c:choose>
            <c:when test="${empty users}">
                <p><spring:message code="label.user.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ users}" var="user">
                    <div class="well well-small">
                        <a href="/user/${user.id}"><c:out value="${user.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>