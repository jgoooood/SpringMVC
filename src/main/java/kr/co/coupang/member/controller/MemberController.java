package kr.co.coupang.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;

@Controller
@SessionAttributes({"memberId", "memberName"})
public class MemberController {
	/*		���� ȣ���� -> ���ο� ��ü��ü�� �����ؼ� ȣ������
	 *		MemberServiceImpl service = new MemberServiceImpl();
	 *		int result = service.registerMember(member);
	 */
	//Spring ���->@������̼� ���
	@Autowired
	private MemberService service;
	// ���� doGet ��Ĵ�� �Ʒ� �ڵ�ó�� �ۼ��� Model���x 
	// �������̵��� : showRegisterForm
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
		//bean������� ������ �Ʒ��� �����޽��� ����->������̼��� �ٿ���� ��
		//WARN : org.springframework.web.servlet.PageNotFound - No mapping for GET /member/register.do
		return "member/register";
	}
	
	//���� doPost�� ��Ĵ�� �Ʒ� �ڵ�ó�� �ۼ��� Model���
	// ������ ����� : registerMember
	@RequestMapping(value="/member/register.do", method=RequestMethod.POST)
	public String registerMember(
			HttpServletRequest request
			, HttpServletResponse response
			, @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, @RequestParam("memberName") String memberName
			, @RequestParam("memberAge") int memberAge
			, @RequestParam("memberGender") String memberGender
			, @RequestParam("memberEmail") String memberEmail
			, @RequestParam("memberPhone") String memberPhone
			, @RequestParam("memberAddress") String memberAddress
			, @RequestParam("memberHobby") String memberHobby
			, Model model) {
//		request.setCharacterEncoding("UTF-8"); -> �����ϰ� FILTER��� WEB.XML�� ����ؾ���
		Member member = new Member(memberId, memberPw, memberName, memberAge, memberGender, memberEmail, memberPhone, memberAddress, memberHobby);
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				//����
				//response.sendRedirect("/index.jsp"); 
				return "redirect:/index.jsp";
			} else {
				//���� : 1.���� result�� 1�� �ƴѰ�� 2. ���ܹ߻��� ���
//				request.setAttribute("msg", "����");
//				request.getRequestDispatcher("/WEB-INF/common/errorPage.jsp").forward(request, response);
				//getRequestDispatcher��� viewResolver���
				model.addAttribute("msg", "ȸ�������� �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) { //���� �߻��� ���
			e.printStackTrace(); //�ܼ�â�� ���������� �߰� ��
			model.addAttribute("msg", e.getMessage()); //�ܼ�â�� �ߴ� �޽����� �� �������� �߰� �� 
			return "common/errorPage";
		}
		//System.out.println(member.toString());
/*		request.getParameter ������ -> @������̼� ��� : @RequestParam("memberId") String memberId 
*		String memberId = request.getParameter("memberId");
*		request.setAttribute("", "");
*		request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
*		response.sendRedirect("/index.jsp");
*/
	}
	
	//�ڵ鷯����
	@RequestMapping(value="/member/login.do", method=RequestMethod.POST)
	public String memberLogin(
			HttpServletRequest request
			,@RequestParam("memberId") String memberId
			,@RequestParam("memberPw") String memberPw
			, Model model) {
		//������ : SELECT * FROM MEMBER_TBL WHERE MEMBER_ID = ? AND MEMBER_PW = ?
		try { //�����޽����� �ܼ�â������ Ȯ���� �� �ְ� try-catch������ ���
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberPw(memberPw);
			Member mOne = service.memberLoginCheck(member);
			if(mOne != null) {
				//����->�α���������
//				System.out.println(mOne.toString());
				//->SpringMVC/pom.xml���� <dependency> jstl���̺귯�� �߰�����(jar������ ���� ���� ����)
				// model.addAttribute("member", mOne);
				// return "redirect:/index.jsp"; without data
				/* ������� : �α��������� ��� ���������� ����� �� �ְ� session�� ��ü ����
				HttpSession session = request.getSession();
				session.setAttribute("memberId", mOne.getMemberId());
				session.setAttribute("memberName", mOne.getMemberName());*/
				//@������̼� ��� : ��ܿ� @SessionAttributes({"memberId", "memberName"}) �ڵ带 �߰����ְ�
				//model.addAttribute("memberId", mOne.getMemberId());�� �ڵ� �߰���
				model.addAttribute("memberId", mOne.getMemberId());
				model.addAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
				//->String���� �����ϱ� ���� �޼ҵ� ��ȯŸ�� ����
			} else {
				// ���� -> ���и޽��� ���
				model.addAttribute("msg", "�����Ͱ� ��ȸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/logout.do", method=RequestMethod.GET)
	public String memberLogout(
			HttpSession sessionPrev
			//SessionStatus�� spring�� ������̼�(SessionAttributes)�� �����Ǵ� ������ �����Ŵ
			//���Ǵ� �޼ҵ�� setComplete(); ->���Ǹ���
			, SessionStatus session
			, Model model) {
		if(session != null) {
			//session.invalidate(); �������
			session.setComplete();
			if(session.isComplete()) {
				//������ ���� ��ȿ�� üũ
			}
			return "redirect:/index.jsp";
			//�������� ���ڿ��̱� ������ ��ȯŸ���� String���� ����
		} else {
			model.addAttribute("msg", "�α׾ƿ��� �Ϸ����� ���߽��ϴ�.");
			return "common/errorPage";
			//�������� ���ڿ��̱� ������ ��ȯŸ���� String���� ����
		}
	}
	
	@RequestMapping(value="/member/mypage.do", method=RequestMethod.GET)
	public String showDetailMember(
			@RequestParam("memberId") String memberId
			, Model model) {
		try {
			Member member = service.showOneById(memberId);
			if(member != null) {
				model.addAttribute("member", member);
				return "member/mypage";
			} else {
				model.addAttribute("msg", "ȸ�������� �ҷ��� �� �����ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/update.do", method=RequestMethod.GET)
	public String showModifyMember(
			@RequestParam("memberId") String memberId
			, Model model) { //Model model : ��ȯ�� ������ �� �ʿ�
		try {
			Member mOne = service.showOneById(memberId);
			if(mOne != null) {
				model.addAttribute("member", mOne);
				return "member/modify";
			} else {
				model.addAttribute("msg", "ȸ�������� �ҷ��� �� �����ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
			//view resolver->return : ������ ������ �����;���->������ select
			//SELECT * FROM MEMBER_TBL MEMBER_ID = ? <-������Ʈ�� ������ �Ű����� �ʿ�
			//<a href="/member/update.do?memberId=${member.memberId }">
		}
	}
	
	@RequestMapping(value="/member/update.do", method=RequestMethod.POST)
	public String modifyMember(
			@RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, @RequestParam("memberEmail") String memberEmail
			, @RequestParam("memberPhone") String memberPhone
			, @RequestParam("memberAddress") String memberAddress
			, @RequestParam("memberHobby") String memberHobby
			, RedirectAttributes redirect
			, Model model) { //Model model : request.setAttributeó�� ��밡��
		//UPDATE MEMBER_TBL SET MEMBER_PW = ?, MEMBER_EMAIL = ?, MEMBER_PHONE = ?, MEMBER_ADDRESS = ?, MEMBER_HOBBY = ? WHERE MEMBER_ID = ?
		try {
			Member member = new Member(memberId, memberPw, memberEmail, memberPhone, memberAddress, memberHobby);
			int result = service.updateMember(member);
			if(result > 0) {
				//���� : �ܼ������� �̵�
				//response.sendRedirect("/member/mypage.do?memberId="+memberId);
				//return "redirect:/member/mypage.do?memberId="+memberId;
				// -> http://127.0.0.1:8888/member/mypage.do?memberId=user01&memberName=�Ͽ���&memberId=user01
				//Redirect�� ������Ʈ�� ����� ������ RedirectAttributes���
				// �޼ҵ� ���ڷ� RedirectAttributes redirect�Ű����� �߰� 
				// -> redirect.addAttribute()������Ʈ�� Ű�� �� ����
				redirect.addAttribute("memberId", memberId); 
				return "redirect:/member/mypage.do";
			} else {
				model.addAttribute("msg", "������ �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}

	@RequestMapping(value="/member/delete.do", method=RequestMethod.GET)
	public String removeMember(
			@RequestParam("memberId") String memberId
			, Model model) {
		try {
			int result = service.removeMember(memberId);
			if(result > 0) {
				//redirect������� : /WEB-INF/views �� �����ϰ� ����
				//return "redirect:/index.jsp"�� �����ı��� �ȵ�
				return "redirect:/member/logout.do"; //�α׾ƿ����ָ鼭 �����ı�
			} else {
				model.addAttribute("msg", "ȸ��Ż�� �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}	
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
			
}
