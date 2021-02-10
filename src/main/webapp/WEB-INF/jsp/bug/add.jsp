<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/bug.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.bug.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/scrumsapientia/bug/add" commandName="bug" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="bug-title"><spring:message code="label.bug.title"/>:</label>

                <div class="controls">
                    <form:input id="bug-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="bug-description"><spring:message code="label.bug.description"/>:</label>

                <div class="controls">
                    <form:textarea id="bug-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-status" class="control-group">
                <label for="bug-status"><spring:message code="label.bug.status"/>:</label>
                
            	<div class="controls">
                	<form action="list" method="post">
                    <select name="status">
                    	<option value = "TO_DO">TO_DO</option>
                    	<option value = "IN_PROGRESS">IN_PROGRESS</option>
                    	<option value = "DONE">DONE</option>
                    </select>
                    <form:errors id="error-status" path="status" cssClass="help-inline"/>
                
            
            		<div class="action-buttons">
                		<a href="/scrumsapientia" class="btn"><spring:message code="label.cancel"/></a>
                		<button id="add-bug-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.bug.button"/></button>
            		</div>
            		</form>
            	</div>
            </div>
        </form:form>
    </div>
</body>
</html>