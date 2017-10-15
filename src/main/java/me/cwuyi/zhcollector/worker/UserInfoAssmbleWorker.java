package me.cwuyi.zhcollector.worker;

import com.alibaba.fastjson.JSON;
import me.cwuyi.zhcollector.bean.ZHAnsw;
import me.cwuyi.zhcollector.bean.ZHQues;
import me.cwuyi.zhcollector.bean.ZHUser;
import me.cwuyi.zhcollector.crawler.Crawler;
import me.cwuyi.zhcollector.parser.UserParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by xia on 17-10-15.
 */
public class UserInfoAssmbleWorker implements Callable<ZHUser>{

    private String userId;

    public UserInfoAssmbleWorker(String userId) {
        this.userId = userId;
    }

    @Override
    public ZHUser call() throws Exception {
        ZHUser zhUser = new ZHUser();
        zhUser.setAsks(new ArrayList<ZHQues>());
        zhUser.setAnsws(new ArrayList<ZHAnsw>());
        zhUser.setFollowers(new ArrayList<String>());
        zhUser.setFollowing(new ArrayList<String>());

        //基本信息
        String plainUserInfo = Crawler.userInfoFetcher(userId);
        UserParser.parseUserBasicInfo(plainUserInfo, userId, zhUser);

        //用户提问信息
        List<ZHQues> quesList = null;

        int offset = 0;

        do {
            String plainQuesPage = Crawler.quesFetcher(userId, offset++);
            quesList = UserParser.parseUserQues(plainQuesPage, userId);
            zhUser.getAsks().addAll(quesList);

        } while (quesList != null && quesList.size() > 0);

        //用户回答信息
        List<ZHAnsw> answsList = null;

        offset = 0;
        do {
            String plainQuesPage = Crawler.answFetcher(userId, offset++);
            answsList = UserParser.parseUserAnsw(plainQuesPage, userId);
            zhUser.getAnsws().addAll(answsList);

        } while (answsList != null && answsList.size() > 0);

        //关注者
        List<String> followingList = null;

        offset = 0;
        do {
            String plainQuesPage = Crawler.followShipFetcher(userId, false, offset++);
            followingList = UserParser.parseFollowingship(plainQuesPage, userId);
            zhUser.getFollowing().addAll(followingList);

        } while (followingList != null && followingList.size() > 0);

        //被关注者
        List<String> followerList = null;

        offset = 0;
        do {
            String plainQuesPage = Crawler.followShipFetcher(userId, true, offset++);
            followerList = UserParser.parseFollowingship(plainQuesPage, userId);
            zhUser.getFollowers().addAll(followerList);

        } while (followerList != null && followerList.size() > 0);

        return zhUser;
    }
}
