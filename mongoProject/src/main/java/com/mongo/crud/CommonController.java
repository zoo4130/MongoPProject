package com.mongo.crud;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonController {
	
	@Autowired
	private MongoService mongoService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		System.out.println("=========================");
		System.out.println("mongo Test");
		
		//mongoService.insert();
		//mongoService.select();
		
		return "home";
	}
	
	/**
	 * 몽고디비 data 조회 ($.ajax @RequestBody)
	 */ 
	@ResponseBody 
	@RequestMapping(value = "/getData",produces="application/json", method = RequestMethod.POST)
	public List<Map<String, Object>> getData(@RequestBody Integer param) throws Exception{
		
		System.out.println("===============================");
		System.out.println("MongoDB MapReduce Result: "+param);
		
		//param에 ""이 붙어서 넘어가니까 ""1111""이 되버림. 그래서 인티저로 넘김
		List<Map<String, Object>> result = mongoService.getData(param);
		
		return result;
	}
	
}
