<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/scrumsapientia/static/js/story.vacation.js"></script>
</head>
<body>
    <h1><spring:message code="label.vacation.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/scrumsapientia/story/vacation/update/${vacation.id}" commandName="vacation" method="POST" enctype="utf8">
                 <div id="control-group-title" class="control-group">
                <label><spring:message code="label.vacation.start.date"/>:</label>

                <div class="controls">
                    <form:input type="date" id="vacationStartDate" path="vacationStartDate"/>
                    <form:errors id="error-vacation" path="vacationStartDate" cssClass="help-inline"/>
                </div>
            </div>
               <div id="control-group-title" class="control-group">
                <label><spring:message code="label.vacation.end.date"/>:</label>

                <div class="controls">
                    <form:input type="date" id="vacationEndDate" path="vacationEndDate"/>
                    <form:errors id="error-vacation" path="vacationEndDate" cssClass="help-inline"/>
                </div>
            </div>

            <div class="action-buttons">
                <a href="/scrumsapientia/vacation/${vacation.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-vacation-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.vacation.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html> 