package kr.co.coupang.member.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;
import kr.co.coupang.member.store.MemberStore;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private SqlSession sqlSession;
	//sqlSession -> root-context.xml에서 설정함
	@Autowired
	private MemberStore mStore;
	
	//MemberService에서 추상메소드로 선언되어있는 registerMember메소드를 정의함
	@Override
	public int registerMember(Member member) {
		
		//기존방식
		//Connection conn = jdbcTemplate.createConnection();
		//int result = mStore.insertMember(conn, member);
		
		int result = mStore.insertMember(sqlSession, member);
		return result;
	}

	@Override
	public int updateMember(Member member) {
		int result = mStore.updateMember(sqlSession, member);
		return result;
	}

	@Override
	public int removeMember(String memberId) {
		int result = mStore.deleteMember(sqlSession, memberId);
		return result;
	}

	@Override
	public Member memberLoginCheck(Member member) {
		Member mOne = mStore.memberLoginCheck(sqlSession, member);
		return mOne;
	}

	@Override
	public Member showOneById(String memberId) {
		Member mOne = mStore.showOneById(sqlSession, memberId);
		return mOne;
	}


}
