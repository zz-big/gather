<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/bootst"></script>
    <script type="text/javascript">
        function gofirst() {
            location.href = "index.jsp";
        }
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div class="page-header" style="margin-left: 20px">
    <h1>添加采集信息</h1>
</div>
<div style="margin:0 20px">
    <form action="insertGather" method="post">
        <fieldset>
            <legend>添加采集信息</legend>
            <table class="table table-hover" style=" width:1000px">
                <tr>
                    <th>数据库url</th>
                    <td><input style="width:200px" class="form-control" name="jdbcUrl" type="text" required="required"
                               value="jdbc:mysql://localhost:3306"
                               placeholder="请输入数据库url"></td>
                </tr>
                <tr>
                    <th>数据库名称</th>
                    <td><input style="width:200px" class="form-control" name="databaseName" type="text"
                               required="required" placeholder="请输入数据库名称"></td>
                </tr>

                <tr>
                    <th>表名</th>
                    <td><input style="width:200px" class="form-control" name="tableName" type="text" required="required"
                               placeholder="请输入表名"></td>
                </tr>
                <%--<tr>--%>

                    <%--<th>jbdc driver</th>--%>
                    <%--<td><input style="width:200px" class="form-control" name="driver" type="text"--%>
                               <%--value="com.mysql.jdbc.Driver"></td>--%>
                <%--</tr>--%>
                <tr>

                    <th>数据库用户名</th>
                    <td><input style="width:200px" class="form-control" name="userName" type="text" required="required"
                               placeholder="请输入数据库用户名"></td>
                </tr>
                <tr>

                    <th>数据库用户密码</th>
                    <td><input style="width:200px" class="form-control" name="passwd" type="text" required="required"
                               placeholder="请输入数据库用户密码"></td>
                </tr>
                <tr>

                    <th>同步方式</th>
                    <td><input style="width:200px" class="form-control" name="syncType" type="text" required="required"
                               placeholder="full or snapshot or incre"></td>
                </tr>
                <tr>

                    <th>dolphin项目名称</th>
                    <td><input style="width:200px" class="form-control" name="dolphinProjectName" type="text"
                               required="required" placeholder="请输入dolphin项目名称"></td>
                </tr>

                <tr>

                    <th>dolphin定时时间</th>
                    <td><input style="width:200px" class="form-control" name="crontab" type="text" required="required"
                               value="0 30 8 * * ? *"></td>
                </tr>

                <%--<tr type="hidden">--%>
                <th type="hidden">是否创建hive表</th>
                <td>
                <input style="width:200px" class="form-control" name="createHiveTable" type="text" value="false">
                </td>
                <%--<th type="hidden">是否上线dolphin项目</th>--%>
                <%--<td>--%>
                    <input style="width:200px" class="form-control" name="isOnline" type="hidden" value="false">
                <%--</td>--%>
                <%--</tr>--%>
                <%--<tr type="hidden">--%>

                <%--<th type="hidden">是否创建dolphin项目</th>--%>
                <%--<td>--%>
                    <input style="width:200px" class="form-control" name="hasCreate" type="hidden"
                           value="false">
                <%--</td>--%>
                <%--</tr>--%>


            </table>
            <button class="btn btn-success " type="submit"><span class="glyphicon glyphicon-ok"></span> 提交</button>
            <button class="btn btn-danger" type="reset"><span class="glyphicon glyphicon-remove"></span> 取消</button>
            <button class="btn btn-info" onclick="gofirst()"><span class="glyphicon glyphicon-home"></span> 返回</button>
        </fieldset>
    </form>
</div>
</body>
</html>