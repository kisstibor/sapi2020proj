<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/scrumsapientia/static/js/story.view.js"></script>
</head>
<body>
    <div id="vacation-id" class="hidden">${vacation.id}</div>
    <h1><spring:message code="label.vacation"/></h1>
    <div class="well page-content">
        <div>
            <p><c:out value="${vacation.vacationStartDate}"/></p>
        </div>
        <div>
            <p><c:out value="${vacation.vacationEndDate}"/></p>
        </div>
    </div>
    <div class="action-buttons">
            <a href="/scrumsapientia/story/vacation/update/${id}" class="btn btn-primary"><spring:message code="label.update.vacation.link"/></a>
             <a href="/scrumsapientia/story/vacation/delete/${id}" id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        
    </div>
</body>
</html>