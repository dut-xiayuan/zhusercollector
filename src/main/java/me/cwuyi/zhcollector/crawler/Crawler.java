package me.cwuyi.zhcollector.crawler;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xia on 17-10-13.
 */
public class Crawler {
    private static CloseableHttpClient client;

    public static String UserInfoFetcher(String userId) {
        HttpGet get = new HttpGet("https://www.zhihu.com/people/" + userId + "/activities");
        CloseableHttpResponse response = getResponse(get);
        String res = "";
        try {
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String FollowShipFetcher(String userId, boolean wantFollower, int pageIndex) {
        String followship = wantFollower ? "followers" : "followees";
        String getUrlHead = "http://www.zhihu.com/api/v4/members/" + userId + "/" + followship;
        String getUrlInclude = "?include=data%5B%2A%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics";
        String getUrl = getUrlHead + getUrlInclude + "&limit=20" + "&offset="+(pageIndex*20);

        HttpGet get = new HttpGet(getUrl);
        CloseableHttpResponse response = getResponse(get);
        String res = "";

        try {
            res = EntityUtils.toString(response.getEntity());
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

        HttpGet get = new HttpGet(getUrl);
        CloseableHttpResponse response = getResponse(get);
        String res = "";

        try {
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String quesFetcher(String userId, int pageIndex) {
        String getUrlHead = "http://www.zhihu.com/api/v4/members/" + userId + "/questions";
        String getUrlInclude = "data[*].created,answer_count,follower_count,author,admin_closed_comment";
        String getUrl = getUrlHead + getUrlInclude + "&limit=20" + "&offset=" + (pageIndex*20);

        HttpGet get = new HttpGet(getUrl);
        CloseableHttpResponse response = getResponse(get);
        String res = "";

        try {
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String getTopicFollower(String topic, int offset) {
        HttpPost post = new HttpPost("https://www.zhihu.com/topic/" + topic + "/followers");
        HttpResponse response = null;
        List<NameValuePair> parameter = new LinkedList<NameValuePair>();
        parameter.add(new BasicNameValuePair("offset", offset+""));
        post.setEntity(new UrlEncodedFormEntity(parameter, Consts.UTF_8));
        String res = "";

        try {
            response = client.execute(post);
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


    public static CloseableHttpResponse getResponse(HttpGet get) {
        CloseableHttpResponse response = null;

        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
