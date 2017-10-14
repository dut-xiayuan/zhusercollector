package me.cwuyi.zhcollector.crawler;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xia on 17-10-13.
 */
public class Crawler {
    private static CloseableHttpClient client;

    static {
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        client = HttpClients.custom().setDefaultRequestConfig(config).build();

        try {
            HttpGet get = new HttpGet("Https://www.zhihu.com/");
            CloseableHttpResponse response = client.execute(get);
            String page = EntityUtils.toString(response.getEntity());
            String xsrfValue = Jsoup.parse(page).getElementsByAttributeValue("name", "_xsrf").get(0).attr("value");


            List<NameValuePair> pairs = new LinkedList<NameValuePair>();
            pairs.add(new BasicNameValuePair("_xsrf", xsrfValue));
            pairs.add(new BasicNameValuePair("password", "4011604A"));
            pairs.add(new BasicNameValuePair("email", "xiayuan.y@foxmail.com"));
            pairs.add(new BasicNameValuePair("captcha_type", "cn"));
            pairs.add(new BasicNameValuePair("remember_me", "true"));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);
            String cap_id = "cap_id=\"NDE2YmYxNjY4OGVlNDlmYzkzMjM3NzYyOTkzMTllOTg=|1507945486|b71b7b1bfae80df8f83b87e9238c2892288dc264\"";


            HttpPost post = new HttpPost("https://www.zhihu.com/login/email");
            post.setHeader("Cookie", cap_id+";_xsrf:"+xsrfValue);
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
            post.setEntity(entity);

            HttpResponse response1 = client.execute(post);
            System.out.println(EntityUtils.toString(response1.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String userInfoFetcher(String userId) {
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

    public static String followShipFetcher(String userId, boolean wantFollower, int pageIndex) {
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


    private static CloseableHttpResponse getResponse(HttpGet get) {
        CloseableHttpResponse response = null;

        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
