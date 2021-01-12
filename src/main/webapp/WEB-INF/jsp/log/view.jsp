<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/log.view.js"></script>
</head>
<body>
    <div id="log-id" class="hidden">${log.id}</div>
    <h1><spring:message code="label.log.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="log-title"><c:out value="${log.title}"/></h2>
        <div>
        <p><c:out value="${log.assignTo}"/></p>
        <p><c:out value="${log.status}"/></p>
        <p><c:out value="${log.doc}"/></p>
            <p><c:out value="${log.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/log/update/${log.id}" class="btn btn-primary"><spring:message code="label.update.log.link"/></a>
            <a id="delete-log-link" class="btn btn-primary"><spring:message code="label.delete.log.link"/></a>
        </div>
    </div>
    
    
     <script id="template-delete-log-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-log-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.log.delete.dialog.title"/></h3>
            </div>
  <div class="modal-body">
                <p><spring:message code="label.log.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-log-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-log-button" href="#" class="btn btn-primary"><spring:message code="label.delete.log.button"/></a>
            </div>
        </div>
    </script>
    
    
</body>
</html>