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
    <h1><spring:message code="label.user.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/user/add" commandName="user" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="user-username"><spring:message code="label.user.username"/>:</label>

                <div class="controls">
                    <form:input id="user-username" path="username"/>
                    <form:errors id="error-username" path="username" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="user-password"><spring:message code="label.user.password"/>:</label>

                <div class="controls">
                    <form:password id="user-password" path="password"/>
                    <form:errors id="error-password" path="password" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-user-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.user.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>