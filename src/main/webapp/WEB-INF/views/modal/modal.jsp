<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


 
<div id="myModal" class="modal">

  <!-- Modal content -->
  <div class="modal-content">
    <div class="modal-header">
      <span class="close">&times;</span>
      <h2 align="center">도서 추가</h2>
    </div>
    <form action="/insert.do" onsubmit="return formCheck(this)">
	    <div class="modal-body">
	     	<table style="height: 226px; width: 100%; ">
				<tr>
					<td>책 제목</td>
					<td><input type="text" name="bName"></td>
		
				</tr>
				<tr>
					<td>저자</td>
					<td><input type="text" name="auth"></td>
				</tr>
				<tr>
					<td>출판사</td>
					<td><input type="text" name="publisher"></td>
				</tr>
				<tr>
					<td>가격</td>
					<td><input type="text" name="price"></td>
				</tr>
			</table>
	    </div>
	    <div class="modal-footer" align="right">
	 	   <button id="myBtn"  class="bBase bStyle2">write</button>
	    </div>
    </form>
  </div>

</div>
<script type="text/javascript" src="script/modal.js"></script>

<script>
	function formCheck(obj){
		if(!obj.bName.value || obj.bName.value.trim().length == 0) {//입력이 됬나 check
			alert("책제목을 입력하시요");
			obj.bName.value="";
			obj.bName.focus();
			return false;
		}
	}

</script>