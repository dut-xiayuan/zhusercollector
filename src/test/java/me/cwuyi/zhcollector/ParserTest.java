package me.cwuyi.zhcollector;

import com.alibaba.fastjson.JSON;
import me.cwuyi.zhcollector.bean.ZHAnsw;
import me.cwuyi.zhcollector.bean.ZHQues;
import me.cwuyi.zhcollector.bean.ZHUser;
import me.cwuyi.zhcollector.parser.UserParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Created by xia on 17-10-13.
 */
public class ParserTest {

    @Test
    public void testParseUserBasicInfo() throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader("/home/xia/userInfo.html"));
//        String page = "";
//        String line = "";
//        while ((line = reader.readLine())!=null) {
//            page += line;
//        }
//        ZHUser zhUser = new ZHUser();
//        UserParser.parseUserBasicInfo(page, "lidang", zhUser);
//        System.out.println(JSON.toJSON(zhUser));
    }

    @Test
    public void testParseUserAnsw() throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader("/home/xia/ques.json"));
//        String page = "";
//        String line = "";
//        while ((line = reader.readLine())!=null) {
//            page += line;
//        }
//
//        List<ZHAnsw> list = UserParser.parseUserAnsw(page, "lidang");
//
//        System.out.println(JSON.toJSON(list));

    }

    @Test
    public void testParseUserQues() throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader("/home/xia/answ.json"));
//        String page = "";
//        String line = "";
//        while ((line = reader.readLine())!=null) {
//            page += line;
//        }
//
//        List<ZHQues> list = UserParser.parseUserQues(page, "lidang");
//
//        System.out.println(JSON.toJSON(list));
    }

    @Test
    public void testParseFollowingship() throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader("/home/xia/followship.json"));
//        String page = "";
//        String line = "";
//        while ((line = reader.readLine())!=null) {
//            page += line;
//        }
//
//        List<String> list = UserParser.parseFollowingship(page, "lidang");
//
//        System.out.println(JSON.toJSON(list));

    }

    @Test
    public void testParseTopicFollower() throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader("/home/xia/topicfollow.json"));
//        String page = "";
//        String line = "";
//        while ((line = reader.readLine())!=null) {
//            page += line;
//        }
//
//        List<String> list = UserParser.parseTopicFollower(page);
//
//        System.out.println(JSON.toJSON(list));

    }

}
