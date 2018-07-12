<%--
  Created by IntelliJ IDEA.
  User: Mr
  Date: 2018/5/11
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
    <script >
        function checkUser(){
            var userName = document.getElementById("userid").value;
            var password = document.getElementById("userpassid").value;
            if(userName == ""  ){
                alert("用户名不能为空");
                return false;
            }
            if(password == ""  ){
                alert("密码不能为空");
                return false;
            }else{
                return true;
            }
        }
    </script>

</head>
<body>
page
<form id="formid"  name= "myform" method = 'post'  action = '/proxy/login/passwordLogin' onsubmit = "return checkUser();" >
    <table width="100%" border="0">
        <tr>
            <td colspan='2'>
                <input id="remember-me-id" name="remember-me" type="checkbox" checked="checked"/>自动登录
            </td>
        </tr>
        <tr>
            <td width="60" height="40" align="right">用户名&nbsp;</td>
            <td><input type="text" value="" class="text2" name = "userName" id = "userid"/></td>
        </tr>
        <tr>
            <td width="60" height="40" align="right">密&nbsp;&nbsp;码&nbsp;</td>
            <td><input type="password" value="" class="text2" name = "password" id = "userpassid"/></td>
        </tr>
        <tr>
            <td width="60" height="40" align="right">&nbsp;</td>
            <td><div class="c4">
                <input type="submit" value="登录" class="btn2"  />
            </div>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
