<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>회원정보 조회</title>
		<link rel="stylesheet" href="../resources/css/main.css">
	</head>
	<body>
		<h1>회원정보조회</h1>
			<input type="hidden" name="memberId" value="${memberId}"> 
			<fieldset>
				<legend>회원 상세 정보</legend>
				<ul>
					<li>
						<label for="memberId">아이디</label>
						<span>${member.memberId }</span>
					</li>
					<li>
						<label for="memberName">이름</label>
						<span>${member.memberName }</span>
					</li>
					<li>
						<label for="memberAge">나이</label>
						<span>${member.memberAge }</span>
					</li>
					<li>
						<label for="memberGender">성별</label>
						<span>${member.memberGender }</span>
					</li>
					<li>
						<label for="memberEmail">이메일</label>
						<span>${member.memberEmail }</span>
					</li>
					<li>
						<label for="memberPhone">전화번호</label>
						<span>${member.memberPhone }</span>
					</li>
					<li>
						<label for="memberAddress">주소</label>
						<span>${member.memberAddress }</span>
					</li>
					<li>
						<label for="memberHobby">취미</label>
						<span>${member.memberHobby }</span>
					</li>
					<li>
						<label for="memberDate">가입날짜</label>
						<span>${member.memberDate }</span>
					</li>
				</ul>
			</fieldset>
			<a href="/index.jsp">메인으로 이동</a>
			<a href="/member/update.do?memberId=${member.memberId }">수정하기</a>
			<a href="/member/delete.do?memberId=${member.memberId }">탈퇴하기</a>

	</body>
</html>