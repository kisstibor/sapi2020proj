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
    </br>
    <div>
    	<form:form action="/story/filter" commandName="story" method="POST" enctype="utf8">
	    	<div id="control-group-title" class="control-group">
               <label for="story-title"><spring:message code="label.story.point"/>:</label>

               <div class="controls">
                   <input type="number" id="tory-title" name="point"/>
                   <form:errors id="error-point" cssClass="help-inline"/>
               </div>
	        </div>
		    <div>
                <button type="submit" class="btn btn-primary">Filter</button>
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
                        <a href="/story/${story.id}"><c:out value="${story.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>