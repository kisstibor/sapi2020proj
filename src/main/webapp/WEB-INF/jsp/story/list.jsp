<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.story.list.page.title"/></h1>
    <div>
		<form:form action="/stories" commandName="storyList" method="GET"
			enctype="utf8">
			<div class="input-append">
				<spring:message code="placeholder.filter.title" var="placeholder"/>
				<form:input id="query-text" path="query" cssClass="span2" placeholder="${placeholder}" />
				<button id="filter-button" type="submit" class="btn">
					<spring:message code="label.filter.button" />
				</button>
			</div>
		</form:form>
	</div>
    <div>
        <a href="/story/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
    </div>
    <div id="story-list" class="page-content">
        <c:choose>
            <c:when test="${empty storyList.stories}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ storyList.stories}" var="story">
                    <div class="well well-small">
                        <a href="/story/${story.id}"><c:out value="${story.title}"/></a>
						<c:if test="${not empty story.user}">
							<br />
							<div>
								<spring:message code="label.story.assignedTo" />
								:&nbsp;
								<c:out value="${story.user.username}" />
							</div>
						</c:if>
					</div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>