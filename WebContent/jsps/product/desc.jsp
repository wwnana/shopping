<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>产品详细</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/product/desc.css'/>">
	<script src="<c:url value='/jsps/js/product/desc.js'/>"></script>
  </head>
  
  <body>
  <div class="divProductName">${product.pname }</div>
  <div>
    <img align="top" src="<c:url value='/${product.image_w }'/>" class="img_image_w"/>
    <div class="divProductDesc">
	    <ul>
	    	<li>商品编号：${product.pid }</li>
	    	<li>定价：<span class="price_n">&yen;${product.price }</span></li>
	    </ul>
		<hr class="hr1"/>
		<table>
			<tr>
				<td colspan="3">
					生产地：${product.place }
				</td>
			</tr>
		</table>
		<div class="divForm">
			<form id="form1" action="<c:url value='/CartItemServlet'/>" method="post">
				<input type="hidden" name="method" value="add"/>
				<input type="hidden" name="pid" value="${product.pid }"/>
  				我要买：<input id="cnt" style="width: 40px;text-align: center;" type="text" name="quantity" value="1"/>
  				<c:choose>
					<c:when test="${parent.cid == 'E54859BC5DC14AED9DD6ACCCCA7FE9AA' }">g</c:when>
					<c:otherwise>件</c:otherwise>
				</c:choose>
  				
  				
  			</form>
  			<a id="btn" href="javascript:$('#form1').submit();"></a>
  		</div>	
	</div>
  </div>
  </body>
</html>
