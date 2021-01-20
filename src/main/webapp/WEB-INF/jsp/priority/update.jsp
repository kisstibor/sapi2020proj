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
    <h1><spring:message code="label.priority.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/scrumsapientia/priority/update" commandName="priority" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-title" class="control-group">
                <label for="priority-title"><spring:message code="label.priority.title"/>:</label>

                <div class="controls">
                    <form:input id="priority-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="/scrumsapientia/priority/${priority.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-priority-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.priority.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>