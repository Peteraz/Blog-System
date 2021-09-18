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

$('#logout').click(function (result){
	$.ajax({
		url:"http://localhost:7090/consumer/Logout",
		type:"POST",
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				$(location).attr("href","http://localhost:7090/consumer/getLogin");
			}else{
				alert(result.msg);
				return;
			}
		}
	});
});

$('#register').click(function (result){
	let map={
		"account":$("#account").val(),
		"password":$("#password").val(),
		"name":$("#name").val(),
		"email":$("#email").val(),
		"birthday":$("#birthday").val(),
		"sex":$("input[name='radios-sex']:checked").val(),
		"phone_number":$("#phone_number").val()
	}
	$.ajax({
		url:"http://localhost:7090/consumer/Register",
		type:"POST",
		contentType: 'application/json;charset=utf-8',
		data: JSON.stringify(map),
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				alert("注册成功！");
				$(location).attr("href","http://localhost:7090/consumer/getLogin");
			}else{
				alert(result.msg);
				return;
			}
		}
	});
});

$('register-form').validate({
	rules:{
		account:"required",
		password:"required",
		name:"required",
		email:"required",
		birthday:"required",
		sex:"required",
		phone_number:"required"
	},
	messages:{
		account:"不能空！",
		password:"不能空！",
		name:"不能空！",
		email:"不能空！",
		birthday:"不能空！",
		sex:"不能空！",
		phone_number:"不能空！"
	}
})