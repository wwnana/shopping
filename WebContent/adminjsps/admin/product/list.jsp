<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>图书分类</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/product/list.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />

<script type="text/javascript" src="<c:url value='/adminjsps/admin/js/product/list.js'/>"></script>
  </head>
  
  <body>
<div class="divProduct">
<ul>

<c:forEach items="${pb.beanList }" var="product">

 <li>
  <div class="inner">
    <a class="pic" href="<c:url value='/admin/AdminProductServlet?method=load&pid=${product.pid }'/>"><img src="<c:url value='/${product.image_b }'/>" border="0"/></a>
    <p class="price" >
		<span class="price_n">&yen;${product.price }</span>
	</p>
	<p><a id="productname" title="${product.pname }" href="<c:url value='/admin/AdminProductServlet?method=load&pid=${product.pid }'/>">${product.pname }</a></p>
	<p class="publishing">
		<span>生产地：${product.place }</span>
	</p>
  </div>
 </li>
</c:forEach>



</ul>
</div>
<div style="float:left; width: 100%; text-align: center;">
	<hr/>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>
  </body>
 
</html>

