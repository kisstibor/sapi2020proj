<!DOCTYPE html>
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/goal.view.js"></script>
</head>
<body>
    <div id="goal-id" class="hidden">${goal.id}</div>
    <h1><spring:message code="label.goal.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="goal-title"><c:out value="${goal.goal}"/></h2>
        <div>
            <p><c:out value="${goal.method}"/></p>
            <p><c:out value="${goal.metrics}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/goal/update/${goal.id}" class="btn btn-primary"><spring:message code="label.update.goal.link"/></a>
            <a id="delete-goal-link" class="btn btn-primary"><spring:message code="label.delete.goal.link"/></a>
        </div>
    </div>
        <script id="template-delete-goal-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-goal-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.goal.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.goal.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-goal-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-goal-button" href="#" class="btn btn-primary"><spring:message code="label.delete.goal.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>