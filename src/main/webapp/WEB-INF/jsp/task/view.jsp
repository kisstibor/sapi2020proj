<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/task.view.js"></script>
</head>
<body>
    <div id="task-id" class="hidden">${task.id}</div>
    <h1><spring:message code="label.task.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="task-title"><c:out value="${task.title}"/></h2>
        <div>
            <p><c:out value="${task.description}"/></p>
            <p><c:out value="${task.priority}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/scrumsapientia/task/update/${task.id}" class="btn btn-primary"><spring:message code="label.update.task.link"/></a>
            <a id="delete-task-link" class="btn btn-primary"><spring:message code="label.delete.task.link"/></a>
        </div>
    </div>
    <script id="template-delete-task-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-task-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.task.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.task.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-task-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-task-button" href="#" class="btn btn-primary"><spring:message code="label.delete.task.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>