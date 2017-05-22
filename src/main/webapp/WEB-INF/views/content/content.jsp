<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/CSS.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<c:set var="value" value="0"/>


<table id="table">
	<tr>
		<td colspan="6" align="right">
			<button id="myBtn"  class="bBase bStyle1">write</button>
		</td>
	</tr>
	<tr id="title">
		<td width="50px">번호 </td>	
		<td width="500px">책 제목</td>	
		<td width="200px">저자</td>	
		<td>출판사</td>	
		<td>가격</td>	
		<td width="150px">대여가능 여부</td>	
	</tr>
 	<c:forEach var="vo" items="${list.list}">
		<c:set var="value" value="${value + 1}"/>
		<tr id="able">
		 	<td>
				${vo.idx}
			</td>
			<td>
				${vo.bname}
			</td>
			<td >
				${vo.auth}
			</td>
			<td >
				${vo.publisher}
			</td>
			<td align="right">
				<fmt:formatNumber type="currency" value="${vo.price }" pattern="###,###" />원
			</td>
			<td>		
				${vo.rental == 1 ? "대여중" : "대여 가능"}
			</td> 
		</tr>
	</c:forEach> 
 	<c:if test="${value != 10}">
		 <c:forEach step="1" begin="${value}" end="10">
		 	<tr id="ta">
		 		<td></td>
		 		<td></td>
		 		<td></td>
		 		<td></td>
		 		<td></td>
		 		<td></td>
		 	</tr>
		 </c:forEach>
	</c:if> 
	<tr id="searchTr">
		<td colspan="6">
			<form name="itemForm" id="itemForm" method="post">
				<input type="text" size="30" id="search"> 
				<input type="button" id="itemSearch" value="검색">
			</form>
			
		</td>
	</tr>
	<tr id="page">
		<td colspan="6"><c:if test="${list.startPage > 1}">
				<input type="button" value="start"	onclick="location.href='?page=1&item=${item}'" />
				<input type="button" value="&#060;&#060;"onclick="location.href='?page=${list.startPage-1}&item=${item}'" />
			</c:if> <c:if test="${list.startPage <= 1}">
				<input type="button" value="start" disabled="disabled" />
				<input type="button" value="&#060;&#060;" disabled="disabled" />
			</c:if> <!-- currentPage가 1보다 크면 이전 페이지가 있다. --> 
			<c:if test="${list.currentPage > 1}">
				<input type="button" value="&#060;"	onclick="location.href='?page=${list.currentPage-1}&item=${item}'" />
			</c:if> 
			<c:if test="${list.currentPage <= 1}">
				<input type="button" value="&#060;" disabled="disabled" />
			</c:if> <!-- 10페이지 단위로 페이지 이동 하이퍼링크를 출력한다. --> 
			
			<c:forEach var="i" begin="${list.startPage}" end="${list.endPage}" step="1">
				<c:if test="${list.currentPage == i}">
					<input type="button" value="${i}" class="button1" disabled="disabled">
				</c:if>
				<c:if test="${list.currentPage != i}">
					<input type="button" value="${i}" onclick="location.href='?page=${i}&item=${item}'" class="button">
				</c:if>
			</c:forEach> <!-- currentPage가 totalPage 보다 작으면 다음 페이지가 있다. --> 
			
			<c:if test="${list.currentPage < list.totalPage}">
				<input type="button" value="&#62;"	onclick="location.href='?page=${list.currentPage+1}&item=${item}'" />
			</c:if> 
			<c:if test="${list.currentPage >= list.totalPage}">
				<input type="button" value="&#62;" disabled="disabled" />
			</c:if> <!-- endPage가 totalPage 보다 작으면 다음 10 페이지가 있다. --> 
			<c:if test="${list.endPage < list.totalPage}">
				<input type="button" value="&#62;&#62;"	onclick="location.href='?page=${list.endPage+1}&item=${item}'" />
				<input type="button" value="end" onclick="location.href='?page=${list.totalPage}&item=${item}'" />
			</c:if> <c:if test="${list.endPage >= list.totalPage}">
				<input type="button" value="&#62;&#62;" disabled="disabled" />
				<input type="button" value="end" disabled="disabled" />
			</c:if>
		</td>
	</tr>
</table>
<script type="text/javascript" src="./script/ajax.js"></script>
