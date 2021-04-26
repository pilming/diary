package gdu.diary.service;


import java.sql.Connection;
import java.sql.SQLException;
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
		Calendar target = Calendar.getInstance();
		
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
		
		//연 월을 설정했으닌 일을 1일로 설정
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
		map.put("targetYear", target.get(Calendar.YEAR));
		map.put("targetMonth", target.get(Calendar.MONTH)+1); //위와 마찬가지로 캘린더는 0을 1월로 인식하기때문에 뷰에서 보여줄때는 +1해서 사람이 알아볼수있게함
		map.put("startBlank", startBlank);
		map.put("endDay", endDay);
		map.put("endBlank", endBlank);
		
		//
		
		this.todoDao = new TodoDao();
		this.dbUtil = new DBUtil();
		List<Todo> todoList = null;
		Connection conn = null;
		
		try {
			conn = this.dbUtil.getConnection();
			todoList = this.todoDao.selectTodoListByDate(conn, memberNo,target.get(Calendar.YEAR), target.get(Calendar.MONTH)+1);
			
			conn.commit();
		} catch(SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		map.put("todoList", todoList);
		
		
		//맵 리턴
		System.out.println("DiaryService.getDiary 응답");		
		return map;
	}
}