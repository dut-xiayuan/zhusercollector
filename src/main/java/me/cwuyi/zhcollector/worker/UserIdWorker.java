package me.cwuyi.zhcollector.worker;

import me.cwuyi.zhcollector.crawler.Crawler;
import me.cwuyi.zhcollector.parser.UserParser;

import java.util.List;
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
        List<String> list = null;
        int offset = 0;
        do {

            String plainFollwer = Crawler.getTopicFollower(topic, offset++);
            list = UserParser.parseTopicFollower(plainFollwer);

            userList.addAll(list);

        } while (list != null && list.size() > 0);
    }
}
