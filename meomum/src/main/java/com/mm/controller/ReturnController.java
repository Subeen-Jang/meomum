package com.mm.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mm.member.model.MemberDTO;
import com.mm.order.model.MyOrderListDTO;
import com.mm.order.model.OrderDAO;
import com.mm.order.model.OrderReportDTO;
import com.mm.pro.model.ProDAO;
import com.mm.shipping.model.ShippingDAO;
import com.mm.shipping.model.ShippingDTO;
import com.mm.turnback.model.ReturnDAO;
import com.mm.turnback.model.ReturnDTO;
import com.mm.turnback.model.ReturnListDTO;


@Controller
public class ReturnController {
	
	@Autowired
	private ReturnDAO returnDao;
	
	@Autowired
	private OrderDAO orderDao;
	
	@Autowired
	private ShippingDAO shipDao;
	
	@Autowired
	private ProDAO proDao;
	
	
	@RequestMapping("/returnForm.do")
	public ModelAndView returnForm(@RequestParam("order_idx") String order_idx,
									@RequestParam("pro_idx")int pro_idx) {

		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		OrderReportDTO dto = orderDao.orderData(map);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.setViewName("turnback/returnForm");
		return mav;
	}
	
	
	
	@RequestMapping("/returnApply.do")
	public ModelAndView returnApply(ReturnDTO dto,
									@RequestParam("order_idx") String order_idx,
									@RequestParam("pro_idx") int pro_idx) {

		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		
		int result=returnDao.returnApplyInsert(dto);

		ModelAndView mav = new ModelAndView();
		
		if(result>0) {
			int statusUpdate=orderDao.returnApplyUpdate(map);
			String msg=statusUpdate>0?"반납 신청이 완료되었습니다.":"반납 신청에 실패하였습니다.";
			String gopage = statusUpdate>0?"opener.document.location.reload(); self.close()":"location.href='returnForm.do';";
			mav.addObject("dto", dto);
			mav.addObject("msg", msg);
			mav.addObject("gopage", gopage);
			mav.setViewName("/mainMsg");
		}else {
			mav.addObject("msg", "반납 신청에 실패하였습니다.");
			mav.addObject("gopage", "location.href='returnForm.do';");
			mav.setViewName("/mainMsg");
		}
		
		return mav;
	}
	
	

	/** 마이페이지 반납내역 */
	@RequestMapping("/myReturnProList.do")
	public ModelAndView myReturnProList(@RequestParam(value="cp",defaultValue = "1")int cp,
										@RequestParam(value="fvalue",defaultValue = "")String fvalue,
										@RequestParam(value="state",defaultValue = "0")String state, HttpSession session) {

		ModelAndView mav=new ModelAndView();
		
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
		fmap.put("user_idx", user_idx);
		
		
		Map tmap=new HashMap();
		tmap.put("fvalue","%"+fvalue+"%");
		tmap.put("state",state);
		tmap.put("user_idx", user_idx);
		
		int totalCnt=returnDao.myReturnProListCnt(tmap);
			
		List<MyOrderListDTO> lists=returnDao.myReturnProList(fmap);
		
		String param = "&fvalue="+fvalue+"&state="+state;
		String pageStr=com.mm.module.PageModule.makePageParam("myReturnProList.do", totalCnt, listSize, 
																	pageSize, cp,param);
			
		mav.setViewName("turnback/myReturnProList");
		mav.addObject("lists", lists);
		mav.addObject("pageStr", pageStr);
		mav.addObject("state", state);
		mav.addObject("fvalue", fvalue);
			
		return mav;
			
	}
	
	
	
