const upload=function(resultFiles,insertImgFn){
			let file=new FormData();
			for(let i=0;i<resultFiles.length;i++){
				file.append("file",resultFiles[i]);
			}
			$.ajax({
				url:'http://localhost:9001/consumer/FileUpload',
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

$('#register').click(function (result){
	let state=$("#inputState").val().substr($("#inputState").val().indexOf("-")+1); //截取省份出来
	let data={
		"account":$("#account").val(),
		"password":$("#password").val(),
		"name":$("#user_name").val(),
		"email":$("#email").val(),
		"birthday":$("#birthday").val(),
		"sex":$("input[name='sex']:checked").val(),
		"phone_number":$("#phone_number").val(),
		"state":state,
		"city":$("#inputCity").val()
	}
	$.ajax({
		url:"http://localhost:9001/consumer/Register?token=123",
		type:"POST",
		contentType: 'application/json;charset=utf-8',
		data: JSON.stringify(data),
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				setTimeout("$(location).attr(\"href\",\"http://localhost:9001/consumer/getLogin\")",2000);
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
			}
		}
	});
});

$('#login').click(function (result){
	let account=$('#account').val();
	let password=$('#password').val();
	if(!account){
		alert("请输入账号！");
	} else if(!password){
		alert("请输入密码！");
	}else{
		$.ajax({
			url:"http://localhost:9001/consumer/Login",
			data:{account:account,password:password},
			type:"POST",
			async: false,
			cache: false,
			dataType: "json",
			success:function(result){
				if(result.status=="1"){
					$(location).attr("href","http://localhost:9001/consumer/getIndex");
				}else{
					let message = result.msg;
					let type = "warning";
					let duration = 2000;
					let ripple = "true";
					let dismissible = "true";
					let positionX = "center";
					let positionY = "top";
					window.notyf.open({
						type,
						message,
						ripple,
						dismissible,
						duration,
						position: {
							x: positionX,
							y: positionY
						}
					});
					return;
				}
			}
		});
	}
});

//判断是否第一次选择
let count=0;
const choose=function(){
	const state=["北京市","上海市","天津市","重庆市","河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省","江西省","山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省","云南省","陕西省","甘肃省","青海省","台湾省","香港特别行政区","澳门特别行政区","内蒙古自治区","广西壮族自治区","西藏自治区","宁夏回族自治区","新疆维吾尔自治区"];
	const city=[["东城区","西城区","西城区","朝阳区","丰台区","石景山区","海淀区","顺义区","通州区","大兴区","房山区","门头沟区","昌平区","平谷区","密云区","怀柔区","延庆区"],
		        ["黄浦区","徐汇区","长宁区","静安区","普陀区","虹口区","杨浦区","闵行区","宝山区","嘉定区","浦东新区","金山区","松江区","青浦区","奉贤区","崇明区"],
		        ["和平区","河东区","河西区","南开区","河北区","红桥区","东丽区","西青区","津南区","北辰区","武清区","宝坻区","滨海新区","宁河区","静海区","蓟州区"],
		        ["万州区","涪陵区","渝中区","大渡口区","江北区","沙坪坝区","九龙坡区","南岸区","北碚区","綦江区","大足区","渝北区","巴南区","黔江区","长寿区","江津区","合川区","永川区","南川区"],
		        ["石家庄市","唐山市","秦皇岛市","邯郸市","邢台市","保定市","张家口市","承德市","沧州市","廊坊市","衡水市"],
				["太原市","大同市","朔州市","忻州市","阳泉市","吕梁市","晋中市","长治市","晋城市","临汾市","运城市"],
				["沈阳市","大连市","鞍山市","抚顺市","本溪市","丹东市","锦州市","营口市","阜新市","辽阳市","盘锦市","铁岭市","朝阳市","葫芦岛市"],
				["长春市","吉林市","四平市","辽源市","通化市","白山市","松原市","白城市","延边朝鲜族自治州","长白山管委会"],
				["哈尔滨市","齐齐哈尔市","鸡西市","鹤岗市","双鸭山市","大庆市","伊春市","佳木斯市","七台河市","牡丹江市","黑河市","绥化市","大兴安岭地区"],
				["南京市","无锡市","徐州市","常州市","苏州市","南通市","连云港市","淮安市","盐城市","扬州市","镇江市","泰州市","宿迁市"],
				["杭州","宁波","温州","绍兴","湖州","嘉兴","金华","衢州","台州","丽水","舟山"],
				["合肥","芜湖","蚌埠","淮南","马鞍山","淮北","铜陵","安庆","黄山","阜阳","宿州","滁州","六安","宣城","池州","亳州"],
				["福州市","厦门市","漳州市","泉州市","三明市","莆田市","南平市","龙岩市","宁德市","平潭综合实验区"],
				["南昌","九江","上饶","抚州","宜春","吉安","赣州","景德镇","萍乡","新余","鹰潭"],
				["济南","青岛","淄博","枣庄","东营","烟台","潍坊","济宁","泰安","威海","日照","滨州","德州","聊城","临沂","菏泽"],
				["郑州市","开封市","洛阳市","平顶山市","安阳市","鹤壁市","新乡市","焦作市","濮阳市","许昌市","漯河市","三门峡市","商丘市","周口市","驻马店市","南阳市","信阳市","济源市"],
				["武汉市","黄石市","十堰市","宜昌市","襄阳市","鄂州市","荆门市","孝感市","荆州市","黄冈市","咸宁市","随州市","恩施土家族苗族自治州","仙桃市","潜江市","天门市","神农架林区"],
				["长沙市","株洲市","湘潭市","衡阳市","邵阳市","岳阳市","常德市","张家界市","益阳市","娄底市","郴州市","永州市","怀化市","湘西土家族苗族自治州"],
				["广州市","韶关市","深圳市","珠海市","汕头市","佛山市","江门市","湛江市","茂名市","肇庆市","惠州市","梅州市","汕尾市","河源市","阳江市","清远市","东莞市","中山市","潮州市","揭阳市","云浮市"],
				["海口市","三亚市","三沙市","儋州市","五指山市","文昌市","琼海市","万宁市","东方市","定安县","屯昌县","澄迈县","临高县","白沙黎族自治县","昌江黎族自治县","乐东黎族自治县","陵水黎族自治县","保亭黎族苗族自治县","琼中黎族苗族自治县"],
				["成都市","自贡市","攀枝花市","泸州市","德阳市","绵阳市","广元市","遂宁市","内江市","乐山市","南充市","眉山市","宜宾市","广安市","达州市","雅安市","巴中市","资阳市","阿坝藏族羌族自治州","甘孜藏族自治州","凉山彝族自治州"],
				["贵阳市","遵义市","六盘水市","安顺市","毕节市","铜仁市","黔东南苗族侗族自治州","黔南布依族苗族自治州","黔西南布依族苗族自治州"],
				["昆明市","曲靖市","玉溪市","昭通市","保山市","丽江市","普洱市","临沧市","德宏傣族景颇族自治州","怒江傈僳族自治州","迪庆藏族自治州","大理白族自治州","楚雄彝族自治州","红河哈尼族彝族自治州","文山壮族苗族自治州","西双版纳傣族自治州"],
				["西安市","宝鸡市","咸阳市","铜川市","渭南市","延安市","榆林市","汉中市","安康市","商洛市"],
				["兰州市","嘉峪关市","金昌市","白银市","天水市","武威市","张掖市","平凉市","酒泉市","庆阳市","定西市","陇南市","临夏回族自治州","甘南藏族自治州"],
				["西宁市","海东市","海北藏族自治州","黄南藏族自治州","海南藏族自治州","果洛藏族自治州","玉树藏族自治州","海西蒙古族藏族自治州"],
				["台北市","高雄市","基隆市","台中市","台南市","台中市","新竹市","嘉义市","新北市","桃园市","宜兰县","新竹县","苗栗县","彰化县","南投县","云林县","嘉义县","屏东县","台东县","花莲县","澎湖县"],
				["香港岛","九龙半岛","新界"],
				["澳门","氹仔","路环","路氹城"],
				["呼和浩特市","包头市","乌海市","赤峰市","通辽市","鄂尔多斯市","呼伦贝尔市","巴彦淖尔市","乌兰察布市","兴安盟","锡林郭勒盟","阿拉善盟"],
				["南宁市","柳州市","桂林市","梧州市","北海市","崇左市","来宾市","贺州市","玉林市","百色市","河池市","钦州市","防城港市","贵港市"],
				["拉萨市","日喀则市","昌都市","林芝市","山南市","那曲市","阿里地区"],
				["银川市","石嘴山市","吴忠市","固原市","中卫市"],
				["乌鲁木齐市","克拉玛依市","吐鲁番市","哈密市","阿克苏地区","喀什地区","和田地区","塔城地区","阿勒泰地区","昌吉回族自治州","博尔塔拉蒙古自治州","博尔塔拉蒙古自治州","克孜勒苏柯尔克孜自治州","伊犁哈萨克自治州"]];

	if(count<1){
		$(state).each(function(i,v){
			$('#inputState').append("<option value='"+i+"-"+v+"'>"+v+"</option>");
		});
	}
    count++;
	if(count>10000){
		count=1;
	}
	$('#inputState').change(function(){
		//每次选择省份后要还原
		$('#inputCity').html("<option selected>请选择城市</option>");
		//获取省份的索引值
		let stateIndex=this.value.substr(0,this.value.indexOf("-"));
		//根据省份的索引值遍历城市
		$(city[stateIndex]).each(function(i,v){
			$('#inputCity').append("<option value='"+v+"'>"+v+"</option>");
		});
	});
}

$('#info_submit').click(function(result){
	let data={
		//"account":$('#inputAccount').val(),
		"biography":$('#inputBio').val(),
		"username":$('#inputUserName').val(),
		"sex":$('#inputSex').val(),
		"age":$('#inputAge').val(),
		"birthday":$('#inputBirthday').val(),
		"phoneNumber":$('#inputPhoneNumber').val(),
		"email":$('#inputEmail').val(),
		"state":$('#state').val(),
		"city":$('#city').val(),
	}
	$.ajax({
		url:"http://localhost:9001/consumer/ResetInfo?token=123",
		contentType: 'application/json;charset=utf-8',
		data:JSON.stringify(data),
		type:"POST",
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 3000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				setTimeout("location.reload()",3000);
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 3000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#password_submit').click(function(result){
	$.ajax({
		url:"http://localhost:9001/consumer/ResetPWD?token=123",
		data:{
			"password":$('#inputPasswordCurrent').val(),
			"password1":$('#inputPasswordNew1').val(),
			"password2":$('#inputPasswordNew2').val(),
		},
		type:"POST",
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				setTimeout(function(){
					//location.reload();
					$('#inputPasswordCurrent').val("");
					$('#inputPasswordNew1').val("");
					$('#inputPasswordNew2').val("");
				},2000);
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#chooseP').click(function(){   //选择照片
	$('#choosePicture').click();
});

$('#choosePicture').on('change',function(e){  //获取图片
	//获取用户选择的文件
	let filelist=e.target.files;
	//检查是否选择了文件
	if(filelist.length === 0){
		return alert("请选择照片");
	}
	//拿到用户选择的文件
	let file=e.target.files[0];
	//通过文件获取到路径
	let imgURL=URL.createObjectURL(file);
	//重新初始化裁剪区域
	$image.cropper('destroy').attr('src',imgURL).cropper(options)
});

$('#rotate').click(function(){  //旋转
	$('#image').cropper('rotate',30);  //旋转图片
});

$('#reset').click(function(){
	$('#image').cropper('reset');  //旋转图片
});

$('#commit').click(function(){  //裁剪
	let casp=$('#image').cropper('getCroppedCanvas',{
		height:128,
		width:128,
		minHeight:128,
		minWidth:128
	});  //获取被裁剪的canvas
	let base64=casp.toDataURL('img/jpeg');  //转换为base64
	console.log(encodeURIComponent(base64));  //输出对特殊字符进行编码后的结果
	$('#preview').prop('src',base64);  //预览裁剪后的图片
});

$('#article_submit').click(function (result){
	let articleName=$('#article_name').val();
	let articleContents=editor.txt.html();
	$.ajax({
		url:"http://localhost:9001/consumer/createArticle?token=123",
		type:"POST",
		data:{ articleName:articleName,articleContents:articleContents },
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				setTimeout(function(){
					location.reload();
				},2000);
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#logout').click(function (result){
	$.ajax({
		url:"http://localhost:9001/consumer/Logout?token=123",
		type:"POST",
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				$(location).attr("href","http://localhost:9001/consumer/getLogin");
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#forget_password').click(function (result){
	$.ajax({
		url:"http://localhost:9001/consumer/SendMail?token=123",
		type:"POST",
		data:{email:$("#email").val()},
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#reset_password').click(function (result){
	if($('#password1').val()!=$('#password2').val()){
		$('#password1').val("");
		$('#password2').val("");
		let message = "两次密码不一样";
		let type = "default";
		let duration = 2000;
		let ripple = "true";
		let dismissible = "true";
		let positionX = "center";
		let positionY = "top";
		window.notyf.open({
			type,
			message,
			ripple,
			dismissible,
			duration,
			position: {
				x: positionX,
				y: positionY
			}
		});
		return;
	}
	$.ajax({
		url:"http://localhost:9001/consumer/ResetPassword?token=123",
		type:"POST",
		data:{
			password1:$('#password1').val(),
			password2:$('#password2').val()
		},
		async: false,
		cache: false,
		dataType: "json",
		success:function(result){
			if(result.status=="1"){
				let message = result.msg;
				let type = "default";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				setTimeout(function(){
					$('#password1').val("");
					$('#password2').val("");
				},2000);
			}else{
				let message = result.msg;
				let type = "warning";
				let duration = 2000;
				let ripple = "true";
				let dismissible = "true";
				let positionX = "center";
				let positionY = "top";
				window.notyf.open({
					type,
					message,
					ripple,
					dismissible,
					duration,
					position: {
						x: positionX,
						y: positionY
					}
				});
				return;
			}
		}
	});
});

$('#reset_login').click(function (result){
	$(location).attr("href","http://localhost:9001/consumer/getLogin");
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