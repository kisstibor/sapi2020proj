<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/worklog.view.js"></script>
</head>
<body>
    <div id="worklog-id" class="hidden">${worklog.id}</div>
    <h1><spring:message code="label.worklog.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="worklog-story_title"><c:out value="${worklog.story_title}"/></h2>
        <div>
        	<p>Logged at: <c:out value="${worklog.logged_at}"/></p>
        	<p>Started at: <c:out value="${worklog.started_at}"/></p>
        	<p>Ended at: <c:out value="${worklog.ended_at}"/></p>
            <p>Log description: <c:out value="${worklog.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/worklog/update/${worklog.id}" class="btn btn-primary"><spring:message code="label.update.worklog.link"/></a>
            <a id="delete-worklog-link" class="btn btn-primary"><spring:message code="label.delete.worklog.link"/></a>
        </div>
    </div>
    <script id="template-delete-worklog-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-worklog-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.worklog.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-worklog-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-worklog-button" href="#" class="btn btn-primary"><spring:message code="label.delete.worklog.button"/></a>
            </div>
        </div>
    </script>
    
</body>
</html>