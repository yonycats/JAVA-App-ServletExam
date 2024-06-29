<%-- 

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
-> 위를 페이지 디렉티브라고 부름, JSP파일에는 무조건 있어야 하며 페이지 디렉터리가 없으면 JSP파일이 아님



1. 디렉티브 (Directive) 에 대하여

 JSP 페이지에 대한 설정 정보를 저장할 때 사용된다. (page, taglib, include 등)

 '<%@' 로 시작하고 그 뒤에 디렉티브 이름이 오고 필요에 따라 속성명이 올 수 있으며, 마지막에 '%>' 로 끝난다.
 
 ex) <%@ 디렉티브이름 속성명="속성값" ... %>
 
 
2. 스크립트 요소에 대하여
  
  JSP에서 문서의 내용을 동적으로 생성하기 위해 사용된다.
  
  - 표현식(Expression) : 값을 출력결과에 포함시키고자 할때 사용한다. ex) <%=값 %>
  - 스크립트 릿(Scriptlet) : 자바코드를 작성할 때 사용한다. ex) <% 자바코드들... %>
  - 선언부(Declaration) : 스크립트 릿이나 표현식에서 사용할 수 있는 메서드를 작성할 때 사용한다. ex) <%! ~~~ %>


3. JSP 기본객체와 영역 (SCOPE) 

- JSP에서 가장 기본이 되는 객체
- 자주 사용하는 객체를 선언하지 않고도 자동으로 만들어주며, 마치 선언한 것처럼 사용할 수 있는 객체들이 있음
- 서블릿 컨텍스트 객체라고 보면 됨
	
  - PAGE 영역 : 하나의 JSP페이지를 처리할 때 사용되는 영역 => pageContext
  - REQUEST 영역 : 하나의 HTTP 요청을 처리할 때 사용되는 영역 => request
  - SESSION 영역 : 하나의 웹브라우저(사용자)와 관련된 영역 => session
  - APPLICATION 영역 : 하나의 웹패을리케이션과 관련된 영역 => application

--%>


<%--  JSP 기본 객체를 사용하여 페이지 만들기 --%>

<%@page import="kr.or.ddit.vo.MemberVO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	List<MemberVO> memList = (List<MemberVO>)request.getAttribute("memList");

	// 넣은 값을 가져오고, null이면 빈칸으로 초기화시키고, null이 아니면 그대로 가져옴 (오류방지), 삼항연산자 사용
// 	String msg = (String)request.getAttribute("msg") == null ? "" : (String)request.getAttribute("msg");
	
	// 세션에 넣은 값으로 다시 가져오기
	String msg = session.getAttribute("msg") == null ? "" : (String)session.getAttribute("msg");
	
	// 세션에 알림창 정보를 넣었기 때문에 새로고침해도 계속 알림창이 뜸
	// 새로고침해도 알림창이 나오지 않도록 할 것임
	session.removeAttribute("msg"); // 한번 사용한 데이터를 삭제하기
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>

<style>
table {
	border: 3px solid pink;
}
th {
	border: 3px dotted red;
	width : 100px;
	height : 30px;
	background-color: lime;
}
td {
	border: 2px solid blue;
	width : 100px;
	height : 30px;
}
</style>

</head>
<body>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>이름</th>
			<th>전화번호</th>
			<th>주소</th>
			<th>첨부파일</th>
		</tr>
		
<%
	int memSize = memList.size();
	if (memSize > 0) {
		for (MemberVO mv : memList) {
%>	
		<tr>
			<%-- 1. out이라는 내장 객체 이용하기 --%>
			<%-- 	<td><% out.print(mv.getMemId()); %></td> --%>
			<%-- 2. 표현식 이용하기 -> 더 간단함, 주로 표현식을 이용해서 코딩을 할 것임 --%>
			<td><%= mv.getMemId() %></td>
			<%-- 이름을 클릭하면 상세화면 보이도록 링크걸기 --%>
			<td><a href="<%=request.getContextPath() %>/member/detail.do?memId=<%= mv.getMemId() %>"><%= mv.getMemName() %></td>
			<td><%= mv.getMemTel() %></td>
			<td><%= mv.getMemAddr() %></td>
			<td><%= mv.getAtchFileId() %></td>
		</tr>
<%
		} // for문
		
	} else {
%>	
		<tr>
			<td colspan="5">회원정보가 존재하지 않습니다.</td>
		</tr>
<%	
	} // if문
%>	
		
		<tr align="center">
			<td colspan="5"><a href="<%=request.getContextPath() %>/member/insert.do">[회원 등록]</a></td>
		</tr>

	</table>
	
<%-- 정상적으로 처리되었을 때만 알림창이 뜨도록 할 것임 --%>
<%
	if (msg.equals("SUCCESS")) {
%>
<script>
// 정상적으로 처리되었을 때만 알림창이 뜨도록 할 것임
	alert('정상적으로 처리되었습니다.');
</script>
<%
	}
%>

</body>
</html>