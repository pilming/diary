package gdu.diary.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.diary.service.DiaryService;

@WebServlet("/auth/diary")
public class DiaryController extends HttpServlet {
	private DiaryService diaryService;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.diaryService = new DiaryService();
		//받은데이터
		String targetYear = request.getParameter("targetYear"); // "2021",...null
		String targetMonth = request.getParameter("targetMonth"); // "4",...null
		
		//맵에 해당년도,월의 달력을 담음
		System.out.println("DiaryController -> diaryService.getDiary 요청");
		Map<String, Object> diaryMap = this.diaryService.getDiary(targetYear, targetMonth);
		
		//리퀘스트객체에 달력저장
		request.setAttribute("diaryMap", diaryMap);
		
		//뷰로 포워딩
		System.out.println("DiaryController -> diary.jsp 포워딩");
		request.getRequestDispatcher("/WEB-INF/view/auth/diary.jsp").forward(request, response);
	}
}
