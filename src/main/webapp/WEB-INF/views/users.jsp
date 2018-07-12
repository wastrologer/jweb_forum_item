<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.github.pagehelper.PageHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>全部商品</title>
    <script type="text/javascript">
        function deleteitems(id){
            var flag=alert("确定要删除吗？");
            if(!flag){
                window.location.href="deleteItems.action?id="+id;
            }
        }

        function firstPage(){
            var pageNum=document.getElementById("pageNum").value;
            if(1==pageNum){
                alert("亲，已经是首页了");
            }else{
                window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+1;
            }
        }
        function prePage(){
            var pageNum=document.getElementById("pageNum").value;
            pageNum--;
            window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+pageNum;
        }
        function nextPage(){
            var pageNum=document.getElementById("pageNum").value;
            pageNum++;
            window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+pageNum;
        }
        function lastPage(){
            var pageNum=document.getElementById("pageNum").value;
            var totalPage=document.getElementById("totalPage").value;
            if(pageNum==totalPage){
                alert("亲，已经是尾页了");
            }else{
                window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+totalPage;
            }
        }
    </script>
</head>
<body>
<a href="additems1.action"> 添加</a>
<h1 style="width: 100%">----</h1>
商品列表：
<table width="100%" border=1>
    <tr>
        <td>商品编号</td>
        <td>商品名称</td>
        <td>商品价格</td>
        <td>生产日期</td>
        <td>商品描述</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${itemsList.getList() }" var="item">
        <tr>
            <td>${item.id}</td>
            <td>${item.name }</td>
            <td>${item.price }</td>
            <td>${item.date }</td>
            <td>${item.detail }</td>
            <td><a href="queryOneItems.action?id=${item.id}">编辑</a> <a
                    href="javascript:void(0)" onclick="deleteitems(${item.id})">删除</a>
            </td>

        </tr>
    </c:forEach>
</table>
<br>
<div>
    <input type="hidden" id="pageNum" value="${itemsList.getPageNum()}">
    <input type="hidden" id="totalPage" value="${itemsList.getPages()}">
    <span onclick="firstPage()">首页</span> <span onclick="prePage()">上一页</span>
    <span onclick="nextPage()">下一页</span> <span onclick="lastPage()">尾页</span>
</div>
<%

%>
</body>
</html><%@ page language="java" contentType="text/html; charset=UTF-8"
                pageEncoding="UTF-8"%>
<%@ page import="com.github.pagehelper.PageHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>全部商品</title>
    <script type="text/javascript">
        function deleteitems(id){
            var flag=alert("确定要删除吗？");
            if(!flag){
                window.location.href="deleteItems.action?id="+id;
            }
        }

        function firstPage(){
            var pageNum=document.getElementById("pageNum").value;
            if(1==pageNum){
                alert("亲，已经是首页了");
            }else{
                window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+1;
            }
        }
        function prePage(){
            var pageNum=document.getElementById("pageNum").value;
            pageNum--;
            window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+pageNum;
        }
        function nextPage(){
            var pageNum=document.getElementById("pageNum").value;
            pageNum++;
            window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+pageNum;
        }
        function lastPage(){
            var pageNum=document.getElementById("pageNum").value;
            var totalPage=document.getElementById("totalPage").value;
            if(pageNum==totalPage){
                alert("亲，已经是尾页了");
            }else{
                window.location.href="<%=request.getContextPath()%>/Items/queryAllItems.action?pageNum="+totalPage;
            }
        }
    </script>
</head>
<body>
<a href="additems1.action"> 添加</a>
<h1 style="width: 100%">----</h1>
商品列表：
<table width="100%" border=1>
    <tr>
        <td>商品编号</td>
        <td>商品名称</td>
        <td>商品价格</td>
        <td>生产日期</td>
        <td>商品描述</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${itemsList.getList() }" var="item">
        <tr>
            <td>${item.id}</td>
            <td>${item.name }</td>
            <td>${item.price }</td>
            <td>${item.date }</td>
            <td>${item.detail }</td>
            <td><a href="queryOneItems.action?id=${item.id}">编辑</a> <a
                    href="javascript:void(0)" onclick="deleteitems(${item.id})">删除</a>
            </td>

        </tr>
    </c:forEach>
</table>
<br>
<div>
    <input type="hidden" id="pageNum" value="${itemsList.getPageNum()}">
    <input type="hidden" id="totalPage" value="${itemsList.getPages()}">
    <span onclick="firstPage()">首页</span> <span onclick="prePage()">上一页</span>
    <span onclick="nextPage()">下一页</span> <span onclick="lastPage()">尾页</span>
</div>
<%

%>
</body>
</html>
