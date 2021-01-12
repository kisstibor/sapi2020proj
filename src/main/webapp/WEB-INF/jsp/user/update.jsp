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
    <h1><spring:message code="label.user.update.page.name"/></h1>
    <div class="well page-content">
        <form:form action="/scrumsapientia/user/update" commandName="user" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-title" class="control-group">
                <label for="user-title"><spring:message code="label.user.name"/>:</label>

                <div class="controls">
                    <form:input id="user-title" path="name"/>
                    <form:errors id="error-title" path="name" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="user-description"><spring:message code="label.user.type"/>:</label>

                <div class="controls">
                    <form:textarea id="user-description" path="type"/>
                    <form:errors id="error-description" path="type" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/user/${user.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-user-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.user.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>