package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/log")
public class VisitLogController {
    @Autowired
    private VisitLogRepository visitLogRepository;

    @GetMapping("/index")
    public String index(@RequestParam(defaultValue = "0") int page , Model model){
        PageRequest pageRequest = PageRequest.of(page<0?0:page, 10, Sort.Direction.DESC, "date");
        model.addAttribute("logs", visitLogRepository.findAll(pageRequest));
        return "log/index";
    }
    @GetMapping("/username")
    public String username(Model model){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "date");
        model.addAttribute("logs", visitLogRepository.findAllByUsernameNotOrderByDate("未登录",pageRequest));
        return "log/username";
    }
}
