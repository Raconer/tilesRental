function formCheck(obj){
	
	
	if(!obj.bName.value || obj.bName.value.trim().length == 0){
		alert("책 이름을입력하시요");	
		obj.bName.focus();
		return false;
	}else if(!obj.auth.value || obj.auth.value.trim().length == 0){
		alert("저자를 입력하시요");	
		obj.auth.focus();
		return false;
	}else if(!obj.publisher.value || obj.publisher.value.trim().length == 0){
		alert("출판사를 입력하시요");	
		obj.publisher.focus();
		return false;
	}else if(!obj.price.value || obj.price.value.trim().length == 0){
		
			alert("가격 을입력하시요");	
			obj.price.focus();	
		
		return false;
	}else if(isNaN(obj.price.value)){
		alert("가격을 숫자로 입력하시요");	
		obj.price.focus();
		return false;
	}
	
	return true;
}