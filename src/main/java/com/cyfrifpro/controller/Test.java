package com.cyfrifpro.controller;

import java.util.Map;
import com.razorpay.*;

import io.swagger.v3.oas.annotations.Operation;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
//@CrossOrigin(origins="http://localhost:3000")
public class Test {

	@Operation(summary = "Method to Tset the Razor Pay payment integration")
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
		System.out.println(data);
		
		int amt= Integer.parseInt(data.get("amount").toString());
		
		var client = new RazorpayClient("rzp_test_LtwJVThA9sZotx", "4736Achq5PCgTHm4UbN5zqc6");
		
		JSONObject obj = new JSONObject();
		obj.put("amount", amt*100);
		obj.put("currency", "INR");
		obj.put("receipt", "txn_2356");
		
		//Creating new order (Request going to razorpay server)
		
		Order order = client.Orders.create(obj);
		System.out.println(order);
		
		//If you want you can save order id or details in data base 
		
		return order.toString(); // Here we return the Details with Order Id to client
	}
	
}
