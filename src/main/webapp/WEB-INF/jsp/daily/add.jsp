<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title></title>
<script type="text/javascript" src="/static/js/daily.form.js"></script>
</head>
<body>
	<h1>
		<spring:message code="label.daily.add.page.title" />
	</h1>
	<div class="well page-content">
		<form:form action="/daily/add" commandName="daily" method="POST"
			enctype="utf8">
			<div id="control-group-title" class="control-group">
				<label for="daily-title"><spring:message
						code="label.daily.title" />:</label>

				<div class="controls">
					<form:input id="daily-title" path="title" />
					<form:errors id="error-title" path="title" cssClass="help-inline" />
				</div>
			</div>

			<div id="control-group-datee" class="control-group">
				<label for="daily-datee"><spring:message
						code="label.daily.datee" />:</label>

				<div class="controls">
					<form:input id="daily-datee" path="datee" />
					<form:errors id="error-datee" path="datee" cssClass="help-inline" />
				</div>
			</div>

			<div id="control-group-duration" class="control-group">
				<label for="daily-duration"><spring:message
						code="label.daily.duration" />:</label>

				<div class="controls">
					<form:input id="daily-duration" path="duration" />
					<form:errors id="error-duration" path="duration"
						cssClass="help-inline" />
				</div>
			</div>

			<div id="control-group-description" class="control-group">
				<label for="daily-description"><spring:message
						code="label.daily.description" />:</label>

				<div class="controls">
					<form:textarea id="daily-description" path="description" />
					<form:errors id="error-description" path="description"
						cssClass="help-inline" />
				</div>
			</div>
			<div class="action-buttons">
				<a href="/daily/list" class="btn"><spring:message
						code="label.cancel" /></a>
				<button id="add-daily-button" type="submit" class="btn btn-primary" style="background : green">
					<spring:message code="label.add.daily.button" />
				</button>
			</div>
		</form:form>
	</div>
</body>
</html>