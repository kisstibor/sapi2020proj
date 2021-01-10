<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>Bug</title>
	<script type="text/javascript" src="/static/js/bug.view.js"></script>
</head>
<body>
	<div id="bug-id" class="hidden">${bug.id}</div>
    <h1>Bug <c:out value="${bug.id}"/></h1>
    <div class="well page-content">
        <h2 id="bug-title"><c:out value="${bug.title}"/></h2>
        <div>
            <p><c:out value="${bug.description}"/></p>
        </div>
        <div class="action-buttons">
         	<a href="/bug/update/${bug.id}" class="btn btn-primary">Update</a>
            <a id="delete-bug-link" class="btn btn-primary">Delete</a>
        </div>
    </div>
    <script id="template-delete-bug-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-bug-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3>Delete bug</h3>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete this bug?</p>
            </div>
            <div class="modal-footer">
                <a id="cancel-bug-button" href="#" class="btn">Cancel</a>
                <a id="delete-bug-button" href="#" class="btn btn-primary">Delete</a>
            </div>
        </div>
    </script>
</body>
</html>