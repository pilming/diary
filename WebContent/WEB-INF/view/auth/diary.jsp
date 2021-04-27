<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>diary</title>
</head>
<body>
	<h1>DDAY List</h1>
	<div>
		<table border = "1">
			<tr>
				<th>todoDate</th>
				<th>todoTitle</th>
				<th>dday</th>
			</tr>
			<c:forEach var = "m" items="${diaryMap.ddayList}">
				<tr>
					<td>${m.todoDate}</td>
					<td>
						<a href="${pageContext.request.contextPath}/auth/todoOne?todoNo=${m.todoNo}">
							${m.todoTitle}
						</a>
					</td>
					<td>-${m.dday}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<c:set var="totalCell" value="${diaryMap.startBlank + diaryMap.endDay + diaryMap.endBlank}"></c:set>
	<div>totalCell : ${totalCell}</div>
	
	<h1>
		<a href="${pageContext.request.contextPath}/auth/diary?targetYear=${diaryMap.targetYear}&targetMonth=${diaryMap.targetMonth-1}">이전달</a>
		${diaryMap.targetYear}년 ${diaryMap.targetMonth + 1}월
		<a href="${pageContext.request.contextPath}/auth/diary?targetYear=${diaryMap.targetYear}&targetMonth=${diaryMap.targetMonth+1}">다음달</a>
	</h1>
	<table border="1" width="90%">
		<tr>
			<td>일</td>
			<td>월</td>
			<td>화</td>
			<td>수</td>
			<td>목</td>
			<td>금</td>
			<td>토</td>
		</tr>
		<tr>
			<c:forEach var="i" begin="1" end="${totalCell}" step="1">
				<c:set var="num" value="${i-diaryMap.startBlank}"></c:set>
				<td>
					<c:if test="${num <= 0}">
						<!-- 이전달의 후반 날들 num이 음수기때문에 +연산 -->
						<a href="${pageContext.request.contextPath}/auth/addTodo?year=${diaryMap.preTargetYear}&month=${diaryMap.preTargetMonth + 1}&day=${diaryMap.preMonthEndDay+num}">
							<div style="background-color: #dcdcdc" >${diaryMap.preMonthEndDay+num}</div>
						</a>
						<div>
	                        <c:forEach var="todo" items="${diaryMap.preMonthTodoList}">
	                           <c:if test="${todo.todoDate == diaryMap.preMonthEndDay+num}">
	                              <div style="background-color: ${todo.todoFontColor}">
	                              	<a href="${pageContext.request.contextPath}/auth/todoOne?todoNo=${todo.todoNo}">${todo.todoTitle}...</a>
	                              </div>
	                           </c:if>
                        	</c:forEach>
                   		</div>
					</c:if>
					<!-- targetMonth todolist보여주기 -->
					<c:if test="${num > 0 && num <= diaryMap.endDay}">
						<a href="${pageContext.request.contextPath}/auth/addTodo?year=${diaryMap.targetYear}&month=${diaryMap.targetMonth+1}&day=${num}">
							<div>${num}</div>
							<div>
		                        <c:forEach var="todo" items="${diaryMap.todoList}">
		                           <c:if test="${todo.todoDate == num}">
		                              <div style="background-color: ${todo.todoFontColor}">
		                              	<a href="${pageContext.request.contextPath}/auth/todoOne?todoNo=${todo.todoNo}">${todo.todoTitle}...</a>
		                              </div>
		                              <!-- todoOne 만들기 수정,삭제 -->
		                           </c:if>
	                        	</c:forEach>
                     		</div>
						</a>
					</c:if>
					
					<c:if test="${num > diaryMap.endDay}">
						<!-- 다음달의 초반 날들 -->
						<a href="${pageContext.request.contextPath}/auth/addTodo?year=${diaryMap.postTargetYear}&month=${diaryMap.postTargetMonth + 1}&day=${num - diaryMap.endDay}">
							<div style="background-color: #dcdcdc" >${num - diaryMap.endDay}</div>
						</a>
						<div>
	                        <c:forEach var="todo" items="${diaryMap.postMonthTodoList}">
	                           <c:if test="${todo.todoDate == (num - diaryMap.endDay)}">
	                              <div style="background-color: ${todo.todoFontColor}">
	                              	<a href="${pageContext.request.contextPath}/auth/todoOne?todoNo=${todo.todoNo}">${todo.todoTitle}...</a>
	                              </div>
	                           </c:if>
                        	</c:forEach>
                   		</div>
					</c:if>
				</td>
				<c:if test="${i%7==0}">
					</tr><tr>
				</c:if>
			</c:forEach>
		</tr>
	</table>
</body>
</html>