<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>Add bug</title>
	<script type="text/javascript" src="/static/js/bug.form.js"></script>
</head>
<body>
	<h1>Add bug page</h1>
    <div class="well page-content">
        <form:form action="/bug/add" commandName="bug" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="bug-title">Title:</label>

                <div class="controls">
                    <form:input id="bug-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="bug-description">Description:</label>

                <div class="controls">
                    <form:textarea id="bug-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/bug/list" class="btn">Cancel</a>
                <button id="add-bug-button" type="submit" class="btn btn-primary">Add</button>
            </div>
        </form:form>
    </div>
</body>
</html>