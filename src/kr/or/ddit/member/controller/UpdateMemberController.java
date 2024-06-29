package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

@WebServlet("/member/update.do")
public class UpdateMemberController extends HttpServlet {

	// doGet으로 오면 업데이트 창 띄우기
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memId = req.getParameter("memId");
		
		IMemberService memberService = MemberServiceImpl.getInstance();
		
		MemberVO mv = memberService.getMember(memId);

		// 리퀘스트 객체에 정보 저장
		req.setAttribute("mv", mv);
		
		req.getRequestDispatcher("/views/member/updateForm.jsp").forward(req, resp);
	}
	
	
	// doPost로 오면 실제 업데이트 실행
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 이걸 매번 넣는게 귀찮아서 사용하는 방법 -> 필터 사용하기
//		req.setCharacterEncoding("UTF-8");
		
		String memId = req.getParameter("memId");
		String memName = req.getParameter("memName");
		String memTel = req.getParameter("memTel");
		String memAddr = req.getParameter("memAddr");
		
		MemberVO mv = new MemberVO(memId, memName, memTel, memAddr);
		
		IMemberService memberService = MemberServiceImpl.getInstance();
		
		int cnt = memberService.modifyMember(mv);
		
		String msg = "";
		
		if (cnt > 0) {
			// 회원정보 수정 성공!
			msg = "SUCCESS";
		} else {
			// 회원정보 수정 실패
			msg = "FAIL";
		}
		
		// 메시지를 세션에 담음, 리다이렉션으로 보내면 반환되는 객체가 사라지기 때문에 정보를 남기기 위해서
		req.getSession().setAttribute("msg", msg);
		
		// 302 헤더의 로케이션 부분에 다시 요청을 날림
		resp.sendRedirect(req.getContextPath() + "/member/list.do");
		
	}
}
