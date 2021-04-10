package com.shiliu.dragon.service.impl;

import com.shiliu.dragon.service.SimpleService;
import org.springframework.stereotype.Service;

@Service
public class SimpleServiceImpl implements SimpleService {

	public String greet(String message) {
		System.out.println("greet:");
		return "hello"+message;
	}
}
