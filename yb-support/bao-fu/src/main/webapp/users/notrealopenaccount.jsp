<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>非实名开户</title>
</head>
<script type="text/javascript">
	function check() {
		if (document.getElementById("userNo").value == "") {
			alert("“平台用户编号”不能为空！");
			return false;
		}
		if (document.getElementById("requestNo").value == "") {
			alert("“请求流水号”不能为空！");
			return false;
		}
		if (document.getElementById("memberType").value == "") {
			alert("“会员类型”不能为空！");
			return false;
		}
		if (document.getElementById("requestChannel").value == "") {
			alert("“请求渠道”不能为空！");
			return false;
		}
	}
</script>
<body>
	<form action="notRealOpenAccount.action" method="POST"
		onsubmit="return check()">
		<div style="margin: 0 auto; width: 500px;">
			<table width="800" height="357" border="0" cellpadding="1"
				cellspacing="1" bgcolor="#33CCFF">
				<tr>
					<td height="84" colspan="2" align="center" bgcolor="#FFFFFF">非实名开户-DEMO</td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">接口名称：</td>
					<td bgcolor="#FFFFFF"><input name="serviceName" type="text"
						id="serviceName" size="25" maxlength="50"
						value="NOT_REAL_OPEN_ACCOUNT" readonly="readonly" /><font color="red"
						size="5px"> *</font></td>
				</tr>
				<%
					Calendar now = Calendar.getInstance();
					String requestNo = "DD" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now.getTime());
					now.add(Calendar.MINUTE, 10);
					String expired = new SimpleDateFormat("yyyyMMddHHmmss").format(now.getTime());
				%>
				<tr>
					<td align="right" bgcolor="#FFFFFF">平台用户编号：</td>
					<td bgcolor="#FFFFFF"><input name="userNo" type="text"
						id="userNo" size="20" maxlength="50" /><font color="red"
						size="5px"> *</font></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">请求流水号：</td>
					<td bgcolor="#FFFFFF"><input name="requestNo" type="text"
						id="requestNo" value="<%=requestNo%>" size="20" maxlength="50" /><font
						color="red" size="5px"> *</font></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">邮箱：</td>
					<td bgcolor="#FFFFFF"><input name="email" type="text"
						id="email" size="20" maxlength="50"></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">注册手机号：</td>
					<td bgcolor="#FFFFFF"><input name="mobile" type="text"
						id="mobile" size="20" maxlength="50"></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">会员类型：</td>
					<td bgcolor="#FFFFFF"><input name="memberType" type="text"
						id="memberType" size="20" maxlength="50"><font
						color="red" size="5px"> *</font></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#FFFFFF">请求渠道：</td>
					<td bgcolor="#FFFFFF"><input name="requestChannel" type="text"
						id="requestChannel" size="20" maxlength="50"><font
						color="red" size="5px"> *</font></td>
				</tr>
				<tr>
					<td align="center" bgcolor="#FFFFFF" colspan="2">“<font
						color="red" size="5px"> * </font>”代表为必填项。
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" bgcolor="#FFFFFF"><input
						name="serverPattern" id="serverPattern" type="hidden"
						value="service"><input type="submit" name="Submit"
						value="注册提交" /></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>