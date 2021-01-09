<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript" src="/static/js/story.review.js"></script>
</head>
<body>
	<h1>
		<spring:message code="label.story.review.title" />
	</h1>
	<div class="well page-content">
		<form:form action="/story/review/${storyId}" commandName="review"
			method="POST" enctype="utf8">

			<div id="control-group-title" class="control-group">
				<label for="review"><spring:message
						code="label.story.review.title" />:</label>

				<div class="controls">
					<form:input id="review" path="review" />
					<form:errors id="error-datee" path="review" cssClass="help-inline" />
				</div>
			</div>

			<div class="action-buttons">

				<c:if test="${not empty review.review}">
					<a href="/story/review/remove/${storyId}" class="btn"
						style="background: red"><spring:message
							code="label.review.remove.story.link" /></a>
				</c:if>

				<a href="/story/${storyId}" class="btn"><spring:message
						code="label.cancel" /></a>


				<button id="review-story-button" type="submit"
					class="btn btn-primary">

					<c:choose>
						<c:when test="${empty review.review}">
							<spring:message code="label.review.story.link" />
						</c:when>
						<c:otherwise>
							Update
						</c:otherwise>
					</c:choose>


				</button>
			</div>

		</form:form>

	</div>
</body>
</html>