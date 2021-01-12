<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/user.view.js"></script>
</head>
<body>
    <div id="user-id" class="hidden">${user.id}</div>
    <h1><spring:message code="label.user.view.page.name"/></h1>
    <div class="well page-content">
        <h2 id="user-title"><c:out value="${user.name}"/></h2>
        <div>
            <p><c:out value="${user.type}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/scrumsapientia/user/update/${user.id}" class="btn btn-primary"><spring:message code="label.update.user.link"/></a>
            <a href="/scrumsapientia/user/delete/${user.id}" id="delete-user-link" class="btn btn-primary"><spring:message code="label.delete.user.link"/></a>
        </div>
    </div>
    <script id="template-delete-user-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-user-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.user.delete.dialog.name"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.user.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-user-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-user-button" href="#" class="btn btn-primary"><spring:message code="label.delete.user.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>