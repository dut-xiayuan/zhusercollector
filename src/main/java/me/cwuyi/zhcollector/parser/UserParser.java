package me.cwuyi.zhcollector.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.cwuyi.zhcollector.bean.ZHAnsw;
import me.cwuyi.zhcollector.bean.ZHQues;
import me.cwuyi.zhcollector.bean.ZHUser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xia on 17-10-12.
 */
public class UserParser {

    public static void parseUserBasicInfo(String page, String userId, ZHUser zhUser) {
        Document doc = Jsoup.parse(page);
        Elements userNameElements = doc.select("span.ProfileHeader-name");
        String username = userNameElements.get(0).text().trim();

        //关注 关注者
        Elements followship = doc.select(".NumberBoard").select(".FollowshipCard-counts");
        int followingCount = Integer.parseInt(followship.get(0).select("a").get(0).select(".NumberBoard-value").text());
        int followerCount = Integer.parseInt(followship.get(0).select("a").get(1).select(".NumberBoard-value").text());

        //回答 提问
        Elements tabs = doc.select(".Tabs").select(".ProfileMain-tabs").select("li");
        int asksCount = Integer.parseInt(tabs.get(2).select("span.Tabs-meta").text());
        int answersCount = Integer.parseInt(tabs.get(1).select("span.Tabs-meta").text());

        //感谢 赞同 收藏
        Elements card = doc.select(".Profile-sideColumnItems");

        Elements sideColumns = card.get(0).select(".Profile-sideColumnItem");
        Element targetEle = null;
        if (sideColumns.size() > 2) {
            targetEle = sideColumns.get(1);
        } else {
            targetEle = sideColumns.get(0);
        }

        //likeString 形如获得XXX次赞同
        String likeString = targetEle.select(".IconGraf").text().trim();
        int likeCount = -1;

        //thankAndCollectedString 形如获得XXX次感谢，XXX次收藏
        String thankAndCollectedString = targetEle.select(".Profile-sideColumnItemValue").text().trim();
        int thankCount = -1, collectedCount = -1;

        String reg = "[^0-9]";
        Pattern pattern = Pattern.compile(reg);

        Matcher matcher = pattern.matcher(likeString);
        likeCount = Integer.parseInt(matcher.replaceAll("").trim());

        String thankString = thankAndCollectedString.split("，")[0];
        String collectedString = "0";

        if (thankAndCollectedString.split("，").length == 2) {
            collectedString = thankAndCollectedString.split("，")[1];
        }

        matcher = pattern.matcher(thankString);
        thankCount = Integer.parseInt(matcher.replaceAll("").trim());

        matcher = pattern.matcher(collectedString);
        collectedCount = Integer.parseInt(matcher.replaceAll("").trim());

        zhUser.setUserId(userId);
        zhUser.setUserName(username);
        zhUser.setFollowingCount(followingCount);
        zhUser.setFollowersCount(followerCount);
        zhUser.setAsksCount(asksCount);
        zhUser.setAnswersCount(answersCount);
        zhUser.setLikeCount(likeCount);
        zhUser.setThankCount(thankCount);
        zhUser.setCollectedCount(collectedCount);
    }

    public static List<ZHQues> parseUserQues(String page, String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<ZHQues> res = new ArrayList<ZHQues>();
        JSONObject jsonObject = JSON.parseObject(page);
        JSONArray array = jsonObject.getJSONArray("data");

        for (int i = 0; i < array.size(); ++i) {
            JSONObject data = array.getJSONObject(i);
            long date = Long.parseLong(data.getString("created"))*1000;
            String postDate = sdf.format(new Date(date));

            String quesId = data.getString("id");
            int followCount = data.getInteger("follower_count");
            int answerCount = data.getInteger("answer_count");
//            String quesTitle = data.getString("title");
            String quesTitle = "";

            res.add(new ZHQues(quesId, quesTitle, postDate, followCount, answerCount));
        }

        return res;
    }

    public static List<ZHAnsw> parseUserAnsw(String page, String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<ZHAnsw> res = new ArrayList<ZHAnsw>();
        JSONObject jsonObject = JSON.parseObject(page);
        JSONArray array = jsonObject.getJSONArray("data");

        for (int i = 0; i < array.size(); ++i) {
            JSONObject data = array.getJSONObject(i);
            long date = Long.parseLong(data.getString("created_time"))*1000;
            Date dateS = new Date(date);
            String dateString = sdf.format(new Date(date));

            String answId = data.getString("id");
            int voteUpCount = data.getInteger("voteup_count");
            int commentCount = data.getInteger("comment_count");

            res.add(new ZHAnsw(answId, dateString, commentCount, voteUpCount));
        }

        return res;
    }

    public static List<String> parseFollowingship(String page, String userId) {
        List<String> res = new ArrayList<String>();
        JSONObject jsonObject = JSON.parseObject(page);
        JSONArray array = jsonObject.getJSONArray("data");

        for (int i = 0; i < array.size(); ++i) {
            res.add(array.getJSONObject(i).getString("url_token"));
        }

        return res;
    }

    public static List<String> parseTopicFollower(String page) {
        List<String> res = new ArrayList<String>();
        JSONObject jsonObject = JSON.parseObject(page);
        JSONArray array = jsonObject.getJSONArray("msg");

        Document followerDoc = Jsoup.parse(array.getString(1));
        Elements elements = followerDoc.select("div.zm-person-item");

        for (int i = 0; i < elements.size(); ++i) {
            String follower = elements.get(i).select("a.zm-list-avatar-medium").get(0).attr("href").split("/")[2];
            res.add(follower);
        }

        return res;
    }



}
