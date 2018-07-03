<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>book_desc.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/product/desc.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>

<script type="text/javascript" src="<c:url value='/adminjsps/admin/js/product/desc.js'/>"></script>

<script type="text/javascript">

$(function() {
	$("#box").attr("checked", false);
	$("#formDiv").css("display", "none");
	$("#show").css("display", "");	
	
	// 操作和显示切换
	$("#box").click(function() {
		if($(this).attr("checked")) {
			$("#show").css("display", "none");
			$("#formDiv").css("display", "");
		} else {
			$("#formDiv").css("display", "none");
			$("#show").css("display", "");		
		}
	});
});
function loadChildren(){
	var pid = $("#pid").val();
	$.ajax({
		async:true,
		cache:false,
		url:"/shopping/admin/AdminProductServlet",
		data:{method:"ajaxFindChildren",paid:paid},
		type:"POST",
		dataType:"json",
		success:function(arr){
			$("#cid").empty();//得到cid，删除它的内容
			$("#cid").append($("<option>===请选择2级分类===</option>"));
			for(var i=0;i<arr.length;i++){
				var option = $("<option>").val(arr[i].cid).text(arr[i].cname);
				$("#cid").append(option);
			}
		}
	});
}
function editForm(){
	$("#method").val("edit");
	$("#form").submit();
}
function delForm(){
	$("#method").val("delete");
	$("#form").submit();
}

</script>
  </head>
  
  <body>
    <input type="checkbox" id="box"><label for="box">编辑或删除</label>
    <br/>
    <br/>
  <div id="show">
    <div class="sm">${product.pname }</div>
    <img align="top" src="<c:url value='/${product.image_w }'/>" class="tp"/>
    <div id="book" style="float:left;">
	    <ul>
	    	<li>商品编号：${product.pid }</li>
	    	<li>定价：&yen;${product.price }</li>
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table class="tab">
			<tr>
				<td colspan="3">
					生产地：${product.place }</a>
				</td>
			</tr>
		</table>
	</div>
  </div>
  
  
  <div id='formDiv'>
   <div class="sm">&nbsp;</div>
   <form action="<c:url value='/admin/AdminProductServlet'/>" method="post" id="form">
   	<input type="hidden" name="method" id="method" value=""/>
   	<input type="hidden" name="pid" value="${product.pid }"/>
    <img align="top" src="<c:url value='/${product.image_w }'/>" class="tp"/>
    <div style="float:left;">
	    <ul>
	    	<li>商品编号：${product.pid }</li>
	    	<li>书名：　<input id="pname" type="text" name="pname" value="${product.pname }" style="width:500px;"/></li>
	    	<li>定价：　<input id="price" type="text" name="price" value="${product.price }" style="width:50px;"/></li>
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table class="tab">
			<tr>
				<td colspan="3">
					生产地：　<input id="place" type="text" name="place" value="${product.place }" style="width:200px;"/>
				</td>
			</tr>
			<tr>
				<td>
					一级分类：<select name="paid" id="paid" onchange="loadChildren()">
						<option value="">==请选择1级分类==</option>
<c:forEach items="${parents }" var="parent">
			    		<option value="${parent.cid }" <c:if test="${parent.cid eq product.category.parent.cid }">selected='selected'</c:if>>${parent.cname }</option>
</c:forEach>
					</select>
				</td>
				<td>
					二级分类：<select name="cid" id="cid">
						<option value="">==请选择2级分类==</option>
<c:forEach items="${children }" var="child">
			    		<option value="${child.cid }" <c:if test="${child.cid eq product.category.cid }">selected='selected'</c:if>>${child.cname }</option>
</c:forEach>
					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
					<input onclick="editForm()" type="button" name="method" id="editBtn" class="btn" value="编　　辑">
					<input onclick="delForm()" type="button" name="method" id="delBtn" class="btn" value="删　　除">
				</td>
				<td></td>
			</tr>
		</table>
	</div>
   </form>
  </div>

  </body>
</html>
