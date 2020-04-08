package com.xchgx.cloud.sso8.assetmanager.command;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCommand implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            User user = new User();
            user.setName("张三");
            user.setUsername("user");
            user.setPassword("123456");
            user.setRole("user");
            userRepository.save(user);
            System.out.println("张三user的账号添加完成");

            User admin = new User();
            admin.setName("李四");
            admin.setUsername("admin");//user
            admin.setPassword("123456789");//123456
            admin.setRole("admin");
            userRepository.save(admin);
            System.out.println("李四admin的账号添加完成");
        }
    }
}
