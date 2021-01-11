<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
</head>
<body>
    <div id="story-id" class="hidden">${scrum.id}</div>
    <h1><spring:message code="label.scrum.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${scrum.title}"/></h2>
        <div>
            <p><c:out value="${scrum.members}"/></p>
        </div>
        <div>
        	<p><c:out value="${scrum.stories}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/story/update/${scrum.id}" class="btn btn-primary"><spring:message code="label.update.scrum.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        </div>
    </div>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.scrum.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.scrum.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-scrum-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-scrum-button" href="#" class="btn btn-primary"><spring:message code="label.delete.scrum.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>