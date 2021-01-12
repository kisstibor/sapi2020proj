<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title><spring:message code="label.story.view.page.title" /></title>
<script type="text/javascript" src="/static/js/story.view.js"></script>
</head>
<body>
	<div id="story-id" class="hidden">${story.id}</div>
	<h1 id="page-title">
		<spring:message code="label.story.view.page.title" />
	</h1>
	<div class="well page-content">
		<h2 id="story-title">
			<c:out value="${story.title}" />
		</h2>
		<div>
			<p>
				<c:out value="${story.description}" />
			</p>
		</div>
		<div class="action-buttons">
			<a id="action-update-button" href="/story/update/${story.id}" class="btn btn-primary"><spring:message
					code="label.update.story.link" /></a> <a id="delete-story-link"
				class="btn btn-primary"><spring:message
					code="label.delete.story.link" /></a>
		</div>


	</div>

	<div class="page-content">
		<h4>
			<spring:message code="label.story.view.comments.title" />
		</h4>

		<div id="comment-list" class="page-content">
			<c:choose>
				<c:when test="${empty story.comments}">
					<p>
						<spring:message code="label.comment.list.empty" />
					</p>
					<p>
						<a href="/story/${story.id}/comment/add" id="add-button"
							class="btn btn-primary"><spring:message
								code="label.add.comment.link" /></a>
					</p>
				</c:when>
				<c:otherwise>
					<p>
						<a href="/story/${story.id}/comment/add" id="add-button"
							class="btn btn-primary"><spring:message
								code="label.add.comment.link" /></a>
					</p>
					<c:forEach items="${ story.comments }" var="comment">
						<div class="well comment-item" style="overflow:hidden">
							<p class="comment-message">
								<c:out value="${comment.message}"/></p>
							<div class="pull-right comment-item-actions">
								<a id="delete-comment-link" class="btn btn-primary"  style="margin:10px" data-comment-id="${ comment.id }"><spring:message
										code="label.delete.comment.link" /></a>
							</div>
						</div>

					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<script id="template-delete-story-confirmation-dialog"
		type="text/x-handlebars-template">
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

	<script id="template-delete-comment-confirmation-dialog"
		type="text/x-handlebars-template">
        <div id="delete-comment-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.comment.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.comment.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-delete-comment-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-comment-button" href="#" class="btn btn-primary"><spring:message code="label.delete.comment.button"/></a>
            </div>
        </div>
    </script>

</body>
</html>