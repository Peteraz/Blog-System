const upload=function(resultFiles,insertImgFn){
			let file=new FormData();
			for(let i=0;i<resultFiles.length;i++){
				file.append("file",resultFiles[i]);
			}
			$.ajax({
				url:'http://localhost:7090/consumer/FileUpload',
				type: "POST",
				data: file,
				async: false,
				cache: false,
				processData: false,
				contentType: false,
				dataType: "json",
				success:function(result){
					if(result.errno.toString()=="0"){
						for(let j=0;j<result.data.length;j++){
							insertImgFn(result.data[j],"picture"+j);
						}
					}else{
						alert(result.msg);
						return;
					}
				}
			});
}

$('#login').click(function (result){
	let account=$("#account").val();
	let password=$("#password").val();
	$.ajax({
		url:"http://localhost:7090/consumer/Login",
		data:{account:account,password:password},
		type:"POST",
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="0"){

			}else{
				alert(result.msg);
				return;
			}
		}
	});
});