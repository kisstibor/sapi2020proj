<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.project.list.page.title"/></title>
</head>
<body>
    <div id="project-list" class="page-content">
        <c:choose>
            <c:when test="${empty project}">
                <p><spring:message code="label.epic.list.empty"/></p>
            </c:when>
            <c:otherwise>
            <h1>${project.title}</h1>
			<h2>${project.description}</h2>
			</c:otherwise>
        </c:choose>
        <c:choose>
			<c:when test="${empty project.epics}">
				<p>No epic added yet</p>
			</c:when>
			<c:otherwise>
           <div>
          	<p>${projects.epic}</p>
			<c:forEach items="${project.epics}" var="epic">
                    <div class="well well-small">
                        <a><c:out value="${epic.title}"/></a>
                        <p>${epic.description}</p>
                    </div>
                </c:forEach>
            </div>
			</c:otherwise> 
			</c:choose>           
    </div>
       <div class="well page-content">
        <form:form action="/project/${project.id}/epic/add" commandName="project" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="story-title">Epic title</label>

                <div class="controls">
                    <form:input id="project-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="story-description">Epic description</label>

                <div class="controls">
                    <form:textarea id="story-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <button id="add-story-button" type="submit" class="btn btn-primary">Add epic</button>
            </div>
        </form:form>
    </div>
</body>
</html>