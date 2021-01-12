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
    <h1><spring:message code="label.user.list.page.name"/></h1>
    <div>
        <a href="/scrumsapientia/user/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.user.link"/></a>
    </div>
            <form:form action="/scrumsapientia/user/sort" commandName="user" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="user-title"><spring:message code="label.user.name"/>:</label>

                <div class="controls">
                    <select id="user-title" name="sort">
                    	<option value="1">Name asc</option>
                    	<option value="2">Name desc</option>
                    	<option value="3">Type asc</option>
                    	<option value="4">Type desc</option>
                    </select>
                </div>
                <button id="add-user-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.sort.user.button"/></button>
            </div>
        </form:form>
    <div id="user-list" class="page-content">
        <c:choose>
            <c:when test="${empty users}">
                <p><spring:message code="label.user.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ users}" var="user">
                    <div class="well well-small">
                        <a href="/user/${user.id}"><c:out value="${user.name}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>