	/** 관리자페이지 반납내역 */
	@RequestMapping("/returnProList_a.do")
	public ModelAndView returnProList(@RequestParam(value="cp",defaultValue = "1")int cp,
			@RequestParam(value="fvalue",defaultValue = "")String fvalue,
			@RequestParam(value="state",defaultValue = "0")String state, HttpSession session) {

		ModelAndView mav=new ModelAndView();

		MemberDTO ssInfo = (MemberDTO) session.getAttribute("ssInfo");
		
		if(ssInfo==null||!ssInfo.getUser_info().equals("관리자")) {
			mav.addObject("msg", "잘못된 접근입니다.");
			mav.addObject("gopage","location.href='index.do';");
			mav.setViewName("mainMsg");
			return mav;
		}
		
		int listSize=5;
		int pageSize=5;
		int start=(cp-1)*listSize+1;
		int end=cp*listSize;
		
		Map fmap=new HashMap();
		
		fmap.put("start", start);
		fmap.put("end", end);
		fmap.put("fvalue","%"+fvalue+"%");
		fmap.put("state",state);
		
		
		Map tmap=new HashMap();
		tmap.put("fvalue","%"+fvalue+"%");
		tmap.put("state",state);
		
		int totalCnt=returnDao.returnProListCnt(tmap);
			
		List<ReturnListDTO> lists=returnDao.returnProList(fmap);
		
		String param = "&fvalue="+fvalue+"&state="+state;
		String pageStr=com.mm.module.PageModule.makePageParam("returnProList_a.do", totalCnt, listSize, 
																	pageSize, cp,param);
			
		mav.setViewName("turnback/returnProList_a");
		mav.addObject("lists", lists);
		mav.addObject("pageStr",pageStr);
		mav.addObject("state",state);
		mav.addObject("fvalue", fvalue);
			
		return mav;
			
	}
	
	
	/** 관리자페이지 반납처리 폼 */
	@RequestMapping("/returnSubmitForm.do")
	public ModelAndView returnSubmitForm(@RequestParam("order_idx") String order_idx,
											@RequestParam("pro_idx") int pro_idx) {
		
		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		ReturnListDTO dto = returnDao.returnData(map);

		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.setViewName("turnback/returnSubmitForm");
		return mav;
	}
	
	
	//관리자 - 반납승인
	@RequestMapping("/returnSubmit.do")
	public ModelAndView returnSubmit(@RequestParam("order_idx") String order_idx,
										@RequestParam("pro_idx") int pro_idx,
										ShippingDTO sdto) {
		
		Map map = new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		int returnUpdate=returnDao.returnStartUpdate(map);//turnback 테이블 반납시작날짜, 반납승인여부 변경하기
		
		ModelAndView mav=new ModelAndView();
		
		if(returnUpdate>0) {

			int result=orderDao.returnSubmitUpdate(map); //주문상태 반납진행으로 변경하기
			
			if(result>0) {
				int returnShipping=shipDao.returnShipInsert(sdto); //반납 회수배송처리
				int returnProUpdate=proDao.returnProUpdate(map);  //반납 완료 시 재고 수정
			}
			
			mav.addObject("msg", "반납승인 처리가 완료되었습니다.");
			mav.addObject("gopage", "opener.document.location.reload(); self.close()");
			mav.setViewName("mainMsg");
		}else {
			mav.addObject("msg", "반납승인 처리에 실패하였습니다.");
			mav.addObject("gopage", "opener.document.location.reload(); self.close()");
			mav.setViewName("mainMsg");
		}
		
		return mav;
		
	}
	
	
	
	
	//관리자 - 반납보류
	@RequestMapping("/returnCancel.do")
	public ModelAndView returnCancel(@RequestParam("order_idx") String order_idx,
										@RequestParam("pro_idx") int pro_idx) {
		Map map=new HashMap();
		map.put("order_idx", order_idx);
		map.put("pro_idx", pro_idx);
		
		int returnUpdate=returnDao.returnNoUpdate(map); //반납 승인여부 N 수정
		int orderStatusUpdate=orderDao.returnCancelUpdate(map); //주문상태 8.반납보류로 수정
		
		ModelAndView mav=new ModelAndView();
		
		if(returnUpdate>0 && orderStatusUpdate>0) {
			mav.addObject("msg", "반납보류 처리가 완료되었습니다.");
			mav.addObject("gopage", "opener.document.location.reload(); self.close()");
			mav.setViewName("mainMsg");
			
		}else {
			mav.addObject("msg", "반납보류 처리에 실패하였습니다.");
			mav.addObject("gopage", "location.href='returnSubmitForm.do';");
			mav.setViewName("mainMsg");
		}

		return mav;
		
	}
	
}
