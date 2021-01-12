<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.scrum.add.page.title"/></h1>
    <div class="well page-content">
    
        <form:form action="/scrum/add" commandName="scrum" method="POST" enctype="utf8">
        
            <div id="control-group-title" class="control-group">
                <label for="story-title"><spring:message code="label.scrum.add.page.name"/>:</label>
                <div class="controls">
                    <form:input id="scrum-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            
            
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.scrum.add.page.members"/>:</label>
                <div class="controls">
                    <form:textarea id="scrum-members" path="members"/>
                    <form:errors id="error-description" path="members" cssClass="help-inline"/>
                </div>
            </div>
           
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary">
                	<spring:message code="label.add.scrum.button"/>
                </button>
            </div>
        
        </form:form>
    </div>
</body>
</html>