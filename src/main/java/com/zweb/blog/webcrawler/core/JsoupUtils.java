package com.zweb.blog.webcrawler.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Jsoup工具类
 *      ：负责处理HTML文档
 */
public class JsoupUtils {
    /**
     *  获取 csdn博客 markdown_views 的内容
     * @param uri 请求的uri
     * @return 返回文档内容
     */
    public static String fectchContent(String uri) {
        if ("".equals(uri) || uri == null) {
            throw new IllegalArgumentException("您输入的URI不正确");
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(uri).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByClass("markdown_views");
        return elements.html();
    }
}
