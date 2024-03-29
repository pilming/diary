package gdu.diary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gdu.diary.util.DBUtil;
import gdu.diary.vo.Todo;

public class TodoDao {
	private DBUtil dbUtil;
	
	//dday출력
	public List<Map<String, Object>> selectTodoDdayList(Connection conn, int memberNo) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(TodoQuery.SELECT_TODO_DDAY_LIST);
			stmt.setInt(1, memberNo);
			//디버깅코드.
			System.out.println("TodoDao.selectTodoDdayList stmt // "+ stmt);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("todoNo", rs.getInt("todoNo"));
				map.put("todoDate", rs.getString("todoDate"));
				map.put("todoTitle", rs.getString("todoTitle"));
				map.put("dday", rs.getInt("dday"));
				list.add(map);
			}
		} finally {
			//여기서는 conn close하면 안됨. 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			
			rs.close(); // rs가 null일경우 예외발생하지만 어차피 나중에 rs를 쓰지않을것임으로 그냥 무시.
			stmt.close();
		}
		
		System.out.println("TodoDao.selectTodoDdayList 응답");
		return list;
	}
	
	public int deleteTodoOne(Connection conn, int todoNo, int memberNo) throws SQLException {
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(TodoQuery.DELETE_TODO);
			//sql ?에 데이터 설정
			stmt.setInt(1, todoNo); 
			stmt.setInt(2, memberNo);
			
			//디버깅 코드
			System.out.println("TodoDao.deleteTodoOne stmt // "+ stmt);
			
			//sql실행
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			stmt.close();
		}
		System.out.println("TodoDao.deleteTodoOne 응답");
		//변경성공하면 1 실패하면 0 리턴;
		return rowCnt;
	}
	public int updateTodoOne(Connection conn, Todo modifyTodo, int memberNo) throws SQLException {
		int returnRowCnt = 0;
		PreparedStatement stmt = null;
		//UPDATE todo SET todo_title = ?, todo_content = ?, todo_font_color = ? WHERE todo_no=?
		try {
			stmt = conn.prepareStatement(TodoQuery.UPDATE_TODO);
			stmt.setString(1, modifyTodo.getTodoTitle());
			stmt.setString(2, modifyTodo.getTodoContent());
			stmt.setString(3, modifyTodo.getTodoFontColor());
			stmt.setInt(4, modifyTodo.getTodoNo());
			stmt.setInt(5, memberNo);
			
			System.out.println("TodoDao.updateTodo stmt // "+ stmt);
			
			returnRowCnt = stmt.executeUpdate();
			
		} finally{
			stmt.close();
		}
		
		System.out.println("TodoDao.updateTodoOne 응답");
		return returnRowCnt;
	}
	
	public Todo selectTodoOne(Connection conn, int todoNo, int memberNo) throws SQLException {
		Todo returnTodoOne = new Todo();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(TodoQuery.SELECT_TODO_ONE_BY_NO);
			stmt.setInt(1, todoNo);
			stmt.setInt(2, memberNo);
			System.out.println("TodoDao.selectTodoOne stmt // "+ stmt);
			
			rs = stmt.executeQuery();
			//SELECT todo_date todoDate, todo_title todoTitle, todo_content todoContent, todo_font_color todoFontColor, todo_add_date todoAddDate FROM todo WHERE member_no = ? AND todoNo = ?
			if(rs.next()) {
				returnTodoOne.setTodoNo(todoNo);
				returnTodoOne.setTodoDate(rs.getString("todoDate"));
				returnTodoOne.setTodoTitle(rs.getString("todoTitle"));
				returnTodoOne.setTodoContent(rs.getString("todoContent"));
				returnTodoOne.setTodoFontColor(rs.getString("todoFontColor"));
				returnTodoOne.setTodoAddDate(rs.getString("todoAddDate"));
			}
		}finally {
			rs.close();
			stmt.close();
		}
		
		System.out.println("TodoDao.selectTodoOne 응답");
		return returnTodoOne;
	}
	
	public List<Todo> selectTodoListByDate(Connection conn, int memberNo, int targetYear, int targetMonth) throws SQLException {
		List<Todo> returnList = new ArrayList<Todo>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(TodoQuery.SELECT_TODO_LIST_BY_DATE);
			stmt.setInt(1, memberNo);
			stmt.setInt(2, targetYear);
			stmt.setInt(3, targetMonth);
			System.out.println("TodoDao.selectTodoListByDate stmt // "+ stmt);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				Todo todo = new Todo();
				todo.setTodoNo(rs.getInt("todoNo"));
				todo.setMemberNo(memberNo);
				todo.setTodoDate(rs.getString("todoDate"));
				todo.setTodoTitle(rs.getString("todoTitle"));
				todo.setTodoFontColor(rs.getString("todoFontColor"));
				returnList.add(todo);
			}
		}finally {
			rs.close();
			stmt.close();
		}
		
		System.out.println("TodoDao.selectTodoListByDate 응답");
		return returnList;
	}
	
	public int insertTodo(Connection conn, Todo todo) throws SQLException {
		int returnRowCnt = 0;
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(TodoQuery.INSERT_TODO);
			stmt.setInt(1, todo.getMemberNo());
			stmt.setString(2, todo.getTodoDate());
			stmt.setString(3, todo.getTodoTitle());
			stmt.setString(4, todo.getTodoContent());
			stmt.setString(5, todo.getTodoFontColor());
			System.out.println("TodoDao.insertTodo stmt // "+ stmt);
			
			returnRowCnt = stmt.executeUpdate();
			
		} finally{
			stmt.close();
		}
		System.out.println("TodoDao.insertTodo 응답");
		return returnRowCnt;
	}
	
	public int deleteTodoByMember(Connection conn, int memberNo) throws SQLException {
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			//데이터가 빠진 sql 전송
			stmt = conn.prepareStatement(TodoQuery.DELETE_TODO_BY_MEMBER);
			//빠진 ?에 데이터 설정
			stmt.setInt(1, memberNo);
			
			//sql실행
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			stmt.close();
		}
		System.out.println("TodoDao.deleteTodoByMember 응답");
		
		//삭제성고하면 1 실패하면 0리턴;
		return rowCnt;
	}
}
