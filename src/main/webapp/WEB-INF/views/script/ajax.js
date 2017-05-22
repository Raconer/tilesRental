$(document).ready(function(){

	/*$('#itemSearch').click(function(){*/ 
	$(document).on("click","#itemSearch",function(){ 
    	var search = $("#search").val(); 
 		var page= 1;
 		var p = $(this).attr('value');
 		if($.isNumeric(p)){
			page = p;	
		}
    	$.ajax({
				type : "post",
				url : "itemSearch",
				dataType : "JSON",
				data : ({"keyword" : "kry","search" : search, "paging" : page}),
				success : function(data) {
					var values = [];
					var j = 0;
					values = data.list;
					alert(data.lista); 	
					$("[id=able]").remove();
					$("[id=ta]").remove();
					
					$.each(values.reverse(), function( index, value){	
						$('#title').after("<tr id='able'>" +
												"<td>"+value.idx+"</td>" +
												"<td>"+value.bname+"</td>" +
												"<td>"+value.auth+"</td>" +
												"<td>"+value.publisher+"</td>" +
												"<td align='right'>"+Number(value.price).toLocaleString('en') +"원"+"</td>" +
												"<td>"+((value.rental == 1)?"대여중":"대여 가능")+"</td>" +
											"</tr>");
						j++;
					});
				
					if(j != 9){
						for(j ; j<10;j++){
							$('#searchTr').before("<tr id='able'>" +
									"<td></td>" +
									"<td></td>" +
									"<td></td>" +
									"<td></td>" +
									"<td></td>" +
									"<td></td>" +
								"</tr>");
						}
					}
					
					
					$('#page').remove();
					var page = 0;
					var pB = "";
					if(data.startPage > 1){
						pB +="<button id='itemSearch' value='"+1+"'>start</button> " +
							"<button id='itemSearch' value='"+(data.startPage-1)+"'><<</button> ";
					}else if(data.startPage <= 1){
						pB += "<button id='itemSearch' disabled='disabled'>start</button> " +
							"<button id='itemSearch' disabled='disabled'><<</button> ";
					}
					if(data.currentPage > 1){
					 	pB += "<button id='itemSearch' value='"+(data.currentPage-1)+"'><</button> ";
					}else if(data.currentPage <= 1){
						pB += "<button id='itemSearch'  disabled='disabled' ><</button> ";
					} 
					for(var i =data.startPage; i<= data.endPage;i++){
						if(data.currentPage == i){
							pB += "<input type='button' value='"+i+"' class='button1' disabled='disabled'> "	;	
						}else if(data.currentPage != i){
							pB += "<input type='button' id='itemSearch'  value='"+i+"' class='button'> ";
						}
					}					
					
					if(data.currentPage < data.totalPage){
						pB += "<button id='itemSearch' value='"+(data.currentPage+1)+"'>></button> ";
					}else if(data.currentPage >= data.totalPage){
						pB +="<button id='itemSearch'  disabled='disabled' >></button> " ;
					}
					if(data.endPage < data.totalPage){
						pB +="<button id='itemSearch' value='"+(data.endtPage+1)+"'>>></button> " +
						"<button id='itemSearch' value='"+(data.totalPage)+"'>end</button>";
					}else if(data.endPage >= data.totalPage){
						pB += "<button id='itemSearch' disabled='disabled'>>></button> " +
						"<button id='itemSearch' disabled='disabled'>end</button>" ;
					}
						
					$('#searchTr').after("<tr id='page'>" +
											"<td colspan='6'>"+
											pB+
											"</td>"+
										"</tr>"
					);
				
				},error : function(data){
					alert("에러"+data);
				},complete : function(data){
					alert("컴플릿"+data);
				}
  			});
	});
});