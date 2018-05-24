/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InternetUtil
 * Author:   apple
 * Date:     2018/5/24 上午11:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.agent.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author apple
 * @create 2018/5/24
 * @since 1.0.0
 */
public class InternetUtil {

    /*
     * 发送POST请求
     */

    public static String sendPost(String url, String body, String ak_id, String ak_secret) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             *      http header 参数
             */

            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = PublicUtil.toGMTString(new Date());

            // 1. 对boby做MD5+BASE64加密
            String bobyMd5 = PublicUtil.MD5Base64(body);
            String stringToSing = method + "\n" + accept + "\n" + bobyMd5
                    + "\n" + content_type + "\n" + date + "\n" + path;

            // 2. 计算 HMAC - SHA1
            String signature = PublicUtil.HMACSha1(stringToSing, ak_secret);

            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;

            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection) conn).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
}