<%@page import="kr.or.ddit.vo.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	MemberVO mv = (MemberVO) session.getAttribute("LOGIN_USER");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>

<%
	if (mv != null) { // 로그인이 된 사용자라면 메인 페이지 출력하기
%>
	<p>환영합니다. <%=mv.getMemName() %>님! 여기는 메인 페이지입니다.</p>
	<p><a href="<%=request.getContextPath() %>/logout.do">로그아웃</p>
<%
	} else {
%>
	<p>아직 로그인을 하지 않으셨군요, 로그인 먼저 해주세요. &nbsp;&nbsp;</p>
	<p><a href="<%=request.getContextPath() %>/login.do">로그인 화면으로 이동</p>
<%
	}
%>

</body>
</html>