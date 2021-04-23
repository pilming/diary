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
			stmt = conn.prepareStatement(TodoQuery.DELETE_TODO_BY_MEMBER);
			stmt.setInt(1, memberNo); 
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//여기서 conn을 닫으면 안됨. 롤백 불가능
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("TodoDao.deleteTodoByMember 응답");
		return rowCnt;
	}
}
