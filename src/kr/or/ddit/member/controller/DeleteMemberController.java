package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import sun.applet.resources.MsgAppletViewer;

@WebServlet("/member/delete.do")
public class DeleteMemberController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String memId = req.getParameter("memId");
		
		IMemberService memberService = MemberServiceImpl.getInstance();
		
		int cnt = memberService.removeMember(memId);

		String msg = "";
		
		if (cnt > 0) {
			// 회원정보 삭제 성공!
			msg = "SUCCESS";
		} else {
			// 회원정보 삭제 실패
			msg = "FAIL";
		}
		
		// 메시지를 세션에 담음, 리다이렉션으로 보내면 반환되는 객체가 사라지기 때문에 정보를 남기기 위해서
		req.getSession().setAttribute("msg", msg);

		resp.sendRedirect(req.getContextPath() + "/member/list.do");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
