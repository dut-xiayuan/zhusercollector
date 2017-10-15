package me.cwuyi.zhcollector.util;

import me.cwuyi.zhcollector.bean.ZHAnsw;
import me.cwuyi.zhcollector.bean.ZHQues;
import me.cwuyi.zhcollector.bean.ZHUser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by xia on 17-10-15.
 */
public class ExportUtil {

    //输出到CSV文件
    public static void export(ZHUser zhUser) {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userinfoOutputPath = p.getProperty("userinfoOutputPath");
        String userAskInfoOutputPath = p.getProperty("userAskInfoOutputPath");
        String userAnswerOutputPath = p.getProperty("userAnswerInfoOutputPath");
        BufferedWriter userinfo = null, userAsk=null, userAnswer=null;
        try {
            userinfo = new BufferedWriter(new FileWriter(userinfoOutputPath, true));
            userAsk = new BufferedWriter(new FileWriter(userAskInfoOutputPath, true));
            userAnswer = new BufferedWriter(new FileWriter(userAnswerOutputPath, true));
            StringBuilder sb = new StringBuilder();

            sb.append(zhUser.getUserId()+",");
            sb.append(zhUser.getUserName()+",");
            sb.append(zhUser.getFollowingCount()+",");
            sb.append(zhUser.getFollowersCount()+",");
            sb.append(zhUser.getAsksCount()+",");
            sb.append(zhUser.getAnswersCount()+",");
            sb.append(zhUser.getFollowing().toString().substring(1, zhUser.getFollowing().toString().length()-1).replaceAll(",", "，")+",");
            sb.append(zhUser.getFollowers().toString().substring(1, zhUser.getFollowers().toString().length()-1).replaceAll(",", "，"));
            userinfo.append(sb.toString());
            userinfo.newLine();
            userinfo.flush();

            sb = new StringBuilder("");
            List<ZHQues> quesList = zhUser.getAsks();
            for (ZHQues zhQues : quesList) {
                sb.append(zhUser.getUserId()+",");
                sb.append(zhQues.getQuesId()+",");
                sb.append(zhQues.getPostDate()+",");
                sb.append(zhQues.getFollowCount()+",");
                sb.append(zhQues.getAnswerCount()+",");
                sb.append("\r\n");

            }
            userAsk.append(sb.toString());
            userAsk.flush();

            sb = new StringBuilder("");
            List<ZHAnsw> answList = zhUser.getAnsws();
            for (ZHAnsw zhAnsw : answList) {
                sb.append(zhUser.getUserId()+",");
                sb.append(zhAnsw.getAnsId()+",");
                sb.append(zhAnsw.getPostDate()+",");
                sb.append(zhAnsw.getCommentCount()+",");
                sb.append(zhAnsw.getVoteUpCount()+",");
                sb.append("\r\n");
            }
            userAnswer.append(sb.toString());
            userAnswer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                userinfo.close();
                userAsk.close();
                userAnswer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
