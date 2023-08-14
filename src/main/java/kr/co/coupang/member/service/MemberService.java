package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * ������ service
	 * @param member
	 * @return int
	 */
	public int registerMember(Member member);

	/**
	 * ȸ������ ����
	 * @param member
	 * @return int
	 */
	public int updateMember(Member member);

	/**
	 * ȸ�� Ż�� Service
	 * @param memberId
	 * @return int
	 */
	public int removeMember(String memberId);

	/**
	 * ��� �α��� Service
	 * @param member ���̵�, ���
	 * @return member��ü
	 */
	public Member memberLoginCheck(Member member);

	/**
	 * ��� ������������ service
	 * @param memberId
	 * @return member��ü
	 */
	public Member showOneById(String memberId);




}
