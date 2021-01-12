<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta charset="UTF-8">
<title>Add project</title>
<script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
 <h1>Add a new project</h1>
    <div class="well page-content">
        <form:form action="/project/add" commandName="project" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="epic-title">Project title</label>

                <div class="controls">
                    <form:input id="epic-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="epic-description">Project description</label>

                <div class="controls">
                    <form:textarea id="epic-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <button id="add-epic-button" type="submit" class="btn btn-primary">Add project</button>
            </div>
        </form:form>
    </div>
</body>
</html>