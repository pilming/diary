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
		
		/*
		 * PreparedStatement은 Statement를 상속받음, 프리컴파일된 sql문을 나타내는 객체, db로 전달하는 역할을 한다. executeQuery()실행할수있음 사용시 반드시 throws 처리해야된다.
		 * ※컴파일이란?  사람이 이해할수있는 언어 sql을 기계가 이해할수있는언어(기계어)로 변환하는것
		 * ResultSet은 db로부터 실행된 결과값을 '임시'로 저장하는 객체 그래서 사용하려면 변환해서 사용해야함.
		 * 
		 */
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(MemberQuery.CHECK_MEMBER_BY_ID);
			//?에대한 데이터 설정
			stmt.setString(1, memberId);
			
			//디버깅코드
			System.out.println("MemberDao.checkMemberById stmt // "+ stmt);
			
			//sql실행후 ResultSet객체에 저장 (이미 위에서 sql을 전송했기때문에 executeQuery()에 매개변수로 sql적으면 안됨.)
			rs = stmt.executeQuery();
			
			//rs에 데이터가 없다면 sql실행 결과가 없다 = 중복이 없다.
			if(rs.next() == false) {
				System.out.println("MemberDao.checkMemberById // 중복없음 회원가입가능!");
				System.out.println("MemberDao.checkMemberById 응답");
				return true;
			}
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			this.dbUtil.close(null, stmt, rs);
		}
		//디버깅코드
		System.out.println("MemberDao.checkMemberById // 아이디중복!");
		
		System.out.println("MemberDao.checkMemberById 응답");
		
		//여기까지 진행된다면 중복되는 아이디가 존재한다.
		return false;
	}
	
	//멤버 비밀번호 변경하는 메서드
	public int updateMemberByKey(Connection conn, Member member) throws SQLException{
		this.dbUtil = new DBUtil();
		
		int returnRowCnt = 0;
		PreparedStatement stmt = null;
		try{
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(MemberQuery.UPDATE_MEMBER_BY_KEY);
			//sql ?에 데이터 설정
			stmt.setString(1, member.getMemberPw());
			stmt.setString(2, member.getMemberId()); 
			//디버깅코드
			System.out.println("MemberDao.updateMemberByKey stmt // "+ stmt);
			
			//sql실행
			returnRowCnt = stmt.executeUpdate();
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.updateMemberByKey 응답");
		
		//변경성공하면 1 실패하면 0 리턴;
		return returnRowCnt;
	}
	
	//멤버 추가하는 메서드
	public int insertMemberByKey(Connection conn, Member member)  throws SQLException{
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(MemberQuery.INSERT_MEMBER_BY_KEY);
			//sql ?에 데이터 설정
			stmt.setString(1, member.getMemberId()); 
			stmt.setString(2, member.getMemberPw());
			
			//디버깅 코드
			System.out.println("MemberDao.insertMemberByKey stmt // "+ stmt);
			//sql실행
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.insertMemberByKey 응답");
		//변경성공하면 1 실패하면 0 리턴;
		return rowCnt;
	}
	
	public int deleteMemberByKey(Connection conn, Member member)  throws SQLException { //예외가 발생하면 서비스로 예외를 던져줘야됨. 그래야 롤백 가능.(여기서 예외를 처리해버리면 서비스에서 예외로 가지않으니 롤백이 안됨.)
		this.dbUtil = new DBUtil();
		
		int rowCnt = 0;
		PreparedStatement stmt = null;
		
		try{
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(MemberQuery.DELETE_MEMBER_BY_KEY);
			//sql ?에 데이터 설정
			stmt.setInt(1, member.getMemberNo()); 
			stmt.setString(2, member.getMemberPw());
			
			//디버깅 코드
			System.out.println("MemberDao.deleteMemberByKey stmt // "+ stmt);
			
			//sql실행
			rowCnt = stmt.executeUpdate();
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			this.dbUtil.close(null, stmt, null);
		}
		System.out.println("MemberDao.deleteMemberByKey 응답");
		//변경성공하면 1 실패하면 0 리턴;
		return rowCnt;
	}
	
	public Member selectMemberByKey(Connection conn, Member member) throws SQLException {
		
		this.dbUtil = new DBUtil();
		Member returnMember = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		System.out.println("MemberDao.selectMemberByKey 매개변수로 받은 member //"+member);
		
		try {
			//데이터가 빠진 sql을 컴파일해서 전송 
			stmt = conn.prepareStatement(MemberQuery.SELECT_MEMBER_BY_KEY);
			//sql ?에 데이터 설정
			stmt.setString(1, member.getMemberId());
			stmt.setString(2, member.getMemberPw());
			//디버깅 코드
			System.out.println("MemberDao.selectMemberByKey stmt // "+ stmt);
			//sql실행
			rs = stmt.executeQuery();
			
			//rs가 참이라면(임시데이터가 있다면) 임시데이터를 리턴멤버객체에 저장
			if(rs.next()) {
				returnMember = new Member();
				returnMember.setMemberNo(rs.getInt("memberNo"));
				returnMember.setMemberId(rs.getString("memberId"));
			}
		
			
		} finally {
			//conn을 닫아버리면 커밋과 롤백을 할수없음. conn close는 서비스에서 실행
			this.dbUtil.close(null, stmt, rs);
			
		}
		System.out.println("MemberDao.selectMemberByKey 응답");
		//리턴멤버 리턴
		return returnMember;
	}
}
