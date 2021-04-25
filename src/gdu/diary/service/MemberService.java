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
			//db연결
			conn = dbUtil.getConnection();
			
			//conn을 매개변수로 전달한다.(커밋과 롤백을 서비스에서 처리하기위해서, 트랜젝션)
			System.out.println("MemberService.modifyMemberByKey -> memberDao.updateMemberByKey 요청");
			rowCnt = this.memberDao.updateMemberByKey(conn, member);
			
			//코드가 문제없이 진행됐다면 커밋 진행
			conn.commit();
		} catch (SQLException e) {
			try {
				//코드 진행도중 예외 발생시 db커밋하지말고 롤백
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			
			//회원정보 수정실패로 false반환
			return false;
		} finally {
			
			//서비스에서 커넥션 사용이 끝났기때문에 커넥션을 닫아준다.
			dbUtil.close(conn, null, null);
		}
		
		//정상적으로 수정이 됐다면 rowCnt에 1이 있고 수정이 안됐다면 0이 있음
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
			//db연결
			conn = dbUtil.getConnection();
			
			//아이디가 중복되지않을때 회원등록실행
			System.out.println("MemberService.addMemberByKey -> memberDao.checkMemberById 요청");
			if(this.memberDao.checkMemberById(conn, member.getMemberId()) == true ) {
				System.out.println("MemberService.addMemberByKey -> memberDao.insertMemberByKey 요청");
				insertRowCnt = this.memberDao.insertMemberByKey(conn, member);
			}
			
			//코드가 문제없이 진행됐다면 커밋
			conn.commit();
		} catch (SQLException e) {
			try {
				//코드 진행도중 예외 발생시 db커밋하지말고 롤백
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			//커넥션 사용 종료후 커넥션 닫아주기
			dbUtil.close(conn, null, null);
		}
		
		//결과 리턴 이런 단순 인트반환과 불리언 반환... 하나로 통일하는게 나을까?
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
			//db연결
			conn = dbUtil.getConnection();
			
			//회원이 작성한 투두리스트 전부 삭제
			System.out.println("MemberService.removeMemberByKey -> todoDao.deleteTodoByMember 요청");
			todoRowCnt = this.todoDao.deleteTodoByMember(conn, member.getMemberNo());
			
			//회원정보 삭제
			System.out.println("MemberService.removeMemberByKey -> memberDao.removeMemberByKey 요청");
			memberRowCnt = this.memberDao.deleteMemberByKey(conn, member);
			
			//투두리스트 삭제, 회원정보삭제에서 예외발생없이 잘 진행됐다면 커밋실행
			conn.commit();
		} catch (SQLException e) {
			try {
				//도중 예외 발생시 롤백.
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			
			System.out.println("MemberService.removeMemberByKey 응답");
			return false;
		} finally {
			//커넥션 사용 종료후 닫아주기
			dbUtil.close(conn, null, null);
		}
		//로그인 된 상태에서 회원삭제가 진행되기때문에 투두리스트와 멤버가 존재하지않아서 둘의 합이 0일 가능성 없음.
		System.out.println("MemberService.removeMemberByKey 응답");
		return (todoRowCnt + memberRowCnt) > 0;
	}
	
	public Member getMemberByKey(Member member) {
		
		this.dbUtil = new DBUtil();
		Member returnMember = null;
		this.memberDao = new MemberDao(); 
		Connection conn = null;
		
		try {
			//db연결
			conn = dbUtil.getConnection();
			
			//매개변수로 받은 멤버객체로 dao를 통해 정보가져오기
			System.out.println("MemberService.getMemberByKey -> memberDao.selectMemberByKey 요청");
			returnMember = this.memberDao.selectMemberByKey(conn, member);
			
			System.out.println("MemberService.getMemberByKey returnMember // "+returnMember);
			
			//문제없이 진행됐다면 커밋
			conn.commit();
			
		} catch(SQLException e) {
			
			try {
				//진행중 예외 발생시 롤백 여기선 셀렉트 함수라 딱히 필요로하지않지만 커넥션을 통해 진행시 약속처럼 사용.
				conn.rollback();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				
			}
			e.printStackTrace();
			
		} finally {
			//커넥션 사용 종료후 닫아주기
			this.dbUtil.close(conn, null, null);
			
		}
		//dao에서 찾은 멤버정보 리턴
		System.out.println("MemberService.getMemberByKey 응답");
		return returnMember;
	}
}
