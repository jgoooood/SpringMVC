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
	/*		기존 호출방식 -> 새로운 객체자체를 생성해서 호출했음
	 *		MemberServiceImpl service = new MemberServiceImpl();
	 *		int result = service.registerMember(member);
	 */
	//Spring 방식->@어노테이션 사용
	@Autowired
	private MemberService service;
	// 기존 doGet 방식대신 아래 코드처럼 작성함 Model사용x 
	// 페이지이동용 : showRegisterForm
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
		//bean등록하지 않으면 아래의 에러메시지 나옴->어노테이션을 붙여줘야 함
		//WARN : org.springframework.web.servlet.PageNotFound - No mapping for GET /member/register.do
		return "member/register";
	}
	
	//기존 doPost를 방식대신 아래 코드처럼 작성함 Model사용
	// 데이터 저장용 : registerMember
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
//		request.setCharacterEncoding("UTF-8"); -> 사용안하고 FILTER사용 WEB.XML에 등록해야함
		Member member = new Member(memberId, memberPw, memberName, memberAge, memberGender, memberEmail, memberPhone, memberAddress, memberHobby);
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				//성공
				//response.sendRedirect("/index.jsp"); 
				return "redirect:/index.jsp";
			} else {
				//실패 : 1.실제 result가 1이 아닌경우 2. 예외발생한 경우
//				request.setAttribute("msg", "실패");
//				request.getRequestDispatcher("/WEB-INF/common/errorPage.jsp").forward(request, response);
				//getRequestDispatcher대신 viewResolver사용
				model.addAttribute("msg", "회원가입이 완료되지 않았습니다.");
				return "common/errorPage";
			}
		} catch (Exception e) { //예외 발생할 경우
			e.printStackTrace(); //콘솔창에 빨간색으로 뜨게 함
			model.addAttribute("msg", e.getMessage()); //콘솔창에 뜨는 메시지를 웹 페이지로 뜨게 함 
			return "common/errorPage";
		}
		//System.out.println(member.toString());
/*		request.getParameter 사용안함 -> @어노테이션 사용 : @RequestParam("memberId") String memberId 
*		String memberId = request.getParameter("memberId");
*		request.setAttribute("", "");
*		request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
*		response.sendRedirect("/index.jsp");
*/
	}
	
	//핸들러매핑
	@RequestMapping(value="/member/login.do", method=RequestMethod.POST)
	public String memberLogin(
			HttpServletRequest request
			,@RequestParam("memberId") String memberId
			,@RequestParam("memberPw") String memberPw
			, Model model) {
		//쿼리문 : SELECT * FROM MEMBER_TBL WHERE MEMBER_ID = ? AND MEMBER_PW = ?
		try { //에러메시지를 콘솔창에서도 확인할 수 있게 try-catch구문을 사용
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberPw(memberPw);
			Member mOne = service.memberLoginCheck(member);
			if(mOne != null) {
				//성공->로그인페이지
//				System.out.println(mOne.toString());
				//->SpringMVC/pom.xml에서 <dependency> jstl라이브러리 추가해줌(jar파일을 직접 넣지 않음)
				// model.addAttribute("member", mOne);
				// return "redirect:/index.jsp"; without data
				/* 기존방식 : 로그인했으면 모든 페이지에서 사용할 수 있게 session에 객체 저장
				HttpSession session = request.getSession();
				session.setAttribute("memberId", mOne.getMemberId());
				session.setAttribute("memberName", mOne.getMemberName());*/
				//@어노테이션 방법 : 상단에 @SessionAttributes({"memberId", "memberName"}) 코드를 추가해주고
				//model.addAttribute("memberId", mOne.getMemberId());의 코드 추가함
				model.addAttribute("memberId", mOne.getMemberId());
				model.addAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
				//->String으로 리턴하기 위해 메소드 반환타입 변경
			} else {
				// 실패 -> 실패메시지 출력
				model.addAttribute("msg", "데이터가 조회되지 않았습니다.");
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
			//SessionStatus는 spring의 어노테이션(SessionAttributes)로 지원되는 세션을 만료시킴
			//사용되는 메소드는 setComplete(); ->세션만료
			, SessionStatus session
			, Model model) {
		if(session != null) {
			//session.invalidate(); 이전방식
			session.setComplete();
			if(session.isComplete()) {
				//세션의 만료 유효성 체크
			}
			return "redirect:/index.jsp";
			//리턴형이 문자열이기 때문에 반환타입을 String으로 변경
		} else {
			model.addAttribute("msg", "로그아웃을 완료하지 못했습니다.");
			return "common/errorPage";
			//리턴형이 문자열이기 때문에 반환타입을 String으로 변경
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
				model.addAttribute("msg", "회원정보를 불러올 수 없습니다.");
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
			, Model model) { //Model model : 반환값 보여줄 때 필요
		try {
			Member mOne = service.showOneById(memberId);
			if(mOne != null) {
				model.addAttribute("member", mOne);
				return "member/modify";
			} else {
				model.addAttribute("msg", "회원정보를 불러올 수 없습니다.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
			//view resolver->return : 저장한 정보를 가져와야함->쿼리문 select
			//SELECT * FROM MEMBER_TBL MEMBER_ID = ? <-쿼리스트링 지정할 매개변수 필요
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
			, Model model) { //Model model : request.setAttribute처럼 사용가능
		//UPDATE MEMBER_TBL SET MEMBER_PW = ?, MEMBER_EMAIL = ?, MEMBER_PHONE = ?, MEMBER_ADDRESS = ?, MEMBER_HOBBY = ? WHERE MEMBER_ID = ?
		try {
			Member member = new Member(memberId, memberPw, memberEmail, memberPhone, memberAddress, memberHobby);
			int result = service.updateMember(member);
			if(result > 0) {
				//기존 : 단순페이지 이동
				//response.sendRedirect("/member/mypage.do?memberId="+memberId);
				//return "redirect:/member/mypage.do?memberId="+memberId;
				// -> http://127.0.0.1:8888/member/mypage.do?memberId=user01&memberName=일용자&memberId=user01
				//Redirect시 쿼리스트링 만들고 싶으면 RedirectAttributes사용
				// 메소드 인자로 RedirectAttributes redirect매개변수 추가 
				// -> redirect.addAttribute()쿼리스트링 키와 값 지정
				redirect.addAttribute("memberId", memberId); 
				return "redirect:/member/mypage.do";
			} else {
				model.addAttribute("msg", "수정이 완료되지 않았습니다.");
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
				//redirect사용이유 : /WEB-INF/views 를 생략하게 해줌
				//return "redirect:/index.jsp"는 세션파괴가 안됨
				return "redirect:/member/logout.do"; //로그아웃해주면서 세션파괴
			} else {
				model.addAttribute("msg", "회원탈퇴가 완료되지 않았습니다.");
				return "common/errorPage";
			}	
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
			
}
