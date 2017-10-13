package me.cwuyi.zhcollector.bean;

/**
 * Created by xia on 17-10-12.
 */
public class ZHQues
{
    private String quesId;
    private String quesTitle;
    private String postDate;
    private int followCount;
    private int answerCount;

    public ZHQues()
    {
    }

    public ZHQues(String quesId, String quesTitle, String postDate, int followCount, int answerCount)
    {
        this.quesId = quesId;
        this.quesTitle = quesTitle;
        this.postDate = postDate;
        this.followCount = followCount;
        this.answerCount = answerCount;
    }

    public String getQuesId()
    {
        return quesId;
    }

    public void setQuesId(String quesId)
    {
        this.quesId = quesId;
    }

    public String getQuesTitle()
    {
        return quesTitle;
    }

    public void setQuesTitle(String quesTitle)
    {
        this.quesTitle = quesTitle;
    }

    public String getPostDate()
    {
        return postDate;
    }

    public void setPostDate(String postDate)
    {
        this.postDate = postDate;
    }

    public int getFollowCount()
    {
        return followCount;
    }

    public void setFollowCount(int followCount)
    {
        this.followCount = followCount;
    }

    public int getAnswerCount()
    {
        return answerCount;
    }

    public void setAnswerCount(int answerCount)
    {
        this.answerCount = answerCount;
    }
}
