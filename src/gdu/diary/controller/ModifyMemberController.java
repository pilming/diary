package gdu.diary.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.diary.service.MemberService;
import gdu.diary.vo.Member;


@WebServlet("/auth/modifyMember")
public class ModifyMemberController extends HttpServlet {
	private MemberService memberService;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ModifyMemberController -> modifyMember.jsp 포워딩");
		request.getRequestDispatcher("/WEB-INF/view/auth/modifyMember.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.memberService = new MemberService();
		
		//받아온 데이터
		String memberId = request.getParameter("memberId");
		String newMemberPw = request.getParameter("newMemberPw");
		
		//멤버객체에 저장
		Member member = new Member();
		member.setMemberId(memberId);
		member.setMemberPw(newMemberPw);
		
		System.out.println("ModifyMemberController -> memberService.modifyMemberByKey 요청");
		boolean result = this.memberService.modifyMemberByKey(member);
		
		if(result == false) {
			System.out.println("ModifyMemberController // 회원정보 수정 실패");
			
		} else {
			System.out.println("ModifyMemberController // 회원정보 수정 성공");
		}
		
		System.out.println("ModifyMemberController -> LogoutController 리다이렉트");
		response.sendRedirect(request.getContextPath()+"/auth/logout");
	}

}
