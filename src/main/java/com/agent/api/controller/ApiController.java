/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ApiController
 * Author:   apple
 * Date:     2018/5/24 上午11:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.agent.api.controller;

import com.agent.api.util.InternetUtil;
import com.agent.api.util.Key;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author apple
 * @create 2018/5/24
 * @since 1.0.0
 */
@RestController
@RequestMapping("/renlian")
public class ApiController {


    @RequestMapping("/jiance")
    @ResponseBody
    public JSONObject api_Detection(HttpServletRequest request) throws IOException {
        System.out.println("请求到检测api");
        BufferedReader br = request.getReader();
        String str, Base = "";
        while((str = br.readLine()) != null){
            Base += str;
        }
        String body = "{\"type\":1 , \"content\": \"" + Base + "\"}";
        // 返回一组Json
        try {
            // 拿Base
            JSONObject jsonObject = Key.jsonObject(InternetUtil.sendPost(Key.UrlDetect, body, Key.ak_id_Detect, Key.ak_secret_Detect));
            if (jsonObject != null) {
                System.out.println("请求成功");
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请求失败");
        return null;
    }


    @RequestMapping("/bidui")
    @ResponseBody
    public JSONObject api_Verify(HttpServletRequest request) throws IOException {
        System.out.println("请求到比对api");
        BufferedReader br = null;
        String str , Base = "";
        String original_Base = "";   //原图
        String contrast_Base = "";
        int check = 0;
        int index = 0;
        br = request.getReader();
            while((str = br.readLine()) != null){
                if(check == 0){
                  index = str.lastIndexOf('}');
                  check = 1;
                }
                Base += str;
            }
        original_Base = Base.substring(0 , index);
        contrast_Base = Base.substring(index + 1,Base.length());
        // 返回一组对比数据
        String body = "{\"type\" : 1 , \"content_1\" :\"" + original_Base + "\",\"content_2\":\"" + contrast_Base + "\"}";
        //String body = "";
        // 通过post处理 
        try {
            JSONObject jsonObject = Key.jsonObject(InternetUtil.sendPost(Key.UrlVerify, body, Key.ak_id_Verify, Key.ak_secret_Verify));
            if(jsonObject != null) {
                System.out.println("请求成功");
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请求失败");
        return null;
    }

}