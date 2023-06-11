package com.example.awaylie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.piyao.org.cn/yybgt/index.htm";
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String html = response.body().string();
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(".tag1");
            for (Element element : elements) {

                String newsOrigin = element.select("h3").text();
                String data = element.select("img").attr("alt").toString();
                URL base = new URL(url);
                String imgurl = element.select("img").attr("src");
                String htmlUrl = element.select("a").attr("href");
                String imageURL = new URL(base,imgurl).toString();
                String htmlURL = new URL(base,htmlUrl).toString();
                // 处理提取出的数据
                System.out.println(data+",  "+imageURL+",   "+htmlURL+",   "+newsOrigin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}