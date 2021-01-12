<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.story.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/story/update" commandName="story" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-title" class="control-group">
                <label for="story-title"><spring:message code="label.story.title"/>:</label>

                <div class="controls">
                    <form:input id="story-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="story-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-fixVersion" class="control-group">
            	<label for="story-fixVersion"><spring:message code="label.story.fixVersion"/></label>
            	<div class="controls">
	            	<select name="fixVersion" path="fixVersion" value=${story.fixVersion }>
	        			<c:forEach items="${fixVersions}" var="fixVersion">
	        				<c:choose>
	        					<c:when test="${fixVersion.id==story.fixVersion }">
	        						<option value="${fixVersion.id}" selected>${fixVersion.name}</option>
	        					</c:when>
	        					<c:otherwise>
	        						<option value="${fixVersion.id}">${fixVersion.name}</option>
	        					</c:otherwise>
	        				</c:choose>
	        			</c:forEach>
	    			</select>
    			</div>
            </div>
            <div class="action-buttons">
                <a href="/story/${story.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.story.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>