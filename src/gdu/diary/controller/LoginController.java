package gdu.diary.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gdu.diary.service.MemberService;
import gdu.diary.vo.Member;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	
	private MemberService memberService;
	
	// 로그인 폼
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//뷰 페이지로 포워딩
		System.out.println("LoginController - > login.jsp 포워딩");
		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
		
	}
	
	// 로그인 액션
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//??
		this.memberService = new MemberService();
		
		//login.jsp 에서 받은 데이터
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		
		//받아온 데이터를 담을 멤버객체 생성 후 객체에 데이터저장
		Member member = new Member();
		member.setMemberId(memberId);
		member.setMemberPw(memberPw);
		
		//로그인 정보 확인후 일치하면 리턴멤버반환
		System.out.println("LoginController -> memberService.getMemberByKey 요청");
		Member returnMember = this.memberService.getMemberByKey(member);
		
		//로그인정보와 일치하는 멤버가 없음
		if(returnMember == null) {
			System.out.println("LoginController // 로그인 실패!");
			
		} else { //로그인정보와 멤버정보 일치함
			
			System.out.println("LoginController // 로그인 성공!");
			//세션에 로그인정보 저장(멤버넘버, 멤버아이디)
			HttpSession session = request.getSession();
			session.setAttribute("sessionMember", returnMember);
			
		}
		
		//로그인컨트롤러로 리다이렉트
		response.sendRedirect(request.getContextPath() + "/login");
		
	}
}