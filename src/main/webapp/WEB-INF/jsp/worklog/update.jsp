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
    <h1><spring:message code="label.update.worklog.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/worklog/update" commandName="worklog" method="POST" enctype="utf8">
        	<form:hidden path="id"/>
        	
        	<div id="control-group-logged-at" class="control-group">
        		<label for="worklog-story-title"><spring:message code="label.worklog.select.story"/>:</label>
		        <form:input type="text" id="worklog-story-title" path="story_title" disabled="true"/>
		      </div>
		      
            <div id="control-group-logged-at" class="control-group">
                <label for="worklog-logged-at"><spring:message code="label.add.worklog.logged.at"/>:</label>

                <div class="controls">
                    <form:input type="Date" id="worklog-logged-ad" path="logged_at"/>
                    <form:errors id="error-logged-at" path="logged_at" cssClass="help-inline"/>
                </div>
            </div>
            
            <div id="control-group-started-at" class="control-group">
                <label for="worklog-started-at"><spring:message code="label.add.worklog.started.at"/>:</label>

                <div class="controls">
                    <form:input type="Time" id="worklog-started-at" path="started_at"/>
                    <form:errors id="error-started-at" path="started_at" cssClass="help-inline"/>
                </div>
            </div>
            
            <div id="control-group-ended-at" class="control-group">
                <label for="worklog-ended-at"><spring:message code="label.add.worklog.ended.at"/>:</label>

                <div class="controls">
                    <form:input type="Time" id="worklog-ended-at" path="ended_at"/>
                    <form:errors id="error-ended-at" path="ended_at" cssClass="help-inline"/>
                </div>
            </div>
            
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.add.worklog.description"/>:</label>

                <div class="controls">
                    <form:textarea id="worklog-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="/worklog/view/${worklog.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-worklog-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.worklog.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>