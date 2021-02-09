<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.task.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/story/{storyId}/task/add" commandName="task" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="task-title"><spring:message code="label.task.title"/>:</label>

                <div class="controls">
                    <form:input id="task-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="task-description"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="task-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-task-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.story.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>