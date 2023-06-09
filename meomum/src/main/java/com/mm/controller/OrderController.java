package com.mm.controller;

import java.util.List;	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.mm.cart.model.CartDAO;
import com.mm.cart.model.CartDTO;
import com.mm.member.model.MemberDTO;
import com.mm.order.model.MyOrderListDTO;
import com.mm.order.model.OrderCancelDTO;
import com.mm.order.model.OrderDAO;
import com.mm.order.model.OrderDTO;
import com.mm.order.model.OrderProDTO;
import com.mm.order.model.OrderReportDTO;
import com.mm.payment.model.PaymentDAO;
import com.mm.payment.model.PaymentDTO;
import com.mm.point.model.PointDAO;
import com.mm.point.model.PointDTO;
import com.mm.pro.model.ProDTO;
import com.mm.turnback.model.ReturnListDTO;

@Controller
public class OrderController {

	@Autowired
	private OrderDAO orderDao;
	@Autowired
	private PaymentDAO payDao;
	@Autowired
	private CartDAO cdao;
	@Autowired
	private PointDAO pdao;
	

	@RequestMapping("/orderList.do")
	public ModelAndView orderList(@RequestParam("pro_idx") int idx,HttpSession session) {
		ModelAndView mav = new ModelAndView();

		if (session.getAttribute("ssInfo") == null) {

			mav.addObject("msg", "로그인 후 이용가능합니다");
			mav.addObject("goUrl", "login.do");
			mav.setViewName("ntc/ntcMsg");
		} else {
			session.getAttribute("ssInfo");
			MemberDTO sdto =(MemberDTO) session.getAttribute("ssInfo");
			int user_idx = sdto.getUser_idx();
			
			ProDTO dto = orderDao.orderList(idx);
			int result = pdao.pointTotal(user_idx);
			
			mav.addObject("dto", dto);
			mav.addObject("result", result);
			mav.setViewName("order/orderList");
		}

		return mav;
	}

	/**장바구니 상품 결제*/
	@RequestMapping("/orderListss.do")
	public ModelAndView orderAllList(@RequestParam("cart_idx") int[] cartIdx, @RequestParam("totalSub") int totalSub,
			@RequestParam("totalCount") int totalCount, @RequestParam("totalDel") int totalDel,
			@RequestParam("finalTotalPrice") int finalTotalPrice,HttpSession session) {

		ModelAndView mav = new ModelAndView();

		if (session.getAttribute("ssInfo") == null) {

			mav.addObject("msg", "로그인 후 이용가능합니다");
			mav.addObject("link", "login.do");
			mav.setViewName("msg");
			return mav;
		}
		
		session.getAttribute("ssInfo");
		MemberDTO sdto =(MemberDTO) session.getAttribute("ssInfo");
		int user_idx = sdto.getUser_idx();
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("totalSub", totalSub);
		map.put("totalCount", totalCount);
		map.put("totalDel", totalDel);
		map.put("finalTotalPrice", finalTotalPrice);

		List<CartDTO> lists = new ArrayList<CartDTO>();
		for (int i = 0; i < cartIdx.length; i++) {
			lists.add(cdao.orderListCartIDX(cartIdx[i]));

		}
		int result = pdao.pointTotal(user_idx);
		
		mav.addObject("result", result);
		mav.addObject("lists", lists);
		mav.addObject("total", map);
		mav.setViewName("order/orderLists");
		return mav;
	}

//	@RequestMapping(value = "/orderForm.do", method = RequestMethod.POST)
//	public ModelAndView order(OrderDTO dto) {
//		int result = orderDao.orderInsert(dto);
//		String msg = result > 0 ? "폼 저장 성공" : "폼 저장 성공 실패";
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("msg", msg);
//		mav.addObject("goUrl", "/meomum/index.do");
//		mav.setViewName("ntc/ntcMsg");
//		return mav;
//	}

