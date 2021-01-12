<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title></title>
<script type="text/javascript" src="/static/js/project.view.js"></script>
</head>
<body>
	<div id="project-id" class="hidden">${project.id}</div>
	<h1>
		<spring:message code="label.project.view.page.title" />
	</h1>
	<div class="well page-content">
		<h2 id="project-name">
			<c:out value="${project.name}" />
		</h2>
		<div>
			<p>
				Product owner:
				<c:out value="${project.productOwner}" />
			</p>
		</div>
		<div>
			<p>
				Scrum master:
				<c:out value="${project.scrumMaster}" />
			</p>
		</div>

		<div>
			<p>
				Development members:
				<c:choose>
					<c:when test="${empty project.members}">
						<p>
							<spring:message code="label.story.list.empty" />
						</p>
					</c:when>
					<c:otherwise>
						<c:forEach items="${project.members}" var="member">
							<p><c:out value="${member}" /></p>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</p>
		</div>

		<div class="action-buttons">
			<a href="/project/update/${project.id}"
				id="update-project-link" class="btn btn-primary"><spring:message
					code="label.update.story.link" /></a> <a
				href="/project/delete/${project.id}"
				id="delete-project-link" class="btn btn-primary"><spring:message
					code="label.delete.story.link" /></a>
			<!-- <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a> -->
		</div>
	</div>
	<script id="template-delete-project-confirmation-dialog"
		type="text/x-handlebars-template">
        <div id="delete-project-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-project-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-project-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>