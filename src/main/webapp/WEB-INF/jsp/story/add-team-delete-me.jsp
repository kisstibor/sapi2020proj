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
        <form:form action="/story/add-team" commandName="story" method="POST" enctype="utf8">
        
            <div id="control-group-title" class="control-group">
                <label for="team-name"><spring:message code="label.story.team.name"/>:</label>

                <div class="controls">
                    <form:input id="team-name" path="name"/>
                    <form:errors id="team-name" path="name" cssClass="help-inline"/>
                </div>
            </div>
            
            <div id="control-group-description" class="control-group">
                <label for="team-members"><spring:message code="label.story.team.member"/>:</label>

                <div class="controls">
                    <form:textarea id="team-members" path="members"/>
                    <form:errors id="error-members" path="members" cssClass="help-inline"/>
                </div>
            </div>
            
            <!-- 
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.story.button"/></button>
            </div>
             -->
            
        </form:form>
    </div>
</body>
</html>