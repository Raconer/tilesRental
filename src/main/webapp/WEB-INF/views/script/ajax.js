$(document).ready(function(){
    $("#itemSearch").click(function(){
    	var search = $("#search").val();
    	$.ajax({
				type : "post",
				url : "itemSearch",
				dataType : "JSON",
				data : ({"keyword" : "kry","search" : search}),
				success : function(data) {
					
					alert("성공"+data.idx+" 데헷 +"+search);
	
					var values = [];
					values = data.list;
					$("[id=able]").remove();
					$.each(values.reverse(), function( index, value){	
						$('#title').after("<tr id='able'>" +
												"<td>"+value.idx+"</td>" +
												"<td>"+value.bname+"</td>" +
												"<td>"+value.auth+"</td>" +
												"<td>"+value.publisher+"</td>" +
												"<td align='right'>"+Number(value.price).toLocaleString('en') +"원"+"</td>" +
												"<td>"+((value.rental == 1)?"대여중":"대여 가능")+"</td>" +
												"</tr>");					
					});
				
				},error : function(data){
					alert("에러"+data+" 데헷");
				},complete : function(data){
					alert("컴플릿"+data+" 데헷");
				}
  			});
	});
});