	//결제부분 시작//
//	@RequestMapping(value = "/orderPay.do")
//	public ModelAndView svcPay(@RequestBody PaymentDTO dto) {
//		System.out.println(dto);
//		int result = payDao.paymentInsert(dto);
//		ModelAndView mav = new ModelAndView();
//
//		String msg = result > 0 ? "결제가 완료되었습니다" : "다시 시도해주세요";
//		String link = result > 0 ? "index.do" : "proList.do";
//
//		mav.addObject("msg", msg);
//		mav.addObject("link", link);
//		mav.setViewName("mmJson");
//
//		return mav;
//	}
//	@RequestMapping(value = "/orderPro.do")
//	public ModelAndView orderPay(@RequestBody OrderProDTO dto) {
//		
//		int result = orderDao.order_proInsert(dto);
//		ModelAndView mav = new ModelAndView();
//
//		String msg = result > 0 ? "결제가 완료되었습니다" : "다시 시도해주세요";
//		String link = result > 0 ? "index.do" : "proList.do";
//
//		mav.addObject("msg", msg);
//		mav.addObject("link", link);
//		mav.setViewName("mmJson");
//
//		return mav;
//	}
	
	//결제부분 끝//
	/** 마이페이지 구독중인 상품 */
	public List<MyOrderListDTO> mySubsProPage(int cp, int ls, int user_idx) {
		int start = (cp - 1) * ls + 1;
		int end = cp * ls;
		Map map = new HashMap();
		map.put("start", start);
		map.put("end", end);
		map.put("user_idx", user_idx);

		List<MyOrderListDTO> lists = orderDao.mySubsAllList(map);
		return lists;
	}
	
	@RequestMapping("/subsProList.do")
	public ModelAndView mysubsProList(@RequestParam(value = "cp", defaultValue = "1") int cp, HttpSession session) {

		ModelAndView mav = new ModelAndView();

		if(session.getAttribute("ssInfo")==null) {
			mav.addObject("msg", "로그인을 해주세요.");
			mav.addObject("gopage","location.href='index.do';");
			mav.setViewName("mainMsg");
			return mav;
		}
		
		
		MemberDTO mdto = (MemberDTO) session.getAttribute("ssInfo");
		int user_idx = mdto.getUser_idx();

		int totalCnt = orderDao.mySubsAllListCnt(user_idx);
	    int listSize = 5;
	    int pageSize = 5;

	    String pageStr = com.mm.module.PageModule.makePage("subsProList.do", totalCnt, listSize, pageSize, cp);

	    List<MyOrderListDTO> list = mySubsProPage(cp, pageSize, user_idx);

		mav.addObject("list", list);
		mav.addObject("pageStr", pageStr);
		mav.setViewName("order/subsProList");
		return mav;
	}
	

	/** 마이페이지 주문배송내역 */
	@RequestMapping("/orderReport.do")
	public ModelAndView myOrderReport(@RequestParam(value = "cp", defaultValue = "1") int cp, 
									@RequestParam(value="fvalue",defaultValue = "")String fvalue,
									@RequestParam(value="state",defaultValue = "0")String state, HttpSession session) {

		ModelAndView mav = new ModelAndView();
		
		if(session.getAttribute("ssInfo")==null) {
			mav.addObject("msg", "로그인을 해주세요.");
			mav.addObject("gopage","location.href='index.do';");
			mav.setViewName("mainMsg");
			return mav;
		}
		
		MemberDTO mdto = (MemberDTO) session.getAttribute("ssInfo");
		int user_idx = mdto.getUser_idx();
		
		int listSize=5;
		int pageSize=5;
		int start=(cp-1)*listSize+1;
		int end=cp*listSize;
		
		Map fmap=new HashMap();
		
		fmap.put("start", start);
		fmap.put("end", end);
		fmap.put("fvalue","%"+fvalue+"%");
		fmap.put("state",state);
		fmap.put("user_idx",user_idx);
		
		
		Map tmap=new HashMap();
		tmap.put("fvalue","%"+fvalue+"%");
		tmap.put("state",state);
		tmap.put("user_idx",user_idx);

		int totalCnt = orderDao.myReportTotalCnt(tmap);

		List<OrderReportDTO> lists = orderDao.myOrderReport(fmap);
		
		String param = "&fvalue="+fvalue+"&state="+state;
		String pageStr=com.mm.module.PageModule.makePageParam("orderReport.do", totalCnt, listSize, 
																	pageSize, cp,param);

		mav.setViewName("order/orderReport");
		mav.addObject("lists", lists);
		mav.addObject("pageStr", pageStr);
		mav.addObject("state",state);
		mav.addObject("fvalue", fvalue);
		mav.addObject("user_idx", user_idx);
			
		return mav;
	}
	

