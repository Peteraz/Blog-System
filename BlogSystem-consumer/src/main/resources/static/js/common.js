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
	if(!account){
		alert("请输入账号！");
	} else if(!password){
		alert("请输入密码！");
	}else{
		$.ajax({
			url:"http://localhost:7090/consumer/Login",
			data:{account:account,password:password},
			type:"POST",
			async: false,
			cache: false,
			dataType: "json",
			success:function(result){
				if(result.status=="1"){
					$(location).attr("href","http://localhost:7090/consumer/getIndex");
				}else{
					alert(result.msg);
					return;
				}
			}
		});
	}
});