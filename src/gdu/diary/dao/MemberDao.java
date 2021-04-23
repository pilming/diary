package gdu.diary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gdu.diary.util.DBUtil;
import gdu.diary.vo.Member;

public class MemberDao {
	
	private DBUtil dbUtil;
	
	//회원가입시 중복된아이디인지 체크하는 메서드 (id중복여부만 확인하면 되므로 아이디만 매개변수로 받아서 확인한다.) 중복 아이디가 true 아이디가 중복이면 false
	public boolean checkMemberById(Connection conn, String memberId) throws SQLException{
		this.dbUtil = new DBUtil();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(MemberQuery.CHECK_MEMBER_BY_ID);
			stmt.setString(1, memberId);
			//디버깅코드
			System.out.println("MemberDao.checkMemberById stmt // "+ stmt);
			rs = stmt.executeQuery();
			
			if(rs.next() == false) {
				System.out.println("MemberDao.checkMemberById // 중복없음 회원가입가능!");
				System.out.println("MemberDao.checkMemberById 응답");
				return true;
			}
			
		} finally {
			this.dbUtil.close(null, stmt, rs);
		}
		//디버깅코드
		System.out.println("MemberDao.checkMemberById // 아이디중복!");
		
		System.out.println("MemberDao.checkMemberById 응답");
		return false;
	}
	
	//멤버 비밀번호 변경하는 메서드
	public int updateMemberByKey(Connection conn, Member member) throws SQLException{
		this.dbUtil = new DBUtil();
		
		int returnRowCnt = 0;
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(MemberQuery.UPDATE_MEMBER_BY_KEY);
			stmt.setString(1, member.getMemberPw());
			stmt.setString(2, member.getMemberId()); 
			//디버깅코드
			System.out.println("MemberDao.updateMemberByKey stmt // "+ stmt);
			returnRowCnt = stmt.executeUpdate();
			
		} finally {
			//여기서 conn을 닫으면 안됨. 롤백 불가능
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.updateMemberByKey 응답");
		return returnRowCnt;
	}
	
	//멤버 추가하는 메서드
	public int insertMemberByKey(Connection conn, Member member)  throws SQLException{
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			stmt = conn.prepareStatement(MemberQuery.INSERT_MEMBER_BY_KEY);
			stmt.setString(1, member.getMemberId()); 
			stmt.setString(2, member.getMemberPw());
			//디버깅 코드
			System.out.println("MemberDao.insertMemberByKey stmt // "+ stmt);
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//여기서 conn을 닫으면 안됨. 롤백 불가능
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.insertMemberByKey 응답");
		return rowCnt;
	}
	
	public int deleteMemberByKey(Connection conn, Member member)  throws SQLException { //예외가 발생하면 서비스로 예외를 던져줘야됨. 그래야 롤백 가능.(여기서 예외를 처리해버리면 서비스에서 예외로 가지않으니 롤백이 안됨.)
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			stmt = conn.prepareStatement(MemberQuery.DELETE_MEMBER_BY_KEY);
			stmt.setInt(1, member.getMemberNo()); 
			stmt.setString(2, member.getMemberPw());
			//디버깅 코드
			System.out.println("MemberDao.deleteMemberByKey stmt // "+ stmt);
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//여기서 conn을 닫으면 안됨. 롤백 불가능
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.deleteMemberByKey 응답");
		return rowCnt;
	}
	
	public Member selectMemberByKey(Connection conn, Member member) throws SQLException {
		
		this.dbUtil = new DBUtil();
		Member returnMember = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		System.out.println("MemberDao.selectMemberByKey 매개변수로 받은 member //"+member);
		
		try {
			stmt = conn.prepareStatement(MemberQuery.SELECT_MEMBER_BY_KEY);
			stmt.setString(1, member.getMemberId());
			stmt.setString(2, member.getMemberPw());
			//디버깅 코드
			System.out.println("MemberDao.selectMemberByKey stmt // "+ stmt);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				returnMember = new Member();
				returnMember.setMemberNo(rs.getInt("memberNo"));
				returnMember.setMemberId(rs.getString("memberId"));
			}
		
			
		} finally {
			this.dbUtil.close(null, stmt, rs);
			
		}
		System.out.println("MemberDao.selectMemberByKey 응답");
		return returnMember;
	}
}
