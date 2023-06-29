package com.example.awaylie.utils;

import com.example.awaylie.bean.HomePagerNewsDataBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//这个工具用于返回list<news>数据
public class NewsDataUtil {

    public static List<HomePagerNewsDataBean> getNewsData(){

        List<HomePagerNewsDataBean> newsDataBeanList;
        OkHttpClient client = new OkHttpClient();
        newsDataBeanList = new ArrayList<>();
        String url = "https://www.piyao.org.cn/yybgt/index.htm";
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String html = response.body().string();
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(".tag1");
            for (Element element : elements) {
                String newSource = element.select("h3").text();
                String data = element.select("img").attr("alt").toString();
                URL base = new URL(url);
                String imgUrl = element.select("img").attr("src");
                String htmlUrl = element.select("a").attr("href");
                String imageURL = new URL(base,imgUrl).toString();
                String htmlURL = new URL(base,htmlUrl).toString();
                // 处理提取出的数据
                System.out.println(data+",  "+imageURL+",   "+htmlURL+",   "+newSource);
                //将数据存于实体类中，并且放于lsit中
                HomePagerNewsDataBean homePagerNewsDataBean = new HomePagerNewsDataBean();
                homePagerNewsDataBean.setText(data);
                homePagerNewsDataBean.setNewsSource(newSource);
                homePagerNewsDataBean.setImageUrl(imageURL);
                homePagerNewsDataBean.setNewsUrl(htmlURL);
                newsDataBeanList.add(homePagerNewsDataBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsDataBeanList;
    }
}
