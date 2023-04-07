<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>나의정보수정</title>

<!-- 사이드바용 css 시작-->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
	integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<!-- 사이드바용 css 끝 -->

<!--주소 검색 api -->
<script
	src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="/meomum/js/request.js"></script>
<!--  -->

<script type="text/javascript">
function pwdCheck(){
	var input_pwd = document.getElementById('input_pwd').value;

	var user_idx = document.getElementById('user_idx').value;
	var user_pwd = ${sessionScope.ssInfo.user_pwd};

	
	
	if(input_pwd==user_pwd){	
		 const form = document.createElement('form');
	        form.method = 'POST';
	        form.action = 'infoEdit.do';
		
	       
			 const userIdxInput = document.createElement('input');
			    userIdxInput.type = 'hidden';
			    userIdxInput.name = 'user_idx';
			    userIdxInput.value = user_idx;
			    form.appendChild(userIdxInput);

			
			    const userOkInput = document.createElement('input');
			    userOkInput.type = 'hidden';
			    userOkInput.name = 'user_ok';
			    userOkInput.value = 'OK';
			    form.appendChild(userOkInput);
			

	        document.body.appendChild(form);
	        form.submit();
	}else{
		alert('비밀번호가 일치하지 않습니다.');
	}
	
}

</script>

<style>
/*헤더 이미지용 url에 이미지 추가하면 됩니다.*/
.page-header {
	background: linear-gradient(rgba(36, 39, 38, 0.5), rgba(36, 39, 38, 0.5)),
		rgba(36, 39, 38, 0.5)
		url(https://cdn.aitimes.com/news/photo/202210/147215_155199_1614.jpg)
		no-repeat center;
	background-size: cover;
	margin: 0;
	border-bottom: none;
	padding-bottom: 0px;
}

.page-caption {
	padding: 90px 0px;
	position: relative;
	z-index: 1;
	color: #fff;
	text-align: center;
}

.page-title {
	color: #fff;
	font-size: 40px;
	font-weight: 400;
	letter-spacing: -1px;
}

/**헤더 이미지용 끝*/
</style>
</head>
<body>
	<%@include file="/WEB-INF/views/header.jsp"%>
	<!-- 헤더 이미지 시작 -->
	<div class="page-header">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="page-caption">
						<h2 class="page-title">마이페이지</h2>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 헤더 이미지 끝 -->

	<section class="shop spad">
		<div class="container">
			<div class="row justify-content-center">
				<%@include file="../myMenu.jsp"%>
				<div class="col-xl-9 col-md-9">
					<div class="row justify-content-center">
						<div class="card text-center">
							<div class="card-body">
								<h4 class="card-title mb-4 text-center">나의 정보 수정</h4>
								<form class="form-inline justify-content-center" >
									<div class="form-group mr-3">
									<input type="hidden" id="user_idx" name="user_idx" value="${sessionScope.ssInfo.user_idx}">
									
										<label for="password-input">비밀번호</label> <input
											type="password" class="form-control" id="input_pwd"
											name="input_pwd" placeholder="비밀번호를 입력하세요" required="required">
									</div>
									<button type="button" class="btn btn-primary"
										onclick="pwdCheck()">확인</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

</body>

</html>