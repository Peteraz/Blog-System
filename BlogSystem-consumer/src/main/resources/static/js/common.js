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
		"name":$("#user_name").val(),
		"email":$("#email").val(),
		"birthday":$("#birthday").val(),
		"sex":$("input[name='sex']:checked").val(),
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

$('#birthday').click(function () {
	//最早是100年前,最小是1年前的日期
	let date=new Date();
    let year=date.getFullYear()-1;
	let month=date.getMonth()+1;
	let day=date.getDate();
	if(month<10){
		month="0"+month;
	}
	if(day<10){
		day="0"+day;
	}
	let maxDay=year+"-"+month+"-"+day
	$('#birthday').attr('max',maxDay);
	year=date.getFullYear()-100;
	maxDay=year+"-01-01"
	$('#birthday').attr('min',maxDay);
});

$('#register-form').validate({
	rules:{
		account:{
			required:true,
			maxlength:30
		},
		password:{
			required:true,
			maxlength:60
		},
		user_name:{
			required:true,
			maxlength:20
		},
		email:{
			required:true,
			email:true
		},
		birthday:"required",
		sex:"required",
		phone_number:{
			required:true,
			maxlength:15
		}
	},
	messages:{
		account:{
			required:"账号不能空!",
			maxlength: $.validator.format( "最多可以输入30个字符" ),
		},
		password:{
			required:"密码不能空!",
			maxlength: $.validator.format( "最多可以输入60个字符" ),
		},
		user_name:{
			required:"名字不能空!",
			maxlength: $.validator.format( "最多可以输入20个字符" ),
		},
		email:{
			required:"邮箱不能空!",
			email:"请输入正确的邮箱!"
		},
		birthday:"不能空！",
		sex:"不能空！",
		phone_number:{
			required:"电话号码不能空!",
			maxlength: $.validator.format( "最多可以输入15个字符" )
		}
	}
});