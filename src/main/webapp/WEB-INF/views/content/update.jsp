<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
	.select{
		width: 50%;
		height: 50%;
		margin: auto;
		display: flex;
	}
</style>
<div class="select">
  <form action="up" onsubmit="return formCheck(this)" method="post">
	<table id="table">
		<tr id="title" >
			<td>번호</td>
			<td>
				${vo.idx}
				<input type="hidden" value="${vo.idx}" name="idx">
			</td>
			<td>책 제목</td>
			<td><input type="text" value="${vo.bname}" name="bName"></td>
		</tr>
		<tr id="title">

			<td>저자</td>
			<td><input type="text" value="${vo.auth}" name="auth"></td>
			<td>출판사</td>
			<td><input type="text" value="${vo.publisher}" name="publisher"></td>
		</tr>
		<tr id="title">

			<td>가격</td>
			<td><input type="text" value="${vo.price}" name="price">원</td>
			<td>대여가능여부</td>
			<td>${vo.rental == 1 ? "대여중" : "대여 가능"}</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
					<input type="submit" value="수정하기"    class="bBase bStyle1">
			</td>
		</tr>
	</table>
	</form>
</div>
