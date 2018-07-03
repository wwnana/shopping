<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'productdesc.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/product/add.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>
<script type="text/javascript">
$(function () {
	
	$("#btn").addClass("btn1");
	$("#btn").hover(
		function() {
			$("#btn").removeClass("btn1");
			$("#btn").addClass("btn2");
		},
		function() {
			$("#btn").removeClass("btn2");
			$("#btn").addClass("btn1");
		}
	);
	
	$("#btn").click(function() {
		var pname = $("#pname").val();
		var price = $("#price").val();
		var place = $("#place").val();
		var paid = $("#paid").val();
		var cid = $("#cid").val();
		var image_w = $("#image_w").val();
		var image_b = $("#image_b").val();
		
		if(!pname || !price || !place || !paid || !cid || !image_w || !image_b) {
			alert("产品名、定价、生产地、1级分类、2级分类、大图、小图都不能为空！");
			return false;
		}
		
		if(isNaN(price)) {
			alert("定价必须是合法小数！");
			return false;
		}
		$("#form").submit();
	});
	
});
function loadChildren(){
	var paid = $("#paid").val();
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

</script>
  </head>
  
  <body>
  <div>
   <p style="font-weight: 900; color: red;">${msg }</p>
   <form action="<c:url value='/admin/AdminAddProductServlet'/>" enctype="multipart/form-data" method="post" id="form">
    <div>
	    <ul>
	    	<li>产品名：　<input id="pname" type="text" name="pname" value="" style="width:500px;"/></li>
	    	<li>大图：　<input id="image_w" type="file" name="image_w"/></li>
	    	<li>小图：　<input id="image_b" type="file" name="image_b"/></li>
	    	
	    	<li>定价：　<input id="price" type="text" name="price" value="" style="width:50px;"/>
	    	
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table>
			<tr>
				<td colspan="3">
					生产地：　<input type="text" name="place" id="place" value="" style="width:200px;"/>
				</td>
			</tr>
			<tr>
				<td>
					一级分类：<select name="paid" id="paid" onchange="loadChildren()">
						<option value="">===请选择1级分类===</option>
					<c:forEach items="${parents }" var="parent">
			    		<option value="${parent.cid }">${parent.cname }</option>
					</c:forEach>
					</select>
				</td>
				<td>
					二级分类：<select name="cid" id="cid">
						<option value="">===请选择2级分类===</option>

					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<input type="button" id="btn" class="btn" value="新品上架">
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</div>
   </form>
  </div>

  </body>
</html>
