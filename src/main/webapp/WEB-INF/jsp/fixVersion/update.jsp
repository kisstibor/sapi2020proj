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
    <h1><spring:message code="label.fixVersion.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/fixVersion/update" commandName="fixVersion" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-name" class="control-group">
                <label for="story-name"><spring:message code="label.story.name"/>:</label>

                <div class="controls">
                    <form:input id="story-name" path="name"/>
                    <form:errors id="error-name" path="name" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/fixVersion/${fixVersion.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-fixVersion-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.fixVersion.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>