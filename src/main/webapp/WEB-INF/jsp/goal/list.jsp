<!DOCTYPE html>
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
	<title></title>
    <script type="text/javascript" src="/static/js/goal.form.js"></script>
</head>
<body>
	<h1>
		<spring:message code="label.goal.list.page.title" />
	</h1>
	<div>
		<a href="/goal/add" id="add-goal-button" class="btn btn-primary"><spring:message
				code="label.add.goal.link" /></a>
	</div>
	<div id="goal-list" class="page-content">
		<c:choose>
			<c:when test="${empty goals}">
				<p>
					<spring:message code="label.goal.list.empty" />
				</p>
			</c:when>
			<c:otherwise>
				<c:forEach items="${ goals }" var="goal">
					<div class="well well-small">
						<a href="/goal/${goal.id}"><c:out value="${goal.goal}" /></a>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>