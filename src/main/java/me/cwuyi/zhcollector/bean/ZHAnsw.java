package me.cwuyi.zhcollector.bean;

/**
 * Created by xia on 17-10-12.
 */
public class ZHAnsw
{
    private String ansId;
    private String postDate;
    private int commentCount;
    private int voteUpCount;

    public ZHAnsw() {}

    public ZHAnsw(String ansId, String postDate, int commentCount, int voteUpCount) {
        this.ansId = ansId;
        this.postDate = postDate;
        this.commentCount = commentCount;
        this.voteUpCount = voteUpCount;
    }
}
