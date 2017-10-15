package me.cwuyi.zhcollector.crawler;

import me.cwuyi.zhcollector.util.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xia on 17-10-13.
 */
public class Crawler {

    private static String xsrf = "";

    public static String userInfoFetcher(String userId) {
        String url = "https://www.zhihu.com/people/" + userId + "/activities";
        String res = "";
        try {
            res = HttpClientUtil.getWebPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //需要登录
    public static String followShipFetcher(String userId, boolean wantFollower, int pageIndex) {
        String followship = wantFollower ? "followers" : "followees";
        String getUrlHead = "http://www.zhihu.com/api/v4/members/" + userId + "/" + followship;
        String getUrlInclude = "?include=data[*].answer_count,articles_count,gender,follower_count,is_followed,is_following,badge[?(type=best_answerer)].topics";
        String getUrl = getUrlHead + getUrlInclude + "&limit=20" + "&offset="+(pageIndex*20);

        String res = "";

        try {
            res = HttpClientUtil.getWebPage(getUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String answFetcher(String userId, int pageIndex) {
        String getUrlHead = "http://www.zhihu.com/api/v4/members/" + userId + "/answers";
        String getUrlInclude = "?include=data[*].is_normal,admin_closed_comment,reward_info,is_collapsed," +
                "annotation_action,annotation_detail,collapse_reason,collapsed_by,suggest_edit," +
                "comment_count,can_comment,content,voteup_count,reshipment_settings,comment_permission,mark_infos," +
                "created_time,updated_time,review_info,question,excerpt,relationship.is_authorized,voting,is_author," +
                "is_thanked,is_nothelp,upvoted_followees;data[*].author.badge[?(type=best_answerer)].topics";
        String getUrl = getUrlHead + getUrlInclude + "&limit=20" + "&offset="+(pageIndex*20);

        String res = "";

        try {
            res = HttpClientUtil.getWebPage(getUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String quesFetcher(String userId, int pageIndex) {
        String getUrlHead = "http://www.zhihu.com/api/v4/members/" + userId + "/questions";
        String getUrlInclude = "?include=data[*].created,answer_count,follower_count,author,admin_closed_comment";
        String getUrl = getUrlHead + getUrlInclude + "&limit=20" + "&offset=" + (pageIndex*20);

        String res = "";

        try {
            res = HttpClientUtil.getWebPage(getUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String getTopicFollower(String topic, int offset) {
        String url = "https://www.zhihu.com/topic/" + topic + "/followers";
        if (xsrf.equals("")) {
            try {
                xsrf = Jsoup.parse(HttpClientUtil.getWebPage(url)).getElementsByAttributeValue("name", "_xsrf").attr("value");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpPost post = new HttpPost(url);
        post.setHeader("X-Xsrftoken", xsrf);
        String res = "";

        try {
            Map<String, String> map = new HashMap<>();
            map.put("offset", offset*20+"");
            HttpClientUtil.setHttpPostParams(post, map);

            CloseableHttpResponse response = HttpClientUtil.getResponse(post);

            res = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }



}
