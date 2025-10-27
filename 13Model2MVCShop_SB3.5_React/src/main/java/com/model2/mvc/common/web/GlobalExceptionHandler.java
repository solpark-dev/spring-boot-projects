package com.model2.mvc.common.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//======================== 추가, 변경된 부분  ==========================/
//==> Spring Boot 시 추가된 부분.  //==> No Meta Data ==> No XML
//======================== 추가, 변경된 부분  ==========================/
@ControllerAdvice
public class GlobalExceptionHandler {
	
	///Field
	
	///Constructor
	public GlobalExceptionHandler(){
		System.out.println("\nCommon :: "+this.getClass()+"\n");
	}	
	
	///Method
	@ExceptionHandler(NullPointerException.class)
	  public ModelAndView handleNpe(NullPointerException ex) {
	    ModelAndView mv = new ModelAndView("/common/nullError.jsp"); // 논리 뷰명
	    mv.addObject("exception", ex);        // ★ JSP에서 ${exception.message} 가능
	    mv.addObject("message", ex.getMessage());
	    return mv;
	  }

	  @ExceptionHandler(NumberFormatException.class)
	  public ModelAndView handleNfe(NumberFormatException ex) {
	    ModelAndView mv = new ModelAndView("/common/numberFormatError.jsp");
	    mv.addObject("exception", ex);
	    mv.addObject("message", ex.getMessage());
	    return mv;
	  }

	  @ExceptionHandler(Exception.class)
	  public ModelAndView handleDefault(Exception ex) {
	    ModelAndView mv = new ModelAndView("/common/error.jsp");
	    mv.addObject("exception", ex);
	    mv.addObject("message", ex.getMessage());
	    return mv;
	  }
    
}