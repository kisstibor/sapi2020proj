<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/priority.view.js"></script>
</head>
<body>
    <div id="priority-id" class="hidden">${priority.id}</div>
    <h1><spring:message code="Priority"/></h1>
    <div class="well page-content">
        <h2 id="priority-name"><c:out value="${priority.name}"/></h2>
        
        <div class="action-buttons">
            <a href="/priority/update/${priority.id}" class="btn btn-primary"><spring:message code="Update"/></a>
            <a href="/priority/delete/${priority.id}" id="delete-priority-link" class="btn btn-primary"><spring:message code="Delete"/></a>
        </div>
    </div>
    <script id="template-delete-priority-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-priority-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.priority.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.priority.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-priority-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-priority-button" href="#" class="btn btn-primary"><spring:message code="label.delete.priority.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>