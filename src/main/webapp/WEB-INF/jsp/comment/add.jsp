<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.comment.add.page.title"/></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1 id="page-title"><spring:message code="label.comment.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="" commandName="comment" method="POST" enctype="utf8">
            <form:input id="story-id" path="storyId" type="hidden"/>

            <div id="control-group-message" class="control-group">
                <label for="comment-message"><spring:message code="label.comment.message"/>:</label>

                <div class="controls">
                    <form:input id="comment-message" path="message"/>
                    <form:errors id="error-title" path="message" cssClass="help-inline"/>
                </div>
            </div>

            <div class="action-buttons">
                <a id="cancel-add-comment-button"  href="/story/${storyId}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-comment-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.comment.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>