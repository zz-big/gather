<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/bootst"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        function gofirst() {
            location.href = "findGatherByPage?pageIndex=1";
        }

    </script>
    <title>Insert title here</title>
</head>
<body>
<div class="page-header" style="margin-left: 20px">
    <h1>修改采集信息</h1>
</div>
<div style="margin:0 20px">
    <form action="updateGather" method="post">
        <input type="hidden" name="jobId" value="${gatherDataEntity.jobId }">
        <fieldset>
            <legend>采集</legend>
            <table class="table table-hover" style=" width:1000px">
                <tr>
                    <th>数据库url</th>
                    <td><input style="width:200px" name="jdbcUrl" class="form-control" type="text" required="required"
                               value="${gatherDataEntity.jdbcUrl }"></td>
                </tr>
                <tr>
                    <th>数据库名称</th>
                    <td><input style="width:200px" class="form-control" name="databaseName" type="text"
                               required="required" value="${gatherDataEntity.databaseName}"></td>
                </tr>

                <tr>
                    <th>表名</th>
                    <td><input style="width:200px" class="form-control" name="tableName" type="text"
                               value="${gatherDataEntity.tableName }"></td>
                </tr>
                <%--<tr>--%>

                <%--<th>jbdc driver</th>--%>
                <%--<td><input style="width:200px" class="form-control" name="driver" type="text" required="required"--%>
                <%--value="${gatherDataEntity.driver }"></td>--%>
                <%--</tr>--%>
                <tr>

                    <th>数据库用户名</th>
                    <td><input style="width:200px" class="form-control" name="userName" type="text" required="required"
                               value="${gatherDataEntity.userName }"></td>
                </tr>
                <tr>

                    <th>数据库用户密码</th>
                    <td><input style="width:200px" class="form-control" name="passwd" type="password"
                               required="required"
                               value="${gatherDataEntity.passwd }"></td>
                </tr>
                <tr>

                    <th>同步方式</th>
                    <td><input style="width:200px" class="form-control" name="syncType" type="text" required="required"
                               value="${gatherDataEntity.syncType }"></td>
                </tr>
                <tr>

                    <th>dolphin项目名称</th>
                    <td><input style="width:200px" class="form-control" name="dolphinProjectName" type="text"
                               required="required"
                               value="${gatherDataEntity.dolphinProjectName }"></td>
                </tr>
                <tr>

                    <th>是否创建hive表</th>
                    <td><input style="width:200px" class="form-control" name="createHiveTable" type="boolean"
                               required="required"
                               value="${gatherDataEntity.createHiveTable }"></td>
                </tr>
                <tr>

                    <th>是否上线dolphin项目</th>
                    <td><input style="width:200px" class="form-control" name="isOnline" type="boolean"
                               required="required"
                               value="${gatherDataEntity.isOnline }"></td>
                </tr>
                <tr>

                    <th>dolphin定时时间</th>
                    <td><input style="width:200px" class="form-control" name="crontab" type="text" required="required"
                               value="${gatherDataEntity.crontab }"></td>
                </tr>
                <%--<tr>--%>

                <%--<th>是否创建dolphin项目</th>--%>
                <%--<td><input style="width:200px" class="form-control" name="hasCreate" type="boolean" required="required"--%>
                <%--value="${gatherDataEntity.hasCreate }"></td>--%>
                <%--</tr>--%>


            </table>
            <button class="btn btn-success " type="submit"><span class="glyphicon glyphicon-ok"></span> 提交</button>
            <button class="btn btn-danger" type="reset"><span class="glyphicon glyphicon-remove"></span>取消</button>
            <button class="btn btn-info" onclick="gofirst()"><span class="glyphicon glyphicon-backward"></span> 返回</button>
        </fieldset>
    </form>
</div>
</body>
</html>