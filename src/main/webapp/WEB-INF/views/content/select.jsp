<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/CSS.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<style>
	.select{
		width: 50%;
		height: 50%;
		margin: auto;
		display: flex;
	}

</style>
<div class="select">
	<table id="table">
		<tr id="title" >
			<td>번호</td>
			<td>
				${vo.idx}
				<input type="hidden" value="${vo.idx}" id="idx"/>
			</td>
			<td>책 제목</td>
			<td>${vo.bname}</td>
		</tr>
		<tr id="title">

			<td>저자</td>
			<td><a>${vo.auth}</a></td>
			<td>출판사</td>
			<td>${vo.publisher}</td>
		</tr>
		<tr id="title">

			<td>가격</td>
			<td><fmt:formatNumber type="currency" value="${vo.price }" pattern="###,###" />원</td>
			<td>대여가능여부</td>
			<td>${vo.rental == 1 ? "대여중" : "대여 가능"}</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<c:if test="${vo.rental == 0}">
					<input type="button" value="대여하기" id="rental"    class="bBase bStyle1">
				</c:if>
				<c:if test="${vo.rental == 1}">
					대여중
				</c:if>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript" src="./script/ajax.js"></script>