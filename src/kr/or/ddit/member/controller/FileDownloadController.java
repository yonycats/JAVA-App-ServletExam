package kr.or.ddit.member.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.comm.service.AtchFileServiceImpl;
import kr.or.ddit.comm.service.IAtchFileService;
import kr.or.ddit.comm.vo.AtchFileDetailVO;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 삼항 연산자 이용 -> null이면 -1 반환, 값이 있다면 반환값이 스트링이기 때문에 해당 타입으로 변환시켜줌
		long atchFileId = req.getParameter("atchFileId") == null ? -1 : Long.parseLong(req.getParameter("atchFileId"));
		int fileSn = req.getParameter("fileSn") == null ? 1 : Integer.parseInt(req.getParameter("fileSn"));
		
		// 키값(atchFileId, fileSn)을 이용해서 우리가 접근해야 할 파일을 상세정보를 가져오기
		IAtchFileService fileService = AtchFileServiceImpl.getInstance();
		
		AtchFileDetailVO atchFileDetailVO = new AtchFileDetailVO();
		
		atchFileDetailVO.setAtchFileId(atchFileId);
		atchFileDetailVO.setFileSn(fileSn);
		
		atchFileDetailVO = fileService.getAtchFileDetail(atchFileDetailVO);
		
		/*
		 	Content-Disposition이 파일 다운로드시 응답헤더로 사용되는 경우
		 	(inline : 읽을 것인가, attachment : 무지성으로 그냥 받을 것인가)
		 	
		 	// attachment를 넣으면 download.do로 다운이 됨
		 	// 파일이름을 넣고 싶을 때는 filename을 넣으면 됨
		 	
		 		Content-Disposition : inline (default)
		 		Content-Disposition : attachment; 					 // 파일 다운로드(@WebServlet 이름으로 다운로드)
		 		Content-Disposition : attachment; filename="abc.jpg" // abc.jpg 이름으로 파일 다운로드 --> 응답 메시지를 만들 때 사용할 것임
		 	
		 	바디에는 파일의 전체 내용을 넣을 예정
		 */
		
		// 파일 다운로드 처리를 위한 응답헤더 정보 설정하기
		// octet-stream : 일반 바이너리 데이터를 의미, 읽어오는 데이터가 바이너리 데이터임을 인식시켜줌
//		resp.setContentType("application/octet-stream");
		
		// inline을 사용하는 경우의 ContentType 사용, 하지만 주로 attachment를 쓸 것임
//		resp.setContentType("image/jpg"); // 웹에서 켜짐
		
//		resp.setHeader("Content-Disposition", "attachment"); // 서블렛 이름으로 다운받기
//		resp.setHeader("Content-Disposition", "attachment; filename=" + atchFileDetailVO.getOrignlFileNm()); // 기존이름으로 다운받기
		// 헤더 안에 따옴표로 넣어서 Content-Disposition : attachment; filename="abc.jpg" 라는 정해진 형식값을 그대로 넣은 것임, 혹시 몰라서 대비용으로
//		resp.setHeader("Content-Disposition", "attachment; filename=\""
//				+URLEncoder.encode(atchFileDetailVO.getOrignlFileNm(), "UTF-8") + "\""); // 기존이름으로 다운받기 + 한글 인코딩 하기
		
		// URL에는 문자를 포함할 수 없다. URLEncoding을 이용하여 인코딩 처리를 하면 
		// 공백은 (+)로 표시되기 때문에 (+)문자를 공백문자인 %20으로 바꿔서 처리해준다. 그래서 replace를 사용해서 바꿔줄 것임
		resp.setHeader("Content-Disposition", "attachment; filename=\""
				+URLEncoder.encode(atchFileDetailVO.getOrignlFileNm(), "UTF-8").replace("//+", "%20") + "\""); // 기존이름으로 다운받기 + 한글 인코딩 하기
		
		// getFileStreCours() -> 실제 파일의 저장경로 정보
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(atchFileDetailVO.getFileStreCours()));
		
		BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream());
		
		int data = 0;
		
		// 파일 정보를 읽는 족족 아웃풋에 넣어서 외부로 저장하고 있음
		while( (data = bis.read()) != -1 ) {
			bos.write(data);
		}
		bis.close();
		bos.close();
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
