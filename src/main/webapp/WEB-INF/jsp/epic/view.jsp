<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
</head>
<body>
    <div id="epic-id" class="hidden">${epic.id}</div>
    <h1><spring:message code="label.epic.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="epic-title"><c:out value="${epic.title}"/></h2>
        <div>
            <p><c:out value="${epic.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/epic/update/${epic.id}" class="btn btn-primary"><spring:message code="label.update.epic.link"/></a>
            <a id="delete-epic-link" class="btn btn-primary"><spring:message code="label.delete.epic.link"/></a>
        </div>
    </div>
    <script id="template-delete-epic-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-epic-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.epic.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.epic.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-epic-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-epic-button" href="#" class="btn btn-primary"><spring:message code="label.delete.epic.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>