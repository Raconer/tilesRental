<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/CSS.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<c:set var="value" value="0"/>

<c:choose>
    <c:when test="${state eq 'delete' || state eq 'update'}">
        <c:set var="td" value="7"/>
    </c:when>
    <c:otherwise>
       <c:set var="td" value="6"/>
    </c:otherwise>
</c:choose>

<table id="table">
	
	<tr>
		<td colspan="${td}" align="right">
		<!-- 	<button id="myBtn"  class="bBase bStyle1">write</button> -->
			 ( 총 ${list.totalPage}Page 중 ${list.currentPage}Page )
		</td>
	</tr>
	<tr id="title">
		<td width="50px">번호 </td>	
		<td width="500px">책 제목</td>	
		<td width="200px">저자</td>	
		<td>출판사</td>	
		<td>가격</td>	
		<td width="150px">대여가능 여부</td>	
		<c:if test="${td == 7}">
			<td width="50px">
		
			</td>
		</c:if>
	</tr>
 	<c:forEach var="vo" items="${list.list}">
		<c:set var="value" value="${value + 1}"/>
		<tr id="able">
		 	<td>
				${vo.idx}
				<input type="hidden" value="${vo.idx}" id="idx"/>
			</td>
			<td  onclick="location.href='selectByIdx?idx=${vo.idx}'">
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
			<c:if test="${td == 7}">
				<td>
			       <button value="${vo.idx}" <c:if test="${state eq 'delete' }">id="dBtn"</c:if>
			        <c:if test="${state eq 'update' }"> onclick="location.href='uBtn?idx=${vo.idx}'" </c:if> >${state}</button>
				</td>
			</c:if>
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
		 		<c:if test="${td == 7}">
					<td></td>
				</c:if>
		 	</tr>
		 </c:forEach>
	</c:if> 
	<tr id="searchTr">
		<td colspan="${td}">
			<form action="index" method="get">
				<input type="text" size="30" name="item"> 
				<input type="submit" id="itemSearch" value="검색">
			</form>
			
		</td>
	</tr>
	<tr id="page">
		<td colspan="${td}"><c:if test="${list.startPage > 1}">
				<input type="button" value="start"	onclick="location.href='?page=1&item=${item}&state=${state}'" />
				<input type="button" value="&#060;&#060;"onclick="location.href='?page=${list.startPage-1}&item=${item}&state=${state}'" />
			</c:if> <c:if test="${list.startPage <= 1}">
				<input type="button" value="start" disabled="disabled" />
				<input type="button" value="&#060;&#060;" disabled="disabled" />
			</c:if> <!-- currentPage가 1보다 크면 이전 페이지가 있다. --> 
			<c:if test="${list.currentPage > 1}">
				<input type="button" value="&#060;"	onclick="location.href='?page=${list.currentPage-1}&item=${item}&state=${state}'" />
			</c:if> 
			<c:if test="${list.currentPage <= 1}">
				<input type="button" value="&#060;" disabled="disabled" />
			</c:if> <!-- 10페이지 단위로 페이지 이동 하이퍼링크를 출력한다. --> 
			
			<c:forEach var="i" begin="${list.startPage}" end="${list.endPage}" step="1">
				<c:if test="${list.currentPage == i}">
					<input type="button" value="${i}" class="button1" disabled="disabled">
				</c:if>
				<c:if test="${list.currentPage != i}">
					<input type="button" value="${i}" onclick="location.href='?page=${i}&item=${item}&state=${state}'" class="button">
				</c:if>
			</c:forEach> <!-- currentPage가 totalPage 보다 작으면 다음 페이지가 있다. --> 
			
			<c:if test="${list.currentPage < list.totalPage}">
				<input type="button" value="&#62;"	onclick="location.href='?page=${list.currentPage+1}&item=${item}&state=${state}'" />
			</c:if> 
			<c:if test="${list.currentPage >= list.totalPage}">
				<input type="button" value="&#62;" disabled="disabled" />
			</c:if> <!-- endPage가 totalPage 보다 작으면 다음 10 페이지가 있다. --> 
			<c:if test="${list.endPage < list.totalPage}">
				<input type="button" value="&#62;&#62;"	onclick="location.href='?page=${list.endPage+1}&item=${item}&state=${state}'" />
				<input type="button" value="end" onclick="location.href='?page=${list.totalPage}&item=${item}&state=${state}'" />
			</c:if> <c:if test="${list.endPage >= list.totalPage}">
				<input type="button" value="&#62;&#62;" disabled="disabled" />
				<input type="button" value="end" disabled="disabled" />
			</c:if>
		</td>
	</tr>
</table>
<script type="text/javascript" src="./script/ajax.js"></script>

