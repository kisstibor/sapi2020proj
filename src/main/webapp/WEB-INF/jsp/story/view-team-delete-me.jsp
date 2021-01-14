<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    
    
    <!-- TODO >>>>>>
    <script type="text/javascript" src="/static/js/team.view.js"></script>
    -->
   
</head>
<body>
    <div id="team-id" class="hidden">${team.id}</div>
    <h1><spring:message code="label.scrumteam.view.page.title"/></h1>
    <div class="well page-content">
    
        <h2 id="team-name"><c:out value="${team.name}"/></h2>
        <div>
            <p><c:out value="${team.membersCSV"/></p>
        </div>
        
        <div class="action-buttons">
            <a href="/story/update-team/${team.id}" class="btn btn-primary"><spring:message code="label.update.story.link"/></a>
            <a id="delete-team-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        </div>
        
    </div>
    <!-- TODO >>>>>>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">

            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>

            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>

            <div class="modal-footer">
                <a id="cancel-story-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-story-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>

        </div>
    </script>
     -->
</body>
</html>