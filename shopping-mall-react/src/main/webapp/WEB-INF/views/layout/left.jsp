<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	
	<title>Model2 MVC Shop</title>

	<link href="/css/left.css" rel="stylesheet" type="text/css">
	
	<!-- CDN(Content Delivery Network) 호스트 사용 -->
	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script type="text/javascript">
	
		function history(){
			popWin = window.open("/history.jsp",
				"popWin",
				"left=300, top=200, width=300, height=200, marginwidth=0, marginheight=0, scrollbars=no, scrolling=no, menubar=no, resizable=no");
		}
	
		//==> jQuery 적용 추가된 부분
		$(function() {
			 
			//==> 개인정보조회 Event 연결처리부분
			$(".Depth03:contains('개인정보조회')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/user/getUser?userId=${user.userId}");
			});
			
			//==> 회원정보조회 Event 연결처리부분
			$(".Depth03:contains('회원정보조회')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/user/listUser");
			}); 
			
			//==> 판매상품등록 Event 연결처리부분
			$(".Depth03:contains('판매상품등록')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/product/addProductView");
			}); 
		 	
			//==> 판매상품관리 Event 연결처리부분
			$(".Depth03:contains('판매상품관리')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/product/listProduct?menu=manage");
			}); 
		 	
			//==> 상품검색 Event 연결처리부분
			$(".Depth03:contains('상 품 검 색')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/product/listProduct?menu=search");
			}); 
		 	
			//==> 구매이력조회 Event 연결처리부분 (URL 수정됨!)
			$(".Depth03:contains('구매이력조회')").on("click", function() {
				$(window.parent.frames["rightFrame"].document.location).attr("href","/purchase/listPurchase");
			}); 
			
			//==> 최근 본 상품 Event 연결처리부분 (팝업창 사용)
			$(".Depth03:contains('최근 본 상품')").on("click", function() {
				history(); // 팝업창 열기
			}); 
			
		});	
		 
	</script>
	
</head>

<body background="/images/left/imgLeftBg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="159" border="0" cellspacing="0" cellpadding="0">

<!--menu 01 line: 사용자 관련 메뉴-->
<tr>
	<td valign="top"> 
		<table border="0" cellspacing="0" cellpadding="0" width="159">	
			<c:if test="${ !empty user }">
				<tr>
					<td class="Depth03">
						<!-- jQuery Event 처리로 변경됨 -->
						개인정보조회
					</td>
				</tr>
			</c:if>
		
			<c:if test="${user.role == 'admin'}">
				<tr>
					<td class="Depth03">
						<!-- jQuery Event 처리로 변경됨 -->
						회원정보조회
					</td>
				</tr>
			</c:if>
			
			<tr>
				<td class="DepthEnd">&nbsp;</td>
			</tr>
		</table>
	</td>
</tr>

<!--menu 02 line: 상품 관리 메뉴 (관리자만)-->
<c:if test="${user.role == 'admin'}">
<tr>
	<td valign="top"> 
		<table border="0" cellspacing="0" cellpadding="0" width="159">
			<tr>
				<td class="Depth03">
					<!-- jQuery Event 처리로 변경됨 -->
					판매상품등록
				</td>
			</tr>
			<tr>
				<td class="Depth03">
					<!-- jQuery Event 처리로 변경됨 -->
					판매상품관리
				</td>
			</tr>
			<tr>
				<td class="DepthEnd">&nbsp;</td>
			</tr>
		</table>
	</td>
</tr>
</c:if>

<!--menu 03 line: 일반 사용자 메뉴-->
<tr>
	<td valign="top"> 
		<table border="0" cellspacing="0" cellpadding="0" width="159">
			<tr>
				<td class="Depth03">
					<!-- jQuery Event 처리로 변경됨 -->
					상 품 검 색
				</td>
			</tr>
			
			<c:if test="${ !empty user && user.role == 'user'}">
			<tr>
				<td class="Depth03">
					<!-- jQuery Event 처리로 변경됨 (URL 수정!) -->
					구매이력조회
				</td>
			</tr>
			</c:if>
			
			<tr>
				<td class="DepthEnd">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="Depth03">
					<!-- jQuery Event 처리로 변경됨 -->
					최근 본 상품
				</td>
			</tr>
		</table>
	</td>
</tr>

</table>

</body>

</html>