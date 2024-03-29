package gdu.diary.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.diary.service.MemberService;
import gdu.diary.vo.Member;

@WebServlet("/auth/removeMember")
public class RemoveMemberController extends HttpServlet {
	private MemberService memberService;
	//비밀번호 입력 폼
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//뷰로 포워딩
		System.out.println("RemoveMemberController -> removeMember.jsp 포워딩");
		request.getRequestDispatcher("/WEB-INF/view/auth/removeMember.jsp").forward(request, response);
	}
	//삭제 액션
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//??
		this.memberService = new MemberService();
		
		//받아온 데이터 세션으로 얻은 멤버객체에 저장
		String  memberPw = request.getParameter("memberPw");
		Member member = (Member)request.getSession().getAttribute("sessionMember");
		member.setMemberPw(memberPw);
		
		//서비스로 삭제요청
		System.out.println("RemoveMemberController -> memberService.removeMemberByKey 요청");
		boolean result = this.memberService.removeMemberByKey(member);
		
		//삭제 실패시
		if(result == false) {
			System.out.println("회원탈퇴실패 RemoveMemberController -> RemoveMemberController 리다이렉트");
			response.sendRedirect(request.getContextPath()+"/auth/removeMember");
			return;
		}
		//삭제성공시 로그아웃컨트롤러로 리다이렉트
		System.out.println("회원탈퇴성공 RemoveMemberController -> LogoutController 리다이렉트");
		response.sendRedirect(request.getContextPath()+"/auth/logout");
	}
}
