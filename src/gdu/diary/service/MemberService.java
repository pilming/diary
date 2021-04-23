package gdu.diary.service;

import java.sql.Connection;
import java.sql.SQLException;

import gdu.diary.dao.MemberDao;
import gdu.diary.dao.TodoDao;
import gdu.diary.util.DBUtil;
import gdu.diary.vo.Member;

public class MemberService {
	
	private DBUtil dbUtil;
	private MemberDao memberDao;
	private TodoDao todoDao;
	
	//회원정보 수정(성공하면 true 실패하면 false)
	public boolean modifyMemberByKey(Member member) {
		this.dbUtil = new DBUtil();
		this.memberDao = new MemberDao(); 
		Connection conn = null;
		int rowCnt = 0;
		
		try {
			conn = dbUtil.getConnection();
			rowCnt = this.memberDao.updateMemberByKey(conn, member);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
			return false;
		} finally {
			dbUtil.close(conn, null, null);
		}
		
		return rowCnt > 0;
	}
	//회원가입
	public int addMemberByKey(Member member) {
		int returnCnt = 0;
		this.dbUtil = new DBUtil();
		this.memberDao = new MemberDao();
		Connection conn = null;
		
		try {
			conn = dbUtil.getConnection();
			returnCnt = this.memberDao.insertMemberByKey(conn, member);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			dbUtil.close(conn, null, null);
		}
		
		return returnCnt;
		
	}
	//회원삭제 성공하면 true(커밋) 실패하면 false(롤백)
	public boolean removeMemberByKey(Member member) {
		this.dbUtil = new DBUtil();
		this.memberDao = new MemberDao(); 
		this.todoDao = new TodoDao();
		Connection conn = null;
		int todoRowCnt = 0;
		int memberRowCnt = 0;
		
		try {
			conn = dbUtil.getConnection();
			todoRowCnt = this.todoDao.deleteTodoByMember(conn, member.getMemberNo());
			memberRowCnt = this.memberDao.deleteMemberByKey(conn, member);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
			return false;
		} finally {
			dbUtil.close(conn, null, null);
		}
		
		return (todoRowCnt + memberRowCnt) > 0;
	}
	
	public Member getMemberByKey(Member member) {
		
		this.dbUtil = new DBUtil();
		Member returnMember = null;
		this.memberDao = new MemberDao(); 
		Connection conn = null;
		
		try {
			
			conn = dbUtil.getConnection();
			returnMember = this.memberDao.selectMemberByKey(conn, member);
			System.out.println(returnMember);
			conn.commit();
			
		} catch(SQLException e) {
			
			try {
				conn.rollback();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				
			}
			e.printStackTrace();
			
		} finally {
			this.dbUtil.close(conn, null, null);
			
		}
		return returnMember;
	}
}
