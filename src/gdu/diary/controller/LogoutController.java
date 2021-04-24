package gdu.diary.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/logout")
public class LogoutController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//리퀘스트에서 세션 받아옴
		HttpSession session = request.getSession();
		
		//세션 초기화
		System.out.println("LogoutController // 세션 초기화");
		session.invalidate();
		
		//로그아웃 후 로그인컨트롤러로 리다이렉트
		System.out.println("LogoutController -> LoginController 리다이렉트");
		response.sendRedirect(request.getContextPath() + "/login");
		
	}

}
