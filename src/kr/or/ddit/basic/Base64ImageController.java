package kr.or.ddit.basic;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

@WebServlet("/base64Img.do")
public class Base64ImageController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 자바에서 직접 base64로 인코딩해서 출력하기
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream("D:\\D_Other\\images\\bara6.webp"));
		// 주소창에 localhost:8888/ServletExam/base64Img.do를 기재하면 이미지가 뜸
		
		int data = 0;
		
		while ( (data = bis.read()) != -1 ) {
			baos.write(data);
		}
		
		bis.close(); // 자원반납
		
		String base64Img = Base64.getEncoder().encodeToString(baos.toByteArray());
		
		

		// 인터넷에서 base64로 인코딩해서 내용을 붙여넣기 해서 출력하기
//		String base64Img = ""; // 자꾸 멈춰서 지움
		
		req.setAttribute("base64Img", base64Img);
		
		req.getRequestDispatcher("/base64_img.jsp").forward(req, resp);
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
