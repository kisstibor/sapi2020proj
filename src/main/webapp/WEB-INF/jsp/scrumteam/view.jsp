<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
</head>
<body>
    <div id="story-id" class="hidden">${team.id}</div>
    <h1><spring:message code="label.scrumofscrums.view.page.title"/></h1>
    <div class="well page-content">
    
        <div>
            <p>
            	<b>Team name:</b>
            	<c:out value="${team.name}"/>
            </p>
        </div>
        
        <div>
        	<p>
        		<b>Members:</b>
        		<c:out value="${team.members}"/>
        	</p>
        </div>
		
		<div id="control-group-title" class="control-group">
           	<label for="story-description">
	           	<b>
	           		<spring:message code="label.scrumteam.add.page.stories"/>:       		
	           	</b>
           	</label>
            <c:forEach items="${team.stories}" var="story">
            	<c:out value="${story.id}.) ${story.title}"/>
				<%--
				<label class="checkbox-inline btn btn-default" for="whichTitle-${story.title}">
					<form:checkbox id="whichTitle-${story.title}" value="${story.title}" path="selectedStories"/>
					${story.title}
			    </label>
			     --%>
			    <br>
			</c:forEach>
		</div>
        
        <div class="action-buttons">
            <a href="/scrumteam/update/${team.id}" class="btn btn-primary"><spring:message code="label.update.scrumteam.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.scrumteam.link"/></a>
        </div>
    </div>
    <script id="template-delete-scrumteam-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-scrumteam-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.scrumteam.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.scrumteam.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-scrumteam-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-scrumteam-button" href="#" class="btn btn-primary"><spring:message code="label.delete.scrumteam.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>