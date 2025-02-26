<%@page import="kr.or.ddit.comm.vo.AtchFileVO"%>
<%@page import="kr.or.ddit.comm.vo.AtchFileDetailVO"%>
<%@page import="kr.or.ddit.vo.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	MemberVO mv = (MemberVO) request.getAttribute("mv");
	AtchFileVO atchFileVO = (AtchFileVO) request.getAttribute("atchFileVO");
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 변경</title>
</head>
<body>
	<form action="<%=request.getContextPath() %>/member/update.do" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memId" value="<%=mv.getMemId() %>">
		<input type="hidden" name="atchFileId" value="<%=mv.getAtchFileId() %>">
		<table>
			<tr>
				<td>I D:</td>
<%-- 				수정 방지를 위해서 input이 불가능하게 값만 넣었지만, 결국에는 id값도 전송해야 되기 때문 히든으로 만듬 --%>
				<td><%=mv.getMemId() %></td>
			</tr>
			<tr>
				<td>이름:</td>
				<td><input type="text" name="memName" value="<%=mv.getMemName() %>"></td>
			</tr>
			<tr>
				<td>전화번호:</td>
				<td><input type="text" name="memTel" value="<%=mv.getMemTel() %>"></td>
			</tr>
			<tr>
				<td>주소:</td>
				<td><textarea name="memAddr" ><%=mv.getMemAddr() %></textarea></td>
			</tr>
			<tr>
			<td>기존첨부파일:</td>
				<td>
				<%
					if(atchFileVO != null) { // 파일이 있으면
						 for(AtchFileDetailVO detailVO : atchFileVO.getAtchFileDetailList()) {
				%>
						<!-- div는 첨부파일 갯수만큼 만들어질 것임 -->
						<div>
							<a href="<%=request.getContextPath() %>/download.do?atchFileId=<%=detailVO.getAtchFileId() %>
							&fileSn=<%=detailVO.getFileSn() %>"><%=detailVO.getOrignlFileNm() %></a>
						</div>
				<%
						} // for
					} // if
				%>
				</td>
			</tr>
			<tr>
				<td>신규첨부파일</td>
				<td><input type="file" name="atchFile" multiple="multiple"></td>
			</tr>
		</table>
		<input type="submit" value="회원정보 수정">

	</form>
</body>
</html>