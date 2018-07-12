<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
</head>
<body>
<h2>Hello World!</h2>
<c:url value="/loginPage.jsp" var="loginPage"/>
<a href="${loginPage}">您没有权限，请登录</a>
</body>
</html>
