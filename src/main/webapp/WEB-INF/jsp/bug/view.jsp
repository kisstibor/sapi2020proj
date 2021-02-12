<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/bug.view.js"></script>
</head>
<body>
    <div id="bug-id" class="hidden">${bug.id}</div>
    <h1><spring:message code="label.bug.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="bug-title"><c:out value="${bug.title}"/></h2>
        <div>
            <p>Description:&nbsp;<c:out value="${bug.description}"/></p>
            <p>Status:&nbsp;<c:out value="${bug.status}"/></p>
            <p>CreatedTime:&nbsp;<c:out value="${bug.creationTime}"/></p>
            <p>ModificationTime:&nbsp;<c:out value="${bug.modificationTime}"/></p>
            
        </div>
        <div class="action-buttons">
            <a href="/scrumsapientia/bug/update/${bug.id}" class="btn btn-primary"><spring:message code="label.update.bug.link"/></a>
            <a id="delete-bug-link" class="btn btn-primary"><spring:message code="label.delete.bug.link"/></a>
        </div>
    </div>
    <script id="template-delete-bug-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-bug-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.bug.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.bug.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-bug-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-bug-button" href="#" class="btn btn-primary"><spring:message code="label.delete.bug.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>