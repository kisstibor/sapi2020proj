<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.vacation"/></h1>
    <div>
        <a href="/scrumsapientia/story/vacation/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.vacation.button"/></a>
    </div>
    <div id="vacation-list" class="page-content">
        <c:choose>
            <c:when test="${empty vacations}">
                <p><spring:message code="label.vacation.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ vacations}" var="story">
                    <div class="well well-small">
                        <a href="/scrumsapientia/vacation/${vacation.id}"><c:out value="${vacation.vacationStartDate}"/><c:out value="${vacation.vacationEndDate}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>