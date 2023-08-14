package kr.co.coupang.member.store.logic;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.store.MemberStore;

@Repository
public class MemberStoreLogic implements MemberStore{

	@Override
	public int insertMember(SqlSession sqlSession, Member member) {
		int result = sqlSession.insert("MemberMapper.insertMember", member);
		//mybatis가 커밋,롤백 모두 알아서 해줌
		return result;
	}

	@Override
	public int updateMember(SqlSession sqlSession, Member member) {
		int result = sqlSession.update("MemberMapper.updateMember", member);
		return result;
	}

	@Override
	public int deleteMember(SqlSession sqlSession, String memberId) {
		int result = sqlSession.delete("MemberMapper.deleteMember", memberId);
		return result;
	}

	@Override
	public Member memberLoginCheck(SqlSession sqlSession, Member member) {
		Member mOne = sqlSession.selectOne("MemberMapper.memberLoginCheck", member);
		return mOne;
	}

	@Override
	public Member showOneById(SqlSession sqlSession, String memberId) {
		Member mOne = sqlSession.selectOne("MemberMapper.showOneById", memberId);
		return mOne;
	}

	
}
