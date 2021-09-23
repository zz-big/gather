<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        function goPage(pageIndex) {
            location.href = "findGatherByPage?pageIndex=" + pageIndex;
        }

        function addGather() {
            location.href = "add.jsp";
        }

        function onLineGather(id) {
            if (confirm("确认上线吗？")) {
                $.post("onLineGather", {"int": id}, function (data) {
                    if (data == 1) {
                        alert("上线成功");
                        location.href = "findGatherByPage?pageIndex=1";
                    }
                    else {
                        alert("上线失败");
                    }

                });
            }
        }

        function offLineGather(id) {
            if (confirm("确认下线吗？")) {
                $.post("offLineGather", {"int": id}, function (data) {
                    if (data == 1) {
                        alert("下线成功");
                        location.href = "findGatherByPage?pageIndex=1";
                    }
                    else {
                        alert("下线失败");
                    }

                });
            }
        }

        function deleteGather(id) {
            var flag = confirm("确认删除此条信息吗？")
            if (flag)
                location.href = "deleteGather?id=" + id;
        }

        function dolphinUrl() {
            location.href = "dolphinUrl";
        }

        function updateGathershow(id) {
            location.href = "updateGatherShow?id=" + id;
        }

        function gofirst() {
            location.href = "index.jsp";
        }

        function deleteGatherBatch() {
            var num = $("input[name='xu']:checked").length;
            if (num == 0) {
                alert("请至少选择一项！");
                return false;
            }
            if (confirm("确定删除所选项目？")) {
                var list = [];
                $("[name='xu']:checked").each(function () {
                    list.push($(this).val());
                });
                $.post("deleteGatherBatch", {"list": list}, function (data) {
                    if (data == 1) {
                        alert("删除成功");
                        location.href = "findGatherByPage?pageIndex=1";
                    }
                    else {
                        alert("删除失败");
                    }

                });
            }
        }


        function onLineBatch() {
            var num = $("input[name='xu']:checked").length;
            if (num == 0) {
                alert("请至少选择一项！");
                return false;
            }
            if (confirm("确定上线所选项目？")) {
                var list = [];
                $("[name='xu']:checked").each(function () {
                    list.push($(this).val());
                });
                $.post("onLineBatch", {"list": list}, function (data) {
                    if (data == 1) {
                        alert("上线成功");
                        location.href = "findGatherByPage?pageIndex=1";
                    }
                    else {
                        alert("上线失败");
                    }

                });
            }
        }


    </script>
    <style type="text/css">
        strong {
            font-size: 20px;
            font-weight: bolder;

        }

        .info > th {
            text-align: center;
        }
    </style>
    <title>Insert title here</title>
</head>
<body>
<div style="margin-left:40px">
    <h2>采集信息</h2><br>
    <td>
        <button onclick="gofirst()" class="btn btn-primary"><span class="glyphicon glyphicon-home"></span>  返回主页</button>
    </td> &nbsp;&nbsp;&nbsp;
    <td>
        <button onclick="deleteGatherBatch()" class="btn btn-primary"><sapn class="glyphicon glyphicon-remove"></sapn> 批量删除</button>
    </td> &nbsp;&nbsp;&nbsp;
    <td>
    <button onclick="onLineBatch()" class="btn btn-primary"><sapn class="glyphicon glyphicon-plus"></sapn> 批量上线</button>
    </td> &nbsp;&nbsp;&nbsp;
    <td>
    <button onclick="dolphinUrl()" class="btn btn-primary"><sapn class="glyphicon glyphicon-open"></sapn> dolphin主页</button>
    </td>
    <br>
    <br>
    查询出总信息数一共<strong>${totalCount}</strong>条,当前为第<strong>${pageIndex }</strong>页，共<strong>${totalPage}</strong>页，每页<strong>${pageSize}</strong>条。
</div>
<div style="margin:0 20px">

    <table class="table table-striped">
        <tr class="info" style="font-size:18px;">
            <th>选择</th>
            <th>任务ID</th>
            <th>数据库url</th>
            <th>数据库名称</th>
            <th>表名</th>
            <th>数据库用户名</th>
            <th>同步方式</th>
            <th>dolphin项目名称</th>
            <th>创建hive表</th>
            <th>上线dolphin项目</th>
            <th>dolphin定时时间</th>
            <th>操作</th>
        </tr>
        <c:forEach var="c" items="${gathers}" varStatus="vs" step="1">
            <tr style="text-align:center">
                <td><input type="checkbox" name="xu" value="${c.jobId}"></td>
                <td>${c.jobId}</td>
                <td>${c.jdbcUrl}</td>
                <td>${c.databaseName}</td>
                <td>${c.tableName}</td>
                <td>${c.userName}</td>
                <td>${c.syncType}</td>
                <td>${c.dolphinProjectName}</td>
                <td>${c.createHiveTable}</td>
                <td>${c.isOnline}</td>
                <td>${c.crontab}</td>
                <td align="left">
                    <button onclick="updateGathershow(${c.jobId})" class="btn btn-warning"><span
                            class="glyphicon glyphicon-pencil"> </span>修改
                    </button>
                    <button onclick="deleteGather(${c.jobId})" class="btn btn-danger"><span
                            class="glyphicon glyphicon-trash"> </span>删除
                    </button>
                    <c:if test="${c.isOnline == false}">
                        <button onclick="onLineGather(${c.jobId})" class="btn btn-info"><span
                                class="glyphicon glyphicon-info-sign"> </span>上线
                        </button>
                    </c:if>
                    <c:if test="${c.isOnline == true}">
                        <button onclick="offLineGather(${c.jobId})" class="btn btn-info"><span
                                class="glyphicon glyphicon-info-sign"> </span>下线
                        </button>
                    </c:if>

                </td>
            </tr>

        </c:forEach>
        <tr>
            <c:if test="${pageIndex>1 }">
                <td colspan="7">
                    <button onclick="goPage(1)" class="btn btn-default">首页</button>
                    <button onclick="goPage(${pageIndex-1})" class="btn btn-default">上一页</button>
                </td>
            </c:if>
            <c:if test="${pageIndex<totalPage }">
                <button onclick="goPage(${pageIndex+1})" class="btn btn-default">下一页</button>
                <button onclick="goPage(${totalPage})" class="btn btn-default">尾页</button>
            </c:if>

        </tr>
    </table>

</div>
</body>
</html>