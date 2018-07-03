//页面加载完成时调用该函数
$(function(){
	//根据class标识遍历元素，用于显示或隐藏错误提示
	$(".lableError").each(function(){
		showError($(this));
	});
	//页面加载完成后，按钮图片切换
	//hover(鼠标进入，鼠标离开)
	$("#submitBtn").hover(
		function(){
			//注意attr要小写
			$("#submitBtn").attr("src","/shopping/images/regist2.jpg");
		},
		function(){
			$("#submitBtn").attr("src","/shopping/images/regist1.jpg");	
	    }
	);
	//输入框得到焦点隐藏错误信息
	$(".inputClass").focus(function(){
		var lableId = $(this).attr("id")+"Error";//获取当前元素的ID属性，并与“Error”字符串进行拼接，得到lableId
		$("#"+lableId).text("");//获取lable元素，并将内容清空
		showError($("#"+lableId));//隐藏对应的错误图片
	});
	//输入框失去焦点，进行校验
	$(".inputClass").blur(function(){
		var id = $(this).attr("id");
		//获取到当前元素的id,并将其拼接成方法名
		var funName = "validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";
		eval(funName);//该方法用于将传入的字符串当做javaScript代码进行执行
	});
	//提交表单时进行校验
	$("#registerForm").submit(function(){
		var bool = true;
		if(!validateLoginname()){
			bool = false;
		}
		if(!validateLoginpass()){
			bool = false;
		}
		if(!validateReloginpass()){
			bool = false;
		}
		if(!validateEmail()){
			bool = false;
		}
		if(!validateVerifyCode()){
			bool = false;
		}
		return bool;
	});
});
/*
 * 用户名校验
 */
function validateLoginname(){
	//1.内容是否为空   2.长度在3~20之间    3.访问服务器校验
	var value = $("#loginname").val();//获取对应ID元素的值
	if(!value){
		$("#loginnameError").text("用户名不能为空！");
		showError($("#loginnameError"));
		return false;
	}
	if(value.length<3 || value.length>20){
		$("#loginnameError").text("用户名长度必须在3~20之间！");
		showError($("#loginnameError"));
		return false;
	}
	$.ajax({
		url:"/shopping/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#loginnameError").text("用户名已被注册！");
				showError($("#loginnameError"));
				return false;
			}
		}
	});
	
	return true;
}
/*
 * 密码校验
 */
function validateLoginpass(){
	//1.内容是否为空   2.长度在3~20之间  
	var value = $("#loginpass").val();//获取对应ID元素的值
	if(!value){
		$("#loginpassError").text("密码不能为空！");
		showError($("#loginpassError"));
		return false;
	}
	if(value.length<3 || value.length>20){
		$("#loginpassError").text("密码长度必须在3~20之间！");
		showError($("#loginpassError"));
		return false;
	}
	return true;
}
/*
 * 确认密码校验
 */
function validateReloginpass(){
	//1.内容是否为空   2.两次是否一样
	var value = $("#reloginpass").val();//获取对应ID元素的值
	if(!value){
		$("#reloginpassError").text("确认密码不能为空！");
		showError($("#reloginpassError"));
		return false;
	}
	if(value != $("#loginpass").val()){
		$("#reloginpassError").text("两次密码输入不一致！");
		showError($("#reloginpassError"));
		return false;
	}
	return true;
}
/*
 * Email校验
 */
function validateEmail(){
	//1.内容是否为空   2.格式    3.访问服务器校验
	var value = $("#email").val();//获取对应ID元素的值
	if(!value){
		$("#emailError").text("Email不能为空！");
		showError($("#emailError"));
		return false;
	}
	if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)){
		$("#emailError").text("Email输入错误！");
		showError($("#emailError"));
		return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateEmail",email:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#emailError").text("Email已被注册！");
				showError($("#emailError"));
				return false;
			}
		}
	});
	return true;
}
/*
 * 验证码校验
 */
function validateVerifyCode(){
	//1.内容是否为空   2.长度等于4    3.访问服务器校验
	var value = $("#verifyCode").val();//获取对应ID元素的值
	if(!value){
		$("#verifyCodeError").text("验证码不能为空！");
		showError($("#verifyCodeError"));
		return false;
	}
	if(value.length != 4){
		$("#verifyCodeError").text("验证码输入有误！");
		showError($("#verifyCodeError"));
		return false;
	}
	$.ajax({
		url:"/goods/UserServlet",//要请求的servlet
		data:{method:"ajaxValidateVerifyCode",verifyCode:value},//请求地址带的参数
		type:"POST",
		dataType:"json",//请求的数据类型
		async:false,//是否异步
		cache:false,//缓存
		success:function(result){//服务器执行成功后调用的函数
			if(!result){
				$("#verifyCodeError").text("验证码错误！");
				showError($("#verifyCodeError"));
				return false;
			}
		}
	});
	return true;
}
/*
 * 错误信息图片隐藏与显示处理
 */
function showError(ele){
	var text = ele.text();//获取元素内容
	if(!text){ //如果元素为空，则不显示
		ele.css("display","none");
	}else{
		ele.css("display","");
	}
}
//换一张验证码图片
function _hyz(){
	//通过ID标识获取该元素，设置图片路径，并在路径后添加毫秒级时间参数，防止浏览器缓存
	$("#imageVerifyCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}