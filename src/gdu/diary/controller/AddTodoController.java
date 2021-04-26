package gdu.diary.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gdu.diary.service.TodoService;
import gdu.diary.vo.Member;
import gdu.diary.vo.Todo;
import gdu.diary.vo.TodoDate;


@WebServlet("/auth/addTodo")
public class AddTodoController extends HttpServlet {
	private TodoService todoService;
	
	//todo 입력폼
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//데이터 받아오기
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		int day = Integer.parseInt(request.getParameter("day"));
		
		//객체에 날짜 저장하기
		TodoDate todoDate = new TodoDate();
		todoDate.setYear(year);
		todoDate.setMonth(month);
		todoDate.setDay(day);
		//디버깅
		System.out.println(todoDate); // todoDate.toString();
		
		//리퀘스트 객체에 날짜 저장
		request.setAttribute("todoDate", todoDate);
		
		//뷰 페이지로 포워딩
		request.getRequestDispatcher("/WEB-INF/view/auth/addTodo.jsp").forward(request, response);
		
		
	}
	
	//todo액션
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//utf8인코딩은 필터에서 진행되므로 생략가능
		
		//세션 받아오기
		HttpSession session = request.getSession();
		int memberNo = ((Member)(session.getAttribute("sessionMember"))).getMemberNo();
		
		//post로 받아온 정보
		String todoDate = request.getParameter("todoDate");
		String todoTitle = request.getParameter("todoTitle");
		String todoContent = request.getParameter("todoContent");
		String todoFontColor = request.getParameter("todoFontColor");
		
		//투두객체 생성후 받아온 정보 저장
		Todo todo = new Todo();
		todo.setMemberNo(memberNo);
		todo.setTodoDate(todoDate);
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		todo.setTodoFontColor(todoFontColor);
		System.out.println(todo); // todo.toString()
		
		//  서비스 호출
		this.todoService = new TodoService();
		this.todoService.addTodo(todo);
		
		String[] arr = todoDate.split("-");
		
		//다이어리 컨트롤러로 년월 넘겨주면 리다이렉트
		response.sendRedirect(request.getContextPath()+"/auth/diary?targetYear="+arr[0]+"&targetMonth="+(Integer.parseInt(arr[1])-1));
		
	}
}
