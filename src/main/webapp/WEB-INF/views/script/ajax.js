$(document).ready(function(){
	$(document).on("click","#rental",function(){ 
		var idx = $('#idx').val();
		$.ajax({
			type : "post",
			url : "rental",
			dataType : "JSON",
			data : ({"idx" : idx}),
			success : function(data) {
				window.location.reload();
				alert("대여 성공");
			},error : function(data){
				alert("대여 실패");
			},complete : function(data){
			}
		});
	});
	$(document).on("click","#bReturn",function(){ 
		var idx = $(this).attr('value');
		$.ajax({
			type : "post",
			url : "bReturn",
			dataType : "JSON",
			data : ({"idx" : idx}),
			success : function(data) {
				if(data == true){
					window.location.reload();
					alert("반납 성공");
				}
			},error : function(data){
				alert("반납 실패");
			},complete : function(data){
			}
		});
	});
	$(document).on("click","#dBtn",function(){ 
		var idx = $(this).attr('value');
		$.ajax({
			type : "post",
			url : "dBtn",
			dataType : "JSON",
			data : ({"idx" : idx}),
			success : function(data) {
				if(data == true){
					window.location.reload();
					alert("삭제 성공");
				}else{
					alert("대여중인 항목은 삭제 할수없습니다.");
				}
			},error : function(data){
				alert("삭제 실패");
			},complete : function(data){
			}
		});
	});
});