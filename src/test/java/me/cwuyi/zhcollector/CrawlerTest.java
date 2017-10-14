package me.cwuyi.zhcollector;

import me.cwuyi.zhcollector.crawler.Crawler;
import org.junit.Test;

/**
 * Created by xia on 17-10-14.
 */
public class CrawlerTest {

    @Test
    public void testUserInfoFetcher() {

        System.out.println(Crawler.getTopicFollower("19551147", 0));

    }


}
