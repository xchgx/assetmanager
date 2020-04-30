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
    public String doLogin(User user, HttpServletRequest request, Model model){//接收用户对象参数
        User u = userRepository.findByUsername(user.getUsername());//通过用户的用户名查询用户对象保存在变量u中
        if (u == null) {//用户对象不存在吗
            model.addAttribute("error","用户名不存在");//在模型层中加入错误提示"用户名不存在";
            return "login";//返回到登录视图。
        }
        if (!u.getPassword().equals(user.getPassword())) {//密码不正确吗
            model.addAttribute("error", "密码错误");//在模型层中加入错误提示“密码错误”
            return "login"; //返回到登录视图。
        }
        request.getSession().setAttribute("user",u);//将成功登录的用户对象保存在session会话对象中
        return "redirect:/"+u.getRole()+"/"+u.getRole();//返回登录用户对象的权限视图
    }

    /**
     * 用户注销退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().setAttribute("user", null);
        return "login";

    }


}
