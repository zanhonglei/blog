package com.zweb.blog.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpClientFactory {
    private static Logger logger = Logger.getLogger(HttpClientFactory.class);
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     *
     * @param uri
     * @return map:
     */
    public static Map get(String uri){
        HashMap<String, Object> map = new HashMap<String,Object>();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(uri);
            map.put("uri", httpget.getURI());
            logger.info("请求地址："+uri);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                logger.info("打印响应状态:"+response.getStatusLine());
                map.put("status",response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    String content = EntityUtils.toString(entity);
                    logger.info("Response content: " + content);
                    map.put("content", content);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String,Object> map = HttpClientFactory.get("http://blog.csdn.net/z3881006/article/details/78936887");
        String content;
        if ("200".endsWith(map.get("status").toString())) {
            content = map.get("content").toString();
        }
        //Pattern pattern = new Pattern();

        //<div class="markdown_views"></div>
    }
}
