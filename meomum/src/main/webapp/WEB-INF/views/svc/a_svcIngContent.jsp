<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>결제</h2>
	<form name="svcIngUpdate" action="asvcIngUpdate.do" method="post"  onsubmit="parentReload()">
		<input type="hidden" name="svc_idx" id="svc_idx" value="${ingdto.svc_idx}" >
		<input type="hidden" name="user_idx" id="user_idx" value="${ingdto.user_idx}" >
		<ul>
			<li>서비스 일자 <input type="datetime-local" name="svc_datetime" id="svc_datetime" value="${ingdto.svc_datetime}" onclick="setMinDate()"></li>

			<li>견적금액 <input type="text" name="estimated" id="estimated" value="${ingdto.estimated}" oninput="getTotal()" ></li>

			<li>할인금액 
				<input type="text" name="discount" id="discount" value="${ingdto.discount}" oninput="getTotal()"> 
			</li>

			<li>결제방식 <select name="type" id="type" >
					<option value="카드결제" ${ingdto.type =="카드결제"?"selected":""}>카드결제</option>
					<option value="계좌이체" ${ingdto.type =="계좌이체"?"selected":""}>계좌이체</option>
			</select>
			</li>

			<li>결제금액<input type="text" name="total" id="total" value="${ingdto.total}"></li>

			<li>결제상태 <select name="pay_state" id="pay_state">
					<option value="견적완료" ${ingdto.pay_state =="견적완료"?"selected":""}>견적완료</option>
					<option value="결제대기" ${ingdto.pay_state =="결제대기"?"selected":""}>결제대기</option>
					<option value="결제완료" ${ingdto.pay_state =="결제완료"?"selected":""}>결제완료</option>
					<option value="결제취소" ${ingdto.pay_state =="결제취소"?"selected":""}>결제취소</option>
			</select>
			</li>
		</ul>

		<div>
			<input type="submit" value="수정">
			<input type="button" value="닫기" onclick="popupClose()">
		</div>
	</form>
	
	<script>
	//스크립트가 아예 안됨(수정)
	/**견적금액과 할인금액을 계산해서 결제금액 출력*/
	function getTotal() {
		var estimated = document.getElementById('estimated').value;
		var discount = document.getElementById('discount').value;
		var total = document.getElementById('total');
		
		dateElement.addEventListener('input', function() {
			if (estimated && discount) {
				var result = parseInt(estimated) - parseInt(discount);
					total.value = result;
				} else {
					total.value = "";
				}
		  });
		}
		

	/**datetime-local 오늘 이전 날짜는 불가하도록 지정*/
	var estimated = document.getElementById('estimated');
	  var discount = document.getElementById('discount');
	  var total = document.getElementById('total');
	
	  estimated.addEventListener('input', function() {
	    getTotal();
	  });
	
	  discount.addEventListener('input', function() {
	    getTotal();
	  });
	
	  function getTotal() {
	    if (estimated.value && discount.value) {
	      var result = parseInt(estimated.value) - parseInt(discount.value);
	      total.value = result;
	    } else {
	      total.value = "";
	    }
	  }
	}
		
	/**a_svcContent.jsp에 값 보내기*/
	 function parentReload(){
			window.opener.location.reload();
			alert('${msg}');
		} 
	
	function popupClose(){
		window.close();
	}
	</script>

</body>

</body>
</html>