<%@page import="kr.or.ddit.comm.vo.AtchFileDetailVO"%>
<%@page import="kr.or.ddit.comm.vo.AtchFileVO"%>
<%@page import="kr.or.ddit.vo.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
// getParameter : 브라우저에서 보내온 정보를 꺼내올 때 사용
// getAttribute : requet 객체에 setAttribute한 정보를 꺼내올 때 사용
	MemberVO mv = (MemberVO)request.getAttribute("mv");

	AtchFileVO atchFileVO = (AtchFileVO) request.getAttribute("atchFileVO");
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 상세</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>I D:</td>
			<td><%=mv.getMemId() %></td>
		</tr>
		<tr>
			<td>이름:</td>
			<td><%=mv.getMemName() %></td>
		</tr>
		<tr>
			<td>전화번호:</td>
			<td><%=mv.getMemTel() %></td>
		</tr>
		<tr>
			<td>주소:</td>
			<td><%=mv.getMemAddr() %></td>
		</tr>
		<tr>
			<td>첨부파일:</td>
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
			<td colspan="2">
			<a href="<%=request.getContextPath() %>/member.list.do">[목록]</a>
			<a href="<%=request.getContextPath() %>/member/update.do?memId=<%=mv.getMemId() %>">[회원정보 수정]</a>
			<a href="<%=request.getContextPath() %>/member/delete.do?memId=<%=mv.getMemId() %>">[회원정보 삭제]</a>
			</td>
		</tr>
	</table>
</body>
</html>