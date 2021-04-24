package gdu.diary.dao;

import java.sql.*;

import gdu.diary.util.DBUtil;

public class TodoDao {
	private DBUtil dbUtil;
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
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("TodoDao.deleteTodoByMember 응답");
		
		//삭제성고하면 1 실패하면 0리턴;
		return rowCnt;
	}
}
