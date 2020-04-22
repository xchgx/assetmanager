package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/log")
public class VisitLogController {
    @Autowired
    private VisitLogRepository visitLogRepository;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("logs", visitLogRepository.findAll());
        return "log/index";
    }
}
