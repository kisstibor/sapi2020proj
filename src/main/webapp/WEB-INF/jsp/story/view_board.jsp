<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.view.js"></script>
    <style>
.card {
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  transition: 0.3s;
  width: 100%;
  position: relative;
}

.card:hover {
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}

.container {
  padding: 2px 16px;
  width: 400px;
  
}

td {
    padding: 40px;
}}

.float-right {
	float: right;
    
}
.lefty  {
	float: left;
    
}

table {
	display: table;
}
table tr {
	display: table-cell;
}
table tr td {
	display: block;
}

</style>
</head>
<body>
   
   <h2>Scrum Board</h2>
   
   <a href="/board/add">Add new task!</a>

<table>

<tr>

<th style="width: 200px!important">To Do</th>

 <c:forEach items="${tasks}" var="task">
 <c:if test="${task.state=='todo'}">
                    <td>
							    
							  <div class="card">
							  <div class="container">
							    <h4><b><a href="/board/show/${task.id}">${task.title}</a></b></h4> 
							    <p>${task.description}</p>
							    <a class="float-right" style="float:right;" href="/board/migrate/r/${task.id}">&gt;&gt;</a>
							    
							
							  </div>
							  </div>
    
					</td>
  </c:if>
  </c:forEach>

    
  </tr> 
  <tr>
  <th>In Progress</th> 
   
    <c:forEach items="${tasks}" var="task">
 	<c:if test="${task.state=='inprogress'}">
                    <td>
							    
							  <div class="card">
							  <div class="container">
							  <h4><b><a href="/board/show/${task.id}">${task.title}</a></b></h4> 
							    <p>${task.description}</p>
							    <a class="float-right" style="float:right;" href="/board/migrate/r/${task.id}">&gt;&gt;</a>
							    <a class="lefty" href="/board/migrate/l/${task.id}">&lt;&lt;</a>
							
							  </div>
							  </div>
    
					</td>
  </c:if>
  </c:forEach>
   
  </tr>
  <tr>
  <th>Done</th> 
    
        <c:forEach items="${tasks}" var="task">
 	<c:if test="${task.state=='done'}">
                    <td>
							    
							  <div class="card">
							  <div class="container">
							  <h4><b><a href="/board/show/${task.id}">${task.title}</a></b></h4> 
							    <p>${task.description}</p>
							    
							    <a class="lefty" href="/board/migrate/l/${task.id}">&lt;&lt;</a>
							
							  </div>
							  </div>
    
					</td>
  </c:if>
  </c:forEach>
    
  </tr>
</table>
   
</body>
</html>