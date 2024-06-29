<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신규회원 등록</title>
</head>
<body>
<%--	요청이 들어오면 지정한 action의 주소로 입력한 내용이 날아감 --%>
<%-- 	루트에는 반드시 해당 애플리케이션의 이름을 지정해줘야 한다.
	 	/ServletExam 대신	<%=request.getContextPath() %> 
	 	컨텍스트 루트를 불러오는 메서드를 사용해서 대체 가능 --%>
	<form action="<%=request.getContextPath() %>/member/insert.do" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>I D:</td>
				<td><input type="text" name="memId" value=""></td>
			</tr>
			<tr>
				<td>이름:</td>
				<td><input type="text" name="memName" value=""></td>
			</tr>
			<tr>
				<td>전화번호:</td>
				<td><input type="text" name="memTel" value=""></td>
			</tr>
			<tr>
				<td>주소:</td>
				<td><textarea name="memAddr" ></textarea></td>
			</tr>
			<tr>
				<td>첨부파일</td>
				<td><input type="file" name="atchFile" multiple="multiple"></td>
			
			</tr>
		</table>
		<input type="submit" value="회원 등록">
	</form>
</body>
</html>