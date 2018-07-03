<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册页面</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/register.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jsps/js/user/register.js'/>"></script>
</head>
<body>
<div id="divMain">
	<div id="divTitle">
		<span id="spanTitle">新用户注册</span>
	</div>
	<div id="divBody">
	<form action="<c:url value='/UserServlet'/>" method="post" id="registerForm">
		<input type="hidden" name="method" value="register"/>
		<table id="tableForm">
			<tr>
				<td class="tdText">用户名：</td>
				<td class="tdInput"><input class="inputClass" type="text" name="loginname" id="loginname" value="${form.loginname }"/></td>
				<td class="tdError"><lable class="lableError" id="loginnameError">${errors.loginname }</lable></td>
			</tr>
			<tr>
				<td class="tdText">登录密码：</td>
				<td class="tdInput"><input class="inputClass" type="password" name="loginpass" id="loginpass" value="${form.loginpass }"/></td>
				<td class="tdError"><lable class="lableError" id="loginpassError">${errors.loginpass }</lable></td>
			</tr>
			<tr>
				<td class="tdText">确认密码：</td>
				<td class="tdInput"><input class="inputClass" type="password" name="reloginpass" id="reloginpass" value="${form.reloginpass }"/></td>
				<td class="tdError"><lable class="lableError" id="reloginpassError">${errors.reloginpass }</lable></td>
			</tr>
			<tr>
				<td class="tdText">Email：</td>
				<td class="tdInput"><input class="inputClass" type="text" name="email" id="email" value="${form.email }"/></td>
				<td class="tdError"><lable class="lableError" id="emailError">${errors.email }</lable></td>
			</tr>
			<tr>
				<td class="tdText">图形验证码：</td>
				<td class="tdInput"><input class="inputClass" type="text" name="verifyCode" id="verifyCode" value="${form.verifyCode }"/></td>
				<td class="tdError"><lable class="lableError" id="verifyCodeError">${errors.verifyCode }</lable></td>
			</tr>
			<tr>
				<td class="tdText"></td>
				<td class="tdInput"><div id="imgVerifyCode"><img id="imageVerifyCode" src="<c:url value="/VerifyCodeServlet"/>"/></div></td>
				<td class="tdError"><a href="javascript:_hyz()">换一张</a></td>
			</tr>
			<tr>
				<td class="tdText"></td>
				<td class="tdInput"><input type="image" src="/shopping/images/regist1.jpg" id="submitBtn"/></td>
				<td class="tdError"></td>
			</tr>
		</table>
	</form>
	</div>
</div>
</body>
</html>