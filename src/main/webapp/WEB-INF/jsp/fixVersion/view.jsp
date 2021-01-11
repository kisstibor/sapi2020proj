<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/fixVersion.view.js"></script>
</head>
<body>
    <div id="fixVersion-id" class="hidden">${fixVersion.id}</div>
    <h1><spring:message code="label.fixVersion.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${fixVersion.name}"/></h2>
        <div class="action-buttons">
            <a href="/fixVersion/update/${fixVersion.id}" class="btn btn-primary"><spring:message code="label.update.fixVersion.link"/></a>
            <a id="delete-fixVersion-link" class="btn btn-primary"><spring:message code="label.delete.fixVersion.link"/></a>
        </div>
    </div>
    <script id="template-delete-fixVersion-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-fixVersion-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.fixVersion.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.fixVersion.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-fixVersion-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-fixVersion-button" href="#" class="btn btn-primary"><spring:message code="label.delete.fixVersion.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>