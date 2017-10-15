package me.cwuyi.zhcollector.util;

/**
 * Created by xia on 17-10-15.
 */
import java.io.IOException;
import java.util.Properties;

/**
 * 加载配置文件
 */
public class Config {
    /**
     * 验证码路径
     */
    public static String verificationCodePath;
    /**
     * 知乎注册手机号码或有限
     */
    public static String emailOrPhoneNum;
    /**
     * 知乎密码
     */
    public static String password;

    /**
     * cookie路径
     */
    public static String cookiePath;
    /**
     * proxyPath
     */
    public static String proxyPath;
    static {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        verificationCodePath = p.getProperty("verificationCodePath");
        emailOrPhoneNum = p.getProperty("zhiHu.emailOrPhoneNum");
        password = p.getProperty("zhiHu.password");
        cookiePath = p.getProperty("cookiePath");
        proxyPath = p.getProperty("proxyPath");
    }

}