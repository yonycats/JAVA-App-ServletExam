package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.comm.service.AtchFileServiceImpl;
import kr.or.ddit.comm.service.IAtchFileService;
import kr.or.ddit.comm.vo.AtchFileVO;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

@MultipartConfig
//@WebServlet(value = "/member/insert.do") // value값이 1개일 때는 value 생략 가능
@WebServlet("/member/insert.do")
public class InsertMemberController extends HttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 링크를 타고 들어가는 것도 get방식임
		// insert가 get방식으로 오면 사용자가 입력할 수 있는 회원가입 폼 페이지를 띄워주기
		req.getRequestDispatcher("/views/member/insertForm.jsp").forward(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// post방식(바디에 오는 방식 (폼 등))으로 오면 회원가입을 진행함
		String memId = req.getParameter("memId");
		String memName = req.getParameter("memName");
		String memTel = req.getParameter("memTel");
		String memAddr = req.getParameter("memAddr");
		
		IMemberService memberService = MemberServiceImpl.getInstance();
		IAtchFileService fileService = AtchFileServiceImpl.getInstance();
		
		// 첨부파일 저장하기
		// multipart 안에 첨부파일이 있으면 첨부파일을 저장하고, 첨부파일이 없으면 null이 저장됨
		AtchFileVO atchFileVO = fileService.saveAtchFileList(req.getParts());
		
		MemberVO mv = new MemberVO(memId, memName, memTel, memAddr);
		
		
		// 누군가 첨부파일을 올리면 atchFileVO 객체 안에다 getAtchFileId 값을 저장해줌
		if(atchFileVO != null) {
			mv.setAtchFileId(atchFileVO.getAtchFileId());
		}
		
		
		int cnt = memberService.registerMember(mv);
		
		String msg = "";
		
		if (cnt > 0) {
			// 회원정보 생성 성공!
			msg = "SUCCESS";
		} else {
			// 회원정보 생성 실패
			msg = "FAIL";
		}
		
		// 메시지를 세션에 담음, 리다이렉션으로 보내면 반환되는 객체가 사라지기 때문에 정보를 남기기 위해서
		req.getSession().setAttribute("msg", msg);
		
		// 1. 포워딩 처리하기
		// 포워딩방식의 가장 큰 특징은 원래의 url을 변경하지 않는다
		// 요청을 보낸 서블릿의 url로 매핑한 응답 서블릿에서 request scope의 데이터들을 그대로 쓸 수 있다.
		// 포워드 방식은 시스템에 변화를 주지 않는 단순 로직(조회, 검색 등)에 적합하다.
		
		// 아이디에 한글치면 오류남 -> 최대 글자수가 8자인데 한글치면 넘어감, 글자수 유의할 것
//		req.getRequestDispatcher("/member/list.do").forward(req, resp);
		

		// redirect(리다이렉트) 처리하기 
		// 요청 방향 틀기, 리다이렉트 처리를 하면 브라우저에 요청이 2번 날아가게 됨
		// 페이지 이동 요청을 보내면, url을 해당 url로 바꿔서 이동한다. 즉, url의 변경이 일어난다.
		// req, res 객체도 새롭게 생성된다. 처음 요청을 보낸 req, res 객체와 완전히 다른 것이다.
		// 따라서 리다이렉트 방식을 사용하면 request scope에 등록된 데이터를 사용할 수 없다.
		// 리다이렉트 방식은 반대로 시스템에 변화를 주는 로직(게시판 글 쓰기, 회원가입, 결제 등)을 수행하는 데에 적합하다.
		
		// 내용을 입력하고 submit을 보내서 리스트가 출력되도, url은 여전히 insert.do임 -> list.do로 바꾸기
		// 주소만 쓰면 컨텍스트 루트(애플리케이션 이름)가 빠져서 404 에러가 남
		resp.sendRedirect(req.getContextPath() + "/member/list.do");
		
		// 작성한 코드는 sendRedirect()메소드가 302방식으로 보내기 때문에 302코드로 뜬다.
		
	}
	
}
