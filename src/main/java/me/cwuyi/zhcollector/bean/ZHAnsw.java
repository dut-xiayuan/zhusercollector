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

    public String getAnsId()
    {
        return ansId;
    }

    public void setAnsId(String ansId)
    {
        this.ansId = ansId;
    }

    public String getPostDate()
    {
        return postDate;
    }

    public void setPostDate(String postDate)
    {
        this.postDate = postDate;
    }

    public int getCommentCount()
    {
        return commentCount;
    }

    public void setCommentCount(int commentCount)
    {
        this.commentCount = commentCount;
    }

    public int getVoteUpCount()
    {
        return voteUpCount;
    }

    public void setVoteUpCount(int voteUpCount)
    {
        this.voteUpCount = voteUpCount;
    }
}
