<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1><spring:message code="label.daily.list.page.title"/></h1>
    <div>
        <a href="/daily/add" id="add-button" class="btn btn-primary" style="background: coral"><spring:message code="label.add.daily.link"/></a>
    </div>
    <div id="daily-list" class="page-content">
        <c:choose>
            <c:when test="${empty dailies}">
                <p><spring:message code="label.daily.list.empty"/>dfgfdgfd</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ dailies}" var="daily">
                    <div class="well well-small">
                        <a href="/daily/${daily.id}"><c:out value="${daily.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>