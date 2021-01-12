<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title></title>
<script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
	<h1>
		<spring:message code="label.project.add.page.title" />
	</h1>
	<div class="well page-content">
		<form:form action="/project/add" commandName="project"
			method="POST" enctype="utf8">
			<div id="control-group-title" class="control-group">
				<label for="project-name"><spring:message
						code="label.project.name" />:</label>

				<div class="controls">
					<form:input id="project-name" path="name" />
					<form:errors id="error-title" path="name" cssClass="help-inline" />
				</div>
			</div>
			<div id="control-group-description" class="control-group">
				<label for="project-productowner"><spring:message
						code="label.project.productOwner" />:</label>

				<div class="controls">
					<form:input id="project-productowner" path="productOwner" />
					<form:errors id="error-description" path="productOwner"
						cssClass="help-inline" />
				</div>
			</div>

			<div id="control-group-description" class="control-group">
				<label for="project-scrummaster"><spring:message
						code="label.project.scrumMaster" />:</label>

				<div class="controls">
					<form:input id="project-scrummaster" path="scrumMaster" />
					<form:errors id="error-description" path="scrumMaster"
						cssClass="help-inline" />
				</div>
			</div>

			<div id="control-group-description" class="control-group">
				<label for="project-members"><spring:message
						code="label.project.teamMembers" />:</label>

				<div class="controls">
					<form:textarea id="project-members" path="members" />
					<form:errors id="error-description" path="members"
						cssClass="help-inline" />
				</div>
			</div>
			<div class="action-buttons">
				<a href="/" class="btn"><spring:message
						code="label.cancel" /></a>
				<button id="add-project-button" type="submit" class="btn btn-primary">
					<spring:message code="label.add.story.button" />
				</button>
			</div>
		</form:form>
	</div>
</body>
</html>