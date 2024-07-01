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
		
		if (mv.getAtchFileId() > 0) { // 첨부파일이 존재하는 경우
			IAtchFileService fileService = AtchFileServiceImpl.getInstance();
			
			AtchFileVO atchFileVO = new AtchFileVO();
			atchFileVO.setAtchFileId(mv.getAtchFileId());
			atchFileVO = fileService.getAtchFile(atchFileVO);
			
			// 파일이 있을 때만 값이 있고, 파일이 없을 때는 항상 null일 것임
			req.setAttribute("atchFileVO", atchFileVO);
		}
		
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
		
		//
		long atchFileId = req.getParameter("atchFileId") == null ? -1 : Long.parseLong(req.getParameter("atchFileId")); // 기존 첨부파일 ID
		//
		
		//
		// 파일 업로드 하는 부분
		IMemberService memberService = MemberServiceImpl.getInstance();
		IAtchFileService fileService = AtchFileServiceImpl.getInstance();
		
		// 파일 업로드 처리하는 부분
		// 새로 업로드한(수정에서) 파일 ID가 리턴됨
		AtchFileVO atchFileVO = fileService.saveAtchFileList(req.getParts());
		
		MemberVO mv = new MemberVO(memId, memName, memTel, memAddr);
		
		// 신규 파일 업로드시(업데이트에서 새로 파일 업로드)
		// 누군가 첨부파일을 올리면 atchFileVO 객체 안에다 getAtchFileId 값을 저장해줌
		if(atchFileVO != null) { // 새로 업로드 파일을 선택한 경우
			mv.setAtchFileId(atchFileVO.getAtchFileId());
		} else { // null인 경우 = 새로운 첨부파일을 선택하지 않은 경우 (기존 첨부파일을 유지하고 싶은 경우)
			mv.setAtchFileId(atchFileId);
		}
		//
		
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
