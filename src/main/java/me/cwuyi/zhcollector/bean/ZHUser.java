package me.cwuyi.zhcollector.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xia on 17-10-12.
 */
public class ZHUser
{
    private String userId;
    private String userName;
    private int followingCount;
    private int followersCount;
    private int asksCount;
    private int answersCount;
    private int likeCount;
    private int thankCount;
    private int collectedCount;
    private int sharedCount;

    private List<ZHQues> asks;
    private List<ZHAnsw> answs;

    private List<String> following;
    private List<String> followers;

    public ZHUser() {}

    public ZHUser(String userId, String userName, int followingCount, int followersCount, int asksCount,
                  int answersCount, int likeCount, int thankCount, int collectedCount)
    {
        this.userId = userId;
        this.userName = userName;
        this.followingCount = followingCount;
        this.followersCount = followersCount;
        this.asksCount = asksCount;
        this.answersCount = answersCount;
        this.likeCount = likeCount;
        this.thankCount = thankCount;
        this.collectedCount = collectedCount;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getFollowingCount()
    {
        return followingCount;
    }

    public void setFollowingCount(int followingCount)
    {
        this.followingCount = followingCount;
    }

    public int getFollowersCount()
    {
        return followersCount;
    }

    public void setFollowersCount(int followersCount)
    {
        this.followersCount = followersCount;
    }

    public int getAsksCount()
    {
        return asksCount;
    }

    public void setAsksCount(int asksCount)
    {
        this.asksCount = asksCount;
    }

    public int getAnswersCount()
    {
        return answersCount;
    }

    public void setAnswersCount(int answersCount)
    {
        this.answersCount = answersCount;
    }

    public int getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(int likeCount)
    {
        this.likeCount = likeCount;
    }

    public int getThankCount()
    {
        return thankCount;
    }

    public void setThankCount(int thankCount)
    {
        this.thankCount = thankCount;
    }

    public int getCollectedCount()
    {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount)
    {
        this.collectedCount = collectedCount;
    }

    public int getSharedCount()
    {
        return sharedCount;
    }

    public void setSharedCount(int sharedCount)
    {
        this.sharedCount = sharedCount;
    }

    public List<ZHQues> getAsks()
    {
        return asks;
    }

    public void setAsks(List<ZHQues> asks)
    {
        this.asks = asks;
    }

    public List<ZHAnsw> getAnsws()
    {
        return answs;
    }

    public void setAnsws(List<ZHAnsw> answs)
    {
        this.answs = answs;
    }

    public List<String> getFollowing()
    {
        return following;
    }

    public void setFollowing(List<String> following)
    {
        this.following = following;
    }

    public List<String> getFollowers()
    {
        return followers;
    }

    public void setFollowers(List<String> followers)
    {
        this.followers = followers;
    }
}
