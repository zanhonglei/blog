package com.zweb.blog.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class JsoupUtils {
    public static String p(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        Elements element = document.select("div[class=markdown_views]");
        return element.html();
    }

    public static void main(String[] args) {
        Map map = HttpClientFactory.get("http://blog.csdn.net/z3881006/article/details/78936887");
        Object content = map.get("content");
        String html = JsoupUtils.p(content.toString());
        System.out.println(html);



    }
}
