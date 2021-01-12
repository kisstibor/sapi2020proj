<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.project.list.page.title"/></title>
</head>
<body>
    <h1>Project list</h1>
    <div>
        <a href="/project/add" id="add-button" class="btn btn-primary">Add a new project</a>
    </div>
    <div id="project-list" class="page-content">
        <c:choose>
            <c:when test="${empty projects}">
                <p>No projects added yet</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ projects}" var="project">
                    <div class="well well-small">
                        <a href="/project/${project.id}"><c:out value="${project.title}"/></a>
                        <form:form action="/project/${project.id}/delete" commandName="story" method="POST" enctype="utf8">
                		<button id="add-epic-button" type="submit" class="btn btn-primary"><spring:message
                        code="Delete"/></button>
                        </form:form>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>