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
    <h1><spring:message code="label.scrumteam.update.page.title"/></h1>
    <div class="well page-content">
    
        <form:form action="/scrumteam/update" commandName="team" method="POST" enctype="utf8">
        	<form:hidden path="id"/>
        
            <div id="control-group-title" class="control-group">
                <label for="story-title"><spring:message code="label.scrumteam.add.page.name"/>:</label>
                <div class="controls">
                    <form:input id="team-name" path="name"/>
                    <form:errors id="error-title" path="name" cssClass="help-inline"/>
                </div>
            </div>
            
            
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.scrumteam.add.page.members"/>:</label>
                <div class="controls">
                    <form:textarea id="team-members" path="members"/>
                    <form:errors id="error-description" path="members" cssClass="help-inline"/>
                </div>
            </div>
           
           
            <div id="control-group-title" class="control-group">
            	<label for="story-description"><spring:message code="label.scrumteam.add.page.stories"/>:</label>
	            <c:forEach items="${team.stories}" var="item">
					<br><label class="checkbox-inline btn btn-default" for="whichTitle-${item.title}">
					<form:checkbox id="whichTitle-${item.title}" value="${item.title}" path="selectedStories"/>
						${item.title}
						<br>
				    </label>
				    <br>
				</c:forEach>
			</div>
           
           
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary">
                	<spring:message code="label.add.scrumteam.button"/>
                </button>
            </div>
            
            
        </form:form>
    </div>
</body>
</html>