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
        <form:form action="/comment/add" commandName="comment" method="POST" enctype="utf8">
        	<form:hidden path="storyId"/>
            <div id="control-group-description" class="control-group">
                <label for="comment-message"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="story-description" path="message"/>
                    <form:errors id="error-description" path="message" cssClass="help-inline"/>
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