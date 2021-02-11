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
    <h1><spring:message code="label.story.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/story/add" commandName="story" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="story-title"><spring:message code="label.story.title"/>:</label>

                <div class="controls">
                    <form:input id="story-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="story-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-comment" class="control-group">
                <label for="story-comment"><spring:message code="label.story.comment"/>:</label>

                <div class="controls">
                    <form:textarea id="story-comment" path="comment"/>
                    <form:errors id="error-comment" path="comment" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-team" class="control-group">
                <label for="story-team"><spring:message code="label.story.team"/>:</label>

                <div class="controls">
                    <form:input id="story-team" path="team"/>
                    <form:errors id="error-team" path="team" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-points" class="control-group">
                <label for="story-points"><spring:message code="label.story.points"/>:</label>

                <div class="controls">
                    <form:input id="story-points" path="points"/>
                    <form:errors id="error-points" path="points" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.story.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>