//package com.inetum.training.allBeans.controller;
//
//import com.inetum.training.allBeans.service.BeansService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Map;
//
//@Controller
//public class BeansController {
//
//    @Autowired
//    private BeansService beansService;
//
//    @RequestMapping(value="/displayallbeans")
//    public String getHeaderAndBody(Map model){
//        model.put("header", beansService.getHeader());
//        model.put("message", beansService.getBody());
//        return "displayallbeans";
//    }
//
//}
