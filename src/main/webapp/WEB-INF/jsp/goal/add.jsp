<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title></title>
</head>
<body>
	<h1>
		<spring:message code="label.goal.add.page.title" />
	</h1>
	<div class="well page-content">
		<form:form action="/goal/add" commandName="goal" method="POST"
			enctype="utf8">
			<div id="control-group-goal" class="control-group">
				<label for="goal-goal"><spring:message
						code="label.goal.goal" />:</label>

				<div class="controls">
					<form:textarea id="goal-goal" path="goal" />
					<form:errors id="error-goal" path="goal" cssClass="help-inline" />
				</div>
			</div>
			<div id="control-group-method" class="control-group">
				<label for="goal-method"><spring:message
						code="label.goal.method" />:</label>

				<div class="controls">
					<form:textarea id="goal-method" path="method" />
					<form:errors id="error-method" path="method" cssClass="help-inline" />
				</div>
			</div>
			<div id="control-group-metrics" class="control-group">
				<label for="goal-metrics"><spring:message
						code="label.goal.metrics" />:</label>

				<div class="controls">
					<form:textarea id="goal-metrics" path="metrics" />
					<form:errors id="error-metrics" path="metrics"
						cssClass="help-inline" />
				</div>
			</div>
			<div class="action-buttons">
				<a href="/goal/list" class="btn"><spring:message
						code="label.cancel" /></a>
				<button id="add-goal-button" type="submit" class="btn btn-primary">
					<spring:message code="label.add.goal.button" />
				</button>
			</div>
		</form:form>
	</div>
</body>
</html>