	/** 관리자페이지 주문배송내역 */
	public List<OrderReportDTO> reportPage(int cp, int ls) {
		int start = (cp - 1) * ls + 1;
		int end = cp * ls;
		Map map = new HashMap();
		map.put("start", start);
		map.put("end", end);
		List<OrderReportDTO> lists = orderDao.orderReport(map);
		return lists;
	}

	@RequestMapping("/orderReport_a.do")
	public ModelAndView orderReport_a(@RequestParam(value = "cp", defaultValue = "1") int cp, HttpSession session) {
		
		ModelAndView mav = new ModelAndView();

		MemberDTO ssInfo = (MemberDTO) session.getAttribute("ssInfo");
		
		if(ssInfo==null||!ssInfo.getUser_info().equals("관리자")) {
			mav.addObject("msg", "잘못된 접근입니다.");
			mav.addObject("gopage","location.href='index.do';");
			mav.setViewName("mainMsg");
			return mav;
		}
		
		
		int totalCnt = orderDao.reportTotalCnt();
		int listSize = 5;
		int pageSize = 5;

		String pageStr = com.mm.module.PageModule.makePage("orderReport_a.do", totalCnt, listSize, pageSize, cp);

		List<OrderReportDTO> lists = reportPage(cp, pageSize);

		mav.addObject("lists", lists);
		mav.setViewName("order/orderReport_a");
		mav.addObject("pageStr", pageStr);

		return mav;
	}

	/** 관리자페이지 배송처리 폼 */
	@RequestMapping("/shipForm.do")
	public ModelAndView shippingForm(@RequestParam("order_idx") String order_idx,
									@RequestParam("pro_idx") int pro_idx) {
		
		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		OrderReportDTO dto = orderDao.orderData(map);

		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.setViewName("shipping/shipForm");
		return mav;
	}

	/** 주문상세내역 팝업창 내용 */
	@RequestMapping("/orderInfoDetail.do")
	public ModelAndView orderInfoDetail(@RequestParam("order_idx") String order_idx,
											@RequestParam("pro_idx") int pro_idx) {

		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		OrderReportDTO dto = orderDao.orderData(map);

		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.setViewName("order/orderInfoDetail");
		return mav;
	}
	
//	
//	/**사용자: 구독일상 결제(point 테이블 insert)*/
//	@RequestMapping(value="/orderPoint.do")
//	public ModelAndView orderPay(@RequestBody PointDTO pdto) {
//		
//		int result = pdao.pointInsert(pdto);
//		ModelAndView mav = new ModelAndView();
//		
//		String msg = result > 0 ? "결제가 완료되었습니다" : "다시 시도해주세요";
//		String link = result > 0 ? "index.do" : "proList.do";
//
//		mav.addObject("msg", msg);
//		mav.addObject("link", link);
//		mav.setViewName("mmJson");
//		
//		return mav;
//	}
	
