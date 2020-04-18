package com.xchgx.cloud.sso8.assetmanager.command;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InitCommand implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private AssetRuKuDanRepository ruKuDanRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public void run(String... args) throws Exception {
        //初始化用户
        if(userRepository.count() == 0){
            List<String> usernames = new ArrayList<>();

            User user1 = new User();
            user1.setName("张三");
            user1.setUsername("user1");
            user1.setPassword("123456");
            user1.setRole("user");
            userRepository.save(user1);

            User user2 = new User();
            user2.setName("李四");
            user2.setUsername("user2");//user
            user2.setPassword("123456");//123456
            user2.setRole("user");
            userRepository.save(user2);

            User admin = new User();
            admin.setName("管理员");
            admin.setUsername("admin");//user
            admin.setPassword("123456");//123456
            admin.setRole("admin");
            userRepository.save(admin);
            userRepository.save(new User("20182011001","20182011001","李佳燊","user"));
            userRepository.save(new User("20182011002","20182011002","陈千","user"));
            userRepository.save(new User("20182011003","20182011003","李启国","user"));
            userRepository.save(new User("20182011005","20182011005","金刘星","user"));
            userRepository.save(new User("20182011006","20182011006","吴宇","user"));
            userRepository.save(new User("20182011007","20182011007","李仁泽","user"));
            userRepository.save(new User("20182011008","20182011008","王业","user"));
            userRepository.save(new User("20182011009","20182011009","吴小坤","user"));
            userRepository.save(new User("20182011010","20182011010","吴鸿韬","user"));
            userRepository.save(new User("20182011012","20182011012","姚玮","user"));
            userRepository.save(new User("20182012011","20182012011","张煜锋","user"));
            userRepository.save(new User("20182011013","20182011013","赖家铭","user"));
            userRepository.save(new User("20182071001","20182071001","宋嘉诚","user"));
            userRepository.save(new User("20182071002","20182071002","龚雄伟","user"));
            userRepository.save(new User("20182071003","20182071003","杨文龙","user"));
            userRepository.save(new User("20182071004","20182071004","胡奇","user"));
            userRepository.save(new User("20182071005","20182071005","汪海","user"));
            userRepository.save(new User("20182071006","20182071006","熊凯","user"));
            userRepository.save(new User("20182071007","20182071007","赵竟","user"));
            userRepository.save(new User("20182071008","20182071008","黄淋康","user"));
            userRepository.save(new User("20182071009","20182071009","陈佳","user"));
            userRepository.save(new User("20182071011","20182071011","李舒普","user"));
            userRepository.save(new User("20182072001","20182072001","尹练宇","user"));
            System.out.println(userRepository.count()+"个用户初始化完毕。");
        }
        //初始化入库单
        if(ruKuDanRepository.count()==0){
            AssetRuKuDan rukudan = new AssetRuKuDan();
            rukudan.setType("办公用品");
            rukudan.setScrq(new Date(2019,3,1));
            rukudan.setReadme("电脑");
            rukudan.setPrice(3000);
            rukudan.setBzq("质保3年");
            rukudan.setName("电脑");
            rukudan.setAmount(32);
            rukudan.setRemained(rukudan.getAmount());
            ruKuDanRepository.save(rukudan);

            AssetRuKuDan rukudan2 = new AssetRuKuDan();
            rukudan2.setType("办公用品");
            rukudan2.setScrq(new Date(2019,3,1));
            rukudan2.setReadme("安卓");
            rukudan2.setPrice(3000);
            rukudan2.setBzq("质保2年");
            rukudan2.setName("手机");
            rukudan2.setAmount(22);
            rukudan2.setRemained(rukudan2.getAmount());
            ruKuDanRepository.save(rukudan2);

            AssetRuKuDan rukudan3 = new AssetRuKuDan();
            rukudan3.setType("办公耗材");
            rukudan3.setScrq(new Date(2019,3,1));
            rukudan3.setReadme("A4");
            rukudan3.setPrice(3000);
            rukudan3.setBzq("质保1年");
            rukudan3.setName("打印纸");
            rukudan3.setAmount(100);
            rukudan3.setRemained(rukudan3.getAmount());
            ruKuDanRepository.save(rukudan3);
            //初始化资产
            Asset asset = new Asset();
            asset.setType(rukudan.getType());
            asset.setReadme(rukudan.getReadme());
            asset.setScrq(rukudan.getScrq());
            asset.setBzq(rukudan.getBzq());
            asset.setPrice(rukudan.getPrice());
            asset.setRukudanId(rukudan.getId());
            asset.setName(rukudan.getName());
            asset.setUsername(null);
            asset.setStatus("空闲");
            assetRepository.save(asset);

            Asset asset2 = new Asset();
            asset2.setType(rukudan2.getType());
            asset2.setReadme(rukudan2.getReadme());
            asset2.setScrq(rukudan2.getScrq());
            asset2.setBzq(rukudan2.getBzq());
            asset2.setPrice(rukudan2.getPrice());
            asset2.setRukudanId(rukudan2.getId());
            asset2.setName(rukudan2.getName());
            asset2.setUsername(null);
            asset2.setStatus("空闲");
            assetRepository.save(asset2);

            Asset asset3 = new Asset();
            asset3.setType(rukudan3.getType());
            asset3.setReadme(rukudan3.getReadme());
            asset3.setScrq(rukudan3.getScrq());
            asset3.setBzq(rukudan3.getBzq());
            asset3.setPrice(rukudan3.getPrice());
            asset3.setRukudanId(rukudan3.getId());
            asset3.setName(rukudan3.getName());
            asset3.setUsername(null);
            asset3.setStatus("空闲");
            assetRepository.save(asset3);


        }



    }
}
