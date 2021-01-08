<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="ro.sapientia2015.story.model.StoryStatus" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
    <style>
	table, th, td {
	  border: 1px solid black;
	  border-collapse: collapse;
	  text-align: center;
	}
	</style>
</head>
<body>
    <div id="story-id" class="hidden">${story.id}</div>
    <h1><spring:message code="label.story.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${story.title}"/></h2>
        <div class="action-buttons">
            <a href="/story/update/${story.id}" class="btn btn-primary"><spring:message code="label.update.story.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.story.link"/></a>
        </div>
        <c:choose>
            <c:when test="${story.getDueDateAsDateTime().isBeforeNow()}">
                <h2 style="color: red;" id="story-dueDate"><c:out value="Due Date: ${story.dueDate}"/></h2>
            </c:when>
            <c:otherwise>
                <h2 style="color: green;" id="story-dueDate"><c:out value="Due Date: ${story.dueDate}"/></h2>
            </c:otherwise>
        </c:choose>
        <h3 id="story-status"><c:out value="Status: ${story.status.getDisplayName()}"/></h3>
        <h3 id="story-modifyDate"><c:out value="Last Updated: ${story.statusModificationTime}"/></h3>
        <div>
            <p><c:out value="${story.description}"/></p>
        </div>
        <div id="comment-list" style="width: 40%;float: left;" class="well page-content content"">
        <h3>Comments:</h3>
	        <c:choose>
	            <c:when test="${empty story.comments}">
	                <p><spring:message code="label.story.list.empty"/></p>
	            </c:when>
	            <c:otherwise>
	                <c:forEach items="${ story.comments}" var="comment">
	                    <div class="well well-small">
	                        <!--  <a href="/story/${story.id}"><c:out value="${story.title}"/></a>-->
	                        <p><c:out value="${comment.message}"/></p>
	                        <p style="float:right;"><c:out value="${comment.modificationTime}"/></p>
	                        <a href="/comment/update/${comment.id}""><c:out value="Modify"/></a>
	                        <a href="/comment/delete/${comment.id}"><c:out value="Delete"/></a>
	                    </div>
	                </c:forEach>
	            </c:otherwise>
	        </c:choose>
	        <div>
	        	<a href="/comment/add/${story.id}" id="add-comment-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
	    	</div>
	    </div>
	    <c:choose>
            <c:when test="${story.status != StoryStatus.DONE}">
            </c:when>
            <c:otherwise>
            	<div class="well page-content content" style="width:50%;float: right;">
            	<h2>Story statistics</h2>
				<p>This is a progress breakdown of this story:</p>
	            <table style="width:100%">
				  <tr>
				    <th>To Do phase</th>
				    <th>Development phase</th> 
				    <th>Testing phase</th>
				  </tr>
				  <tr>
				    <td><c:out value="${story.todoStatusTime}"/></td>
				    <td><c:out value="${story.progressStatusTime}"/></td>
				    <td><c:out value="${story.testingStatusTime}"/></td>
				  </tr>
				</table>
				<div>
            </c:otherwise>
	    </c:choose>
        
    </div>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-story-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-story-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>