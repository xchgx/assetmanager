package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller //Web风格不是REST风格，可以显示视图(V层)
@RequestMapping("/login") //接口（网址）前缀
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    private RukudanController rukudanController;
    /**
     * 直接返回login视图
     * @return
     */
    @GetMapping("/") //默认首页 /login/
    public String index(){
        return "login";//V层 视图
    }

    /**
     * 执行登录判断
     * @param user 登录表单（含用户名和密码）
     * @param request 请求对象
     * @param model Model模式层
     * @return
     */
    @PostMapping("/doLogin")
    public String doLogin(User user, HttpServletRequest request, Model model){
        User u = userRepository.findByUsername(user.getUsername());
        if (u == null) {
            model.addAttribute("error","用户名不存在");
            return "login";
        }
        if (!u.getPassword().equals(user.getPassword())) {
            model.addAttribute("error", "密码错误");
            return "login";
        }
        request.getSession().setAttribute("user",u);//记录下登录成功的标记。
        //返回的视图名称，由用户的权限决定
        //如果用户是user权限（普通用户），那么就返回user视图
        //如果用户是admin权限（管理员），那么就返回admin视图
        return "redirect:/"+u.getRole();//返回角色视图，准备好user.html视图和admin.html视图
    }



}
