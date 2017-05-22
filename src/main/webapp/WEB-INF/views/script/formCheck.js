	function formCheck(obj){
		if(!obj.bName.value || obj.bName.value.trim().length == 0) {//입력이 됬나 check
			alert("책제목을 입력하시요");
			obj.bName.value="";
			obj.bName.focus();
			return false;
		}
		if(!obj.auth.value || obj.auth.value.trim().length == 0) {//입력이 됬나 check
			alert("저자를 입력하시요");
			obj.auth.value="";
			obj.auth.focus();
			return false;
		}
		if(!obj.publisher.value || obj.publisher.value.trim().length == 0) {//입력이 됬나 check
			alert("출판사를 입력하시요");
			obj.publisher.value="";
			obj.publisher.focus();
			return false;
		}
		if(!obj.price.value || obj.price.value.trim().length == 0) {//입력이 됬나 check
			alert("가격을 입력하시요");
			obj.price.value="";
			obj.price.focus();
			return false;
		}else if(isNaN(obj.price.value)){
			alert("가격을 숫자로 입력하시요");
			obj.price.value="";
			obj.price.focus();
			return false;
		}
		return true;
	}
