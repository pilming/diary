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
			
			System.out.println("MemberService.modifyMemberByKey -> memberDao.updateMemberByKey 요청");
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
		System.out.println("MemberService.modifyMemberByKey 응답");
		return rowCnt > 0;
	}
	//회원가입
	public int addMemberByKey(Member member) {
		
		this.dbUtil = new DBUtil();
		this.memberDao = new MemberDao();
		Connection conn = null;
		
		int insertRowCnt =0;
		
		try {
			conn = dbUtil.getConnection();
			
			//아이디가 중복되지않을때 회원등록실행
			System.out.println("MemberService.addMemberByKey -> memberDao.checkMemberById 요청");
			if(this.memberDao.checkMemberById(conn, member.getMemberId()) == true ) {
				System.out.println("MemberService.addMemberByKey -> memberDao.insertMemberByKey 요청");
				insertRowCnt = this.memberDao.insertMemberByKey(conn, member);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			dbUtil.close(conn, null, null);
		}
		
		System.out.println("MemberService.addMemberByKey 응답");
		return insertRowCnt;
		
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
			
			System.out.println("MemberService.removeMemberByKey -> todoDao.deleteTodoByMember 요청");
			todoRowCnt = this.todoDao.deleteTodoByMember(conn, member.getMemberNo());
			
			System.out.println("MemberService.removeMemberByKey -> memberDao.removeMemberByKey 요청");
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
			System.out.println("MemberService.removeMemberByKey 응답");
			return false;
		} finally {
			dbUtil.close(conn, null, null);
		}
		System.out.println("MemberService.removeMemberByKey 응답");
		return (todoRowCnt + memberRowCnt) > 0;
	}
	
	public Member getMemberByKey(Member member) {
		
		this.dbUtil = new DBUtil();
		Member returnMember = null;
		this.memberDao = new MemberDao(); 
		Connection conn = null;
		
		try {
			
			conn = dbUtil.getConnection();
			
			System.out.println("MemberService.getMemberByKey -> memberDao.selectMemberByKey 요청");
			returnMember = this.memberDao.selectMemberByKey(conn, member);
			
			System.out.println("MemberService.getMemberByKey returnMember // "+returnMember);
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
		System.out.println("MemberService.getMemberByKey 응답");
		return returnMember;
	}
}
