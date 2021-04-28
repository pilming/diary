package gdu.diary.service;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gdu.diary.dao.TodoDao;
import gdu.diary.util.DBUtil;
import gdu.diary.vo.Todo;

public class DiaryService {
	private DBUtil dbUtil;
	private TodoDao todoDao;
	public Map<String, Object> getDiary(int memberNo,String targetYear, String targetMonth) {
		// 타겟 년, 월, 일(날짜)
		// 타겟의 1일(날짜)
		// 타겟의 마지막일의 숫자 -> endDay
		
		// startBlank + endDay + (7-(startBlank + endDay)%7)
		// 전체 셀의 갯수(마지막일의 숫자보다는 크고 7로 나누어 떨어져야 한다)
		// 앞의 빈셀의 갯수 -> startBlank
		// 이번달 숫자가 나와야 할 셀의 갯수(1~마지막날짜)
		// 뒤의 빈셀의 갯수 -> endBlank -> (startBlank + endDay)%7 
		
		//달력을 담을 맵객체 생성
		Map<String, Object> map = new HashMap<String, Object>();
		
		//캘린더 인스턴스로 날짜 정보가져옴
		Calendar target = Calendar.getInstance(); //현재 달
		Calendar preTarget = Calendar.getInstance(); // 이전 달
		Calendar postTarget = Calendar.getInstance(); //다음 달
	
		if(targetYear != null && targetMonth != null) {
			
			//System.out.println("@@@@@@@@targetYear : " + targetYear);
			target.set(Calendar.YEAR, Integer.parseInt(targetYear));
			//System.out.println("@@@@@@@@targetMonth : " +targetMonth);
			target.set(Calendar.MONTH, Integer.parseInt(targetMonth)); //캘린더가 자동으로 targetMonth가 -1 이면 년도 -1하고 월을 11로(우리가볼땐 12월) 설정, targetMonth이 12일경우 자동으로 년도 +1 하고 월을 0으로(우리가볼땐 1월) 

		}
		
		//이전달 다음달 년 월 설정
		preTarget.set(Calendar.YEAR, target.get(Calendar.YEAR));
		preTarget.set(Calendar.MONTH, target.get(Calendar.MONTH)-1); //이전 달
		preTarget.set(Calendar.DATE, 1);//1일로 설정
		
		postTarget.set(Calendar.YEAR, target.get(Calendar.YEAR));
		postTarget.set(Calendar.MONTH,  target.get(Calendar.MONTH)+1); //다음 달
		postTarget.set(Calendar.DATE, 1);//1일로 설정
		

		/* 
		 * 
		 * 위의 캘린터 클래스 내부적으로 아래의 코드와 비슷한 코드가 진행된다.
		 * 
		 * 
		//초기 연월 초기화
		int numTargetMonth = 0;
		int numTargetYear = 0;
		
		//get으로 받은 연 월이 있다면 그 정보로 연월 설정
		if(targetMonth != null && targetYear != null) {
			numTargetYear = Integer.parseInt(targetYear);
			numTargetMonth = Integer.parseInt(targetMonth);
			
			//전달받은 Month값이 0 이라면 년도에서 1년 빼고 월을 12월로 설정
			if(numTargetMonth == 0) {
				numTargetYear = numTargetYear - 1;
				numTargetMonth = 12;
			} else if(numTargetMonth == 13) { //전달받은 Month값이 13 이라면 년도에서 1년 더하고 월을 1월로 설정
				numTargetYear = numTargetYear + 1;
				numTargetMonth = 1;
			}
			
			//날짜 정보를 get으로 받은 연 월값으로 변경
			target.set(Calendar.YEAR, numTargetYear);
			target.set(Calendar.MONTH, numTargetMonth-1); //캘린더는 0을 1월로 인식하기때문에 우리가 사용하는 월에서 -1해서 저장함
		}
		*/
		
		//이전달의 마지막날
		int preMonthEndDay = preTarget.getActualMaximum(Calendar.DATE);
		
		//target월을 1일로 설정
		target.set(Calendar.DATE, 1);
		
		// target월의 1숫자앞에 와야할 빈셀의 갯수
		int startBlank = target.get(Calendar.DAY_OF_WEEK) - 1;
		
		// target월의 마지막 날짜
		int endDay = target.getActualMaximum(Calendar.DATE);
		
		//마지막날은 보여주고 남은 칸을 채우기위한 변수
		int endBlank = 0;
		if ((startBlank + endDay) % 7 != 0) {
			endBlank = 7 - (startBlank + endDay)%7;
		}
		// int totalCell = startBlank + endDay + endBlank; 
		
		//맵에 저장
		//이전달
		map.put("preTargetYear", preTarget.get(Calendar.YEAR));
		map.put("preTargetMonth", preTarget.get(Calendar.MONTH));
		
		//보여줄 달
		map.put("targetYear", target.get(Calendar.YEAR));
		map.put("targetMonth", target.get(Calendar.MONTH));
		
		//다음달
		map.put("postTargetYear", postTarget.get(Calendar.YEAR));
		map.put("postTargetMonth", postTarget.get(Calendar.MONTH));
		
		map.put("startBlank", startBlank);
		map.put("endDay", endDay);
		map.put("preMonthEndDay", preMonthEndDay); //이전달의 마지막날
		map.put("endBlank", endBlank);
		
		//투두리스트와 dday추카
		
		this.todoDao = new TodoDao();
		this.dbUtil = new DBUtil();
		List<Todo> todoList = null;
		List<Todo> preMonthTodoList = null;
		List<Todo> postMonthTodoList = null;
		
		List<Map<String, Object>> ddayList = null;
		Connection conn = null;
		
		try {
			conn = this.dbUtil.getConnection();
			
			//현재 보여주기위한 달의 todoList
			System.out.println("DiaryService.getDiary -> todoDao.selectTodoListByDate 요청");
			todoList = this.todoDao.selectTodoListByDate(conn, memberNo,target.get(Calendar.YEAR), target.get(Calendar.MONTH)+1); //db에서 검색할때는 Calendar표기가 아닌 일반적인 표기로 전달
			
			//이전 달의 todoList
			System.out.println("DiaryService.getDiary -> todoDao.selectTodoListByDate 요청");
			preMonthTodoList = this.todoDao.selectTodoListByDate(conn, memberNo, preTarget.get(Calendar.YEAR), preTarget.get(Calendar.MONTH)+1);
			
			//다음달의 todoList
			System.out.println("DiaryService.getDiary -> todoDao.selectTodoListByDate 요청");
			postMonthTodoList = this.todoDao.selectTodoListByDate(conn, memberNo, postTarget.get(Calendar.YEAR), postTarget.get(Calendar.MONTH)+1);
			
			//디데이 정보 가져오기
			System.out.println("DiaryService.getDiary -> todoDao.selectTodoDdayList 요청");
			ddayList = this.todoDao.selectTodoDdayList(conn, memberNo);
			
			//여기까지 문제없이 진행된다면 커밋
			conn.commit();
		} catch(SQLException e) {
			try {
				//예외가 발생한다면 롤백, 여기선 전부 select쿼리라서 롤백이 굳이 필요하지않지만 약속으로 넣기
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		map.put("todoList", todoList); //target월의 todoList
		map.put("preMonthTodoList", preMonthTodoList); //preTarget월의 todoList
		map.put("postMonthTodoList", postMonthTodoList); //postTarget월의 todoList
		map.put("ddayList", ddayList); // dday정보 리턴 할 맵에 저장
		
		//맵 리턴
		System.out.println("DiaryService.getDiary 응답");		
		return map;
	}
}