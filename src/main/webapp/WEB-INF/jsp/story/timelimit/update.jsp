<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/scrumsapientia/static/js/story.timelimit.js"></script>
</head>
<body>
    <h1><spring:message code="label.storytimelimit.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/scrumsapientia/story/timelimit/update/${story.id}/${storytimelimit.id}" commandName="storytimelimit" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="timelimit"><spring:message code="label.storytimelimit"/>:</label>

                <div class="controls">
                    <form:input type="date" id="timelimit" path="timelimit"/>
                    <form:errors id="error-timelimit" path="timelimit" cssClass="help-inline"/>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="/scrumsapientia/story/${story.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-timelimit-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.storytimelimit.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>