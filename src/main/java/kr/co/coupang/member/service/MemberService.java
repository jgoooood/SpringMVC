package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * 멤버등록 service
	 * @param member
	 * @return int
	 */
	public int registerMember(Member member);

	/**
	 * 회원정보 수정
	 * @param member
	 * @return int
	 */
	public int updateMember(Member member);

	/**
	 * 회원 탈퇴 Service
	 * @param memberId
	 * @return int
	 */
	public int removeMember(String memberId);

	/**
	 * 멤버 로그인 Service
	 * @param member 아이디, 비번
	 * @return member객체
	 */
	public Member memberLoginCheck(Member member);

	/**
	 * 멤버 정보가져오는 service
	 * @param memberId
	 * @return member객체
	 */
	public Member showOneById(String memberId);




}
