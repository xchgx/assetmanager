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

import java.util.Date;

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
            User user1 = new User();
            user1.setName("张三");
            user1.setUsername("user1");
            user1.setPassword("123456");
            user1.setRole("user");
            userRepository.save(user1);
            System.out.println("张三 user1 的账号添加完成");

            User user2 = new User();
            user2.setName("李四");
            user2.setUsername("user2");//user
            user2.setPassword("123456");//123456
            user2.setRole("user");
            userRepository.save(user2);
            System.out.println("李四 user2 的账号添加完成");

            User admin = new User();
            admin.setName("管理员");
            admin.setUsername("admin");//user
            admin.setPassword("123456");//123456
            admin.setRole("admin");
            userRepository.save(admin);
            System.out.println("管理员 admin 的账号添加完成");

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
            asset.setStatus(null);
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
            asset2.setStatus(null);
            assetRepository.save(asset2);

        }



    }
}
