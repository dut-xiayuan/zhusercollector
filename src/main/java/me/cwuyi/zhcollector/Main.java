package me.cwuyi.zhcollector;

import me.cwuyi.zhcollector.bean.ZHUser;
import me.cwuyi.zhcollector.util.Config;
import me.cwuyi.zhcollector.util.ExportUtil;
import me.cwuyi.zhcollector.worker.UserIdWorker;
import me.cwuyi.zhcollector.worker.UserInfoAssmbleWorker;
import me.cwuyi.zhcollector.zhihu.ModelLogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by xia on 17-10-15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = p.getProperty("zhiHu.emailOrPhoneNum");
        String password = p.getProperty("zhiHu.password");

        int threadPoolSize = Integer.parseInt(p.getProperty("threadPoolSize"));

        ModelLogin login = new ModelLogin();
        System.out.println("在resource目录下找到验证码图片并输入(zhiHuYZM.gif):");
        login.login(username, password);

        List<String> userList = new ArrayList<>();
        String topic = p.getProperty("topic.id");

        UserIdWorker userIdWorker = new UserIdWorker(userList, topic);
        userIdWorker.work();

        System.out.println("该话题有关注用户: " + userList.size());

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        List<Future<ZHUser>> taskList = new ArrayList<>();

        for (String userId : userList) {
            taskList.add(executor.submit(new UserInfoAssmbleWorker(userId)));
        }

        int total = taskList.size();
        int nowCount = 0;
        int forward = 10;

        for (Future<ZHUser> task : taskList) {
            ZHUser zhUser = task.get();
            ExportUtil.export(zhUser);
            nowCount++;
            if (((double)nowCount/total)*100 > forward) {
                System.out.println("完成 "+nowCount+"/"+total);
                forward+=10;
            }
        }
        executor.shutdown();
        System.out.println("收集完成");
    }
}
