<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%--
	데이터 URL 스킴(data URL Scheme) 이란?
	- 외부 자원(이미지 등)을 문서(HTML, CSS, SVG 등)에 
	    인라인(inline) 방식의 데이터로 포함시킬 수 있는 방법을 제공하기 위한 
	    통합 자원 식별자(URL) 스킴(표현법)을 말한다.
	    
	형식) data:[<media type>][;base64],<data>
	형식) data:[<미디어 파일 타입>][;base64로 인코딩을 했다고 알려주는 부분],<이미지를 인코딩한 실제 데이터값>
	
 --%>
 
<%
	String base64Img = (String)request.getAttribute("base64Img");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Base64 인코딩된 이미지 데이터 사용하기</title>
</head>
<body>

<!-- 클라이언트가 주소창에 url로 사진을 접근할 수 있다
	 => WebContent 내부에 있는 파일들만  url로 접근할 수 있음. 
	 다른 곳에 파일이 있다면 url로 접근할 수가 없으며, 서블릿으로 접근이 가능함. -->

<%-- <img alt="" src="<%=request.getContextPath() %>/images/ala.jpg"> --%>
<!-- <img alt="" src="./images/ala.jpg"> -->


<%-- FileDownloadController(/download.do) 서블렛을 이용하여 이미지에 접근하기, 
	  서블렛을 이용하면 어디든지 접근할 수 있다. --%>
<!-- <img alt="" src="http://localhost:8888/ServletExam/download.do?atchFileId=1&fileSn=1"> -->


<!-- 데이터 URL 스킴 사용해서 이미지 접근하기 -->
<!-- base64로 인코딩한 데이터를 DB에 넣어놓고 보여주고 싶은 이미지가 있으면 서블렛에 경로를 출력하면 됨
	  아니면 경로에 없어도 하드코딩으로 이미지 인코딩 내용을 넣어놓고 출력해도 됨 -->
<img alt="" src="data:image/jpg;base64,<%=base64Img %>">

</body>
</html>