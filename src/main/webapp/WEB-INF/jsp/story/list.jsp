<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.story.list.page.title"/></h1>
    <div>
        <a href="/story/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
    </div>
    
    <div class="well page-content">
        <form:form action="/filteredStories" commandName="selectedPriority" method="GET" enctype="utf8">
             <div id="control-group-priority" class="control-group">
                <label for="story-priority"><spring:message code="Priority"/>:</label>
                <form:select name="priorities" path="id">  
				       <form:options items="${priorities}" itemLabel="name" itemValue="id"/>   
				</form:select>
            </div>
            <div class="action-buttons">
                <button id="filter-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="Filter"/></button>
            </div>
        </form:form>
    </div>
    
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty stories}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ stories}" var="story">
                    <div class="well well-small">
                        <a id="story-${story.id}" href="/story/${story.id}"><c:out value="${story.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>