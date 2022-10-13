<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>로그인 페이지</title>
<body>
<form name="form1" action="/smartux_adm/admin/user/userList.do" method="GET">
아이디 : <input type="text" name="smartux_id" value="${smartux_id}" /><br>
비밀번호 : <input type="text" name="smartux_pwd" value="${smartux_pwd}" /><br>

<input type="submit" value="전송" />
</form>

</body>
</html>