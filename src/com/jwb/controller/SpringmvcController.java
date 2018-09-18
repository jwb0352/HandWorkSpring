package com.jwb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwb.annotation.Controller;
import com.jwb.annotation.Quatifier;
import com.jwb.annotation.RequestMapping;
import com.jwb.service.impl.MyService;
import com.jwb.service.impl.MyTestService;
import com.jwb.service.impl.MyTestServiceImpl;
import com.jwb.service.impl.SpringmvcServiceImpl;

@Controller("jwb")
public class SpringmvcController {
	@Quatifier("MyServiceImpl")
	MyService myService;
	@Quatifier("SpringmvcServiceImpl")
	SpringmvcServiceImpl smService;
	@Quatifier("MyTestServiceImpl")
	MyTestServiceImpl myTestService;
	
	@RequestMapping("insert")
	public String insert(HttpServletRequest req,HttpServletResponse res,String param){
		myService.insert(null);
		smService.insert(null);
		return null;
	}

	@RequestMapping("delete")
	public String delete(HttpServletRequest req,HttpServletResponse res,String param){
		myService.delete(null);
		smService.delete(null);
		return null;
	}

	@RequestMapping("update")
	public String update(HttpServletRequest req,HttpServletResponse res,String param){
		myService.update(null);
		smService.update(null);
		return null;
	}

	@RequestMapping("select")
	public String select(HttpServletRequest req,HttpServletResponse res,String param){
		myService.select(null);
		smService.select(null);
		return null;
	}
	
	@RequestMapping("test")
	public String test(HttpServletRequest req,HttpServletResponse res,String param){
		myTestService.doTest(null);
		return null;
	}
}
