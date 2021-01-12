<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.log.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/log/add" commandName="log" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="log-title"><spring:message code="label.log.title"/>:</label>

                <div class="controls">
                    <form:input id="log-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            
             <div id="control-group-assignTo" class="control-group">
                <label for="log-assignTo"><spring:message code="label.log.assignTo"/>:</label>

                <div class="controls">
                    <form:input id="log-assignTo" path="assignTo"/>
                    <form:errors id="error-assignTo" path="assignTo" cssClass="help-inline"/>
                </div>
            </div>
            
            
            <div id="control-group-status" class="control-group">
                <label for="log-status"><spring:message code="label.log.status"/>:</label>

                <div class="controls">
                    <form:input id="log-status" path="status"/>
                    <form:errors id="error-status" path="status" cssClass="help-inline"/>
                </div>
            </div>
            
            
            
              <div id="control-group-doc" class="control-group">
                <label for="log-doc"><spring:message code="label.log.doc"/>:</label>

                <div class="controls">
                    <form:input type="file" id="log-doc" path="doc"/>
                    <form:errors id="error-doc" path="doc" cssClass="help-inline"/>
                </div>
            </div>
            
            
           
            
         
            
            
            
            <div id="control-group-description" class="control-group">
                <label for="log-description"><spring:message code="label.log.description"/>:</label>

                <div class="controls">
                    <form:textarea id="log-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-log-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.log.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>