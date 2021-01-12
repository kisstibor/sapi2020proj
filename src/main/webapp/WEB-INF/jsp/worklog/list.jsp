<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/worklog.list.js"></script>
</head>
<body>
    <h1><spring:message code="label.worklog.list.page.title"/></h1>
    
    <div>
        <a href="/worklog/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.worklog.button"/></a>
    </div>
	
	<div id="control-group-logged-at" class="control-group">
	
	    <div class="controls">
	        <input type="Date" id="worklog-logged-ad"/>
	    </div>
	    
	    <a href="worklog/list/" onclick="addTextBoxData(this)">Search by date</a>
	    
	</div>
    
    <div id="worklog-list" class="page-content">
        <c:choose>
            <c:when test="${empty worklogs}">
                <p><spring:message code="label.worklog.list.empty"/></p>
            </c:when>
            <c:otherwise>
            	<table style="width=60%">
            		<tr>
            			<th>Story</th>
            			<th>Logged at</th>
            			<th>Started at</th>
            			<th>Ended at</th>
            			<th>Quick view</th>
            		</tr>
            
	                <c:forEach items="${worklogs}" var="worklog">
	                	<tr>
		                	<td>${worklog.story_title}</td>
		                	<td>${worklog.logged_at}</td>
		                	<td>${worklog.started_at}</td>
		                	<td>${worklog.ended_at}</td>
		                    <td>
		                        <a href="http://localhost:8080/worklog/view/${worklog.id}"><c:out value="view"/></a>
		                    </td>
		            	</tr>
	                </c:forEach>
	        	</table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>