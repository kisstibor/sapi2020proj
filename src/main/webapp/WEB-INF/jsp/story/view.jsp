<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/scrumsapientia/static/js/story.view.js"></script>
</head>
<body>
    <div id="story-id" class="hidden">${story.id}</div>
    <h1><spring:message code="label.story.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${story.title}"/></h2>
        <div>
            <p><c:out value="${story.description}"/></p>
        </div>
        <c:choose>
		    <c:when test="${empty storytimelimit}">
		        <div>
            		<a href="/scrumsapientia/story/timelimit/add/${story.id}" class="btn btn-primary"><spring:message code="label.add.storytimelimit.button"/></a>
       			</div>
		    </c:when>    
		    <c:otherwise>
		        <div>
            		<p><spring:message code="label.storytimelimit"/>: <c:out value="${storytimelimit.timelimit}"/></p>
            		<a href="/scrumsapientia/story/timelimit/update/${story.id}/${storytimelimit.id}" class="btn btn-primary"><spring:message code="label.update.storytimelimit.link"/></a>
            		<a href="/scrumsapientia/story/timelimit/delete/${story.id}/${storytimelimit.id}" class="btn btn-primary"><spring:message code="label.delete.storytimelimit.link"/></a>
            	</div>
		    </c:otherwise>
		</c:choose>
        <div class="action-buttons">
            <a href="/scrumsapientia/story/update/${story.id}" class="btn btn-primary"><spring:message code="label.update.story.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        </div>
    </div>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-story-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-story-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>