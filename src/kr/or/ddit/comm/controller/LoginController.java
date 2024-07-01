package kr.or.ddit.comm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// get으로 들어오면 로그인 창으로 이동만 하기
		// 아주 작은 로직과 아주 작은 작업이라도, 반드시 컨트롤러를 만들어서 서블릿을 거쳐 지나갈 수 있도록 할 것!
		// 이유 : jsp를 바로 가면 에러 500이 터짐 -> 화면을 그리기 위한 데이터 없이 바로 화면으로 가기 때문에 널포인트에러 예외가 터짐
		// 각각의 정해진 역할이 있음, 웹브라우저 > 서블렛 > jsp의 과정을 거칠 것.
		req.getRequestDispatcher("/views/login/loginForm.jsp").forward(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// post로 요청이 들어오면 실제 작업 시작하기
		String memId = req.getParameter("memId");
		String memPass = req.getParameter("memPass");
		
		// 서비스 생성
		IMemberService memberService = new MemberServiceImpl().getInstance();
		
		MemberVO mv = memberService.getMember(memId);
		
		boolean isAuthenticated = false; // 인증된 사용자인지의 여부
		
		if (mv != null) {
			if (memPass.equals(mv.getMemPass())) { // 입력과 getMember()의 패스워드가 일치하면
				isAuthenticated = true; // 인가한다.
			}
		}
		
		///////////////////////////////////////
		
		if (isAuthenticated) { // 인증이 된 사용자라면
			// 세션에 로그인 정보 설정하기
			// 세션에 "LOGIN_USER"라는 속성이 있다면, 정상적으로 로그인한 사람으로 인정할 것임
			req.getSession().setAttribute("LOGIN_USER", mv); 
			
			// 메인 페이지로 이동하기
			resp.sendRedirect(req.getContextPath() + "/main.do");
		} else { // 인증 실패 시
			// 로그인 화면으로 이동하기
			resp.sendRedirect(req.getContextPath() + "/login.do");
		}
		
	}
	
}
