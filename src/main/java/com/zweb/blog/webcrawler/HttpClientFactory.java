package com.zweb.blog.webcrawler;

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

public class HttpClientFactory {
    private static Logger logger = Logger.getLogger(HttpClientFactory.class);
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 通过Get请求，返回该网页的html代码
     * @param uri 请求的地址
     * @return map:
     *              uri:请求的地址
     *              status：http状态码
     *              content：返回的html代码
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
                    logger.info("Response content length: " + entity.getContentLength());
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
}
