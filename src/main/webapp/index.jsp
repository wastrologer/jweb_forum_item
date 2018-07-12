<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
</head>
<body>
<h2>Hello World!</h2>
<c:url value="/login/logout" var="logoutUrl"/>
<a href="${logoutUrl }">退出系统</a>
</body>
</html>
