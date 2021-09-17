<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
<script type="text/javascript">
function show(){
	location.href="findGatherByPage?pageIndex=1";
}
function add(){
	location.href="add.jsp";
}

</script>

<title>Insert title here</title>
</head>
<body>
<div style="margin-left:50px" >
<h1>欢迎登录自动化数据采集生成系统</h1>
<p style="font-size:18px;color:green">请选择需要的操作</p>
<button class="btn btn-info btn-lg"  onclick="show()"><span class="glyphicon glyphicon-list" ></span> 显示采集信息</button><br><br>
<button class="btn btn-primary btn-lg" onclick="add()"><span class="glyphicon glyphicon-th-large" ></span> 新增采集信息</button>
</div>
</body>
</html>