	//dto 3개 전달받아 결제
	@RequestMapping(value="/totalOrders.do")
	public ModelAndView totalOrder(@RequestBody Map<String, Object> requestData) {
		ObjectMapper objectMapper=new ObjectMapper();
	    PointDTO pdto = objectMapper.convertValue(requestData.get("pdto"), PointDTO.class);
	    PaymentDTO paydto = objectMapper.convertValue(requestData.get("paydto"), PaymentDTO.class);
	    OrderProDTO odto = objectMapper.convertValue(requestData.get("odto"), OrderProDTO.class);
	    OrderDTO ordto= objectMapper.convertValue(requestData.get("ordto"), OrderDTO.class);
	    
	    int result1 = pdao.pointInsert(pdto);
	    int result2 = payDao.paymentInsert(paydto);
	    int result3 = orderDao.order_proInsert(odto);
	    int result4 = orderDao.orderInsert(ordto);
			    
	    ModelAndView mav = new ModelAndView();
	    String msg = (result1 > 0 && result2 > 0 && result3  > 0 && result4  > 0) ? "결제가 완료되었습니다" : "다시 시도해주세요";
	    String link = (result1 > 0 && result2 > 0 && result3 > 0 && result4  > 0) ? "index.do" : "proList.do";

	    mav.addObject("msg", msg);
	    mav.addObject("link", link);
	    mav.setViewName("mmJson");
			
	    return mav;
	}
		
	
	//장바구니 상품 결제 (여러개 상품)
	@RequestMapping(value="/totalOrderss.do",method = RequestMethod.POST)
	public ModelAndView totalOrders(@RequestBody Map<String, Object> requestData,HttpSession session) {

		ModelAndView mav = new ModelAndView();
		
		 MemberDTO sdto =(MemberDTO) session.getAttribute("ssInfo");
	     int user_idx = sdto.getUser_idx();
	     
	    ObjectMapper objectMapper = new ObjectMapper();

	    PointDTO pdto = objectMapper.convertValue(requestData.get("pdto"), PointDTO.class);
	    PaymentDTO paydto = objectMapper.convertValue(requestData.get("paydto"), PaymentDTO.class);
	    OrderProDTO[] odtos = objectMapper.convertValue(requestData.get("odto"), OrderProDTO[].class);
	    List<OrderProDTO> orderProList = Arrays.asList(odtos);
	    OrderDTO ordto= objectMapper.convertValue(requestData.get("ordto"), OrderDTO.class);

	    int result1 = pdao.pointInsert(pdto);
	    int result2 = payDao.paymentInsert(paydto);
	    int result3 = 0;
	    for(int i=0;i<orderProList.size();i++) {
	        result3 += orderDao.order_proInsert(orderProList.get(i));
	        if(result3>0) {
	        	cdao.orderCartDelete(user_idx,orderProList.get(i).getPro_idx());
	            int cartnum = cdao.userCartCount(user_idx);
	            session.setAttribute("cart", cartnum);
	        }
	    }
	    int result4 = orderDao.orderInsert(ordto);

	    String msg = (result1 > 0 && result2 > 0 && result3  > 0 && result4  > 0) ? "결제가 완료되었습니다" : "다시 시도해주세요";
	    String link = (result1 > 0 && result2 > 0 && result3 > 0 && result4  > 0) ? "index.do" : "proList.do";

	    mav.addObject("msg", msg);
	    mav.addObject("link", link);
	    mav.setViewName("mmJson");

	    return mav;
	}
	
	//////////////////////////////////////////이초은 시작
	/**주문 완료 페이지 */
	@RequestMapping("orderSucces.do")
	public ModelAndView orderSucces(@RequestParam("order_idx")String order_idx) {
	
	ModelAndView mav = new ModelAndView();
	mav.addObject("order_idx",order_idx);
	mav.setViewName("order/orderSucces");
	return mav;
	
	}
	
	@RequestMapping("/cancel.do")
	public ModelAndView orderCancel(@RequestParam("order_idx")String order_idx) {
		
		int result =orderDao.orderCancelUpdate(order_idx);
		String msg=result>0?"주문취소 되었습니다.":"주문취소 하실 수 없습니다";
		ModelAndView mav=new ModelAndView();
		mav.addObject("msg", msg);
		mav.addObject("goUrl", "index.do");
		mav.setViewName("ntc/ntcMsg");
		
		return mav;
		
		
	}
}
