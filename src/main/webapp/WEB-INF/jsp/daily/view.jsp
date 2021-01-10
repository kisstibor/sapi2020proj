<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title></title>
<script type="text/javascript" src="/static/js/daily.view.js"></script>
</head>
<body>
	<div id="daily-id" class="hidden">${daily.id}</div>
	<h1>
		<spring:message code="label.daily.view.page.title" />
	</h1>
	<div class="well page-content">
		<h2 id="daily-title">
			<c:out value="${daily.title}" />
		</h2>
		<div>
			<p id=daily-duration>
				<c:out value="Duration: ${daily.duration}" />
			</p>
		</div>
		<div>
			<p id=daily-datee>
				<c:out value="Date: ${daily.datee}" />
			</p>
		</div>
		<div>
			<p>
				<c:out value="Description: ${daily.description}" />
			</p>
		</div>
		<div class="action-buttons">
			<a href="/daily/update/${daily.id}" class="btn btn-primary" id="update-button"><spring:message
					code="label.update.daily.link" /></a> <a id="delete-daily-link"
				class="btn btn-primary" style="background: #DC143C"><spring:message
					code="label.delete.daily.link"/></a>
		</div>
	</div>

	<script id="template-delete-daily-confirmation-dialog"
		type="text/x-handlebars-template">
        <div id="delete-daily-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.daily.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.daily.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-daily-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-daily-button" href="#" class="btn btn-primary"><spring:message code="label.delete.daily.button"/></a>
            </div>
        </div>
    </script>


</body>
</html>