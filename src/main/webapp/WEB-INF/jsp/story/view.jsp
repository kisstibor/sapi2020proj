<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
</head>
<body>
    <div id="story-id" class="hidden">${story.id}</div>
    <h1><spring:message code="label.story.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${story.title}"/></h2>
        <div>
            <p><c:out value="${story.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/story/update/${story.id}" class="btn btn-primary"><spring:message code="label.update.story.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        </div>
    </div>
    <h3><spring:message code="label.task.add.page.title"/></h3>
    <div class="well page-content">
        <form:form action="/story/${story.id}/add_task" commandName="new_task" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="new_task-title"><spring:message code="label.task.title"/>:</label>

                <div class="controls">
                    <form:input id="new_task-title" path="title" default=""/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            
            <div class="action-buttons">
                <button id="add-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.task.button"/></button>
            </div>
        </form:form>
    </div>
        
    
    <div id="task-list" class="page-content">
        <c:choose>
            <c:when test="${empty tasks}">
                <p><spring:message code="label.task.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${tasks}" var="task">
                	<div id="task-id" class="hidden">${task.id}</div>
	                <div class="well page-content">
	                    <div class="well well-small">
	                        <%--<a href="/story/${story.id}/task/${task.id}">--%><c:out value="${task.title}"/><%--</a>--%>
	                    </div>
	                    <div class="action-buttons">
				            <a id="delete-task-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
				        </div>
				    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-story-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-story-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
    <script id="template-delete-task-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-task-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.task.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.task.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-task-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-task-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>