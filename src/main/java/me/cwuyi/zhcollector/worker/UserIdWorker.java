package me.cwuyi.zhcollector.worker;

import me.cwuyi.zhcollector.crawler.Crawler;
import me.cwuyi.zhcollector.parser.UserParser;
import me.cwuyi.zhcollector.util.Config;
import me.cwuyi.zhcollector.util.HttpClientUtil;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xia on 17-10-15.
 */
public class UserIdWorker {
    List<String> userList;
    String topic;

    public UserIdWorker(){}

    public UserIdWorker(List<String> list, String topic) {
        this.userList = list;
        this.topic = topic;
    }

    public List<String> getList()
    {
        return userList;
    }

    public void setList(List<String> list)
    {
        this.userList = list;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public void work() {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int totalFollower = 0;
        try {
            String page = HttpClientUtil.getWebPage("https://www.zhihu.com/topic/" + topic + "/followers");
            totalFollower = Integer.parseInt(Jsoup.parse(page).select("div.zm-topic-side-followers-info").get(0).select("strong").text().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("关注人数 " + totalFollower);

        List<String> list = null;
        int offset = 0;
        do {

            String plainFollwer = Crawler.getTopicFollower(topic, offset);
            list = UserParser.parseTopicFollower(plainFollwer);

            userList.addAll(list);

            if (list.size() == 0 && userList.size() < totalFollower) {
                offset = offset;
            } else {
                offset++;
            }

            System.out.print("话题关注者偏移量 " + offset);
            System.out.println("  已收集数" + userList.size());
            System.out.println();

        } while (userList.size() < totalFollower);
    }
}
