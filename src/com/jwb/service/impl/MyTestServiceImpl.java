package com.jwb.service.impl;

import java.util.Map;

import com.jwb.annotation.Service;

@Service("MyTestServiceImpl")
public class MyTestServiceImpl implements MyTestService {

	@Override
	public int doTest(Map map) {
		System.out.println("this is my test");
		return 0;
	}

}
