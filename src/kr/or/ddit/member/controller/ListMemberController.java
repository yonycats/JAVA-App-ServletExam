package kr.or.ddit.member.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

// 어노테이션(주석) 부여하기
// xml 파일에서 연결하지 않고 주석으로 url 바로 연결하기 
// .do는 별도의 의미 부여, 패턴 넣기 -> 패턴을 주면 필터를 걸때도 선택하기 좋음
// localhost:8888/ServletExam/member/list.do 라는 주소로 웹에서 검색을 하면
// /member/list.do로 요청을 토스함

@WebServlet(value = "/member/list.do")
public class ListMemberController extends HttpServlet {

	// JSP -> 일종의 서블릿, 화면을 그려주는 역할 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 어떤 회원 정보를 받아서  html 문서를 만들어서 보내는 작업하기
		
		// 서비스 객체 생성하기
		IMemberService memberService = MemberServiceImpl.getInstance();
		
		// 아래 회원정보가 담긴 리스트를 jsp가 사용할 수 있도록 후처리
		List<MemberVO> memList = memberService.getTotalMember();
		
		// forward하기 위해서 리퀘스트 안에 memList를 넣어줌
		req.setAttribute("memList", memList);
		
		// 넣어준 데이터를 forward해서 jsp파일에 보내줌, 이제 list.jsp에 가서 틀 가공 작업
		// 화면 띄워보기, JSP로 토스
		// WebContent가 루트이므로 거기서부터 시작
		req.getRequestDispatcher("/views/member/list.jsp").forward(req, resp);
		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
